package com.aly8246.goods.controller;

import com.aly8246.common.res.Result;
import com.aly8246.goods.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Api(value = "商品控制器")
@RequestMapping("goods/")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    @ApiOperation("根据商品ID查询商品详情")
    @GetMapping("{goodsId}")
    public Flux<Object> queryByGoodsId(@PathVariable Long goodsId){
        return goodsService.queryByGoodsId(goodsId).map(Result::ok);
    }

}
