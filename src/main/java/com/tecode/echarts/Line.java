package com.tecode.echarts;

import com.tecode.echarts.enums.SeriesType;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本：2018/11/20 V1.0
 * 成员：李晋
 */
public class Line<T> extends Series<T> {

    /**
     * 数据集
     */
    protected List<T> data = new ArrayList<>();

    public Line() {
        super();
        super.type = SeriesType.line;
    }

    public List<T> getData() {
        return data;
    }


    @Override
    public Line<T> addData(T d) {
        this.data.add(d);
        return this;
    }
}
