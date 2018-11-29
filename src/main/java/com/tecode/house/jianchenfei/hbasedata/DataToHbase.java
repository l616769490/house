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
                String rowKey = fields[0] + "_" + fields[2];
                Put put = new Put(Bytes.toBytes(rowKey));
                for (int i = 0; i < list.length; i++) {
                    String cloumnName = fields[i].toLowerCase();

                    String columnFamliy;
                    if (cloumnName.startsWith("cost") && cloumnName.length() > 7) {
                        columnFamliy = "cost";
                    } else if (cloumnName.startsWith("fmt")) {
                        columnFamliy = "fmt";
                    } else {
                        columnFamliy = "info";
                    }
                    String value = list[i];

                    put.addColumn(Bytes.toBytes(columnFamliy), Bytes.toBytes(cloumnName), Bytes.toBytes(value));


                    puts.add(put);

                    if (puts.size() >= 1024) {
                        HBaseUtil.addDatas("thads:2013", puts);
                        puts.clear();
                    }

                    if (puts.size() > 0) {
                        HBaseUtil.addDatas("thads:2013", puts);
                        puts.clear();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       // HBaseUtil.createTable("thads:2011","info","cost","fmt");

        if (HBaseUtil.tableExists("thads:2013")){
            HBaseUtil.deleteTable("thads:2013");
            HBaseUtil.createTable("thads:2013","info","cost","fmt");
        }else{
            HBaseUtil.createTable("thads:2013","info","cost","fmt");
        }

        String path = DataToHbase.class.getClassLoader().getResource("").getPath() + "../notebook/upload/2013.csv";
        fileToHbase(path);



    }

}
