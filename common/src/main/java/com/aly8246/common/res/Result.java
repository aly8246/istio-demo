package com.aly8246.common.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result<T> implements Serializable {
    /**
     * 返回状态码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    public static Result<Void> ok(){
        return new Result<>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(),null);
    }

    public static <T> Result<T> ok(T data){
        return new Result<>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(),data);
    }

    public static <T> Mono<Object> mono(T data){
        return Mono.just(new Result<>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(),data));
    }

}
