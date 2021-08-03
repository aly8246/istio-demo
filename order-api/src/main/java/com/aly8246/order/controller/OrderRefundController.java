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
@RequestMapping("/api/order/orderRefund/")
@Api(value = "订单控制器")
@RequiredArgsConstructor
@Slf4j
public class OrderRefundController {

    @Timed
    @Counted
    @SneakyThrows
    @ApiOperation(value = "创建订单退款")
    @PostMapping
    public Result<String> createOrderRefund() {
        return Result.ok("createOrderRefund");
    }

    @ApiOperation(value = "查询订单退款")
    @GetMapping
    public Result<String> queryOrderRefund() {
        return Result.ok("queryOrderRefund");
    }

}

