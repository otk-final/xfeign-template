package com.yryz.template.xfeign.delegator;

import java.lang.reflect.Method;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/18 下午7:27
 * Created by huangxy
 */
public interface XFeignInvocationDispatch {

    Object invoke(Object proxy, Method method, Object[] argv) throws Throwable;

    /**
     * XFeignBeanPreBinding 调用toString 时转换
     *
     * @return
     */
    String toStringConvert();
}
