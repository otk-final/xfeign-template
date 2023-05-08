package com.yryz.template.xfeign.generic;


import com.yryz.framework.core.vo.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午4:00
 * Created by huangxy
 * <p>
 * 服务级别代理接口
 */
@FeignClient("${xfeign.client.service:${spring.application.name}}")
public interface XFeignGenericApi {


    @PostMapping("/xfeign/execute")
    Response<GenericResult> execute(@RequestBody GenericParameter body);
}
