package com.tecode.house.lijin.filter;

import java.io.Serializable;

/**
 * 版本：2018/12/6 V1.0
 * 成员：李晋
 */
public class Law implements Serializable {
    private String name;
    private String min;
    private String max;

    Law(String name, String min, String max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Law{" +
                "name='" + name + '\'' +
                ", min='" + min + '\'' +
                ", max='" + max + '\'' +
                '}';
    }
}
