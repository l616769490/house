package com.tecode.house.liaolian.service.impl;

import com.tecode.house.lijin.service.InsertMysqlServer;
import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.mysql.bean.*;
import com.tecode.mysql.dao.*;
import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
public class InsertBasicsRoomsNumServer extends InsertMysqlServer {
    /**
     * 报表组
     */
//    private static final String BASICS_GROUP_NAME = "基础分析";
    /**
     * 报表表名字
     */
//    private static final String BASICS_ROOM_NUM_REPORT_NAME = "基础-房间数分析";

    /**
     * 图表名字
     */
//    private static final String BASICS_ROOM_NUM_DIAGRAM_NAME = "房间数和卧室数分布图";

    /**
     * 副标题
     */
//    private static final String BASICS_ROOM_NUM_SUBNAME = "房间数卧室数分布趋势";

    /**
     * x轴名字
     */
//    private static final String X_AXIS_NAME = "分类";
    /**
     * y轴名字
     */
//    private static final String Y_AXIS_NAME = "数量";

    /**
     * x轴维度组
     */
//    private static final String X_DIM_GROUP_NAME = "房间数";

    /**
     * 图例名
     */
//    private static final String LEGEND_NAME = "房间数卧室数";

    /**
     * 图例维度组
     */
//    private static final String LEGEND_DDIM_GROUP_NAME = "房间数卧室数统计";
    public InsertBasicsRoomsNumServer() {
        super(ConfigUtil.get("mybatis-config2"));
    }

    @Override
    public void insert(Map<String, Map<String, String>> datas, int year) {
        // 报表表
        Report report = getReport(year, "基础-房间数分析", "基础分析", "http://166.166.0.13:8080/basics_rooms_num");
//        System.out.println("报表：");
//        System.out.println(report);
        // 图表表
        List<Diagram> diagrams = getDiagrams(report, new String[]{"房间数和卧室数分布图"}, new String[]{"房间数卧室数分布趋势"});
//        System.out.println("图表：");
//        System.out.println(diagrams);
        // x轴
        List<XAxis> xAxes = getXAxes(diagrams.get(0), new String[]{"分类"}, new String[]{"房间数"});
//        System.out.println("X轴：");
//        System.out.println(xAxes);

        // y轴
        List<YAxis> yAxes = getYAxes(diagrams.get(0), new String[]{"数量"});
//        System.out.println("Y轴：");
//        System.out.println(yAxes);

        // 图例
        List<Legend> legends = getLefends(diagrams.get(0), new String[]{"房间数卧室数"}, new String[]{"房间数卧室数统计"});
//        System.out.println("图例：");
//        System.out.println(legends);

        // 生成数据库能识别的数据集
        List<Data> dataList = getDatas(report, datas, xAxes.get(0), legends.get(0));
        // 写入数据
        insertDataList(dataList);
//        System.out.println("数据：");
//        System.out.println(dataList);

        // 修改状态
        reStatus(report);
//        System.out.println("更改状态：");
//        System.out.println(report);

        // 提交并关闭事务
        close();
    }
}