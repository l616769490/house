package com.tecode.house.lijun.bean;

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

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", create=" + create +
                ", year=" + year +
                ", group='" + group + '\'' +
                ", status=" + status +
                ", url='" + url + '\'' +
                '}';
    }

    public Report() {
    }

    public Report(String name, long create, int year, String group, int status, String url) {
        this.name = name;
        this.create = create;
        this.year = year;
        this.group = group;
        this.status = status;
        this.url = url;
    }

    public Report(int id, String name, long create, int year, String group, int status, String url) {
        this.id = id;
        this.name = name;
        this.create = create;
        this.year = year;
        this.group = group;
        this.status = status;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
