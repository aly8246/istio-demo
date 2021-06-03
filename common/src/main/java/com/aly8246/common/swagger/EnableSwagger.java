package com.aly8246.common.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SwaggerConfiguration.class)
public @interface EnableSwagger {
    String service() default "default";

    String developer() default "default_developer";

    String email() default "dev@qq.com";
}
