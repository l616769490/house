package com.tecode.house.liuhao.bean;

public class Legend {
    /*
id	int		自增id，维度顺序按id排序
name	varchar	50	名字
dimGroupName	varchar	50	维度组名字（对应维度表的维度组名）
diagramId	int		表id，外键（FK_legend_diagram_id）

     */
    private int id;
    private String name;
    private String dimGroupName;
    private int diagramId;

    public Legend(String name, String dimGroupName, int diagramId) {
        this.name = name;
        this.dimGroupName = dimGroupName;
        this.diagramId = diagramId;

    }

    public Legend() {
    }

    public Legend(int id, String name, String dimGroupName, int diagramId) {
        this.id = id;
        this.name = name;
        this.dimGroupName = dimGroupName;
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
