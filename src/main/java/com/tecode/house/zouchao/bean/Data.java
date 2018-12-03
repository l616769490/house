package com.tecode.house.zouchao.bean;

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

    public Data(String value, int xId, int legendId) {
        this.value = value;
        this.xId = xId;
        this.legendId = legendId;
    }

    public Data() {
    }

    public Data(int id, String value, int xId, int legendId) {
        this.id = id;
        this.value = value;
        this.xId = xId;
        this.legendId = legendId;
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
}
