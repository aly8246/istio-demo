package com.aly8246.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "库存实体类")
public class StockDto {
    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("商店ID")
    private Long shopId;

    @ApiModelProperty("商店余量")
    private Integer number;

    @ApiModelProperty("商店销量")
    private Integer salesVolume;
}