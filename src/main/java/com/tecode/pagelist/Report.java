package com.tecode.pagelist;

/**
 * 报表
 */
public class Report {
    private String name;
    private String url;

    public Report(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
