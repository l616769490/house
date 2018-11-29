package com.tecode.house.dengya.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class HbaseUtil {
    //hbase的配置文件对象
   private static Configuration conf;
    private static Connection conn;
    private static final String info = "info";
    private static final String cost = "cost";
    private static final String fmt = "fmt";








     public static void initNameSpace(String namepace) throws IOException {
         conf = HBaseConfiguration.create();

         conn = ConnectionFactory.createConnection(conf);
         Admin admin = conn.getAdmin();
         // 命名空间描述其
         NamespaceDescriptor nd = NamespaceDescriptor.create(namepace).build();
         //构建命名空间
         admin.createNamespace(nd);
         admin.close();
         conn.close();

     }


public static  boolean isTableExists(String tableName) throws IOException{
        //获得配置文件的对象  new HBaseConfiguration()过时，使用HBaseConfiguration.create()替换了。
         conf = HBaseConfiguration.create();
        //使用hbase的配置文件来构建HBaseAdnin的对象  老的API
        //HBaseAdmin admin = new HBaseAdmin(conf);

        //新的API
         conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        //Bytes 现在是hbase提供的类
        //HBaseAdmin类中的方法admin.tableExists(Bytes.toBytes(tableName));
        //TableName.valueOf(tableName)  返回TableName的对象
        return  admin.tableExists(TableName.valueOf(tableName));

        }

     /**
      *
      * @param tableName
      * @param cf   可变参数，定义多个列族    ColumnFamily
      * @throws IOException
      */
     public static void createTable(String tableName,String...cf) throws IOException{


         if(isTableExists(tableName)){
             System.out.println("表已存在");
             return ;
         }
          conf = HBaseConfiguration.create();
          conn = ConnectionFactory.createConnection(conf);
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

         System.out.println("创建成功");
     }

     /**
      * 删除表
      * @param
      * @throws IOException
      */

     public static void deleteTable(String tableName) throws IOException{

          conf = HBaseConfiguration.create();
          conn = ConnectionFactory.createConnection(conf);
         Admin admin = conn.getAdmin();

         //判断表是否是disable
         if(!admin.isTableDisabled(TableName.valueOf(tableName))){
             //设置表为disable
             admin.disableTable(TableName.valueOf(tableName));
         }

         //进行表的删除
         admin.deleteTable(TableName.valueOf(tableName));
         System.out.println("删除成功");
     }

     /**
      * 添加数据
      * @param tableName  表名称


      * @throws IOException
      */

     public static List<Put> addRow(String tableName, List<String> INFO, List<String> COST, List<String> FMT, String path) throws IOException{

          conf = HBaseConfiguration.create();
          conn = ConnectionFactory.createConnection(conf);
          List<Put> list = new ArrayList<>();
         FileInputStream file = new FileInputStream(path);
         InputStreamReader reader = new InputStreamReader(file);
         BufferedReader buffer = new BufferedReader(reader);
         buffer.readLine();
        // System.out.println(line);

         //使用Connection连接对象调用getTable(TableName tableName) 来获得Table的对象
         Table table = conn.getTable(TableName.valueOf(tableName));
         while(buffer.ready()){
             String line = buffer.readLine();
             String[] split = line.split(",");
            // System.out.println(split);
             String rowKey = split[2].replaceAll("\'","")+"_"+split[0].replace("\'","");
             //构建Put的对象  使用 行键作为构造器的参数传入Put的构造器，来返回一个Put的对象。
           //  System.out.println(INFO);
           //  System.out.println(COST);
            // System.out.println(FMT);
             if(INFO.size()+COST.size()+FMT.size() != split.length){

                 System.out.println("数据有误");
                 return  null;}
                 Put put = new Put(Bytes.toBytes(rowKey));
                 //使用addColumn()方法 来设置需要在哪个列族的列新增值
             for(int i = 0;i < split.length;i ++){
                 String word = split[i];
                 if(i < INFO.size()){
                     put.addColumn(Bytes.toBytes(info), Bytes.toBytes(INFO.get(i)),  Bytes.toBytes(word.replace("\'","")));
                 }else if(i < INFO.size()+COST.size()){
                     put.addColumn(Bytes.toBytes(cost), Bytes.toBytes(COST.get(i-INFO.size())),  Bytes.toBytes(word.replace("\'","")));
                 }else{
                     put.addColumn(Bytes.toBytes(fmt), Bytes.toBytes(FMT.get(i-INFO.size()-COST.size())),  Bytes.toBytes(word.replace("\'","")));
                 }


             }
             list.add(put);
            table.put(list);


         }
         return list;
     }
}
