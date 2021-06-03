package com.aly8246.common.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Map;

@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
@ConditionalOnProperty(prefix = "spring",name = "profiles.active",havingValue = "dev")
@RequiredArgsConstructor
public class SwaggerConfiguration {
    private final ApplicationContext context;

    @Bean(value = "defaultApi")
    public Docket defaultApi() throws Exception {
        Map<String, Object> annotatedBeans = context.getBeansWithAnnotation(EnableSwagger.class);
        if(annotatedBeans.size()>1){
            throw new Exception("@EnableSwagger只需要配置一个在启动类");
        }

        Object annotationClass = annotatedBeans.values().stream().findFirst().get();
        String defaultAppName = annotatedBeans.keySet().stream().findFirst().get();
        EnableSwagger enableSwagger = annotationClass.getClass().getAnnotation(EnableSwagger.class);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("#365天使科技restful Api文档")
                        .contact(new Contact(enableSwagger.developer()+"::"+enableSwagger.email(),"",""))
                        .description("365天使科技restful Api文档")
                        .termsOfServiceUrl("http://www.365tskj.com/")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName(enableSwagger.service().equals("default")?defaultAppName: enableSwagger.service())
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }
}