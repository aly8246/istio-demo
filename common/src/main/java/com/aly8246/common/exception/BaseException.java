package com.aly8246.common.exception;

import com.aly8246.common.res.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException{

    private ResultCode code;

    public BaseException(ResultCode code) {
        this.code = code;
    }

    public BaseException(Throwable cause, ResultCode code) {
        super(cause);
        this.code = code;
    }
}
