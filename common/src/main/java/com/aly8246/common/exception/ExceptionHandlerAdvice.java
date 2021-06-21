package com.aly8246.common.exception;

import com.aly8246.common.res.Result;
import com.aly8246.common.res.ResultCode;
import com.aly8246.common.spring.SpringBootInfo;
import com.aly8246.common.util.JsonUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    /**
     * 处理未捕获的Exception
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e,HttpServletResponse response){
        log.error(e.getMessage(),e);
        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), e.getMessage(),response);
    }


    /**
     * 线程池执行异常
     */
    @ExceptionHandler(ExecutionException.class)
    public Result<Object> handleExecutionException(ExecutionException e, HttpServletResponse response){
        log.error(e.getMessage(),e);

        //线程池里包含feign的404异常
        if(e.getCause() instanceof FeignException.NotFound){
            FeignException.NotFound ex=(FeignException.NotFound) e.getCause();

            return new Result<>(RESOURCES_NOT_EXIST.getHttpCode(), RESOURCES_NOT_EXIST.getMsg(), JsonUtil.jsonToMap(ex.contentUTF8()));
        }
        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), e.getMessage(),response);
    }

    /**
     * 处理feign异常并且抛出自定义404
     * 主要是处理远程调用以后接收到的404响应
     * @param e feign异常
     */
    @ExceptionHandler(FeignException.class)
    public Result<Object> handleFeignException(FeignException e,HttpServletResponse response){
        //资源不存在，则直接抛出404异常
        if(e instanceof FeignException.NotFound){
            return result(RESOURCES_NOT_EXIST.getHttpCode(), RESOURCES_NOT_EXIST.getMsg(), JsonUtil.jsonToMap(e.contentUTF8()),response);
        }

        //资源是503，则是由istio抛出，应该返回body包含503.http为200的响应体，然后istio开启熔断
        if (e instanceof FeignException.ServiceUnavailable){
            response.setStatus(203);
            return new Result<>(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), e.getMessage());
        }

        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), e.getMessage(),response);
    }

    /**
     * 处理业务异常BaseException
     * 包含的404主要是发起404响应
     * @param e 业务异常
     * @return 统一响应体
     */
    @ExceptionHandler(ServerException.class)
    public Result<?> handleBaseException(ServerException e, HttpServletRequest request, HttpServletResponse response){
        log.error(e.getMessage());

        //从异常中获取枚举类
        ResultCode code=e.getCode();

        //响应http状态码为200 说明是业务异常，直接抛出
        if (code.getHttpCode()== SUCCESS.getCode()){
            return result(code.getCode(),code.getMsg(),null,response);
        }

        //响应http状态码为404 说明是资源不存在，直接抛出
        if(code.getHttpCode()== RESOURCES_NOT_EXIST.getHttpCode()){
            return result(code.getCode(), code.getMsg(), springBootInfo.getService()+request.getRequestURI(),response);
        }

        //进行其他异常处理
        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), e.getMessage(),response);
    }


    public Result<Object> result(int code,String msg,Object data,HttpServletResponse response){
        response.setStatus(code);
        return new Result<>(code,msg,data);
    }

}
