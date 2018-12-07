package com.tecode.house.chenyong.dao.impl;

import com.tecode.house.chenyong.dao.ImportIntoData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImportIntoDataImpl implements ImportIntoData {

    private static final String INFO = "info";
    private static final String COST = "cost";
    private static final String FMT = "fmt";



    private Configuration conf = HBaseConfiguration.create();

    @Override
    public List<String> readData() {
        List<String> list = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream("F:\\houseData\\thads2013n.csv"));
            BufferedReader br = new BufferedReader(isr);
            //一行一行的读取数据
            String line = br.readLine();
            //按逗号切分
            String[] split = line.split(",");
            for (String s : split) {
                //去除含有引号的字段
                if(s.contains("\'")){
                    s.replace("\'","");
                }
                list.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void createTable(String tableName) throws IOException {
        //创建链接
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        //构建表的描述器
        HTableDescriptor hd = new HTableDescriptor(TableName.valueOf(tableName));
        //构建列族的描述器
        HColumnDescriptor hcd = new HColumnDescriptor(INFO);
        //设置开启块的缓存
        hcd.setBlockCacheEnabled(true);
        hcd.setBlocksize(2*1024*1024);
        //设置数据的版本
        hcd.setMinVersions(1);
        hcd.setMaxVersions(1);
        //把列族绑定到表描述器中
        hd.addFamily(hcd);

        HColumnDescriptor chcd = new HColumnDescriptor(COST);
        //设置开启块的缓存
        hcd.setBlockCacheEnabled(true);
        hcd.setBlocksize(2*1024*1024);
        //设置数据的版本
        hcd.setMinVersions(1);
        hcd.setMaxVersions(1);
        //把列族绑定到表描述器中
        hd.addFamily(chcd);

        HColumnDescriptor fhcd = new HColumnDescriptor(FMT);
        //设置开启块的缓存
        hcd.setBlockCacheEnabled(true);
        hcd.setBlocksize(2*1024*1024);
        //设置数据的版本
        hcd.setMinVersions(1);
        hcd.setMaxVersions(1);
        //把列族绑定到表描述器中
        hd.addFamily(fhcd);

        admin.createTable(hd);
        admin.close();
        conn.close();


    }

    @Override
    public void insertIntoData(String tableName, String rowkey, String columnFamily, String column, String value) throws IOException {
        //创建链接
        Connection conn = ConnectionFactory.createConnection(conf);
        //创建table表对象
        Table table = conn.getTable(TableName.valueOf(tableName));
        //向表中插入数据
        Put put = new Put(Bytes.toBytes(rowkey));
        //向put对象中封装进数据
        put.addColumn(Bytes.toBytes(columnFamily),Bytes.toBytes(column),Bytes.toBytes(value));
        table.put(put);
        table.close();
    }
}
