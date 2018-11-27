package com.tecode.house.zouchao.dao.DaoImpl;

import com.tecode.house.zouchao.dao.HBaseDao;
import com.tecode.house.zouchao.dao.HBaseScalaDao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import scala.Tuple2;
import scala.Tuple3;


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

    @Override
    public List<Tuple2<String, Integer>> getAllRentByCreateYear(String tableName) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan().addColumn(Bytes.toBytes("info"), Bytes.toBytes("FMR"))
                .addColumn(Bytes.toBytes("info"), Bytes.toBytes("BUILT"));
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            Cell[] cells = result.rawCells();
            String name = null;
            String value = null;
            for (Cell cell : cells) {
                name = CellUtil.cloneQualifier(cell).toString();
                value = CellUtil.cloneValue(cell).toString();
            }
        }
        return null;
    }

    @Override
    public List<Tuple3<String, Integer, Integer>> getAllRoomByCreateYear(String tableName) {
        return null;
    }
}
