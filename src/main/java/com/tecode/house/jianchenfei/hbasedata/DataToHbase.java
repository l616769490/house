package com.tecode.house.jianchenfei.hbasedata;

import com.tecode.house.jianchenfei.utils.HBaseUtil;
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
public class DataToHbase {

    public DataToHbase() {
    }

    /**
     * 从文件中导入数据到HBase
     *
     * @param path 文件路径
     */
    public static void fileToHbase(String path) {

        try {
           // long t1 = System.currentTimeMillis();
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            // 取第一个做判断
            char c = line.charAt(0);
            while (!Character.isLowerCase(c) && !Character.isUpperCase(c)) {
                line = br.readLine();
                c = line.charAt(0);
            }

            // 将第一行的字段名存为数组
            String[] fields = line.split(",");

            List<Put> puts = new ArrayList<>(1024);

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }


                String[] list = line.split(",");
                list = DataToHbase.trim(list);
                String rowKey = list[2] + "_" + list[0];
                Put put = new Put(Bytes.toBytes(rowKey));

                for (int i = 0; i < list.length; i++) {
                    String cloumnName = fields[i];

                    String columnFamliy;
                    if (cloumnName.toLowerCase().startsWith("cost") && cloumnName.length() > 7) {
                        columnFamliy = "cost";
                    } else if (cloumnName.toLowerCase().startsWith("fmt")) {
                        columnFamliy = "fmt";
                    } else {
                        columnFamliy = "info";
                    }
                    String value = list[i];

                    put.addColumn(Bytes.toBytes(columnFamliy), Bytes.toBytes(cloumnName), Bytes.toBytes(value));

                }
                puts.add(put);
                if (puts.size() >= 1024) {
                   // System.out.println("asasa");
                    HBaseUtil.addDatas("thads:2013", puts);
                    puts.clear();
                }
            }

            if (puts.size() > 0) {
                HBaseUtil.addDatas("thads:2013", puts);
                puts.clear();
            }
           // long t2 = System.currentTimeMillis();
            //System.out.println(t2 - t1);
        } catch (IOException e) {
            e.printStackTrace();
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
        // HBaseUtil.createTable("thads:2011","info","cost","fmt");

        if (HBaseUtil.tableExists("thads:2013")) {
            HBaseUtil.deleteTable("thads:2013");
            HBaseUtil.createTable("thads:2013", "info", "cost", "fmt");
        } else {
            HBaseUtil.createTable("thads:2013", "info", "cost", "fmt");
        }

        String path = DataToHbase.class.getClassLoader().getResource("").getPath() + "../notebook/upload/2013.csv";
        fileToHbase(path);


    }

}
