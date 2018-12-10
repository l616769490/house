package com.tecode.house.dengya.bean;

import java.util.List;

public class Page {
    private int pageNum;    //当前页，从请求那边传过来
    private int pageSize;   //每页显示的数据条数

    //需要计算的
    private int potalPage; //总页数，通过totalrecord和pageSize计算可以得来
    private int starIndex;  //开始索引

    //煤业显示的数据放在list集合中
    private List<List<String>> list;


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPotalPage() {
        return potalPage;
    }

    public void setPotalPage(int potalPage) {
        this.potalPage = potalPage;
    }

    public int getStarIndex() {
        return starIndex;
    }

    public void setStarIndex(int starIndex) {
        this.starIndex = starIndex;
    }

    public List<List<String>> getList() {
        return list;
    }

    public void setList(List<List<String>> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", potalPage=" + potalPage +
                ", starIndex=" + starIndex +
                ", list=" + list +
                '}';
    }
}
