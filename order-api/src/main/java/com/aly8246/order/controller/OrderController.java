package com.aly8246.order.controller;

import com.aly8246.common.res.Result;
import com.aly8246.common.util.IDUtil;
import com.aly8246.order.entity.GoodsDto;
import com.aly8246.order.entity.Order;
import com.aly8246.order.entity.OrderCreateDto;
import com.aly8246.order.entity.StockDto;
import com.aly8246.order.remote.GoodsApi;
import com.aly8246.order.remote.StockApi;
import com.aly8246.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("order")
@Api(value = "订单控制器")
@RequiredArgsConstructor
public class OrderController {
    private final GoodsApi goodsApi;
    private final StockApi stockApi;

    private final OrderService orderService;

    @SneakyThrows
    @ApiOperation(value = "创建订单")
    @ApiResponses({
            //code重复的情况下，第一个声明的生效。
            @ApiResponse(code = 200,message = "删除成功" ),
            @ApiResponse(code = 202,message = "删除失败，用户不存在")
    })
    @PostMapping
    public Result<List<String>> createOrder(@RequestBody@Validated OrderCreateDto orderCreateDto){
        List<String> stringList=new LinkedList<>();

        stringList.add("1.获取商品信息");
        GoodsDto goodsDto = goodsApi.queryByGoodsId(orderCreateDto.getGoodsId()).result();
        stringList.add(goodsDto.toString());

        stringList.add("2.扣除库存");
        StockDto stockDto = stockApi.deductGoodsStock(orderCreateDto.getGoodsId(), orderCreateDto.getNumber()).result();
        stringList.add(stockDto.toString());

        stringList.add("3.生成订单数据");
        Order order=new Order(IDUtil.nextSnowflakeId(), orderCreateDto.getUserId(), orderCreateDto.getGoodsId(),
                stockDto.getShopId(),goodsDto.getGoodsPrice(),new BigDecimal("0.0"),
                Arrays.asList("未支付","已支付").get(new Random().nextInt(2)), LocalDateTime.now(),
                Arrays.asList("微信","支付宝","paypal","银联").get(new Random().nextInt(4)));
        stringList.add(order.toString());

        orderService.save(order);

        return Result.ok(stringList);
    }

}

