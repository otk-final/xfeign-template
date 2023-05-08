package com.yryz.example.dto;


import java.io.Serializable;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2023/3/24 上午11:26
 * Created by huangxy
 */
public class DynamicResult implements Serializable {

    private String type;

    private SubB sb;

    private SubA sa;

    private IDynamicDefine define;

    public SubB getSb() {
        return sb;
    }

    public void setSb(SubB sb) {
        this.sb = sb;
    }

    public SubA getSa() {
        return sa;
    }

    public void setSa(SubA sa) {
        this.sa = sa;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public IDynamicDefine getDefine() {
        return define;
    }

    public void setDefine(IDynamicDefine define) {
        this.define = define;
    }
}
