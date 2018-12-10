package com.tecode.house.chenyong.bean;

public class Search {

    private int id;

    private String name;

    private String dimGroupName;

    private int reportId;

    public Search(int id, String name, String dimGroupName, int reportId) {
        this.id = id;
        this.name = name;
        this.dimGroupName = dimGroupName;
        this.reportId = reportId;
    }

    public Search(String name, String dimGroupName, int reportId) {
        this.name = name;
        this.dimGroupName = dimGroupName;
        this.reportId = reportId;
    }

    public Search() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDimGroupName() {
        return dimGroupName;
    }

    public void setDimGroupName(String dimGroupName) {
        this.dimGroupName = dimGroupName;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Search{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dimGroupName='" + dimGroupName + '\'' +
                ", reportId=" + reportId +
                '}';
    }
}
