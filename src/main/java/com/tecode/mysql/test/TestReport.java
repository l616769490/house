package com.tecode.mysql.test;

import com.tecode.mysql.bean.Report;
import com.tecode.mysql.dao.ReportMapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 版本：2018/12/6 V1.0
 * 成员：李晋
 */
public class TestReport {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ReportMapper mapper = context.getBean(ReportMapper.class);
        Report report = mapper.selectByNameAndYear("基础-房间数分析", 2013);
        System.out.println(report);
    }
}
