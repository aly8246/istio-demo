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
public class ReactiveResult<T> implements Serializable {
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

    public static <T> Mono<ReactiveResult<T>> ok(Mono<T> monoBody){
        return responseBodyCreate(monoBody, null);
    }

    public static <T> Mono<ReactiveResult<T>> ok (Mono<T> monoBody, String msg) {
        return responseBodyCreate(monoBody, msg);
    }

    private static <T> Mono<ReactiveResult<T>> responseBodyCreate(Mono<T> monoData, String msg) {
        return monoData.map(data-> {
            final ReactiveResult<T> responseInfo = new ReactiveResult<>();
            responseInfo.setCode(200);
            responseInfo.setData(data);
            responseInfo.setMsg(msg);
            return responseInfo;
        });
    }
}
