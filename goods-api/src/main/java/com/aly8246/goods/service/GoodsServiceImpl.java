package com.aly8246.goods.service;

import com.aly8246.goods.entity.Goods;
import com.aly8246.goods.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {
    private final GoodsRepository goodsRepository;
    @Override
    public Mono<Goods> queryByGoodsId(Long goodsId) {
        return goodsRepository.selectById(goodsId);
    }
}
