package com.tecode.mysql.bean;

public class YAxis {
    private Integer id;

    private String name;

    private Integer diagramid;

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

    public Integer getDiagramid() {
        return diagramid;
    }

    public void setDiagramid(Integer diagramid) {
        this.diagramid = diagramid;
    }
}