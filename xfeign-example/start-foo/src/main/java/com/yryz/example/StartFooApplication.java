package com.yryz.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignProxyClients;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/19 下午3:00
 * Created by huangxy
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignProxyClients(basePackages = {"com.yryz.example","com.template.xfeign"})
public class StartFooApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StartFooApplication.class, args);
    }
}
