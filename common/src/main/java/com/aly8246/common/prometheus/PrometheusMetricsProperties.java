package com.aly8246.common.prometheus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aly8246.common.prometheus.PrometheusMetricsProperties.PROMETHEUS_PROPERTIES_PREFIX;

@Configuration
@ConfigurationProperties(prefix = PROMETHEUS_PROPERTIES_PREFIX)
@Data
public class PrometheusMetricsProperties {
    public static final String PROMETHEUS_PROPERTIES_PREFIX="management.endpoints.prometheus";

    public List<Map<String,String>> metrics;

    /**
     * 包装指标配置
     */
    @Bean
//    @ConditionalOnProperty(prefix = PROMETHEUS_PROPERTIES_PREFIX,name = "enable",havingValue = "true")
    public PrometheusMetrics prometheusMetrics(){
        PrometheusMetrics prometheusMetrics = new PrometheusMetrics();

        List<PrometheusMetrics> prometheusMetricsList = this.metrics
                .stream()
                .map(e -> new PrometheusMetrics(e.get("class-name"), Boolean.valueOf(e.get("enable")==null?"true":e.get("enable")), e.get("explain"), null))
                .filter(PrometheusMetrics::getEnabled)
                .collect(Collectors.toList());

        prometheusMetrics.setPrometheusMetricsList(prometheusMetricsList);
        return prometheusMetrics;
    }
}
