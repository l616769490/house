package com.tecode.house.zouchao.bean;

import scala.Tuple2;
import scala.collection.immutable.List;


public class Rent {
    private List<Tuple2<String, Integer>> list;

    public Rent( List<Tuple2<String, Integer>> list) {
        this.list = list;
    }


    public void setList(List<Tuple2<String, Integer>> list) {
        this.list = list;
    }

    public List<Tuple2<String, Integer>> getList() {
        return list;
    }
}
