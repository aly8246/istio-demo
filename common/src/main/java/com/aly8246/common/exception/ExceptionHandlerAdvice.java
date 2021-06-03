package com.aly8246.common.exception;

import com.aly8246.common.res.Result;
import com.aly8246.common.res.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
    /**
     * 处理未捕获的Exception
     * @param e 异常
     * @return 统一响应体
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e){
        log.error(e.getMessage(),e);
        return new Result<>(ResultCode.SERVICE_ERROR.getCode(),ResultCode.SERVICE_ERROR.getMsg(),e.getMessage());
    }

    /**
     * 处理未捕获的RuntimeException
     * @param e 运行时异常
     * @return 统一响应体
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Object> handleRuntimeException(RuntimeException e){
        log.error(e.getMessage(),e);
        return new Result<>(ResultCode.SERVICE_ERROR.getCode(),ResultCode.SERVICE_ERROR.getMsg(),e.getMessage());
    }

    /**
     * 处理业务异常BaseException
     * @param e 业务异常
     * @return 统一响应体
     */
    @ExceptionHandler(BaseException.class)
    public Result<Object> handleBaseException(BaseException e){
        log.error(e.getMessage(),e);
        ResultCode code=e.getCode();
        return new Result<>(code.getCode(),code.getMsg(),e.getMessage());
    }
}
