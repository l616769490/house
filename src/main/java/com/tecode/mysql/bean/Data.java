package com.tecode.mysql.bean;

public class Data {
    private Integer id;

    private String value;

    private Integer xid;

    private Integer legendid;

    private String x;

    private String legend;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Integer getXid() {
        return xid;
    }

    public void setXid(Integer xid) {
        this.xid = xid;
    }

    public Integer getLegendid() {
        return legendid;
    }

    public void setLegendid(Integer legendid) {
        this.legendid = legendid;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x == null ? null : x.trim();
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend == null ? null : legend.trim();
    }
}