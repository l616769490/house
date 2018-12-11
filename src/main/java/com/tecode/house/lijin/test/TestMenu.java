package com.tecode.house.lijin.test;

import com.tecode.pagelist.Group;
import com.tecode.pagelist.PageList;
import com.tecode.pagelist.Report;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @version ：2018/12/11 V1.0
 * @author: 李晋
 */
@Controller
public class TestMenu {
    /**
     * 测试获取列表信息
     * @return 列表信息
     */
    @ResponseBody
    @RequestMapping(value = "/test-page-list", method = RequestMethod.POST)
    public PageList testMenu() {
        PageList pl = new PageList();
        pl.addYear(2011).addYear(2013);
        Group group1 = new Group("基础分析");
        group1.addReport(new Report("测试报表一", "/test-line"))
                .addReport(new Report("测试报表二", "/test-line1"))
                .addReport(new Report("测试报表三", "/test-line2"))
                .addReport(new Report("测试报表四", "/test-line3"));
        pl.addGroup(group1);

        Group group2 = new Group("地区分析");
        group2.addReport(new Report("测试报表五", "/test-bar"))
                .addReport(new Report("测试报表六", "/test-bar2"))
                .addReport(new Report("测试报表七", "/test-bar2"))
                .addReport(new Report("测试报表八", "/test-bar3"));
        pl.addGroup(group2);

        Group group3 = new Group("家庭分析");
        group3.addReport(new Report("测试报表九", "/test-houseDuty"))
                .addReport(new Report("测试报表十", "/test-houseDuty1"));
        pl.addGroup(group3);

        return pl;
    }
}
