package com.tecode.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 一行数据
 */
public class Row implements Serializable {
    private List<String> row = new ArrayList<>();

    public List<String> getRow() {
        return row;
    }

    public Row addRow(String r) {
        this.row.add(r);
        return this;
    }

    public void setRow(List<String> row) {
        this.row = row;
    }
}
