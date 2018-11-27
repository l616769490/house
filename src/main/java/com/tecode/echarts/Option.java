package com.tecode.echarts;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public class Option {

    /**
     * 表头
     */
    private Title title;
    /**
     * 提示框
     */
    private Tooltip tooltip;

    /**
     * 图例，每个图表最多仅有一个图例，混搭图表共享
     */
    private Legend legend;

    /**
     * 直角坐标系内绘图网格
     */
    private Grid grid;

    /**
     * 是否显示拖拽用的手柄（手柄能拖拽调整选中范围）。
     */
    private Boolean calculable  = true;

    /**
     * 直角坐标系中横轴数组，数组中每一项代表一条横轴坐标轴，最多同时存在2条横轴
     */
    private List<Axis> xAxis = new ArrayList<>();
    /**
     * 直角坐标系中纵轴数组，数组中每一项代表一条纵轴坐标轴，最多同时存在2条纵轴
     */
    private List<Axis> yAxis = new ArrayList<>();
    /**
     * 驱动图表生成的数据内容，数组中每一项代表一个系列的特殊选项及数据
     */
    private List<Series> series = new ArrayList<>();


    public Title getTitle() {
        return title;
    }

    public Option setTitle(Title title) {
        this.title = title;
        return this;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public Option setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public Legend getLegend() {
        return legend;
    }

    public Option setLegend(Legend legend) {
        this.legend = legend;
        return this;
    }


    public Grid getGrid() {
        return grid;
    }

    public Option setGrid(Grid grid) {
        this.grid = grid;
        return this;
    }

    public Boolean getCalculable() {
        return calculable;
    }

    public Option setCalculable(Boolean calculable) {
        this.calculable = calculable;
        return this;
    }

    public List<Axis> getxAxis() {
        return xAxis;
    }

    public Option addxAxis(Axis x) {
        this.xAxis.add(x);
        return this;
    }

    public List<Axis> getyAxis() {
        return yAxis;
    }

    public Option addyAxis(Axis y) {
        this.yAxis.add(y);
        return this;
    }

    public List<Series> getSeries() {
        return series;
    }

    public Option addSeries(Series serie) {
        this.series.add(serie);
        return this;
    }
}
