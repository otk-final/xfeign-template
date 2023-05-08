package com.yryz.example.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2023/3/24 上午11:25
 * Created by huangxy
 */
//    多态序列化

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "mode",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SubA.class, name = "subA"),
        @JsonSubTypes.Type(value = SubB.class, name = "subB"),
})
public abstract class IDynamicDefine implements Serializable {

    private String mode;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
