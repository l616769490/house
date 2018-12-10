package com.tecode.house.lijun.bean;

public class Search {
    /*
    id	int		自增id，维度顺序按id排序
name	varchar	50	名字
dimGroupName	varchar	50	维度组名字（对应维度表的维度组名）
reportId	int		报表id，外键（FK_search_report_id）

     */

    private int id;
    private String name;
    private String dimGroupName;
    private int reportId;

    @Override
    public String toString() {
        return "Search{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dimGroupName='" + dimGroupName + '\'' +
                ", reportId=" + reportId +
                '}';
    }

    public Search(String name, String dimGroupName, int reportId) {
        this.name = name;
        this.dimGroupName = dimGroupName;
        this.reportId = reportId;
    }

    public Search() {
    }

    public Search(int id, String name, String dimGroupName, int reportId) {
        this.id = id;
        this.name = name;
        this.dimGroupName = dimGroupName;
        this.reportId = reportId;
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
}
