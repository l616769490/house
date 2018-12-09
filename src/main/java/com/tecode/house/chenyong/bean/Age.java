package com.tecode.house.chenyong.bean;

import scala.Tuple2;

import java.util.List;

public class Age {

    private int maxAge;

    private int minAge;

    private Double avgAge;

    private List<Tuple2<String,Integer>> list;

    public Age(int maxAge, int minAge, Double avgAge, List<Tuple2<String, Integer>> list) {
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.avgAge = avgAge;
        this.list = list;
    }

    public Age() {
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public Double getAvgAge() {
        return avgAge;
    }

    public void setAvgAge(Double avgAge) {
        this.avgAge = avgAge;
    }

    public List<Tuple2<String, Integer>> getList() {
        return list;
    }

    public void setList(List<Tuple2<String, Integer>> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Age{" +
                "maxAge=" + maxAge +
                ", minAge=" + minAge +
                ", avgAge=" + avgAge +
                ", list=" + list +
                '}';
    }
}
