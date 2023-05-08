package com.yryz.template.xfeign.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午3:56
 * Created by huangxy
 */
public class GenericParameter implements Serializable {

    private String origin;

    private String target;

    private String api;

    private String method;

    private List<GenericArg> args = new ArrayList<>();

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<GenericArg> getArgs() {
        return args;
    }

    public void setArgs(List<GenericArg> args) {
        this.args = args;
    }
}
