package com.yryz.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
//@EnableFeignClients
@EnableFeignProxyClients(basePackages = "com.yryz.example")
public class StartAllApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StartAllApplication.class, args);
    }
}
