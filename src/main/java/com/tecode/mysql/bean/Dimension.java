package com.tecode.mysql.bean;

public class Dimension {
    private Integer id;

    private String groupname;

    private String dimname;

    private String dimnameen;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname == null ? null : groupname.trim();
    }

    public String getDimname() {
        return dimname;
    }

    public void setDimname(String dimname) {
        this.dimname = dimname == null ? null : dimname.trim();
    }

    public String getDimnameen() {
        return dimnameen;
    }

    public void setDimnameen(String dimnameen) {
        this.dimnameen = dimnameen == null ? null : dimnameen.trim();
    }
}