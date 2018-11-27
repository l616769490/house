package com.tecode.echarts;

import com.tecode.echarts.enums.SeriesType;

/**
 * 柱状图
 * 版本：2018/11/20 V1.0
 * 成员：李晋
 */
public class Bar<T> extends Line<T> {
    public Bar() {
        super();
        super.type = SeriesType.bar;
    }
}
