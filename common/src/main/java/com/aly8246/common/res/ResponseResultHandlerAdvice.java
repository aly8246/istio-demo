package com.aly8246.common.res;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import springfox.documentation.spring.web.plugins.Docket;

@Component
@ControllerAdvice
@Slf4j
public class ResponseResultHandlerAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 只支持不包含swagger资源
     * @param returnType 返回类型
     * @param converterType 转换类型
     * @return 是否支持
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        if (returnType.getDeclaringClass().toString().contains("swagger")) return false;

        return !returnType.getDeclaringClass().equals(Docket.class);
    }


    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(MediaType.APPLICATION_JSON.equals(selectedContentType) || MediaType.APPLICATION_JSON_UTF8.equals(selectedContentType)){ // 判断响应的Content-Type为JSON格式的body
            if(body instanceof Result){ // 如果响应返回的对象为统一响应体，则直接返回body
                return body;
            }else{
                // 只有正常返回的结果才会进入这个判断流程，所以返回正常成功的状态码
              return new Result<>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(),body);
            }
        }
        // 非JSON格式body直接返回即可
        return body;
    }
}
