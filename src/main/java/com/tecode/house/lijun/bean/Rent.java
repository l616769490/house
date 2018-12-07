package com.tecode.house.lijun.bean;

import scala.Tuple2;
import scala.collection.immutable.List;


public class Rent {
    private int max;
    private int min;
    private double avg;
    private List<Tuple2<String, Integer>> list;

    public Rent(int max, int min, double avg, List<Tuple2<String, Integer>> list) {
        this.max = max;
        this.min = min;
        this.avg = avg;
        this.list = list;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public void setList(List<Tuple2<String, Integer>> list) {
        this.list = list;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public double getAvg() {
        return avg;
    }

    public List<Tuple2<String, Integer>> getList() {
        return list;
    }
}
