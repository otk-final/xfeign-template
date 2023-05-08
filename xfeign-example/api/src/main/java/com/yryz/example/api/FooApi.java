package com.yryz.example.api;

import com.yryz.template.xfeign.XFeignClient;

import java.util.List;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/19 下午2:43
 * Created by huangxy
 */
@XFeignClient("example-foo")
public interface FooApi {

    String get(String key1, String key2);

    String get(String key1, String key2, String key3);

    String post(List<String> body);

    AllModel model();
}
