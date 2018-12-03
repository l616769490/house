package com.tecode.house.lijin.service;

import com.tecode.echarts.Option;
import com.tecode.table.Table;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 存储数据到数据库
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public abstract class InsertMysqlServer {
    /**
     * 数据源
     */
    protected SqlSession session = null;

    public InsertMysqlServer(String path) {
        String resource = path;
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据写入到数据库中
     *
     * @param datas 数据集
     * @param year  数据所属年份
     */
    public abstract void insert(Map<String, Map<String, String>> datas, int year);

    /**
     * 提交并关闭session
     */
    public void close() {
        session.commit();
        session.close();
    }
}
