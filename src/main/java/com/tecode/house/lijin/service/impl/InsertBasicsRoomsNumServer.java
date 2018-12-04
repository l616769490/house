package com.tecode.house.lijin.service.impl;

import com.tecode.house.lijin.service.InsertMysqlServer;
import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.mysql.bean.*;
import com.tecode.mysql.dao.*;
import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基础-房间数统计
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public class InsertBasicsRoomsNumServer extends InsertMysqlServer {

    public InsertBasicsRoomsNumServer() {
        super(ConfigUtil.get("mybatis-config2"));
    }

    @Override
    public void insert(Map<String, Map<String, String>> datas, int year) {
        // 报表表
        Report report = getReport(year, "基础-房间数分析", "基础分析", "http://166.166.0.13:8080/basics_rooms_num");

        // 图表表
        List<Diagram> diagrams = getDiagrams(report, new String[]{"房间数和卧室数分布图"}, new String[]{"房间数卧室数分布趋势"});

        // x轴
        List<XAxis> xAxes = getXAxes(diagrams.get(0), new String[]{"分类"}, new String[]{"房间数"});

        // y轴
        List<YAxis> yAxes = getYAxes(diagrams.get(0), new String[]{"数量"});

        // 图例
        List<Legend> legends = getLefends(diagrams.get(0), new String[]{"房间数卧室数"}, new String[]{"房间数卧室数统计"});

        // 生成数据库能识别的数据集
        List<Data> dataList = getDatas(report, datas, xAxes.get(0), legends.get(0));
        // 写入数据
        insertDataList(dataList);

        // 修改状态
        reStatus(report);

        // 提交并关闭事务
        close();
    }
}
