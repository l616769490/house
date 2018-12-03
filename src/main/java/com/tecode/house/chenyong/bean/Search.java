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

    public Search() {
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
