package com.tecode.echarts;

/**
 * 绘图网格
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public class Grid {
    /**
     * 直角坐标系内绘图网格左上角横坐标，数值单位px，支持百分比（字符串），如'50%'(显示区域横向中心)
     */
    private String x2;
    /**
     * 直角坐标系内绘图网格左上角纵坐标，数值单位px，支持百分比（字符串），如'50%'(显示区域纵向中心)
     */
    private String y2;
    /**
     * grid 区域是否包含坐标轴的刻度标签，在无法确定坐标轴标签的宽度，容器有比较小无法预留较多空间的时候，可以设为 true 防止标签溢出容器
     */
    private Boolean containLabel;

    public String getX2() {
        return x2;
    }

    public Grid setX2(String x2) {
        this.x2 = x2;
        return this;
    }

    public String getY2() {
        return y2;
    }

    public Grid setY2(String y2) {
        this.y2 = y2;
        return this;
    }

    public Boolean getContainLabel() {
        return containLabel;
    }

    public Grid setContainLabel(Boolean containLabel) {
        this.containLabel = containLabel;
        return this;
    }
}
