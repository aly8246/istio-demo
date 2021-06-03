package com.aly8246.order.controller;

import com.aly8246.common.res.Result;
import com.aly8246.order.entity.GoodsDto;
import com.aly8246.order.entity.OrderDto;
import com.aly8246.order.remote.GoodsApi;
import com.aly8246.order.remote.StockApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "订单控制器")
@RequiredArgsConstructor
public class OrderController {
    private final GoodsApi goodsApi;
    private final StockApi stockApi;

    @ApiOperation(value = "创建订单")
    @ApiResponses({
            //code重复的情况下，第一个声明的生效。
            @ApiResponse(code = 200,message = "删除成功" ),
            @ApiResponse(code = 202,message = "删除失败，用户不存在")
    })
    @PostMapping
    public void createOrder(@RequestBody@Validated OrderDto orderDto){
        //step1 获取商品价格
        System.out.println("orderDto = " + orderDto);
        GoodsDto goodsDto = goodsApi.queryByGoodsId(orderDto.getGoodsId()).result();

        System.out.println(goodsDto);

        //step2 扣除库存
        stockApi.deductGoodsStock(orderDto.getGoodsId(),orderDto.getNumber());


        //step3 生成支付链接
    }

}

