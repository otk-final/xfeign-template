package com.yryz.example.api;

import com.yryz.framework.core.vo.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2023/3/24 上午11:24
 * Created by huangxy
 */
@FeignClient("xfeign-foo")
public interface BaseFeignApi {

    @GetMapping("/base/feign")
    Response<AllModel> doQuery();
}
