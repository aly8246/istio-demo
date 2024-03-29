package com.aly8246.goods.service;

import com.aly8246.goods.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface GoodsService extends IService<Goods> {

    Goods getById(Long id);
}
