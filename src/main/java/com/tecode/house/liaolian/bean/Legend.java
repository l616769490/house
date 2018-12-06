package com.tecode.house.liaolian.bean;

/**
 * Created by Administrator on 2018/12/6.
 */
public class Legend {
    private Integer id;

    private String name;

    private String dimgroupname;

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

    public String getDimgroupname() {
        return dimgroupname;
    }

    public void setDimgroupname(String dimgroupname) {
        this.dimgroupname = dimgroupname == null ? null : dimgroupname.trim();
    }

    public Integer getDiagramid() {
        return diagramid;
    }

    public void setDiagramid(Integer diagramid) {
        this.diagramid = diagramid;
    }

    @Override
    public String toString() {
        return "Legend{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dimgroupname='" + dimgroupname + '\'' +
                ", diagramid=" + diagramid +
                '}';
    }
}
