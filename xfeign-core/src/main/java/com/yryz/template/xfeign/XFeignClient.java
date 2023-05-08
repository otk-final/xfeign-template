package com.yryz.template.xfeign;

import org.springframework.cloud.openfeign.FeignClient;

import java.lang.annotation.*;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午3:39
 * Created by huangxy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XFeignClient {

    /**
     * 模块名
     *
     * @return
     */
    String value();

    /**
     * 独立配置
     *
     * @return
     */
    FeignClient client() default @FeignClient();
}
