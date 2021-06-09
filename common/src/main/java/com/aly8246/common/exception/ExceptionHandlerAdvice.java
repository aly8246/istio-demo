package com.aly8246.common.exception;

import com.aly8246.common.res.Result;
import com.aly8246.common.res.ResultCode;
import com.aly8246.common.spring.SpringBootInfo;
import com.aly8246.common.util.IPUtil;
import com.aly8246.common.util.JsonUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.aly8246.common.res.ResultCode.*;

@Component
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {
    private final SpringBootInfo springBootInfo;
    /**
     * 处理未捕获的Exception
     * @param e 异常
     * @return 统一响应体
     */
    @ExceptionHandler(Exception.class)
    public Result<ServerExceptionResponse> handleException(Exception e,HttpServletRequest request,HttpServletResponse response){
        log.error(e.getMessage(),e);
        response.setStatus(SERVICE_NOT_UNAVAILABLE.getCode());
        return exceptionResponse(e,request);
    }

    /**
     * 处理未捕获的RuntimeException
     * @param e 运行时异常
     * @return 统一响应体
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<ServerExceptionResponse> handleRuntimeException(RuntimeException e,HttpServletRequest request,HttpServletResponse response){
        log.error(e.getMessage(),e);

        response.setStatus(SERVICE_NOT_UNAVAILABLE.getCode());
        return exceptionResponse(e,request);
    }

    /**
     * 处理feign异常并且抛出自定义404
     * 主要是处理远程调用以后接收到的404响应
     * @param e feign异常
     */
    @ExceptionHandler(FeignException.class)
    public Result<ServerExceptionResponse> handleFeignException(FeignException e,HttpServletRequest request,HttpServletResponse response){
        response.setStatus(RESOURCES_NOT_EXIST.getHttpCode());

        /*
         * 如果不包含"no healthy upstream" 如果是404或者503就向下传递
         * no healthy upstream 是由istio发起的无健康实例
         */
        //TODO upstream connect error or disconnect/reset before headers. reset reason: connection failure
        if(!e.contentUTF8().contains("no healthy upstream") && (e instanceof FeignException.NotFound || e instanceof FeignException.ServiceUnavailable)){
            //获取result响应体
            Result<?> result = JsonUtil.jsonToObject(e.contentUTF8(), Result.class);

            //获取原始的404结构
            Object resourceNotFoundResponseMap = result.getData();
            ServerExceptionResponse serverExceptionResponse = JsonUtil.mapToPojo(resourceNotFoundResponseMap, ServerExceptionResponse.class);

            //设置响应状态码
            response.setStatus(e.status());
            return exceptionResponse(e,request,serverExceptionResponse);
        }

        response.setStatus(SERVICE_NOT_UNAVAILABLE.getHttpCode());
        return exceptionResponse(e,request);
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
        response.setStatus(code.getHttpCode());

        //响应http状态码为200 说明是业务异常，直接抛出
        if (code.getHttpCode()== SUCCESS.getCode()){
            return new Result<>(code.getCode(),code.getMsg(),null);
        }

        //进行其他异常处理
        return exceptionResponse(e,request);
    }

    /**
     * 服务器异常处理
     * @param e 异常
     * @param request 请求
     */
    private Result<ServerExceptionResponse> exceptionResponse(Exception e, HttpServletRequest request){
       return exceptionResponse(e,request,null);
    }

    /**
     * 服务器异常处理
     * @param e 异常
     * @param request 请求
     */
    private Result<ServerExceptionResponse> exceptionResponse(Exception e, HttpServletRequest request, ServerExceptionResponse stack){
        ServerExceptionResponse serverExceptionResponse = new ServerExceptionResponse();
        serverExceptionResponse.setService(springBootInfo.getService());
        serverExceptionResponse.setMainClassName(springBootInfo.getMainClass().getClass().getName());
        serverExceptionResponse.setExceptionTime(new Date());
        serverExceptionResponse.setServerIp(IPUtil.getInstance().getIpAddr(request));
        serverExceptionResponse.setExceptionMsg(e.getMessage());
        serverExceptionResponse.setMethod(request.getMethod());
        serverExceptionResponse.setPath(request.getRequestURI());
        serverExceptionResponse.setStack(stack);
        return new Result<>(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), serverExceptionResponse);
    }

}
