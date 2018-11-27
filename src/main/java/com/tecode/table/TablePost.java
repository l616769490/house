package com.tecode.table;

import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于接收表请求数据
 */
public class TablePost {
    /**
     * 年份
     */
    private Integer year;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 搜索条件
     */
    private List<Search> searches = new ArrayList<>();

    public TablePost() {
        super();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Search> getSearches() {
        return searches;
    }

    public void setSearches(List<Search> searches) {
        this.searches = searches;
    }


}
