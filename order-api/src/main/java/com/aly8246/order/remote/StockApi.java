package com.aly8246.order.remote;


import com.aly8246.common.res.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "stock",url = "stock:8002")
public interface StockApi {
    @PutMapping("stock/{goodsId}")
    Result<Void> deductGoodsStock(@PathVariable("goodsId") Long goodsId, @RequestParam Integer number);
}
