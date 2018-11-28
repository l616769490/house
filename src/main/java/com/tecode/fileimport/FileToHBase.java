package com.tecode.fileimport;

import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.house.lijin.utils.HBaseUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件导入到HBase
 */
public class FileToHBase {

    /**
     * 缓冲区大小
     */
    private static final int MAX_NUM = 1024;
    /**
     * 列名
     */
    private String[] fields;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列族
     */
    private String[] infos = ConfigUtil.get("hbase_infos").split(",");

    private List<Put> puts = new ArrayList<>(MAX_NUM);

    /**
     * 计数器
     */
    private static long count = 0;

    /**
     * 从文件中导入数据到HBase，文件头部需要包含列名信息，列名必须是英文字母
     *
     * @param path 文件路径
     * @param del  如果表存在是否删除表
     * @return 是否成功
     */
    public boolean fileToHbase(String path, boolean del) {
        return fileToHbase(path, null, del);
    }


    /**
     * 从文件中导入数据到HBase
     *
     * @param path   文件路径
     * @param fields 列名
     * @param del    如果表存在是否删除表
     * @return 是否成功
     */
    public boolean fileToHbase(String path, String[] fields, boolean del) {
        try {
            // 获取文件名
            String fileName = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
            // 获取输入流
            BufferedReader br = new BufferedReader(new FileReader(path));
            // 设置列名
            if (fields != null && fields.length > 0) {
                this.fields = fields;
            } else {
                setFields(br);
            }

            // 获取表名
            tableName = ConfigUtil.get("hbase_namespace") + ":" + fileName;

            // 如果表存在
            if (HBaseUtil.tableExists(tableName)) {
                // 如果用户选择删除原表
                if (del) {
                    HBaseUtil.deleteTable(tableName);
                } else {
                    return false;
                }
            }

            // 创建表
            HBaseUtil.createTable(ConfigUtil.get("hbase_namespace") + ":" + fileName, infos);

            // 将数据写入表中
            write(br);

            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 从文件中读取列名
     *
     * @param br 文件流
     * @throws IOException 读取出错
     */
    private void setFields(BufferedReader br) throws IOException {
        String line = br.readLine();

        char c = line.charAt(0);
        while (!Character.isLowerCase(c) && !Character.isUpperCase(c)) {
            line = br.readLine();
            c = line.charAt(0);
        }
        // 列名
        fields = line.split(",");
    }

    /**
     * 写入数据
     *
     * @param br 输入流
     */
    private void write(BufferedReader br) {

        br.lines()
                // 过滤空字符串
                .filter(s -> s != null && !s.isEmpty())
                // 拆分并去除首尾空格和引号
                .map(s -> s.split(",")).map(FileToHBase::trim)
                .forEach(line -> insert(line));
        if (puts.size() > 0) {
            HBaseUtil.addDatas(tableName, puts);
        }
    }

    /**
     * 插入一行数据
     *
     * @param line 数据
     */
    private void insert(String[] line) {
        if (fields == null || line == null || fields.length != line.length || tableName == null || tableName.isEmpty() || infos == null) {
            return;
        }

        // rowKey
        String rowKey = line[2] + "_" + line[0];
        // 列族
        String cf = infos[0];
        Put put = new Put(Bytes.toBytes(rowKey));
        for (int i = 0; i < line.length; i++) {
            // 列名
            String columnName = fields[i];
            // 判断是否是cost或fmt列族
            for (int j = 1; j < infos.length; j++) {
                if (columnName.startsWith(infos[j].toUpperCase()) && infos[j].length() > 7) {
                    cf = infos[j].toLowerCase();
                }
            }

            // 添加列族、列名、列值
            put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(columnName), Bytes.toBytes(line[i]));
        }
        puts.add(put);
        if (puts.size() >= MAX_NUM - 24) {
            HBaseUtil.addDatas(tableName, puts);
            System.out.println("########" +puts.size());
            puts.clear();
            System.out.println(">>>>>" + puts.size());
        }

    }

    /**
     * 去除首尾空格和引号
     *
     * @param arr 需要去除的字符
     * @return 去除后的结果
     */
    public static String[] trim(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            char[] value = arr[i].toCharArray();
            int len = value.length;
            int st = 0;
            char[] val = value;

            while ((st < len) && (val[st] <= ' ' || val[st] == '\'')) {
                st++;
            }
            while ((st < len) && (val[len - 1] <= ' ' || val[len - 1] == '\'')) {
                len--;
            }
            arr[i] = ((st > 0) || (len < value.length)) ? arr[i].substring(st, len) : arr[i];
        }
        return arr;
    }


    public static void main(String[] args) {
        String path = FileToHBase.class.getClassLoader().getResource("").getPath();
        new FileToHBase().fileToHbase(path + "../notebook/upload/2013.csv", true);
    }

}
