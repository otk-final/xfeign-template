package com.yryz.example.api;

import com.yryz.example.dto.DynamicResult;
import com.yryz.template.xfeign.XFeignClient;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2023/3/24 上午11:24
 * Created by huangxy
 */
@XFeignClient("example-foo")
public interface DynamicApi {

    DynamicResult doQuery(String type);

}
