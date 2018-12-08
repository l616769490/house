package com.tecode.house.lijin.service.impl;

import com.tecode.house.lijin.service.HBaseServer;
import com.tecode.house.lijin.service.TableServer;
import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.mysql.bean.*;
import com.tecode.mysql.dao.DimensionMapper;
import com.tecode.mysql.dao.ReportMapper;
import com.tecode.mysql.dao.SearchMapper;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import jdk.nashorn.internal.runtime.regexp.joni.constants.TargetInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 版本：2018/12/6 V1.0
 * 成员：李晋
 */
@Service
public class TableServerImpl implements TableServer {
    /**
     * Spring Context
     */
//    private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    @Autowired
    ReportMapper reportMapper;
    @Autowired
    SearchMapper searchMapper;
    @Autowired
    DimensionMapper dimensionMapper;



    @Override
    public Table getTable(String reportName, TablePost tablePost, String path) {
        // 获取原始数据
        HBaseServer baseServer = new HBaseServer(path, reportName, tablePost);
        Table table = baseServer.select();
        List<com.tecode.table.Search> searches = selectSearch(reportName, tablePost.getYear());
        table.setSearch(searches);
        return table;
    }

    /**
     * 获取搜索表里的信息
     *
     * @param reportName 报表名
     * @param year       年份
     * @return 搜索信息
     */
    private List<com.tecode.table.Search> selectSearch(String reportName, Integer year) {
        // 查询报表
//        ReportMapper reportMapper = context.getBean(ReportMapper.class);
        ReportExample reportExample = new ReportExample();
        Report report = reportMapper.selectByNameAndYear(reportName, year);

        // 查询搜索
//        SearchMapper searchMapper = context.getBean(SearchMapper.class);
        SearchExample searchExample = new SearchExample();
        searchExample.or().andReportidEqualTo(report.getId());
        List<Search> searches = searchMapper.selectByExample(searchExample);

        // 查询维度
//        DimensionMapper dimensionMapper = context.getBean(DimensionMapper.class);

        // 生成搜索
        List<com.tecode.table.Search> tableSearches = new ArrayList<>();
        for (Search search : searches) {
            com.tecode.table.Search search1 = new com.tecode.table.Search();
            String title = search.getName();
            search1.setTitle(title);

            String dimGroupName = search.getDimgroupname();
            DimensionExample dimensionExample = new DimensionExample();
            dimensionExample.or().andGroupnameEqualTo(dimGroupName);
            List<Dimension> dimensions = dimensionMapper.selectByExample(dimensionExample);
            for (Dimension dimension : dimensions) {
                search1.addValue(dimension.getDimname());
            }

            tableSearches.add(search1);
        }
        return tableSearches;
    }
}
