package com.aly8246.common.feign;


import org.springframework.beans.factory.annotation.Value;

public class CustomFeignClientConfiguration {

    @Value("${server.port:8080}")
    private String port;
//
//    @Bean
//    public Client feignClient() {
//        return new IstioFeignClient(null, null, this.port);
//    }
}
