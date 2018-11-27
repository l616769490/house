package com.tecode.echarts;

import com.tecode.echarts.enums.SeriesType;
import java.io.Serializable;

/**
 * 数据
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public abstract class Series<T> implements Serializable {
    /**
     * 名称
     */
    protected String name;
    /**
     * 图表类型
     */
    protected SeriesType type;
    /**
     * 标注
     */
    protected Mark markPoint;
    /**
     * 标线
     */
    protected Mark markLine;

    /**
     * 使用的 x 轴的 index，在单个图表实例中存在多个 x 轴的时候有用。
     */
    protected Integer xAxisIndex = 0;

    /**
     * 使用的 y 轴的 index，在单个图表实例中存在多个 y轴的时候有用。
     */
    protected  Integer yAxisIndex = 0;




    public Integer getxAxisIndex() {
        return xAxisIndex;
    }

    public void setxAxisIndex(Integer xAxisIndex) {
        this.xAxisIndex = xAxisIndex;
    }

    public Integer getyAxisIndex() {
        return yAxisIndex;
    }

    public void setyAxisIndex(Integer yAxisIndex) {
        this.yAxisIndex = yAxisIndex;
    }

    public String getName() {
        return name;
    }

    public Series<T> setName(String name) {
        this.name = name;
        return this;
    }

    public SeriesType getType() {
        return type;
    }

    public Mark getMarkPoint() {
        return markPoint;
    }

    public Series<T> setMarkPoint(Mark markPoint) {
        this.markPoint = markPoint;
        return this;
    }

    public Mark getMarkLine() {
        return markLine;
    }

    public Series<T> setMarkLine(Mark markLine) {
        this.markLine = markLine;
        return this;
    }

    public abstract Series<T> addData(T d);
}
