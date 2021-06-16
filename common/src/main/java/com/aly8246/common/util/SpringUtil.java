package com.aly8246.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class SpringUtil implements ApplicationContextAware {

    public static AnnotationConfigApplicationContext APP;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.APP == null) {
            SpringUtil.APP = (AnnotationConfigApplicationContext) applicationContext;
        }
    }

}