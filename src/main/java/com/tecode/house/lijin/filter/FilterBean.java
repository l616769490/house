package com.tecode.house.lijin.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * HBase数据过滤需要用到的Bean
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public class FilterBean {
    /**
     * 过滤类型
     */
    private String type;
    /**
     * 过滤哪个字段
     */
    private String field;
    /**
     * 每个过滤条件对应的过滤规则
     */
    private List<Law> items;

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

    public void addItem(String name, String min, String max) {
        Law law = new Law(name, min, max);
        if(items == null) {
            items = new ArrayList<>();
        }
        items.add(law);
    }

    @Override
    public String toString() {
        return "FilterBean{" +
                "type='" + type + '\'' +
                ", field='" + field + '\'' +
                ", items=" + items +
                '}';
    }

    private class Law {
        private String name;
        private String min;
        private String max;

        Law(String name, String min, String max) {
            this.name = name;
            this.min = min;
            this.max = max;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        @Override
        public String toString() {
            return "Law{" +
                    "name='" + name + '\'' +
                    ", min='" + min + '\'' +
                    ", max='" + max + '\'' +
                    '}';
        }
    }
}
