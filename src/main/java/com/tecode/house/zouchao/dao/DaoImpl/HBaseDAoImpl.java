package com.tecode.house.zouchao.dao.DaoImpl;

import com.tecode.house.zouchao.dao.HBaseDao;

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

public class HBaseDAoImpl implements HBaseDao {

    @Override
    public List<Integer> getAllRent(String tableName) throws IOException {
        List<Integer> rents = new ArrayList<>();
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf(tableName));
        //添加过滤器，只获取“info”列族下的公平市场租金列
        Scan scan = new Scan().addColumn(Bytes.toBytes("info"), Bytes.toBytes("FMR"));
        ResultScanner rs = table.getScanner(scan);
        for (Result result : rs) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                //将结果添加到集合中
                rents.add(Integer.valueOf(CellUtil.cloneValue(cell).toString()));
            }
        }
        return rents;
    }
}
