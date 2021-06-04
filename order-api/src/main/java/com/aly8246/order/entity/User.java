package com.aly8246.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户实体类")
public class User implements Serializable {
    @ApiModelProperty(value="id" ,required= true,example = "123")
    private Integer id;

    @ApiModelProperty(value="用户姓名" ,required=true,example = "test")
    private String name;
}