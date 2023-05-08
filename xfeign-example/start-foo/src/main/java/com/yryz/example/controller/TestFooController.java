package com.yryz.example.controller;

import com.yryz.example.api.AllModel;
import com.yryz.example.api.BarApi;
import com.yryz.example.api.FooApi;
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
public class TestFooController {

    @Autowired
    private BarApi barApi;

    @Autowired
    private FooApi fooApi;


    @GetMapping("/base/feign")
    public Response<AllModel> doQuery() {
        AllModel model = new AllModel();
        model.setName("xxx");
        return ResponseUtils.returnObjectSuccess(model);
    }
}
