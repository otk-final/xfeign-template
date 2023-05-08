package com.yryz.example.foo;

import com.yryz.example.api.DynamicApi;
import com.yryz.example.dto.DynamicResult;
import com.yryz.example.dto.SubA;
import com.yryz.example.dto.SubB;
import org.springframework.stereotype.Service;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2023/3/24 上午11:28
 * Created by huangxy
 */
@Service
public class DynamicImpl implements DynamicApi {

    @Override
    public DynamicResult doQuery(String type) {
        DynamicResult result = new DynamicResult();
        result.setType(type);
        if (type.equalsIgnoreCase("subA")) {
            SubA a = new SubA();
            a.setValueA("valueA");
            a.setMode(type);
            result.setSa(a);
            result.setDefine(a);
        } else {
            SubB b = new SubB();
            b.setValueB("valueB");
            b.setMode(type);
            b.setName("xxxB");
            result.setSb(b);
            result.setDefine(b);
        }
        return result;
    }
}
