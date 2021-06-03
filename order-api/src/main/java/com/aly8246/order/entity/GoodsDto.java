package com.aly8246.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商品实体类")
public class GoodsDto {
    @ApiModelProperty("商品id")
    private Long id;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品描述")
    private String goodsDesc;
    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;
    @ApiModelProperty("商品图片链接")
    private String url;
    @ApiModelProperty("商品类型")
    private String goodsType;
}
