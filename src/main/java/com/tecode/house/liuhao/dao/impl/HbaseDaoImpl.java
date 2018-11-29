package com.tecode.house.liuhao.dao.impl;

import com.tecode.house.liuhao.dao.HbaseDao;
import com.tecode.house.liuhao.utils.HBaseUtil;
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

/**
 * Created by Administrator on 2018/11/29.
 */
public class HbaseDaoImpl implements HbaseDao {
    /**
     * 获取链接
     * @return
     */
    @Override
    public Connection getConnection() {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = null;
        try {
             conn = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     *
     * @param tablname 表名
     * @param column 列名
     * @param Family 列族
     * @return
     */
    @Override
    public List getOneColumn(String tablname, String column, String Family) {
        List<String> list = new ArrayList<>();
        Connection conn = getConnection();
        try {
            Table table = conn.getTable(TableName.valueOf(Bytes.toBytes(tablname)));
           // Get get = new Get(Bytes.toBytes())
            Scan scan = new Scan();
            //值扫描指定的列族和列名
            scan.addColumn(Bytes.toBytes(Family),Bytes.toBytes(column));
            //使用扫描器，扫描一张表，返回一个ResultScanner对象，这个对象，应该存放的是已rowkey为键的多个记录
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String columnValue = CellUtil.cloneValue(cell).toString();
                    list.add(columnValue);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }




}
