package com.aly8246.common.res;

import com.aly8246.common.exception.ServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    /**
     * 成功返回的状态码
     */
    SUCCESS(200, "success",200),
    /**
     * 资源不存在的状态码
     */
    RESOURCES_NOT_EXIST(404, "资源不存在",404),
    /**
     * 所有无法识别的异常默认的返回状态码
     */
    SERVICE_ERROR(500, "服务器异常",503),

    /**
     * 服务器不可用
     */
    SERVICE_NOT_UNAVAILABLE(503,"HTTP/1.1 503 Service Unavailable",503),

    /**
     * 收到了未知的状态码
     */
    NOT_EXIST_CODE(601, "未知状态码",503),

    /**
     * 商品不存在
     */
    GOODS_NOT_EXIST(700,"该商品不存在",200)
    ;

    /**
     * 状态码
     */
    private final int code;
    /**
     * 返回信息
     */
    private final String msg;

    /**
     * http状态码
     */
    private final int httpCode;

    public static ResultCode getByCode(int code){
        ResultCode[] ResultCodeEnums = values();
        for (ResultCode resultCode : ResultCodeEnums) {
            if (resultCode.code==code) {
                return resultCode;
            }
        }
        throw new ServerException(NOT_EXIST_CODE);
    }
}
