package com.aly8246.goods.service;

import com.aly8246.goods.entity.Goods;
import com.aly8246.goods.repository.GoodsRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsRepository,Goods> implements GoodsService {

    @Override
    @Cacheable(value="istio-demo",key="'goods:'+#id")
    public Goods getById(Long id) {
        return this.baseMapper.selectById(id);
    }
}
