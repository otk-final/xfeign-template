package com.yryz.template.xfeign;

import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/12/17 上午11:47
 * Created by huangxy
 */
public final class XFeignUtils {

    /**
     * 统一抛出业务方的Runtime异常
     *
     * @param e
     * @return
     */
    public static RuntimeException throwRuntimeException(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        //从线程池中获取异常
        if (e instanceof ExecutionException) {
            e = e.getCause();
        }
        //从反射method.invoke获取异常
        if (e instanceof InvocationTargetException) {
            e = e.getCause();
        }
        //判断是否为业务异常，不是则包装业务异常进行返回
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }


    public static String shortClassNameParse(String className) {
        if (StringUtils.isEmpty(className)) {
            return "";
        }
        String[] packages = className.split("\\.");
        StringBuilder shortPath = new StringBuilder();
        for (int i = 0; i < packages.length; i++) {
            if (i == packages.length - 1) {
                shortPath.append(packages[i]);
            } else {
                shortPath.append(packages[i].charAt(0)).append(".");
            }
        }
        return shortPath.toString();
    }
}
