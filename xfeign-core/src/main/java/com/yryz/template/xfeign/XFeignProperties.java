package com.yryz.template.xfeign;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午5:06
 * Created by huangxy
 */
@ConfigurationProperties(prefix = "xfeign.client")
public class XFeignProperties implements Serializable {

    private boolean enable = true;

    /**
     * 默认服务
     */
    private String service;

    /**
     * 模块映射
     */
    private Map<String, String> modules = new HashMap<>();

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<String, String> getModules() {
        return modules;
    }

    public void setModules(Map<String, String> modules) {
        this.modules = modules;
    }
}
