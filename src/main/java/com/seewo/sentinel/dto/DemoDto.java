package com.seewo.sentinel.dto;

public class DemoDto {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DemoDto{" +
                "name='" + name + '\'' +
                '}';
    }
}

