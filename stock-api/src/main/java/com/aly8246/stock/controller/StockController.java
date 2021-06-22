package com.aly8246.stock.controller;

import com.aly8246.common.exception.ServerException;
import com.aly8246.common.res.Result;
import com.aly8246.stock.entity.Stock;
import com.aly8246.stock.entity.UnavailableCtl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.aly8246.common.res.ResultCode.*;

@RestController
@RequestMapping("stock")
@Api(value = "库存控制器")
@RequiredArgsConstructor
@Slf4j
public class StockController {
    private final RedisTemplate<String,Object> redisTemplate;

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

        //每次都查询redis。看看要不要开启503
        UnavailableCtl unavailableCtl = (UnavailableCtl) redisTemplate.opsForValue().get("unavailable_ctl:" + CircuitBreakerCtl.serverId);

        if (unavailableCtl!=null)
        {
            log.info(unavailableCtl.toString());
            if (!unavailableCtl.getAvailableService()){
                //发起未知异常
                throw new RuntimeException();
            }
        }

        if(goodsNumber>999){
            throw new ServerException(STOCK_NOT_ENOUGH);
        }

        System.out.println("deductGoodsStock::goodsId = " + goodsId + ", goodsNumber = " + goodsNumber);
        return Mono.just(Result.ok(new Stock(goodsId,1L,100,50)));
    }

}
