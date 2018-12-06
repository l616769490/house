package com.tecode.house.liaolian.bean;

/**
 * Created by Administrator on 2018/12/6.
 */
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

    @Override
    public String toString() {
        return "YAxis{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", diagramid=" + diagramid +
                '}';
    }
}
