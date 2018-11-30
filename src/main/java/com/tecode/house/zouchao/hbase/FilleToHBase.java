package com.tecode.house.zouchao.hbase;

import com.tecode.house.zouchao.util.HBaseUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilleToHBase {
    private String[] name;

    public void readFile(String path, String tableName) throws IOException {
        HBaseUtil util = new HBaseUtil();
        util.createTable(tableName, "info", "cost", "fmt");
        BufferedReader br = new BufferedReader(new FileReader(path));
        int count = 0;
        List<String[]> lists = new ArrayList<>();
        while (br.ready()) {
            String line = br.readLine();
            String[] splits = line.split(",");
            if (count < 1) {
                count++;
                name = splits;
            } else {
                String[] split = trims(splits);
                if (lists.size() < 500) {
                    lists.add(split);
                } else {
                    putInHBase(lists, tableName);
                    lists.clear();
                    lists.add(split);
                }
            }
        }
        putInHBase(lists, tableName);
        br.close();
    }

    private void putInHBase(List<String[]> lists, String tableName) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        List<Put> puts = new ArrayList<>();
        for (String[] split : lists) {
            //构建Put的对象  使用 行键作为构造器的参数传入Put的构造器，来返回一个Put的对象。
            Put put = new Put(Bytes.toBytes(split[2] + "_" + split[0]));
            //使用addColumn()方法 来设置需要在哪个列族的列新增值
            for (int i = 0; i < split.length; i++) {
                String famliy = "info";
                if (i <= 49) {
                } else if (49 < i && i <= 73) {
                    famliy = "cost";
                } else {
                    famliy = "fmt";
                }
                put.addColumn(Bytes.toBytes(famliy), Bytes.toBytes(name[i]), Bytes.toBytes(split[i]));
            }
            puts.add(put);
        }
        //使用Connection连接对象调用getTable(TableName tableName) 来获得Table的对象
        Table table = conn.getTable(TableName.valueOf(tableName));
        //批量添加数据。
        table.put(puts);
        conn.close();
    }

    //去除首尾引号
    public String[] trims(String[] split) {
        for (int i = 0; i < split.length; i++) {
            char[] value = split[i].toCharArray();
            int len = value.length;
            int st = 0;
            char[] val = value;

            while ((st < len) && (val[st] <= ' ' || val[st] == '\'')) {
                st++;
            }
            while ((st < len) && (val[len - 1] <= ' ' || val[len - 1] == '\'')) {
                len--;
            }
            split[i] = ((st > 0) || (len < value.length)) ? split[i].substring(st, len) : split[i];
        }
        return split;

    }
}
