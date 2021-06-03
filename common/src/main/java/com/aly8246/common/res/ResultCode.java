package com.aly8246.common.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    /**
     * 成功返回的状态码
     */
    SUCCESS(200, "success"),
    /**
     * 资源不存在的状态码
     */
    RESOURCES_NOT_EXIST(404, "资源不存在"),
    /**
     * 所有无法识别的异常默认的返回状态码
     */
    SERVICE_ERROR(500, "服务器异常");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 返回信息
     */
    private final String msg;
}
