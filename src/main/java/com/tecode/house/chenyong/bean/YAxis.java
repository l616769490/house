package com.tecode.house.chenyong.bean;

public class YAxis {

    private int id;
    private String name;
    private int diagramId;

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



    public YAxis(int id, String name, int diagramId) {
        this.id = id;
        this.name = name;
        this.diagramId = diagramId;
    }

    public YAxis() {
    }

    @Override
    public String toString() {
        return "XAxis{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", diagramId=" + diagramId +
                '}';
    }
}
