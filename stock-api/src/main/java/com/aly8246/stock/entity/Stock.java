package com.aly8246.stock.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "用户实体类")
public class Stock {
    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("商店ID")
    private Long shopId;

    @ApiModelProperty("商店余量")
    private Integer number;

    @ApiModelProperty("商店销量")
    private Integer salesVolume;
}
