package com.tecode.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索值
 */
public class Search implements Serializable {
    /**
     * 搜索条件，如：按地区
     */
    private String title;
    /**
     * 对应的具体搜索值，如：西部地区
     */
    private List<String> values = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public Search setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getValues() {
        return values;
    }

    public Search addValue(String value) {
        this.values.add(value);
        return this;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
