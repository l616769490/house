package com.tecode.house.zxl.init;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class CreateTable {

   private static Configuration conf= HBaseConfiguration.create();
    public static void main(String[] args) {
        init();
    }
    private static void init(){
        try {
//            nameSpace();
            createTable("thads:2011");
            createTable("thads:2013");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void nameSpace() throws IOException {
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        NamespaceDescriptor namespaceDescriptor =NamespaceDescriptor.create("thads").build();
        admin.createNamespace(namespaceDescriptor);
        admin.close();
        conn.close();
    }
    private static void createTable(String tablename) throws IOException {
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tablename));
        HColumnDescriptor hd1 =new HColumnDescriptor("info") ;
        hd1.setBlockCacheEnabled(true);
        hd1.setBlocksize(1024*1024*2);
        hd1.setMaxVersions(1);
        hd1.setMinVersions(1);

        HColumnDescriptor hd2=new HColumnDescriptor("cost");
        hd2.setMinVersions(1);
        hd2.setMaxVersions(1);
        hd2.setBlocksize(1024*1024*2);
        hd2.setBlockCacheEnabled(true);

        HColumnDescriptor hd3=new HColumnDescriptor("fmt");
        hd3.setBlockCacheEnabled(true);
        hd3.setBlocksize(1024*1024*2);
        hd3.setMaxVersions(1);
        hd3.setMinVersions(1);
        hTableDescriptor.addFamily(hd1).addFamily(hd2).addFamily(hd3);
        admin.createTable(hTableDescriptor);

        admin.close();
        conn.close();

    }

}
