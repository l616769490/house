package com.tecode.house.dengya.bean;

public class Diagram {
    private int id;
    private String name;
    private int type;
    private int reportId;
    private String subtext;

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
