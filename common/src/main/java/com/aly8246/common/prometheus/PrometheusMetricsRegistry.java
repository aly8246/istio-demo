package com.aly8246.common.prometheus;

import com.aly8246.common.util.CustomBeanDefinitionRegistryPostProcessor;
import com.aly8246.common.util.SpringUtil;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * 创建指标类
 * 绑定到Prometheus
 * 注册到ioc容器
 */
@ConditionalOnBean(PrometheusMetrics.class)
@RequiredArgsConstructor
@Slf4j
public class PrometheusMetricsRegistry{
    private final PrometheusMeterRegistry prometheusMeterRegistry;
    private final PrometheusMetrics prometheusMetrics;
    private final CustomBeanDefinitionRegistryPostProcessor registryPostProcessor;

    @PostConstruct
    public void register(){
        for (PrometheusMetrics metrics : prometheusMetrics.getPrometheusMetricsList()) {
            PrometheusMetricsBeanDefinition metricsBeanDefinition = new PrometheusMetricsBeanDefinition(metrics.getClassName(), prometheusMeterRegistry);
            registryPostProcessor.getRegistry().registerBeanDefinition(metrics.getName(),metricsBeanDefinition);
            log.info("Bean '"+metrics.getName()+"' of explain["+metrics.getExplain()+"] of type ["+metrics.getClassName()+"] ");
        }
    }

}
