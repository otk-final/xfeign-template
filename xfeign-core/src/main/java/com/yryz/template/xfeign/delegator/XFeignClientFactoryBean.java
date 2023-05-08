package com.yryz.template.xfeign.delegator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午4:09
 * Created by huangxy
 */
public class XFeignClientFactoryBean implements FactoryBean<Object>, ApplicationContextAware {

    private Class<?> type;

    private String module;

    private String typeBeanName;

    private String moduleBindServiceId;

    private ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, new XFeignInvocationHandler(applicationContext, type, typeBeanName, module, moduleBindServiceId));
    }

    @Override
    public String toString() {
        return "XFeignClientFactoryBean{" +
                "type=" + type +
                ", module='" + module + '\'' +
                ", typeBeanName='" + typeBeanName + '\'' +
                ", moduleBindServiceId='" + moduleBindServiceId + '\'' +
                '}';
    }

    public String getTypeBeanName() {
        return typeBeanName;
    }

    public void setTypeBeanName(String typeBeanName) {
        this.typeBeanName = typeBeanName;
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleBindServiceId() {
        return moduleBindServiceId;
    }

    public void setModuleBindServiceId(String moduleBindServiceId) {
        this.moduleBindServiceId = moduleBindServiceId;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
