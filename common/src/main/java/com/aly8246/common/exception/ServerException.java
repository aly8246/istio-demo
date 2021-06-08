package com.aly8246.common.exception;

import com.aly8246.common.res.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ServerException extends RuntimeException{

    private ResultCode code;

    public ServerException(ResultCode code) {
        this.code = code;
    }

    public ServerException(Throwable cause, ResultCode code) {
        super(cause);
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.getCode().getMsg();
    }
}
