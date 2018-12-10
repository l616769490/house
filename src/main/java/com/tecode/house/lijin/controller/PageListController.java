package com.tecode.house.lijin.controller;

import com.tecode.mysql.bean.Report;
import com.tecode.mysql.bean.ReportExample;
import com.tecode.mysql.dao.ReportMapper;
import com.tecode.pagelist.Group;
import com.tecode.pagelist.PageList;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 报表详单获取
 */
@Controller
public class PageListController {

    @Autowired
    private ReportMapper reportMapper;

    // <年份， 报表组>
    private Map<Integer, PageList> pageListMap;
    @ResponseBody
    @RequestMapping("/menu")
    public PageList getMenu(Integer year) {
        if(year == null || year == 0) {
            year = 2013;
        }
        if(pageListMap == null) {
            pageListMap = new HashedMap();
            Set<Integer> yearSet = new HashSet<>();
             // <年份， <报表组名, Group>>
            Map<Integer, Map<String, Group>> groupOfYear = new TreeMap<>();
            List<Report> reportList = reportMapper.selectByExample(new ReportExample());
            for (Report report : reportList) {
                yearSet.add(report.getYear());
                // 取出当年的报表列表 <报表组名， Group>
                Map<String, Group> map = groupOfYear.get(report.getYear());
                if (map == null) {
                    map = new HashedMap();
                    groupOfYear.put(report.getYear(),map);
                }
                // 取出报表分组
                Group group1 = map.get(report.getGroup());
                if (group1 == null) {
                    group1 = new Group(report.getGroup());
                    map.put(report.getGroup(), group1);
                }
                group1.addReport(new com.tecode.pagelist.Report(report.getName(), report.getUrl()));
            }

            // 添加报表组
            for (Map.Entry<Integer, Map<String, Group>> mapEntry : groupOfYear.entrySet()) {

                PageList pageList = pageListMap.get(mapEntry.getKey());
                if(pageList == null) {
                    pageList = new PageList();
                    for (Integer y : yearSet) {
                        pageList.addYear(y);
                    }
                    pageListMap.put(mapEntry.getKey(), pageList);
                }
                Map<String, Group> mapEntryValue = mapEntry.getValue();
                for (Map.Entry<String, Group> groupEntry : mapEntryValue.entrySet()) {
                    pageList.addGroup(groupEntry.getValue());
                }
            }
        }

        return pageListMap.get(year);
    }

    @RequestMapping("/toMenu")
    public String toMenu() {
        return "../menu";
    }

    public static void main(String[] args) {
        PageListController pageListController = new PageListController();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        pageListController.reportMapper = context.getBean(ReportMapper.class);
        PageList menu = pageListController.getMenu(2013);
        System.out.println(menu);
    }
}
