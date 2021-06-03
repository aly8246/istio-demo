package com.aly8246.order.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @ApiModelProperty(value = "用户ID",example = "1",required = true)
    @NotNull
    private Long userId;

    @ApiModelProperty(value = "订单渠道",example = "phone",required = true)
    @NotBlank
    private String canal;

    @ApiModelProperty(value = "商品ID",example = "1",required = true)
    @NotNull
    private Long goodsId;

    @ApiModelProperty(value = "购买数量",example = "1",required = true)
    @NotNull
    private Integer number;

//    @ApiModelProperty(value = "购物车编号",example = "[1,2]",required = true,dataType = "Long")
//    @NotEmpty
//    private List<Long> shoppingCardIds;
}
