package com.aly8246.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("istio_order")
public class Order implements Serializable {
    @ApiModelProperty("订单ID")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("商品ID")
    private Long goodsId;
    @ApiModelProperty("商店ID")
    private Long shopId;
    @ApiModelProperty("订单价格")
    private BigDecimal orderPrice;
    @ApiModelProperty("订单支付价格")
    private BigDecimal payPrice;
    @ApiModelProperty("订单状态")
    private String orderState;
    @ApiModelProperty("订单创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("订单支付类型")
    private String payType;
}
