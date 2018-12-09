package com.tecode.house.chenyong.bean;

public class Report {

    private int id;
    private String name;
    private Long create;
    private int year;
    private String group;
    private int status;

    public Report(int id, String name, Long create, int year, String group, int status) {
        this.id = id;
        this.name = name;
        this.create = create;
        this.year = year;
        this.group = group;
        this.status = status;
    }

    public Report() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCreate() {
        return create;
    }

    public int getYear() {
        return year;
    }

    public String getGroup() {
        return group;
    }

    public int getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreate(Long create) {
        this.create = create;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", create=" + create +
                ", year=" + year +
                ", group='" + group + '\'' +
                ", status=" + status +
                '}';
    }


}
