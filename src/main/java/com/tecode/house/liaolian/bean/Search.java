package com.tecode.house.liaolian.bean;

/**
 * Created by Administrator on 2018/12/6.
 */
public class Search {
    private Integer id;

    private String name;

    private String dimgroupname;

    private Integer reportid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDimgroupname() {
        return dimgroupname;
    }

    public void setDimgroupname(String dimgroupname) {
        this.dimgroupname = dimgroupname == null ? null : dimgroupname.trim();
    }

    public Integer getReportid() {
        return reportid;
    }

    public void setReportid(Integer reportid) {
        this.reportid = reportid;
    }

    @Override
    public String toString() {
        return "Search{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dimgroupname='" + dimgroupname + '\'' +
                ", reportid=" + reportid +
                '}';
    }
}
