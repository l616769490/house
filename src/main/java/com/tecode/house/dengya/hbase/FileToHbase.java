package com.tecode.house.dengya.hbase;

import com.tecode.house.dengya.utils.HbaseUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileToHbase {
    private static org.apache.hadoop.conf.Configuration conf;
    private static Connection conn;
    private static List<String> INFO;
    private static List<String> COST;
    private static List<String> FMT;
    private static String path ;
    private static final String info = "info";
    private static final String cost = "cost";
    private static final String fmt = "fmt";
    /**
     * 获取列名
     * @throws Exception
     */
    public static void Column() throws Exception {
        INFO = new ArrayList<>();
        COST = new ArrayList<>();
        FMT = new ArrayList<>();
         path ="D:\\thads2013n.csv";
        File file = new File(path);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        BufferedReader buffer = new BufferedReader(reader);
        for(int i = 0;i <1;i++){
            String line = buffer.readLine();
            System.out.println(line);
           String[] split = line.split(",");
            for(int j = 0;j <split.length ;j++){
                if(j < 50){
                    INFO.add(split[j]);
                }else if(j > 49 && j < 74){
                    COST.add(split[j]);
                }else{
                    FMT.add(split[j]);
                }

            }
          //  System.out.println("info:"+info);
          //  System.out.println("cost:"+cost);
         //   System.out.println("fmt:"+fmt);


        }



    }

    public static void insert() throws Exception {
        path ="D:\\thads2013n.csv";
         conf = new Configuration();
         conn = ConnectionFactory.createConnection(conf);
        Table table =  conn.getTable(TableName.valueOf("thads:2013"));
     //   List<Put> list = HbaseUtil.addRow(Bytes.toBytes("thads:2013"),INFO.toArray(),COST.toArray(),FMT.toArray(),path);
      }
    public static void main(String[] args) throws Exception {
      // HbaseUtil.initNameSpace("thads");
       // HbaseUtil.createTable("thads:2013","info","cost","fmt");

    }

}
