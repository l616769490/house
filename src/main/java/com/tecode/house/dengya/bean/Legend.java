package com.tecode.house.dengya.bean;

public class Legend {
    private int id;
    private String name;
    private String dimGroupName;
    private int diagramId;

    public Legend(int id, String name, String dimGroupName, int diagramId) {
        this.id = id;
        this.name = name;
        this.dimGroupName = dimGroupName;
        this.diagramId = diagramId;
    }

    public Legend() {
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

    public int getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(int diagramId) {
        this.diagramId = diagramId;
    }
}
