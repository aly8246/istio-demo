package com.aly8246.order.controller;

import com.aly8246.common.res.Result;
import com.aly8246.order.entity.GoodsDto;
import com.aly8246.order.entity.Order;
import com.aly8246.order.entity.OrderCreateDto;
import com.aly8246.order.entity.StockDto;
import com.aly8246.order.remote.GoodsApi;
import com.aly8246.order.remote.StockApi;
import com.aly8246.order.service.OrderService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order/order/")
@Api(value = "订单控制器")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final GoodsApi goodsApi;
    private final StockApi stockApi;

    private final OrderService orderService;

    private final Counter counter = Metrics.counter("order.counter.total", "aa", "bb");

    @Timed
    @Counted
    @SneakyThrows
    @ApiOperation(value = "创建订单")
    @ApiResponses({
            //code重复的情况下，第一个声明的生效。
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 202, message = "删除失败，用户不存在")
    })
    @PostMapping
    public Result<Order> createOrder(@RequestBody @Validated OrderCreateDto orderCreateDto) {
        CompletableFuture<GoodsDto> goodsDtoCompletableFuture = CompletableFuture.supplyAsync(() -> goodsApi.queryByGoodsId(orderCreateDto.getGoodsId()).result());
        CompletableFuture<StockDto> stockDtoCompletableFuture = CompletableFuture.supplyAsync(() -> stockApi.deductGoodsStock(orderCreateDto.getGoodsId(), orderCreateDto.getNumber()).result());

        CompletableFuture<Order> orderCompletableFuture = goodsDtoCompletableFuture.thenCombineAsync(
                stockDtoCompletableFuture,
                (
                        (goodsDto, stockDto) -> {
                            Order order = new Order(null, orderCreateDto.getUserId(), orderCreateDto.getGoodsId(), stockDto.getShopId(), goodsDto.getGoodsPrice(), new BigDecimal("0.0"), Arrays.asList("未支付", "已支付").get(new Random().nextInt(2)), new Date(), Arrays.asList("微信", "支付宝", "paypal", "银联").get(new Random().nextInt(4)));
                            orderService.save(order);
                            return order;
                        })
        );


        Order order = orderCompletableFuture.get();
        log.info(order.toString());

        counter.increment();
        return Result.ok(order);
    }

    @ApiOperation(value = "查询订单")
    @GetMapping("{orderId}")
    public Result<Order> queryOrderById(@PathVariable("orderId") String orderId) {
        return Result.ok(orderService.getById(orderId));
    }

}

