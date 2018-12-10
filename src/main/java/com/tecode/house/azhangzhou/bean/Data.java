package com.tecode.house.azhangzhou.bean;

import java.util.Objects;

public class Data {

    /*
    id	int		自增id，维度顺序按id排序
value	varchar	50	数据值
xId	int		x轴id
legendId	int		图例id
 `x` varchar(50)    x维度名    DEFAULT NULL,
  `legend` varchar(50)  数据集维度名  DEFAULT NULL,
     */
    private int id;
    private String value;
    private int xId;
    private int legendId;
    private String x;
    private String legend;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return id == data.id &&
                xId == data.xId &&
                legendId == data.legendId &&
                Objects.equals(value, data.value) &&
                Objects.equals(x, data.x) &&
                Objects.equals(legend, data.legend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, xId, legendId, x, legend);
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", xId=" + xId +
                ", legendId=" + legendId +
                ", x='" + x + '\'' +
                ", legend='" + legend + '\'' +
                '}';
    }

    public Data(int id, String value, int xId, int legendId, String x, String legend) {
        this.id = id;
        this.value = value;
        this.xId = xId;
        this.legendId = legendId;
        this.x = x;
        this.legend = legend;
    }

    public Data(String value, int xId, int legendId, String x, String legend) {
        this.value = value;
        this.xId = xId;
        this.legendId = legendId;
        this.x = x;
        this.legend = legend;
    }

    public Data() {
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
