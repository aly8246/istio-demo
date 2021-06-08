package com.aly8246.common.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class JacksonWebConfiguration {

    private static ObjectMapper OBJECT_MAPPER = null;

    @SneakyThrows
    public static ObjectMapper getObjectMapper() {
        if (OBJECT_MAPPER == null) {
            TimeUnit.SECONDS.sleep(1);
            return getObjectMapper();
        }
        return OBJECT_MAPPER;
    }


    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new SimpleModule().addDeserializer(BaseEnum.class, baseEnumDeserializerConfiguration));

        //
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //设置日期格式
        //   DateFormat dateFormat = objectMapper.getDateFormat();
        // objectMapper.setDateFormat(new CXDateFormat(dateFormat));

        //设置日期格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        format.setTimeZone(timeZone);
        objectMapper.setDateFormat(format);
        objectMapper.setTimeZone(timeZone);

        //设置Long序列化String
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Long.TYPE, ToStringSerializer.instance)
        );

        //注册objectMapper
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

        //设置中文编码格式
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);

        OBJECT_MAPPER = objectMapper;
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public HttpMessageConverters jacksonHttpMessageConverters(@Qualifier("mappingJackson2HttpMessageConverter") MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        return new HttpMessageConverters(mappingJackson2HttpMessageConverter);
    }
}
