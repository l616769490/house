package com.tecode.echarts;

import com.tecode.echarts.enums.SeriesType;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.*;

/**
 * 饼图
 * 版本：2018/11/20 V1.0
 * 成员：李晋
 */
public class Pie extends Series<Pie.PieData> {
    /**
     * 半径，支持百分数和像素值
     */
    protected String radius = "75%";

    /**
     * 圆心，支持百分数和像素值
     */
    protected List<String> center = new ArrayList<>(2);

    /**
     * 数据集
     */
    protected List<Pie.PieData> data = new ArrayList<>();

    public Pie() {
        super();
        super.type = SeriesType.pie;
        center.add("50%");
        center.add("50%");
    }

    public String getRadius() {
        return radius;
    }

    public Pie setRadius(String radius) {
        this.radius = radius;
        return this;
    }

    public List<String> getCenter() {
        return center;
    }

    /**
     * 设置圆心坐标，支持像素和百分比
     */
    public Pie setCenter(String x, String y) {
        this.center.set(0, x);
        this.center.set(1, y);
        return this;
    }

    @Override
    public Pie addData(Pie.PieData d) {
        data.add(d);
        return this;
    }

    public List<PieData> getData() {
        return data;
    }

    public static class PieData<E> {
        private String name;
        private E value;

        public PieData(String name, E value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }


        public E getValue() {
            return value;
        }

    }
}


