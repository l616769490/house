package com.tecode.house.chenyong.dao.impl;

import com.tecode.house.chenyong.dao.GetDataIntoReport;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class GetDataIntoReportImpl implements GetDataIntoReport {

    private Configuration conf =  HBaseConfiguration.create();
    @Override
    public void getFromHBase(String tableName,String rowkey) {
        try {
            //创建链接
            Connection conn = ConnectionFactory.createConnection(conf);
            Admin admin = conn.getAdmin();
            Table table =  conn.getTable(TableName.valueOf(tableName));
            //创建GET对象
            Get get = new Get(Bytes.toBytes(rowkey));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
