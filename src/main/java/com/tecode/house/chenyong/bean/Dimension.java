package com.tecode.house.chenyong.bean;

public class Dimension {

    private int id;
    private String groupName;
    private String dimName;
    private String dimNameEN;

    public Dimension(int id, String groupName, String dimName, String dimNameEN) {
        this.id = id;
        this.groupName = groupName;
        this.dimName = dimName;
        this.dimNameEN = dimNameEN;
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

    public String getDimNameEN() {
        return dimNameEN;
    }

    public void setDimNameEN(String dimNameEN) {
        this.dimNameEN = dimNameEN;
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", dimName='" + dimName + '\'' +
                ", dimNameEN='" + dimNameEN + '\'' +
                '}';
    }
}
