package com.tecode.house.lijin.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;

/**
 * HBase工具类
 * <br>
 * 版本：2018/10/24 V1.0<br>
 * 开发：李晋
 */
public class HBaseUtil {


    /**
     * HBase 配置
     */
    private static Configuration cfg = HBaseConfiguration.create();
    /**
     * HBase连接
     */
    private static Connection connection;

    static {
        try {
            // 获取连接
            connection = ConnectionFactory.createConnection(cfg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 判断表是否存在
     *
     * @param tableName 表名
     * @return  是否存在
     */
    public static boolean tableExists(String tableName) {
        try {
            Admin admin = connection.getAdmin();
            return admin.tableExists(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建表
     *
     * @param tableName    表名
     * @param columnFamily 列族
     * @return 是否创建成功
     */
    public static boolean createTable(String tableName, String... columnFamily) {
        if (tableExists(tableName)) {
            return false;
        }
        try {
            Admin admin = connection.getAdmin();
            HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
            for (String s : columnFamily) {
                desc.addFamily(new HColumnDescriptor(s));
            }
            admin.createTable(desc);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     * @return 是否删除成功
     */
    public static boolean deleteTable(String tableName) {
        if (!tableExists(tableName)) {
            return false;
        }
        try {
            Admin admin = connection.getAdmin();
            TableName tn = TableName.valueOf(tableName);
            if (!admin.isTableDisabled(tn)) {
                admin.disableTable(tn);
            }
            admin.deleteTable(tn);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 插入单条数据
     *
     * @param tableName    表名
     * @param rowKey       行键
     * @param columnFamily 列族
     * @param columnName   列名
     * @param value        列值
     * @return 是否插入成功
     */
    public static boolean insertData(String tableName, String rowKey, String columnFamily, String columnName,
                                     String value) {
        if (!tableExists(tableName)) {
            return false;
        }
        try {
            // 获取表名
            TableName tn = TableName.valueOf(tableName);
            // 获取表
            Table table = connection.getTable(tn);
            Put put = new Put(Bytes.toBytes(rowKey));
            // 添加列族、列名、列值
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName), Bytes.toBytes(value));
            table.put(put);
            table.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param puts      数据集
     * @return 是否插入成功
     */
    public static boolean addDatas(String tableName, List<Put> puts) {
        if (!tableExists(tableName)) {
            return false;
        }

        try {
            // 获取表名
            TableName tn = TableName.valueOf(tableName);
            // 获取表
            Table table = connection.getTable(tn);
            table.put(puts);
            table.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 删除数据
     *
     * @param tableName    表名
     * @param rowKey       行键
     * @param columnFamily 列族
     * @param columnName   列名
     * @return 是否删除成功
     */
    public static boolean deleteData(String tableName, String rowKey, String columnFamily, String columnName) {
        if (!tableExists(tableName)) {
            return false;
        }
        try {
            // 获取表名
            TableName tn = TableName.valueOf(tableName);
            // 获取表
            Table table = connection.getTable(tn);
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            // 添加需要删除的行
            delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
            table.delete(delete);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取一行数据
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @return 结果数组/null
     */
    public static Cell[] getRow(String tableName, String rowKey) {
        if (!tableExists(tableName)) {
            return null;
        }
        try {
            // 获取表名
            TableName tn = TableName.valueOf(tableName);
            // 获取表
            Table table = connection.getTable(tn);
            Get get = new Get(Bytes.toBytes(rowKey));
            Result rs = table.get(get);
            return rs.rawCells();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一列数据
     *
     * @param tableName  表名
     * @param rowKey     行键
     * @param familyName 列族
     * @param columnName 列名
     * @return 结果数组/null
     */
    public static Cell[] getColumn(String tableName, String rowKey, String familyName, String columnName) {
        if (!tableExists(tableName)) {
            return null;
        }
        try {
            // 获取表名
            TableName tn = TableName.valueOf(tableName);
            // 获取表
            Table table = connection.getTable(tn);
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
            Result rs = table.get(get);
            return rs.rawCells();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * HBase的连接方法
     *
     * @return
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * 在程序处理期间不用人为的关闭链接，
     */
    public static void closeConn() {
        try {
            connection.close();
        } catch (IOException e) {
            System.out.println("关闭连接失败");
            e.printStackTrace();
        }
    }
}