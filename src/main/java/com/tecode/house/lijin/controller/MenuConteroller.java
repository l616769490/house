package com.tecode.house.lijin.controller;

import com.tecode.pagelist.Group;
import com.tecode.pagelist.PageList;
import com.tecode.pagelist.Report;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 版本：2018/12/8 V1.0
 * 成员：李晋
 */
@Controller
public class MenuConteroller {


    @ResponseBody
    @RequestMapping("/menu")
    public PageList getMenu(){
        PageList  pageList = new PageList();
        pageList.addYear(2011).addYear(2012).addYear(2013).addYear(2014).addYear(2015).addYear(2016);
        Group group1 = new Group("分组1");

        group1.addReport(new Report("图表1","http://166.166.166.166:8080/test1") );
        group1.addReport(new Report("图表2","http://166.166.166.166:8080/test1") );
        group1.addReport(new Report("图表3","http://166.166.166.166:8080/test1") );
        pageList.addGroup(group1);

        Group group2 = new Group("分组2");

        group2.addReport(new Report("图表1","http://166.166.166.166:8080/test1") );
        group2.addReport(new Report("图表2","http://166.166.166.166:8080/test1") );
        group2.addReport(new Report("图表3","http://166.166.166.166:8080/test1") );
        pageList.addGroup(group2);

        return  pageList;
    }
}
