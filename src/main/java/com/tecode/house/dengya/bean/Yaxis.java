package com.tecode.house.dengya.bean;

public class Yaxis {
    private int id;
    private String name;
    private int diagramId;

    public Yaxis(int id, String name, int diagramId) {
        this.id = id;
        this.name = name;
        this.diagramId = diagramId;
    }

    public Yaxis() {
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

    public int getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(int diagramId) {
        this.diagramId = diagramId;
    }
}
