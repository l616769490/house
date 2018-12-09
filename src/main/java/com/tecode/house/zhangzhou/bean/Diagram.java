package com.tecode.house.zhangzhou.bean;

public class Diagram {
    /*
    id	int		自增id
name	varchar	50	图表名
type	int	1	0：折线图，1：柱状图，2：饼图
reportId	int		报表id，外键（FK_diagram_report_id）
subtext	varchar	100	描述

     */
    private int id;
    private String name;
    private int type;
    private int reportId;
    private String subtext;

    @Override
    public String toString() {
        return "Diagram{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", reportId=" + reportId +
                ", subtext='" + subtext + '\'' +
                '}';
    }

    public Diagram(String name, int type, int reportId, String subtext) {
        this.name = name;
        this.type = type;
        this.reportId = reportId;
        this.subtext = subtext;
    }

    public Diagram() {
    }

    public Diagram(int id, String name, int type, int reportId, String subtext) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.reportId = reportId;
        this.subtext = subtext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }
}
