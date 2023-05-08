package com.yryz.example.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Date: Created in 2021 2021/11/24 9:50
 * @Author: zh
 */
public class TestModel implements Serializable {

    private static final long serialVersionUID = 5659043151115087510L;
    private String name;

    private List<TestObject> list;

    private Map<String,TestObject> map;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TestObject> getList() {
        return list;
    }

    public void setList(List<TestObject> list) {
        this.list = list;
    }

    public Map<String, TestObject> getMap() {
        return map;
    }

    public void setMap(Map<String, TestObject> map) {
        this.map = map;
    }

    public static class TestObject implements Serializable{
        private static final long serialVersionUID = 1660737412447156816L;
        private String name;
        private List<String> list;
        private Map<String,String> map;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
