package com.tecode.house.libo.bean;

public class Data {

    /*
    id	int		自增id，维度顺序按id排序
value	varchar	50	数据值
xId	int		x轴id
legendId	int		图例id

     */
    private int id;
    private String value;
    private int xId;
    private int legendId;
    private String x;
    private String legend;

    public Data(){}
    public Data(int id, String value, int xId, int legendId, String x, String legend) {
        this.id = id;
        this.value = value;
        this.xId = xId;
        this.legendId = legendId;
        this.x = x;
        this.legend = legend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
