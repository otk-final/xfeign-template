package com.yryz.example.bar;

import com.alibaba.fastjson.JSONObject;
import com.yryz.example.api.BarApi;
import com.yryz.example.api.FooApi;
import com.yryz.example.api.TestModel;
import com.yryz.framework.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/19 下午2:48
 * Created by huangxy
 */
@Service
public class BarImpl implements BarApi {

    private static Logger logger=LoggerFactory.getLogger(BarImpl.class);

    @Autowired
    private FooApi fooApi;

    @Override
    public String put(String key1, String key2) {
        for (int i = 0; i < 1000; i++) {
            fooApi.get(key1, key2, "bar put out");
        }
        return fooApi.get(key1, key2, "bar put out");
    }

    @Override
    public String delete(List<String> body) {
        return "bar delete out";
    }

    @Override
    public int len(String... keys) {

//        throw new BusinessException("xxx");
        return new Random().nextInt(100);
    }

    @Override
    public int len(int[] values) {
        return 0;
    }

    @Override
    public String testModel(List<TestModel> list) {
        logger.info("list:{}",JSONObject.toJSONString(list));
        return null;
    }

    @Override
    public String testModel(TestModel model) {
        logger.info("obj:{}",JSONObject.toJSONString(model));
        return null;
    }

    @Override
    public String testModel(Map<String, TestModel> map) {
        logger.info("map:{}",JSONObject.toJSONString(map));
        return null;
    }
}
