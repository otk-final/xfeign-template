package com.yryz.example.foo;

import com.yryz.example.api.AllModel;
import com.yryz.example.api.BarApi;
import com.yryz.example.api.FooApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/19 下午2:56
 * Created by huangxy
 */
@Service
public class FooImpl implements FooApi {

    @Autowired
    private BarApi barApi;

    @Override
    public String get(String key1, String key2) {
        return "foo get out";
    }

    @Override
    public String get(String key1, String key2, String key3) {
        return key3;
    }

    @Override
    public String post(List<String> body) {
        return barApi.put("a", null);
    }

    @Override
    public AllModel model() {
        AllModel m = new AllModel();
        m.setName("parent");
        m.setFiled("field");
        return m;
    }
}
