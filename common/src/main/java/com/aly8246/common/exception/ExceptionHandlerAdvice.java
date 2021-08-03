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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public Result<Object> handleException(Exception e) {
        if (e.getMessage() != null) log.error(e.getMessage(), e);
        else log.error("unknown exception message!", e);

        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), e.getMessage());

        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), exceptionRes);
    }

    /**
     * 请求method不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), e.getMessage());
        return result(ERROR_OPERATE.getCode(), ERROR_OPERATE.getMsg(), exceptionRes);
    }

    /**
     * json转换异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), e.getMessage());
        return result(ERROR_OPERATE.getCode(), ERROR_OPERATE.getMsg(), exceptionRes);
    }


    /**
     * 线程池执行异常
     */
    @ExceptionHandler(ExecutionException.class)
    public Result<?> handleExecutionException(ExecutionException e) {
        log.error(e.getMessage(), e);

        Throwable cause = e.getCause();

        //线程池执行包装feign异常
        if (cause instanceof FeignException) {
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
     *
     * @param e feign异常
     */
    @ExceptionHandler(FeignException.class)
    public Result<?> handleFeignException(FeignException e) {
        return handleAllFeignException(e);
    }

    /**
     * 处理业务异常BaseException
     * 包含的404主要是发起404响应
     *
     * @param e 业务异常
     * @return 统一响应体
     */
    @ExceptionHandler(ServerException.class)
    public Result<?> handleBaseException(ServerException e) {
        log.error(e.getMessage());

        //从异常中获取枚举类
        ResultCode code = e.getCode();

        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), e.getMessage());

        //响应http状态码大于等于700 说明是业务异常，直接抛出
        if (code.getCode() >= BUSINESS_EXCEPTION.getCode()) {
            return notAccept(code.getCode(), code.getMsg(), exceptionRes);
        }

        //响应http状态码为404 说明是资源不存在，直接抛出
        if (code.getCode() == RESOURCES_NOT_EXIST.getCode()) {
            return result(RESOURCES_NOT_EXIST.getCode(), RESOURCES_NOT_EXIST.getMsg(), exceptionRes);
        }

        //进行其他异常处理
        return result(SERVICE_NOT_UNAVAILABLE.getCode(), SERVICE_NOT_UNAVAILABLE.getMsg(), exceptionRes);
    }

    /**
     * feign接受到的异常
     *
     * @param e 404 700 other:503
     * @return 异常响应
     */
    private Result<?> handleAllFeignException(FeignException e) {
        if (e instanceof FeignException.ServiceUnavailable) {
            //503 上游服务无正常实例，开启熔断
            response.setStatus(NO_HEALTHY_UPSTREAM.getCode());
            return Result.res(NO_HEALTHY_UPSTREAM.getCode(), "上游服务中断", null);
        }

        //取出原本的错误异常，转为result对象
        Result<?> result = JsonUtil.jsonToObject(e.contentUTF8(), Result.class);

        if (e instanceof FeignException.NotFound) {
            //404 由Exception和ExecutionException捕获的feign 404资源不存在
            response.setStatus(RESOURCES_NOT_EXIST.getCode());
        } else if (e.status() >= BUSINESS_EXCEPTION.getCode()) {
            //大于等于700 业务异常,抛出业务异常数据,然后传递到前端
            response.setStatus(BUSINESS_EXCEPTION.getCode());
        } else {
            //503 未处理的feign异常,直接抛出,并且由istio接管熔断
            response.setStatus(SERVICE_NOT_UNAVAILABLE.getCode());
        }

        return result;
    }


    public Result<Object> result(int code, String msg, Object data) {
        response.setStatus(code);
        return new Result<>(code, msg, data);
    }

    public Result<Object> notAccept(int code, String msg, Object data) {
        response.setStatus(BUSINESS_EXCEPTION.getCode());
        return new Result<>(code, msg, data);
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        System.out.println(request.getRequestURI());
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        log.warn(methodArgumentNotValidException.getMessage());
        LinkedHashMap<String, String> result = IntStream
                .range(0, bindingResult.getFieldErrorCount())
                .mapToObj(i -> bindingResult.getFieldErrors()
                        .get(i))
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::toString,
                        (k, v) -> v,
                        LinkedHashMap::new));
        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), result);

        return result(ERROR_OPERATE.getCode(), ERROR_OPERATE.getMsg(), exceptionRes);
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Object> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        System.out.println(request.getRequestURI());
        log.warn(constraintViolationException.getMessage());
        LinkedHashMap<String, String> result = constraintViolationException.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(e -> e.getPropertyPath().toString().split("\\.")[1],
                        ConstraintViolation::getMessage,
                        (a, b) -> b,
                        LinkedHashMap::new));
        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), result);

        return result(ERROR_OPERATE.getCode(), ERROR_OPERATE.getMsg(), exceptionRes);
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException bindException) {
        System.out.println(request.getRequestURI());
        log.warn(bindException.getMessage());
        BindingResult bindingResult = bindException.getBindingResult();
        LinkedHashMap<String, String> result = IntStream
                .range(0, bindingResult.getFieldErrorCount())
                .mapToObj(i -> bindingResult.getFieldErrors()
                        .get(i))
                .collect(Collectors.toMap(FieldError::getField,
                        e -> Objects.requireNonNull(e.getDefaultMessage()).split(":")[1],
                        (a, b) -> b,
                        LinkedHashMap::new));

        ExceptionRes exceptionRes = new ExceptionRes(springBootInfo.getService(), request.getRequestURI(), result);

        return result(ERROR_OPERATE.getCode(), ERROR_OPERATE.getMsg(), exceptionRes);
    }

}
