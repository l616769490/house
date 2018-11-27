package com.tecode.echarts.enums;

/**
 * 触发类型
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public enum Trigger {
    /**
     * 数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
     */
    item,
    /**
     * 坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用。
     */
    axis,
    /**
     * 什么都不触发。
     */
    none
}
