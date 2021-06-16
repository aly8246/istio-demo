package com.aly8246.common.prometheus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "management.endpoints.prometheus")
@Data
public class PrometheusMetricsProperties {
    public List<Map<String,String>> metrics;

    /**
     * 包装指标配置
     */
    @Bean
    public PrometheusMetrics prometheusMetrics(){
        PrometheusMetrics prometheusMetrics = new PrometheusMetrics();

        List<PrometheusMetrics> prometheusMetricsList = this.metrics
                .stream()
                .map(e -> new PrometheusMetrics(e.get("class-name"), Boolean.valueOf(e.get("enable")==null?"true":e.get("enable")), e.get("explain"), null))
                .filter(PrometheusMetrics::getEnable)
                .collect(Collectors.toList());

        prometheusMetrics.setPrometheusMetricsList(prometheusMetricsList);
        return prometheusMetrics;
    }
}
