package com.aly8246.order.controller;

import com.aly8246.order.entity.Order;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Test2 {
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println(new Date()+"--Start");
        CompletableFuture<Order> orderCompletableFuture = CompletableFuture.supplyAsync(Order::new)
                .thenApplyAsync(order -> {
                    System.out.println(new Date()+"--goods.Start");
                    try {
                        TimeUnit.SECONDS.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    order.setOrderPrice(new BigDecimal("1.0"));
                    System.out.println(new Date()+"--goods.Done");
                    return order;
                })
                .thenApply(order -> {
                    System.out.println(new Date()+"--stock.Start");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    order.setShopId(1L);
                    System.out.println(new Date()+"--stock.Done");
                    return order;
                });
        System.out.println(orderCompletableFuture.get());
        System.out.println(new Date()+"--Done");
    }
}
