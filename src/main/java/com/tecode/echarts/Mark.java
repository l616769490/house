package com.tecode.echarts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标注
 * 版本：2018/11/20 V1.0
 * 成员：李晋
 */
public class Mark {
    private List<Map> data = new ArrayList<>();

    public List<Map> getData() {
        return data;
    }

    public Mark addData(Map d) {
        this.data.add(d);
        return this;
    }
}
