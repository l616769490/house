package com.tecode.house.jianchenfei.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HBase数据过滤需要用到的Bean，从配置文件里面读取
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public class FilterBean implements Serializable {
    /**
     * 过滤类型
     */
    private String type;
    /**
     * 过滤哪个字段
     */
    private String field;
    /**
     * 数据分组字段
     */
    private String groupName;
    /**
     * or 还是 and
     */
    private String rule;
    /**
     * 每个过滤条件对应的过滤规则
     */
    private List<Law> items;

    /**
     * 中文-数据库列名  对照表
     */
    private Map<String, String> columns;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<Law> getItems() {
        return items;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public FilterBean addColumn(String zh, String us) {
        if (columns == null) {
            columns = new HashMap<>();
        }
        columns.put(zh, us);
        return this;
    }

    public FilterBean addItem(String name, String min, String max) {
        Law law = new Law(name, min, max);
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(law);
        return this;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setItems(List<Law> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "FilterBean{" +
                "type='" + type + '\'' +
                ", field='" + field + '\'' +
                ", groupName='" + groupName + '\'' +
                ", rule='" + rule + '\'' +
                ", items=" + items +
                ", columns=" + columns +
                '}';
    }
}
