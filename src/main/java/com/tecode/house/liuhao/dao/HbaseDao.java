package com.tecode.house.liuhao.dao;

import org.apache.hadoop.hbase.client.Connection;

import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */
public interface HbaseDao {
    /**
     * 获取链接
     * @return
     */
    Connection getConnection();
    /**
     * 获取某一列的值
     * @param tablname 表名
     * @param column 列名
     * @param Family 列族
     * @return 获取的值
     */
    List getOneColumn(String tablname,String column,String Family);
}
