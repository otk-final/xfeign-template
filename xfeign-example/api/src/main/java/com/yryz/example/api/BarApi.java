package com.yryz.example.api;

import com.yryz.template.xfeign.XFeignAtomic;
import com.yryz.template.xfeign.XFeignClient;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/19 下午2:43
 * Created by huangxy
 */
@XFeignClient("example-bar")
public interface BarApi {


    String put(String key1, String key2);

    String delete(List<String> body);

    /**
     * 本地项目同步执行
     * @param keys
     * @return
     */
    @XFeignAtomic
    int len(String... keys);

    int len(int[] values);

    String testModel(List<TestModel> list);

    String testModel(TestModel model);

    String testModel(Map<String,TestModel> map);


}
