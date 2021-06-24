package com.aly8246.common.prometheus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrometheusMetrics {
    //指标类名
    private String className;
    //指标是否开启，默认开启
    private Boolean enabled;
    //指标信息
    private String explain;

    public String getName(){
        return this.className.split("\\.")[this.className.split("\\.").length-1];
    }

    private List<PrometheusMetrics> prometheusMetricsList=null;
}
