package com.aly8246.goods.controller;

import com.aly8246.common.exception.ServerException;
import com.aly8246.common.res.Result;
import com.aly8246.goods.entity.Goods;
import com.aly8246.goods.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.aly8246.common.res.ResultCode.GOODS_NOT_EXIST;
import static com.aly8246.common.res.ResultCode.GOODS_UN_SELL;

@RestController
@Api(value = "商品控制器")
@RequestMapping("/api/goods/goods/")
@RequiredArgsConstructor
@Slf4j
public class GoodsController {
    private final GoodsService goodsService;

    @SneakyThrows
    @ApiOperation("根据商品ID查询商品详情")
    @GetMapping("{goodsId}")
    public Result<Goods> queryByGoodsId(@PathVariable Long goodsId) {
        log.info("queryByGoodsId::goodsId = " + goodsId);
        if (goodsId == 3) {
            throw new ServerException(GOODS_UN_SELL);
        }
        return Result.ok(goodsService.getById(goodsId));
    }

}
