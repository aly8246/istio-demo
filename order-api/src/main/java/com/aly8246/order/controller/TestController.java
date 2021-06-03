package com.aly8246.order.controller;

import com.aly8246.common.res.Result;
import com.aly8246.order.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Api(value = "测试控制器")
public class TestController {

    @ApiOperation(value = "测试200响应",nickname = "测试一下200红红火火恍恍惚惚")
    @GetMapping("1")
    public Result<Void> test1(){
        return Result.ok();
    }

    @ApiOperation(value = "测试200响应",nickname = "测试一下200红红火火恍恍惚惚")
    @GetMapping("2")
    public User test2(){
        return new User(1,"");
    }
}
