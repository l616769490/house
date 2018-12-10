package com.tecode.house.chenyong.bean;

/**
 * Created by Administrator on 2018/12/9.
 */
public class Param {
    int legendId = 0;
    int xId = 0;

    public Param(int legendId, int xId) {
        this.legendId = legendId;
        this.xId = xId;
    }

    public Param() {
    }

    public int getLegendId() {
        return legendId;
    }

    public void setLegendId(int legendId) {
        this.legendId = legendId;
    }

    public int getxId() {
        return xId;
    }

    public void setxId(int xId) {
        this.xId = xId;
    }

    @Override
    public String toString() {
        return "Param{" +
                "legendId=" + legendId +
                ", xId=" + xId +
                '}';
    }
}
