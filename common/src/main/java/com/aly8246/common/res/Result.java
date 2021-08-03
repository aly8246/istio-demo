package com.aly8246.common.res;

import com.aly8246.common.exception.ServerException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static com.aly8246.common.res.ResultCode.RESOURCES_NOT_EXIST;
import static com.aly8246.common.res.ResultCode.SUCCESS;

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

    public static Result<Void> ok() {
        return new Result<>(SUCCESS.getCode(), SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> ok(T data) {
        if (data == null) {
            throw new ServerException(RESOURCES_NOT_EXIST);
        }
        return new Result<>(SUCCESS.getCode(), SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> res(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> ResponseEntity<Result<T>> success(T data) {
        return new ResponseEntity<>(new Result<>(SUCCESS.getCode(), SUCCESS.getMsg(), data), HttpStatus.OK);
    }

    public static <T> Result<T> not_found() {
        return new Result<>(RESOURCES_NOT_EXIST.getCode(), RESOURCES_NOT_EXIST.getMsg(), null);
    }

    public static <T> Result<T> not_found(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public T result() {
        if (this.getData() instanceof Collection) {
            List data = (List) this.getData();
            if (data.size() > 0) return this.getData();
            return null;
        }
        if (this.getData() != null) return this.getData();
        throw new ServerException(ResultCode.getByCode(this.getCode()));
    }


}
