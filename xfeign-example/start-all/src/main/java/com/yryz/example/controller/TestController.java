package com.yryz.example.controller;

import com.google.common.collect.Lists;
import com.yryz.example.api.AllModel;
import com.yryz.example.api.BarApi;
import com.yryz.example.api.FooApi;
import com.yryz.example.api.TestModel;
import com.yryz.framework.core.util.ResponseUtils;
import com.yryz.framework.core.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/19 下午3:03
 * Created by huangxy
 */
@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private BarApi barApi;

    @Autowired
    private FooApi fooApi;


    @GetMapping("/subType")
    public Response<String> subType() {

        //形参 实参数 一致
        TestModel model = new TestModel();
        model.setName("org");
        barApi.testModel(model);


        //形参 实参数 不一致 server存在
        AllModel allModel = new AllModel("field");
        allModel.setName("org");
        barApi.testModel(allModel);


        //形参 实参数 不一致 server不存在
        SubMode subMode = new SubMode("fn");
        barApi.testModel(subMode);

        return null;
    }


    public static class SubMode extends TestModel {
        private String fn;

        public SubMode(String fn) {
            this.fn = fn;
        }
    }


    @GetMapping("/test")
    public Response<String> testRest() {
//        //barApi.put("1", "2");
        barApi.delete(Lists.newArrayList("123"));
//
//        try {
//            int a = barApi.len("1", "2", "3");
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return ResponseUtils.returnObjectSuccess("xxx");
//        }
//
//        int b = barApi.len("1", null, "3");
//
//        barApi.len(new int[]{1, 2});

        List<TestModel> list = new ArrayList<>();
        Map<String, TestModel> map = new HashMap<>();
        TestModel testModel = new TestModel();
        testModel.setName("testModel");
        TestModel.TestObject testObject = new TestModel.TestObject();
        testObject.setName("testObject");
        testObject.setList(Arrays.asList("a", "b"));
        Map<String, String> data = new HashMap<>();
        data.put("a", "b");
        testObject.setMap(data);
        testModel.setList(Arrays.asList(testObject));
        Map<String, TestModel.TestObject> mapD = new HashMap<>();
        mapD.put("a", testObject);
        testModel.setMap(mapD);
        map.put("test", testModel);
        list.add(testModel);
//        barApi.testModel(testModel);
//        barApi.testModel(map);
        barApi.testModel(list);
        return null;
    }


   /* @Transactional
    public void a(){
        //a报错 b的事务不会回滚
        //b报错 a的事务回滚
        b();
    }

    @Transactional
    @XFeignCrossTransaction
    public void b(){

    }*/
}
