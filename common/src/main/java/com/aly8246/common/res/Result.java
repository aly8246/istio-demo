package com.aly8246.common.res;

import com.aly8246.common.exception.BaseException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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

    public static <T> ResponseEntity<Result<T>> success(T data){
        return new ResponseEntity<>(new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data), HttpStatus.OK);
    }

    public static <T> Result<T> not_found(){
        return new Result<>(ResultCode.RESOURCES_NOT_EXIST.getCode(), ResultCode.RESOURCES_NOT_EXIST.getMsg(), null);
    }
    public static <T> Result<T> not_found(ResultCode resultCode){
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public T result() {
        if (this.getData() instanceof Collection) {
            List data =  (List)this.getData();
            if (data.size() > 0) return this.getData();
            return null;
        }
        if (this.getData() != null) return this.getData();
        throw new BaseException(ResultCode.getByCode(this.getCode()));
    }



}
