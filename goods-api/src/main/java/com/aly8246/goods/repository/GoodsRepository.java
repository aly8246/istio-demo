package com.aly8246.goods.repository;

import com.aly8246.goods.entity.Goods;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GoodsRepository extends ReactiveCrudRepository<Goods,Long> {

    @Query("select * from goods where id= :goodsId")
    Mono<Goods> selectById(Long goodsId);

}
