package com.tecode.house.dengya.hbase;

import com.tecode.house.dengya.utils.HbaseUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileToHbase {
    private static Configuration conf;
    private static Connection conn;
    private static List<String> INFO;
    private static List<String> COST;
    private static List<String> FMT;
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
        String path = "d:/thads2013n.csv";
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
                }else if(j < 74){
                    COST.add(split[j]);
                }else{
                    FMT.add(split[j]);
                }

            }
           // System.out.println("info:"+INFO);
          //  System.out.println("cost:"+COST);
           // System.out.println("fmt:"+FMT);


        }



    }

    public static void insert() throws Exception {
        String path = "d:/thads2011.txt";
        conf = HBaseConfiguration.create();
        conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf("thads:2011"));

       // Table table =  conn.getTable(TableName.valueOf("thads:2013"));
        List<List<Put>> lists = HbaseUtil.addRow( INFO, COST, FMT, path);
        for (List<Put> list : lists) {
            table.put(list);
        }
        table.close();

      }
    public static void main(String[] args) throws Exception {
      // HbaseUtil.initNameSpace("thads");
       // HbaseUtil.createTable("thads:2013","info","cost","fmt");

        Column();
        long t1 = System.currentTimeMillis();
       insert();
       long t2 = System.currentTimeMillis();
       System.out.println((t2 -t1)/1000);

    }

}
