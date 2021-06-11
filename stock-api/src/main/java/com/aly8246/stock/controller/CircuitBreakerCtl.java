package com.aly8246.stock.controller;

import com.aly8246.common.util.IDUtil;
import com.aly8246.stock.entity.UnavailableCtl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CircuitBreakerCtl{
    private final RedisTemplate<String,Object> redisTemplate;
    public final static Long serverId= IDUtil.nextSnowflakeId();

    @PostConstruct
    public void init(){
        log.info("服务器ID:"+serverId);
        redisTemplate.opsForValue().set("unavailable_ctl:"+serverId,new UnavailableCtl(true,serverId),30L, TimeUnit.SECONDS);
    }

    /**
     * 永远把存活时间更新到30s
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void update(){
        redisTemplate.opsForValue().set("unavailable_ctl:"+serverId,new UnavailableCtl(true,serverId),30L, TimeUnit.SECONDS);
    }

}
