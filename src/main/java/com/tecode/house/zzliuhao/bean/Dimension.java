package com.tecode.house.zzliuhao.bean;

public class Dimension {
    /*
    id	int		自增id，维度顺序按id排序
groupName	varchar	50	维度组名字
dimName	varchar	50	维度名
dimNameEN	varchar	50	维度组对应的英文名

     */

    private int id;
    private String groupName;
    private String dimName;
    private String dimNameEN;

    public Dimension(String groupName, String dimName, String dimNameEN) {
        this.groupName = groupName;
        this.dimName = dimName;
        this.dimNameEN = dimNameEN;
    }

    public Dimension() {
    }

    public Dimension(int id, String groupName, String dimName, String dimNameEN) {
        this.id = id;
        this.groupName = groupName;
        this.dimName = dimName;
        this.dimNameEN = dimNameEN;
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
}
