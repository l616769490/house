package com.tecode.house.zouchao.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HBaseUtil {
    public static Boolean isExits(String tableName) throws IOException {
        //获得配置文件的对象  new HBaseConfiguration()过时，使用HBaseConfiguration.create()替换了。
        Configuration conf = HBaseConfiguration.create();
        //新的API
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        return admin.tableExists(TableName.valueOf(tableName));
    }

    public static void createTable(String tableName, String... cf) throws IOException {
        if (isExits(tableName)) {
            System.out.println("表已存在");
            return;
        }
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();


        //创建表的描述器
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
        //遍历传入的列族。
        for (int i = 0; i < cf.length; i++) {
            //HColumnDescriptor  表示列的描述器
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf[i]);

            //向表的描述器中添加列族
            hTableDescriptor.addFamily(hColumnDescriptor);
        }

        //创建表，创建表时需要传入一个HTableDescriptor 对象，用于表的描述的。
        admin.createTable(hTableDescriptor);
    }

    public static void createNameSpace(String name) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        //创建命名空间
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(name).build();
        admin.createNamespace(namespaceDescriptor);
    }

    public static Boolean isNameSpaceExits(String name) throws IOException {
        Boolean b = false;
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        List<NamespaceDescriptor> namespaceDescriptors = Arrays.asList(admin.listNamespaceDescriptors());
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(name).build();
        if (namespaceDescriptors.contains(namespaceDescriptor)) {
            b = true;
        }
        return b;
    }
}
