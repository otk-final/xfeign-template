package com.yryz.example.api;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2022/5/14 下午5:02
 * Created by huangxy
 */
public class AllModel extends TestModel {

    private String filed;

    public AllModel() {
    }

    public AllModel(String filed) {
        this.filed = filed;
    }

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }
}
