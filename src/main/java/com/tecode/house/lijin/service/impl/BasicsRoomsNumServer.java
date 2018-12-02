package com.tecode.house.lijin.service.impl;

import com.tecode.echarts.Option;
import com.tecode.house.lijin.service.InsertMysqlServer;
import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.mysql.bean.Diagram;
import com.tecode.mysql.bean.Report;
import com.tecode.mysql.dao.*;

import java.util.List;
import java.util.Map;

/**
 * 基础-房间数统计-房间数
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public class BasicsRoomsNumServer extends InsertMysqlServer {
    /**
     * 报表组
     */
    private static final String BASICS_GROUP_NAME = ConfigUtil.get("BASICS_GROUP_NAME");
    /**
     * 报表表名字
     */
    private static final String BASICS_ROOM_NUM_REPORT_NAME = ConfigUtil.get("BASICS_ROOM_NUM_REPORT_NAME");

    public BasicsRoomsNumServer() {
        super(ConfigUtil.get("mybatis-config2"));
    }

    @Override
    public void insert(Map<String, String> datas, int year) {
        // 报表表>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        Report report = getReport(year);
        System.out.println(report);
        // 图表表
        List<Diagram> diagrams = getDiagrams(report);
        System.out.println(diagrams);
        // 取出需要的维度
        // x轴
        // y轴
        // 图例
        // 数据
        // 搜索

        // 提交并关闭事务
        // close();
    }

    /**
     * 查询图表表是否存在，存在则获取，不存在则创建
     *
     * @param report 报表表
     * @return 图表表集
     */
    private List<Diagram> getDiagrams(Report report) {
        DiagramMapper diagramMapper = session.getMapper(DiagramMapper.class);
        List<Diagram> diagrams = diagramMapper.selectByReportKey(report.getId());
        if (diagrams == null || diagrams.size() == 0) {
            System.out.println("图表表不存在");
        }
        return diagrams;
    }

    /**
     * 查询报表表是否存在，存在则获取，不存在则创建
     *
     * @param year 报表所属年份
     * @return 报表表
     */
    private Report getReport(int year) {
        ReportMapper reportMapper = session.getMapper(ReportMapper.class);
        Report report = reportMapper.selectByNameAndYear(BASICS_ROOM_NUM_REPORT_NAME, year);
        if (report == null) {
            report = new Report();
            report.setCreate(System.currentTimeMillis());
            report.setGroup(BASICS_GROUP_NAME);
            report.setName(BASICS_ROOM_NUM_REPORT_NAME);
            report.setStatus(0);
            report.setYear(year);
            // 写入报表数据
            reportMapper.insert(report);
        }
        return report;
    }

}
