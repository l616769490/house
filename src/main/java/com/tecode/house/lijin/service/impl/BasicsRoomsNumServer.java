package com.tecode.house.lijin.service.impl;

import com.tecode.house.lijin.service.InsertMysqlServer;
import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.mysql.bean.*;
import com.tecode.mysql.dao.*;

import java.util.ArrayList;
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
    private static final String BASICS_GROUP_NAME = "基础分析";
    /**
     * 报表表名字
     */
    private static final String BASICS_ROOM_NUM_REPORT_NAME = "基础-房间数分析";

    /**
     * 图表名字
     */
    private static final String BASICS_ROOM_NUM_DIAGRAM_NAME = "房间数和卧室数分布图";

    /**
     * 副标题
     */
    private static final String BASICS_ROOM_NUM_SUBNAME = "房间数卧室数分布趋势";

    /**
     * x轴名字
     */
    private static final String X_AXIS_NAME = "分类";
    /**
     * y轴名字
     */
    private static final String Y_AXIS_NAME = "数量";

    /**
     * x轴维度组
     */
    private static final String X_DIM_GROUP_NAME = "";

    /**
     * 图例名
     */
    private static final String LEGEND_NAME = "";

    /**
     * 图例维度组
     */
    private static final String LEGEND_DDIM_GROUP_NAME = "";

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
        // x轴
        List<XAxis> xAxes = getXAxes(diagrams.get(0));
        System.out.println(xAxes);

        // y轴
        List<YAxis> yAxes = getYAxes(diagrams.get(0));
        System.out.println(yAxes);

        // 图例
        List<Legend> legends = getLefends(diagrams.get(0));
        System.out.println(legends);

        // 数据
        // 搜索

        // 提交并关闭事务
        // close();
    }

    /**
     * 获取图例
     *
     * @param diagram 图表
     * @return 图例
     */
    private List<Legend> getLefends(Diagram diagram) {
        LegendMapper mapper = session.getMapper(LegendMapper.class);
        LegendExample legendExample = new LegendExample();
        legendExample.or().andDiagramidEqualTo(diagram.getId());
        List<Legend> legends = mapper.selectByExample(legendExample);
        if (legends == null || legends.size() == 0) {
            Legend legend = new Legend();
            legend.setDiagramid(diagram.getId());
            legend.setDimgroupname(LEGEND_DDIM_GROUP_NAME);
            legend.setName(LEGEND_NAME);
            legends.add(legend);
            mapper.insert(legend);
        }
        return legends;
    }

    /**
     * 获取y轴
     *
     * @param diagram 图表
     * @return y轴
     */
    private List<YAxis> getYAxes(Diagram diagram) {
        YAxisMapper mapper = session.getMapper(YAxisMapper.class);
        YAxisExample yAxisExample = new YAxisExample();
        yAxisExample.or().andDiagramidEqualTo(diagram.getId());
        List<YAxis> yAxes = mapper.selectByExample(yAxisExample);
        if (yAxes == null || yAxes.size() == 0) {
            YAxis yAxis = new YAxis();
            yAxis.setDiagramid(diagram.getId());
            yAxis.setName(Y_AXIS_NAME);
            yAxes = new ArrayList<>();
            yAxes.add(yAxis);
            mapper.insert(yAxis);
        }
        return yAxes;
    }

    /**
     * 获取x轴
     *
     * @param diagram 图表
     * @return x轴
     */
    private List<XAxis> getXAxes(Diagram diagram) {
        XAxisMapper mapper = session.getMapper(XAxisMapper.class);
        XAxisExample xAxisExample = new XAxisExample();
        xAxisExample.or().andDiagramidEqualTo(diagram.getId());
        List<XAxis> xAxes = mapper.selectByExample(xAxisExample);
        if (xAxes == null || xAxes.size() == 0) {
            XAxis xAxis = new XAxis();
            xAxis.setName(X_AXIS_NAME);
            xAxis.setDiagramid(diagram.getId());
            xAxis.setDimgroupname(X_DIM_GROUP_NAME);
            xAxes = new ArrayList<>();
            xAxes.add(xAxis);
            mapper.insert(xAxis);
        }

        return xAxes;
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
            // 不存在则创建图表
            Diagram diagram = new Diagram();
            diagram.setName(BASICS_ROOM_NUM_DIAGRAM_NAME);
            diagram.setReportid(report.getId());
            diagram.setSubtext(BASICS_ROOM_NUM_SUBNAME);
            diagram.setType(1);
            diagrams = new ArrayList<>();
            diagrams.add(diagram);
            diagramMapper.insert(diagram);
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
