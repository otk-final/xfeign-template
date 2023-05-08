package com.yryz.example.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/25 下午3:20
 * Created by huangxy
 */
public class ExampleDto implements Serializable {

    private String f1;

    private BigDecimal f2;


    public ExampleDto() {
    }

    public ExampleDto(String f1, BigDecimal f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    public BigDecimal getF2() {
        return f2;
    }

    public void setF2(BigDecimal f2) {
        this.f2 = f2;
    }
}
