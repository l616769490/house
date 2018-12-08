package com.tecode.house.dengya.bean;

public class Report {
    private int id;
    private String name;
    private Long create;
    private int year;
    private String group;
    private int status;
    private String url;

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

    public Long getCreate() {
        return create;
    }

    public void setCreate(Long create) {
        this.create = create;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Report(int id, String name, Long create, int year, String group, int status, String url) {
        this.id = id;
        this.name = name;
        this.create = create;
        this.year = year;
        this.group = group;
        this.status = status;
        this.url = url;
    }

    public Report() {
    }
}
