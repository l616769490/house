package com.tecode.house.zzliuhao.bean;

public class Report {

    /*

id	int		自增id
name	varchar	50	报表名
create	bigint		创建时间的时间戳
year	int	4	报表所统计的年份
group	varchar	50	所属分组的名字
status	int	1	0：创建中，1：已创建
     */
    private int id;
    private String name;
    private long create;
    private int year;
    private String group;
    private int status;
    private String url;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreate(long create) {
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

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public long getCreate() {
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

    public String getUrl() {
        return url;
    }
}
