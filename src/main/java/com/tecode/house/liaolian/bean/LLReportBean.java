package com.tecode.house.liaolian.bean;

import scala.math.BigInt;

import java.util.Objects;

public class LLReportBean {
    private String name;
    private BigInt create;
    private Integer year ;
    private String group;
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInt getCreate() {
        return create;
    }

    public void setCreate(BigInt create) {
        this.create = create;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LLReportBean that = (LLReportBean) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(create, that.create) &&
                Objects.equals(year, that.year) &&
                Objects.equals(group, that.group) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, create, year, group, status);
    }

    @Override
    public String toString() {
        return "LLReportBean{" +
                "name='" + name + '\'' +
                ", create=" + create +
                ", year=" + year +
                ", group='" + group + '\'' +
                ", status=" + status +
                '}';
    }

    public LLReportBean(String name, BigInt create, Integer year, String group, Integer status) {
        this.name = name;
        this.create = create;
        this.year = year;
        this.group = group;
        this.status = status;
    }

    public LLReportBean() {
    }
}
