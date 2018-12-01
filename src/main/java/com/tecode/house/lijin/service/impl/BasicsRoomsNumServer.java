package com.tecode.house.lijin.service.impl;

import com.tecode.echarts.Option;
import com.tecode.house.lijin.service.MysqlServer;
import com.tecode.mysql.bean.Report;
import com.tecode.mysql.dao.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 基础-房间数统计-房间数
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
@Service
public class BasicsRoomsNumServer implements MysqlServer {
    /**
     * 报表表
     */
    @Autowired
    private ReportMapper report;
    /**
     * 图表表
     */
    @Autowired
    private DiagramMapper diagram;

    /**
     * x轴表
     */
    @Autowired
    private XAxisMapper xAxis;

    /**
     * y轴
     */
    @Autowired
    private YAxisMapper yAxis;

    /**
     * 维度
     */
    @Autowired
    private DimensionMapper dimension;

    /**
     * 图例
     */
    @Autowired
    private LegendMapper legend;

    /**
     * 数据表
     */
    @Autowired
    private DataMapper data;

    /**
     * 搜索表
     */
    @Autowired
    private SearchMapper search;

    private SqlSession session;

    public BasicsRoomsNumServer() {
        initSqlSession();
    }

    @Override
    public void insert(Map<String, String> datas) {
        // 报表表>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // 查询报表是否存在，不存在则创建Report report = this.report.selectByName("基础分析-房间数统计");
        //        System.out.println(report);



        // 图表表
        // 取出需要的维度
        // x轴
        // y轴
        // 图例
        // 数据
        // 搜索
    }

    @Override
    public Option select(String reportName) {
        return null;
    }

    /**
     * 获取数据源
     *
     * @return
     */
    private void initSqlSession() {
        String resource = "mybatis-config2.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}