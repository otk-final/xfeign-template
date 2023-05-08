package com.yryz.template.xfeign;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/12/6 下午1:56
 * Created by huangxy
 */
public class XFeignThreadPool {

    private String name;

    private ThreadPoolTaskExecutor taskExecutor;

    public XFeignThreadPool(String name, ThreadPoolTaskExecutor taskExecutor) {
        this.name = name;
        this.taskExecutor = taskExecutor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ThreadPoolTaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}
