package com.aly8246.common.spring;

import com.aly8246.common.swagger.EnableSwagger;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@RequiredArgsConstructor
public class SpringBootInfo {
    private final ApplicationContext applicationContext;
    private String service;
    private Object mainClass;
    private List<Annotation> mainAnnotations;

    private EnableSwagger swagger;

    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> annotatedBeans = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);

        if(annotatedBeans.size()>1){
            throw new Exception("@SpringBootApplication只需要配置一个在启动类");
        }

        this.mainClass= annotatedBeans.values().stream().findFirst().get();
        String appName = this.mainClass.getClass().getAnnotation(EnableSwagger.class).service();
        String defaultAppName = annotatedBeans.keySet().stream().findFirst().get();

        this.service=appName.equals("default")?defaultAppName:appName;

        this.mainAnnotations= Stream.of(this.mainClass.getClass().getAnnotations()).collect(Collectors.toList());

        this.swagger=this.mainClass.getClass().getAnnotation(EnableSwagger.class);
    }
}
