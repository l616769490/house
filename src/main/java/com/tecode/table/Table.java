package com.tecode.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格
 */
public class Table implements Serializable {
    /**
     * 年份
     */

    private Integer year;
    /**
     * 分页数据
     */
    private Page page;
    /**
     * 搜索信息
     */
    private List<Search> search = new ArrayList<>();
    /**
     * 表头
     */
    private List<String> top = new ArrayList<>();

    /**
     * 表数据
     */
    private List<Row> data = new ArrayList<>();

    public Integer getYear() {
        return year;
    }

    public Table setYear(Integer year) {
        this.year = year;
        return this;
    }

    public Page getPage() {
        return page;
    }

    public Table setPage(Page page) {
        this.page = page;
        return this;
    }

    public List<Search> getSearch() {
        return search;
    }

    public Table addSearchs(Search s) {
        this.search.add(s);
        return this;
    }

    public List<String> getTop() {
        return top;
    }

    public Table addTop(String t) {
        this.top.add(t);
        return this;
    }

    public List<Row> getData() {
        return data;
    }

    public Table addData(Row row) {
        this.data.add(row);
        return this;
    }

    public void setSearch(List<Search> search) {
        this.search = search;
    }

    public void setTop(List<String> top) {
        this.top = top;
    }

    public void setData(List<Row> data) {
        this.data = data;
    }
}
