package com.tecode.house.dengya.service.impl;

import com.tecode.house.dengya.bean.Page;

import java.util.List;

/**
 *  private int pageNum;    //当前页，从请求那边传过来
 *     private int pageSize;   //每页显示的数据条数
 *
 *     //需要计算的
 *     private int potalPage; //总页数，通过totalrecord和pageSize计算可以得来
 *     private int starIndex;  //开始索引
 *
 *     //煤业显示的数据放在list集合中
 *     private List<List<String>> list;
 */
public class PageLimit {
    public static void  limit(Page page, List<List<String[]>> lists){
        //传入一个存放data数据的list集合
        List<List<String[]>> list = lists;
        //刚开始的页面为第一页
        if(page.getPageNum() == 0){
            page.setPageNum(1);
        }else{
            page.setPageNum(page.getPageNum());
        }

        //设置每页数据为十条
        page.setPageSize(10);
        //每页的开始数
        page.setStarIndex((page.getPageNum() -1) * page.getPageSize());
        //list的大小
        int count = list.size();
        //设置总页数
        page.setPotalPage(count % 10 == 0 ?count /10 :count /10 +1 );
     
    }
}
