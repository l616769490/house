package com.tecode.house.dengya.bean;

public class Dimension {
    private int id;
    private String groupName;
    private String dimName;
    private String getDimNameEN;

    public Dimension(int id, String groupName, String dimName, String getDimNameEN) {
        this.id = id;
        this.groupName = groupName;
        this.dimName = dimName;
        this.getDimNameEN = getDimNameEN;
    }

    public Dimension() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public String getGetDimNameEN() {
        return getDimNameEN;
    }

    public void setGetDimNameEN(String getDimNameEN) {
        this.getDimNameEN = getDimNameEN;
    }
}

