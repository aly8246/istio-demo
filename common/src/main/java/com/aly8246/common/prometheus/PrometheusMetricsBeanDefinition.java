package com.aly8246.common.prometheus;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Supplier;

public class PrometheusMetricsBeanDefinition extends GenericBeanDefinition {
    private final MeterBinder meterBinder;

    @Override
    public Supplier<?> getInstanceSupplier() {
        return ()-> this.meterBinder;
    }

    @Override
    public Class<?> getBeanClass() throws IllegalStateException {
        return this.meterBinder.getClass();
    }

    @Override
    public String getBeanClassName() {
        return this.meterBinder.getClass().getName();
    }

    public PrometheusMetricsBeanDefinition(String metricsClassName, MeterRegistry meterRegistry) {
        super();
        //构建类并且绑定到Prometheus注册器
        this.meterBinder=this.buildMeterBinder(metricsClassName);
        this.meterBinder.bindTo(meterRegistry);
    }

    /**
     * 构建类
     */
    private MeterBinder buildMeterBinder(String metricsClassName){
        //加载一个metrics的类
        Object o = null;
        try {
            o = Class.forName(metricsClassName).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        assert o != null;

        //初始化完成，返回给ioc容器
        return (MeterBinder) o;
    }
}
