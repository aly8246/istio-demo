package com.aly8246.stock.controller;

import com.aly8246.common.exception.ServerException;
import com.aly8246.common.res.Result;
import com.aly8246.stock.entity.Stock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Random;

import static com.aly8246.common.res.ResultCode.RESOURCES_NOT_EXIST;
import static com.aly8246.common.res.ResultCode.SERVICE_NOT_UNAVAILABLE;

@RestController
@RequestMapping("stock")
@Api(value = "库存控制器")
public class StockController {

    @ApiOperation(value = "查询产品库存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId",value = "商品ID",required = true,paramType = "path")
    })
    @GetMapping("{goodsId}")
    public Mono<Result<Stock>> queryGoodsStock(@PathVariable("goodsId") Long goodsId){
        if (goodsId==2L){
            throw new ServerException(RESOURCES_NOT_EXIST);
        }
        return Mono.just(Result.ok(new Stock(goodsId,1L,100,50)));
    }

    @SneakyThrows
    @ApiOperation(value = "扣除产品库存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId",value = "商品ID",example = "1",required = true,paramType = "path"),
            @ApiImplicitParam(name = "goodsNumber",value = "商品数量",example = "1",required = true,paramType = "path")
    })
    @PutMapping("{goodsId}::{goodsNumber}")
    public Mono<Result<Stock>> deductGoodsStock(@PathVariable("goodsId") Long goodsId,@PathVariable("goodsNumber") Integer goodsNumber){
        //设定有50%几率抛出http 503
        if (new Random().nextInt()%2==0){
            throw new ServerException(SERVICE_NOT_UNAVAILABLE);
        }

        System.out.println("deductGoodsStock::goodsId = " + goodsId + ", goodsNumber = " + goodsNumber);
        return Mono.just(Result.ok(new Stock(goodsId,1L,100,50)));
    }

}
