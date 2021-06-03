package com.aly8246.goods.service;

import com.aly8246.goods.entity.Goods;
import com.aly8246.goods.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {
    private final GoodsRepository goodsRepository;
    @Override
    public Flux<Goods> queryByGoodsId(Long goodsId) {
        return goodsRepository.selectById(goodsId);
    }
}
