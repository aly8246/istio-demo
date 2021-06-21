package com.aly8246.stock;

import com.aly8246.common.swagger.OpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenApi(developer="小明",email = "1@qq.com",service = "库存服务")
@EnableScheduling
public class StockApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class,args);
    }
}
