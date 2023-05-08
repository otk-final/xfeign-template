package com.yryz.template.xfeign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/19 下午6:57
 * Created by huangxy
 * <p>
 * 代理接口注册完成，设置Dispatch
 */
public class XFeignBeanPreBinding implements BeanPostProcessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        /**
         * 提前调用代理默认方法，触发dispatch绑定，防止运行时触发
         */
        if (beanName.endsWith("XFeignDelegator")) {
            String target = bean.toString();
            logger.debug("【XFeignRegister】：{} <=> {}", XFeignUtils.shortClassNameParse(beanName), target);
        }
        return bean;
    }
}
