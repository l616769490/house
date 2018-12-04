package com.tecode.house.lijin.service.impl;

import com.tecode.house.lijin.service.InsertMysqlServer;
import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.mysql.bean.*;

import java.util.List;
import java.util.Map;

/**
 * 插入数据：按地区-房产税分析
 * 版本：2018/12/4 V1.0
 * 成员：李晋
 */
public class InsertRegionZsmhcNumServer  extends InsertMysqlServer {
    public InsertRegionZsmhcNumServer() {
        super(ConfigUtil.get("mybatis-config2"));
    }

    @Override
    public void insert(Map<String, Map<String, String>> datas, int year) {
        // 报表表
        Report report = getReport(year, "按区域-房产税分析", "区域统计", "http://166.166.0.13:8080/region_zxmhc_num");

        // 图表表
        List<Diagram> diagrams = getDiagrams(report, new String[]{"各区域房产税分布图"}, new String[]{"各房产税阶段户数统计"});

        // x轴
        List<XAxis> xAxes = getXAxes(diagrams.get(0), new String[]{"分类"}, new String[]{"房产税"});


        // y轴
        List<YAxis> yAxes = getYAxes(diagrams.get(0), new String[]{"户数"});


        // 图例
        List<Legend> legends = getLefends(diagrams.get(0), new String[]{"区域划分"}, new String[]{"区域"});


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
