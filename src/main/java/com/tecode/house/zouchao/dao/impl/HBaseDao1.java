package com.tecode.house.zouchao.dao.impl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HBaseDao1 {
    public List<Integer> get(String tableName) throws IOException {
        List<Integer> rents = new ArrayList<>();
        //获得配置文件的对象
        Configuration conf = HBaseConfiguration.create();
        //新的API
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf(tableName));
        //    添加过滤器，获取info列族下的FMR列
        Scan scan = new Scan().addColumn(Bytes.toBytes("info"), Bytes.toBytes("FMR"));
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            Cell[] cells = result.rawCells();
            for (Cell elem : cells) {
                //        将结果添加到数组中
                rents.add(Integer.valueOf(Bytes.toString(CellUtil.cloneValue(elem))));
            }
        }
        return rents;
    }
}
