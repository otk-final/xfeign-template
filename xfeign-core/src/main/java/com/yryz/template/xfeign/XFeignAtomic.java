package com.yryz.template.xfeign;

import java.lang.annotation.*;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午8:07
 * Created by huangxy
 * 本地调用强制声明同步原子执行
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XFeignAtomic {

}
