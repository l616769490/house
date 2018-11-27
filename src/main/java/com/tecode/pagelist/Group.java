package com.tecode.pagelist;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表组
 */
public class Group {
    /**
     * 组名
     */
    private String name;
    /**
     * 报表
     */
    private List<Report> report = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public Group addReport(Report r) {
        report.add(r);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<Report> getReport() {
        return report;
    }
}
