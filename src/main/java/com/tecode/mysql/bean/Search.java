package com.tecode.mysql.bean;

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
}