package com.tecode.mysql.server;

import com.tecode.mysql.bean.ReportExample;
import com.tecode.mysql.dao.ReportMapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 版本：2018/12/14 V1.0
 * 成员：李晋
 */
public class DelSQL {
    /**
     * 清空整年的报表
     *
     * @param year 年份
     */
    public static void delSqlByYear(Integer year) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ReportMapper mapper = context.getBean(ReportMapper.class);
        ReportExample reportExample = new ReportExample();
        reportExample.or().andYearEqualTo(year);
        mapper.deleteByExample(reportExample);
    }

    public static void main(String[] args) {
        new DelSQL().delSqlByYear(2013);
    }
}
