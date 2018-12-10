package com.tecode.house.azhangzhou.bean;

public class Xaxis {
    /*
    id	int		自增id
name	varchar	20	x轴显示的名字
diagramId	int		表id，外键（FK_xAxis_diagram_id）
dimGroupName	varchar	50	维度组名字（对应维度表的维度组名）

     */
    private int id;
    private String name;
    private int diagramId;
    private String dimGroupName;

    @Override
    public String toString() {
        return "Xaxis{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", diagramId=" + diagramId +
                ", dimGroupName='" + dimGroupName + '\'' +
                '}';
    }

    public Xaxis(int id, String name, int diagramId, String dimGroupName) {
        this.id = id;
        this.name = name;
        this.diagramId = diagramId;
        this.dimGroupName = dimGroupName;
    }

    public Xaxis(String name, int diagramId, String dimGroupName) {
        this.name = name;
        this.diagramId = diagramId;
        this.dimGroupName = dimGroupName;
    }

    public Xaxis() {
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

    public String getDimGroupName() {
        return dimGroupName;
    }

    public void setDimGroupName(String dimGroupName) {
        this.dimGroupName = dimGroupName;
    }
}
