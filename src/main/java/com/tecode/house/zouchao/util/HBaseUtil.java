package com.tecode.house.zouchao.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HBaseUtil {
    public static Boolean isTableExits(String tableName) throws IOException {
        //获得配置文件的对象  new HBaseConfiguration()过时，使用HBaseConfiguration.create()替换了。
        Configuration conf = HBaseConfiguration.create();
        //新的API
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        Boolean b = admin.tableExists(TableName.valueOf(tableName));
        conn.close();
        return b;
    }

    public static void createTable(String tableName, String... cf) throws IOException {
        if (isTableExits(tableName)) {
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
        conn.close();
    }

    //创建命名空间
    public static void createNameSpace(String name) throws IOException {
        if (isNameSpaceExits(name)) {
            return;
        }
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        //创建命名空间
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(name).build();
        admin.createNamespace(namespaceDescriptor);
        conn.close();

    }

    //判断命名空间是否存在
    public static Boolean isNameSpaceExits(String name) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        List<String> lists = new ArrayList<>();
        //获取命名空间描述器
        List<NamespaceDescriptor> namespaceDescriptors = Arrays.asList(admin.listNamespaceDescriptors());
        for (NamespaceDescriptor descriptor : namespaceDescriptors) {
            //获取命名空间名
            lists.add(descriptor.getName());
        }
        conn.close();
        //判断命名空间是否存在
        return lists.contains(name);
    }

    //清空表
    public static void truncateTable(String tableName) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        if (isTableExits(tableName)) {
            Admin admin = conn.getAdmin();
            admin.truncateTable(TableName.valueOf(tableName), true);
        } else {
            createTable(tableName, "info", "cost", "fmt");
        }
        conn.close();
    }

    //删除表
    public static void deleteTable(String tableName) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();

        //判断表是否是disable
        if (!admin.isTableDisabled(TableName.valueOf(tableName))) {
            //设置表为disable
            admin.disableTable(TableName.valueOf(tableName));
        }
        //进行表的删除
        admin.deleteTable(TableName.valueOf(tableName));
        conn.close();
    }

    public static void deleteNameSpace(String name) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        admin.deleteNamespace(name);
        conn.close();
    }
}
