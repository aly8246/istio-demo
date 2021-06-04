package com.aly8246.goods.service;

import com.aly8246.goods.entity.Goods;
import com.aly8246.goods.repository.GoodsRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsRepository,Goods> implements GoodsService {

}
