package com.aly8246.order.remote;


import com.aly8246.common.res.Result;
import com.aly8246.order.entity.StockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "stock", url = "stock:8002", path = "api/stock/stock")
public interface StockApi {

    @PutMapping("{goodsId}::{goodsNumber}")
    Result<StockDto> deductGoodsStock(@PathVariable("goodsId") Long goodsId, @PathVariable("goodsNumber") Integer goodsNumber);
}
