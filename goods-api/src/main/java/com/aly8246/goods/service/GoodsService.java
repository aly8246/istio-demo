package com.aly8246.goods.service;

import com.aly8246.goods.entity.Goods;
import reactor.core.publisher.Mono;

public interface GoodsService {
    /**
     * 根据商品id查询航拍
     * @param goodsId 商品id
     * @return 商品
     */
    Mono<Goods> queryByGoodsId(Long goodsId);
}