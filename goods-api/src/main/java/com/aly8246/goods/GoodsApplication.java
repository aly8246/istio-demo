package com.aly8246.goods;

import com.aly8246.common.swagger.OpenApi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenApi(developer="小明",email = "1@qq.com",service = "商品服务")
@MapperScan("com.aly8246.goods.repository")
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class,args);
    }
}
