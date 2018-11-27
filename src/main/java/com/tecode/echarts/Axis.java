package com.tecode.echarts;

import com.tecode.echarts.enums.AxisType;

import java.util.ArrayList;
import java.util.List;

/**
 * 坐标轴
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public class Axis {
    /**
     * 是否显示
     */
    private Boolean show = true;
    /**
     * 坐标轴类型，横轴默认为类目型'category'，纵轴默认为数值型'value'
     */
    private AxisType type;
    /**
     * 坐标轴数值
     */
    private List<String> data = new ArrayList<>();

    public Boolean getShow() {
        return show;
    }

    public Axis setShow(Boolean show) {
        this.show = show;
        return this;
    }

    public AxisType getType() {
        return type;
    }

    public Axis setType(AxisType type) {
        this.type = type;
        return this;
    }

    public List<String> getData() {
        return data;
    }

    public Axis addData(String d) {
        this.data.add(d);
        return this;
    }
}
