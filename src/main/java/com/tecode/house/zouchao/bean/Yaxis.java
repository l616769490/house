package com.tecode.house.zouchao.bean;

public class Yaxis {
    /*
    id	int		自增id
name	varchar	20	x轴显示的名字
diagramId	int		表id，外键（FK_xAxis_diagram_id）

     */
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

    public Yaxis(String name, int diagramId) {
        this.name = name;
        this.diagramId = diagramId;
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
