package com.yryz.example.controller;

import com.yryz.example.api.AllModel;
import com.yryz.example.api.BaseFeignApi;
import com.yryz.example.api.DynamicApi;
import com.yryz.example.api.FooApi;
import com.yryz.example.dto.DynamicResult;
import com.yryz.framework.core.util.ResponseUtils;
import com.yryz.framework.core.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/12/17 下午5:04
 * Created by huangxy
 */
@RestController
public class TestBarController {

    @Autowired
    private DynamicApi dynamicApi;

    @Autowired
    private FooApi fooApi;

    @GetMapping("/bar/dynamic")
    public Response<DynamicResult> dynamic() {
        DynamicResult result = dynamicApi.doQuery("subB");

        AllModel m = fooApi.model();
        return ResponseUtils.returnObjectSuccess(result);
    }

    @Autowired
    private BaseFeignApi feignApi;

    @GetMapping("/bar/feign")
    public Response<AllModel> feign() {
        Response<AllModel> res = feignApi.doQuery();
        return res;
    }
}
