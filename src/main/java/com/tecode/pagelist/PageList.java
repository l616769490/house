package com.tecode.pagelist;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于返回页面列表
 */
public class PageList {
    private List<Integer> year = new ArrayList<>();
    private List<Group> group = new ArrayList<>();

    /**
     * 添加年份
     *
     * @param y 年
     * @return 自身
     */
    public PageList addYear(int y) {
        year.add(y);
        return this;
    }

    /**
     * 添加报表组
     *
     * @param g 报表组
     * @return 自身
     */
    public PageList addGroup(Group g) {
        group.add(g);
        return this;
    }

    public List<Integer> getYear() {
        return year;
    }

    public List<Group> getGroup() {
        return group;
    }
}
