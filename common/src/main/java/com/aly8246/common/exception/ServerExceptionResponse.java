package com.aly8246.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("异常响应类")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServerExceptionResponse {
    @ApiModelProperty("服务模块")
    private String service;

    @ApiModelProperty("主类名")
    private String mainClassName;

    @ApiModelProperty("异常发生时间")
    private Date exceptionTime;

    @ApiModelProperty("服务器ip")
    private String serverIp;

    @ApiModelProperty("异常消息")
    private String exceptionMsg;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("请求路径")
    private String path;

    private ServerExceptionResponse stack;
}
