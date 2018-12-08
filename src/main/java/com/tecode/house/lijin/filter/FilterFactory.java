package com.tecode.house.lijin.filter;

/**
 * 过滤器工厂
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public class FilterFactory {
    private FilterFactory() {
    }

    /**
     * 根据类型获取过滤器
     *
     * @param type 过滤类型
     * @return 过滤器
     */
    public static HBaseFilter getFilter(String type) {
        switch (type) {
            case "equals":
                return new EqualsFilter();
            case "field":
                return new FieldFilter();
            case "range":
                return new RangeFilter();
            default:
                return null;
        }
    }
}
