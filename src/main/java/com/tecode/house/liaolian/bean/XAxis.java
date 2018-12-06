package com.tecode.house.liaolian.bean;

/**
 * Created by Administrator on 2018/12/6.
 */
public class XAxis {
    private Integer id;

    private String name;

    private Integer diagramid;

    private String dimgroupname;

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

    public String getDimgroupname() {
        return dimgroupname;
    }

    public void setDimgroupname(String dimgroupname) {
        this.dimgroupname = dimgroupname == null ? null : dimgroupname.trim();
    }

    @Override
    public String toString() {
        return "XAxis{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", diagramid=" + diagramid +
                ", dimgroupname='" + dimgroupname + '\'' +
                '}';
    }
}
