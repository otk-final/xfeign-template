package com.yryz.template.xfeign.generic;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午3:58
 * Created by huangxy
 */
public class GenericArg implements Serializable {

    //参数idx
    private int index;

    //型参
    private String classType;

    //实参
    private String argType;

    //值
    private JsonNode value;

    public String getArgType() {
        return argType;
    }

    public void setArgType(String argType) {
        this.argType = argType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public JsonNode getValue() {
        return value;
    }

    public void setValue(JsonNode value) {
        this.value = value;
    }
}
