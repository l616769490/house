package com.tecode.table;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息
 */
public class Page {
    private Integer thisPage;
    private List<Integer> data = new ArrayList<>();

    public Integer getThisPage() {
        return thisPage;
    }

    public Page setThisPage(Integer thisPage) {
        this.thisPage = thisPage;
        return this;
    }

    public List<Integer> getData() {
        return data;
    }

    public Page addData(Integer d) {
        this.data.add(d);
        return this;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
