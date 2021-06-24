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
    SUCCESS(200, "success"),
    /**
     * 资源不存在的状态码
     */
    RESOURCES_NOT_EXIST(404, "资源不存在"),

    /**
     * 所有无法识别的异常默认的返回状态码
     */
    SERVICE_ERROR(500, "Internal Server Error"),

    /**
     * 服务器不可用
     */
    SERVICE_NOT_UNAVAILABLE(503,"HTTP/1.1 503 Service Unavailable"),

    ERROR_OPERATE(600,"错误操作"),

    BUSINESS_EXCEPTION(700,"业务异常"),
    GOODS_NOT_EXIST(701,"该商品不存在"),
    STOCK_NOT_ENOUGH(702,"商品库存不足"),
    GOODS_UN_SELL(703,"商品暂时不允许购买")
    ;

    /**
     * 状态码
     */
    private final int code;
    /**
     * 返回信息
     */
    private final String msg;

    public static ResultCode getByCode(int code){
        ResultCode[] ResultCodeEnums = values();
        for (ResultCode resultCode : ResultCodeEnums) {
            if (resultCode.code==code) {
                return resultCode;
            }
        }
        throw new RuntimeException("未知状态码");
    }
}
