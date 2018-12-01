package com.tecode.mysql.bean;

public class Data {
    private Integer id;

    private String value;

    private Integer xid;

    private Integer legendid;

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
}