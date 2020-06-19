package com.data.model;

public class CoeffInfo {

    private String name;
    private Double value;

    public CoeffInfo() {
    }

    public CoeffInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
