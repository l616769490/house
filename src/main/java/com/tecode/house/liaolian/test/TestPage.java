package com.tecode.house.liaolian.test;

import com.tecode.pagelist.Group;
import com.tecode.pagelist.PageList;
import com.tecode.pagelist.Report;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试获取表格
 */
@Controller
public class TestPage {

    @ResponseBody
    @RequestMapping(value = "/test-page", method = RequestMethod.POST)
    public PageList testPage() {
        return new PageList()
                .addYear(2011).addYear(2013).addYear(2015)
                .addGroup(new Group("基础分析")
                        .addReport(new Report("年龄分析", "http://www.baidu.com"))
                        .addReport(new Report("房价分析", "http://www.baidu.com"))
                ).addGroup(new Group("地区分析")
                        .addReport(new Report("地区年龄分析", "http://www.baidu.com"))
                        .addReport(new Report("地区房价分析", "http://www.baidu.com"))
                );
    }
}
