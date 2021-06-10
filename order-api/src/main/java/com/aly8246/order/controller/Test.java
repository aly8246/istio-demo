package com.aly8246.order.controller;

import com.aly8246.order.entity.Order;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    @SneakyThrows
    public static void main(String[] args) {
        Order o=new Order();
        System.out.println(new Date()+"--Start");
        CompletableFuture<Order> goodsCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(new Date()+"--goods.Start");
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            o.setShopId(1L);
            System.out.println(new Date()+"--goods.Done");
            return o;
        });

        CompletableFuture<Order> stockCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(new Date()+"--stock.Start");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            o.setOrderPrice(new BigDecimal("1.0"));
            System.out.println(new Date()+"--stock.Done");
            return o;
        });

        CompletableFuture<Order> orderCompletableFuture = goodsCompletableFuture.thenCombineAsync(stockCompletableFuture1, (o1, o2) -> {
            Order order = new Order();
            order.setShopId(o1.getShopId());
            order.setOrderPrice(o2.getOrderPrice());

            return order;
        });

        System.out.println(orderCompletableFuture.get());
        System.out.println(new Date()+"--Done");
    }

}
