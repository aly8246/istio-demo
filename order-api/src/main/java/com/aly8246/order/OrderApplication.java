package com.aly8246.order;

import com.aly8246.common.swagger.OpenApi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenApi(developer="小明",email = "1@qq.com",service = "订单服务")
@EnableFeignClients("com.aly8246.*.remote")
@MapperScan("com.aly8246.order.repository")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }

}
