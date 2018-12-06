package com.tecode.house.dengya.utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import scala.Tuple2;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class HbaseUtil {
    //hbase的配置文件对象
   private static Configuration conf;
    private static Connection conn;
    private static final String info = "info";
    private static final String cost = "cost";
    private static final String fmt = "fmt";
    /**
     * 创建命名空间
     * @param namepace
     * @throws IOException
     */
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
        //新的API
         conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
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
      * 添加数据
      * @throws IOException
      */
     public static List<List<Put>> addRow( List<String> INFO, List<String> COST, List<String> FMT, String path) throws IOException{

          conf = HBaseConfiguration.create();
          conn = ConnectionFactory.createConnection(conf);
          List<List<Put>>  lists = new ArrayList<>();
          List<Put> list = new ArrayList<>();
         FileInputStream file = new FileInputStream(path);
         InputStreamReader reader = new InputStreamReader(file);
         BufferedReader buffer = new BufferedReader(reader);
         String s;
         //使用Connection连接对象调用getTable(TableName tableName) 来获得Table的对象
         while((s=buffer.readLine())!=null){
             String[] split = s.split(",");
             System.out.println(split.length);
             String rowKey = split[2].replace("\'","")+"_"+split[0].replace("\'","");
             //构建Put的对象  使用 行键作为构造器的参数传入Put的构造器，来返回一个Put的对象。
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


                        list.add(put);
                    if(list.size() == 100){
                        lists.add(list);
                        list = new ArrayList<>();

                    }
             }

         }
        lists.add(list);
         return lists;

     }

    /**
     * Hbase查询数据
     */
    public static List<List<String[]>> getAll(String tableName) throws IOException{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        List<List<String[]>>  lists = new ArrayList<>();
        List<String[]> list = new ArrayList<>();

        Table table = conn.getTable(TableName.valueOf(tableName));
        //构建扫描器
        Scan scan = new Scan();
        //值扫描指定的列族
        scan.addFamily(Bytes.toBytes("info"));

        //使用扫描器，扫描一张表，返回一个ResultScanner对象，这个对象，应该存放的是已rowkey为键的多个记录
        ResultScanner scanner = table.getScanner(scan);
        //房屋编号
        String CONTROL = null;
        //城市等级
        String METRO3 = null;
        //建成年份
        String BUILT = null;
        //建筑单元书
        String NUNITS = null;
        for (Result result : scanner) {

            Cell[] rawCells = result.rawCells();
            for (Cell cell : rawCells) {
                String s1 = Bytes.toString(CellUtil.cloneQualifier(cell));
                String s2 = Bytes.toString(CellUtil.cloneValue(cell));
                String s3 = Bytes.toString(CellUtil.cloneRow(cell));

                String[] rowKey = s3.split("_");
                CONTROL = rowKey[1];
                if(s1.equals("METRO3")){
                    METRO3 = s2;
                }
                if(s1.equals("BUILT")){
                    BUILT = s2;
                }
                if(s1.equals("NUNITS")){
                    NUNITS = s2;
                }

            }
            list.add(new String[]{CONTROL,METRO3, BUILT, NUNITS});



            lists.add(list);
        }

        return lists;
    }



}

