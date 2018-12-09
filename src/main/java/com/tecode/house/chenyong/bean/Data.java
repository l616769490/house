package com.tecode.house.chenyong.bean;

public class Data {

    private int Id;

    private String value;

    private int xId;

    private int legendId;

    private String x;

    private String legend;

    public Data(int id, String value, int xId, int legendId) {
        Id = id;
        this.value = value;
        this.xId = xId;
        this.legendId = legendId;
    }

    public Data(int id, String value, int xId, int legendId, String x, String legend) {
        Id = id;
        this.value = value;
        this.xId = xId;
        this.legendId = legendId;
        this.x = x;
        this.legend = legend;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public Data() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getxId() {
        return xId;
    }

    public void setxId(int xId) {
        this.xId = xId;
    }

    public int getLegendId() {
        return legendId;
    }

    public void setLegendId(int legendId) {
        this.legendId = legendId;
    }

    @Override
    public String toString() {
        return "Data{" +
                "Id=" + Id +
                ", value='" + value + '\'' +
                ", xId=" + xId +
                ", legendId=" + legendId +
                '}';
    }
}
