package com.aly8246.order.remote;

import com.aly8246.common.res.Result;
import com.aly8246.order.entity.GoodsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "goods", url = "goods:8001", path = "api/goods/goods/")
public interface GoodsApi {

    @GetMapping("{goodsId}")
    Result<GoodsDto> queryByGoodsId(@PathVariable Long goodsId);
}
