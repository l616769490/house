package com.tecode.house.lijin.hbase;

import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.house.lijin.utils.HBaseUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HBaseFromFile {
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


    /**
     * 文件路径
     */
    private String path;

    public HBaseFromFile(String path) {
        this.path = path;
    }

    /**
     * 从文件中导入数据到HBase，文件头部需要包含列名信息，列名必须是英文字母
     *
     * @param del 如果表存在是否删除表
     * @return 是否成功
     */
    public boolean fileToHbase(boolean del) {
        return fileToHbase(null, del);
    }


    /**
     * 从文件中导入数据到HBase
     *
     * @param fields 列名
     * @param del    如果表存在是否删除表
     * @return 是否成功
     */
    public boolean fileToHbase(String[] fields, boolean del) {
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

            if (br != null) {
                br.close();
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

            // 关闭连接
            HBaseUtil.closeConn();

            // 将数据写入表中
            writeData();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 写入数据
     */
    private void writeData() {

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

    public static void main(String[] args) {
        String path = HBaseFromFile.class.getClassLoader().getResource("").getPath() + "../notebook/upload/2013.csv";
        new HBaseFromFile(path).fileToHbase(true);
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

}
