package com.aly8246.common.exception;

import com.aly8246.common.res.Result;
import com.aly8246.common.res.ResultCode;
import com.aly8246.common.spring.SpringBootInfo;
import com.aly8246.common.util.JsonUtil;
import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Server;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutionException;

import static com.aly8246.common.res.ResultCode.*;

@Component
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {
    private final SpringBootInfo springBootInfo;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    /**
     * 处理未捕获的Exception
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e){
        if (e.getMessage()!=null) log.error(e.getMessage(),e);
        else log.error("unknown exception message!",e);

        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), e.getMessage());

        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), exceptionRes);
    }


    /**
     * 线程池执行异常
     */
    @ExceptionHandler(ExecutionException.class)
    public Result<?> handleExecutionException(ExecutionException e){
        log.error(e.getMessage(),e);

        Throwable cause = e.getCause();

        //线程池执行包装feign异常
        if (cause instanceof FeignException){
            return handleAllFeignException((FeignException) cause);
        }

//        //业务异常
//        if (cause instanceof ServerException){
//            ServerException exception = (ServerException) cause;
//            ResultCode code = exception.getCode();
//            Result<String> result = new Result<>(code.getCode(), code.getMsg(), e.getMessage());
//            return notAccept(BUSINESS_EXCEPTION.getCode(), BUSINESS_EXCEPTION.getMsg(), result);
//        }

        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), e.getMessage());
    }

    /**
     * 处理feign异常并且抛出自定义404
     * 主要是处理远程调用以后接收到的404响应
     * @param e feign异常
     */
    @ExceptionHandler(FeignException.class)
    public Result<?> handleFeignException(FeignException e){
       return handleAllFeignException(e);
    }

    /**
     * 处理业务异常BaseException
     * 包含的404主要是发起404响应
     * @param e 业务异常
     * @return 统一响应体
     */
    @ExceptionHandler(ServerException.class)
    public Result<?> handleBaseException(ServerException e){
        log.error(e.getMessage());

        //从异常中获取枚举类
        ResultCode code=e.getCode();

        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), e.getMessage());

        //响应http状态码大于等于600 说明是业务异常，直接抛出
        if (code.getCode()>= BUSINESS_EXCEPTION.getCode()){
            return notAccept(code.getCode(),code.getMsg(),exceptionRes);
        }

        //响应http状态码为404 说明是资源不存在，直接抛出
        if(code.getCode()== RESOURCES_NOT_EXIST.getCode()){
            return result(RESOURCES_NOT_EXIST.getCode(), RESOURCES_NOT_EXIST.getMsg(), exceptionRes);
        }

        //进行其他异常处理
        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), exceptionRes);
    }

    /**
     * feign接受到的异常
     * @param e 404 600 other:503
     * @return 异常响应
     */
    private Result<?> handleAllFeignException(FeignException e){
        //取出原本的错误异常，转为result对象
        Result<?> result = JsonUtil.jsonToObject(e.contentUTF8(), Result.class);

        if(e instanceof FeignException.NotFound){
            //404 由Exception和ExecutionException捕获的feign 404资源不存在
            response.setStatus(RESOURCES_NOT_EXIST.getCode());
        }else if (e.status()>=BUSINESS_EXCEPTION.getCode()){
            //大于等于600 业务异常,抛出业务异常数据,然后传递到前端
            response.setStatus(BUSINESS_EXCEPTION.getCode());
        } else {
            //503 未处理的feign异常,直接抛出,并且由istio接管熔断
            response.setStatus(SERVICE_NOT_UNAVAILABLE.getCode());
        }

        return result;
    }


    public Result<Object> result(int code,String msg,Object data){
        response.setStatus(code);
        return new Result<>(code,msg,data);
    }

    public Result<Object> notAccept(int code,String msg,Object data){
        response.setStatus(BUSINESS_EXCEPTION.getCode());
        return new Result<>(code,msg,data);
    }
}
