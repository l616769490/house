package com.tecode.house.zzliuhao.bean;

/**
 * Created by Administrator on 2018/12/4.
 */
public class Data {
    private int id;
    private String value;
    private int xId;
    private int legendId;
    private String x;
    private String legend;

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setxId(int xId) {
        this.xId = xId;
    }

    public void setLegendId(int legendId) {
        this.legendId = legendId;
    }

    public void setX(String x) {
        this.x = x;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public int getxId() {
        return xId;
    }

    public int getLegendId() {
        return legendId;
    }

    public String getX() {
        return x;
    }
}
