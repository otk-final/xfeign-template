package com.yryz.template.xfeign.generic;

import com.fasterxml.jackson.databind.JsonNode;
import jdk.nashorn.internal.ir.ObjectNode;

import java.io.Serializable;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午4:03
 * Created by huangxy
 * 返回数据统一采用string接受
 */
public class GenericResult implements Serializable {

    private String returnType;

    private JsonNode returnData;

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public JsonNode getReturnData() {
        return returnData;
    }

    public void setReturnData(JsonNode returnData) {
        this.returnData = returnData;
    }
}
