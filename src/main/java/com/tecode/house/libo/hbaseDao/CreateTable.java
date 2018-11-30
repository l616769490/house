package com.tecode.house.libo.hbaseDao;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CreateTable {

    private static Connection conn = null;
    private static Configuration conf = null;


    private static final String NS = "thads";                      //namespace
    private static final String TABLENAME_2011 = "2011";           //表名
    private static final String TABLENAME_2013 = "2013";
    private static final String INFO = "info";                     //列族
    private static final String COST = "cost";
    private static final String FMT = "fmt";                      //rowkey

    private static String [] info_clm = {"CONTROL","AGE1","METRO3","REGION","LMED","FMR","L30","L50","L80","IPOV","BEDRMS","BUILT","STATUS"
                     ,"TYPE","VALUE","VACANCY","TENURE","NUNITS","ROOMS","WEIGHT","PER","ZINC2","ZADEQ","ZSMHC","STRUCTURETYPE"
                     ,"OWNRENT","UTILITY","OTHERCOST","COST06","COST12","COST08","COSTMED","TOTSAL","ASSISTED","GLMED","GL30","GL50"
                      ,"GL80","APLMED","ABL30","ABL50","ABL80","ABLMED","BURDEN","INCRELAMIPCT","INCRELAMICAT","INCRELPOVPCT",
                    "INCRELPOVCAT","INCRELFMRPCT","INCRELFMRCAT"};
    private static String [] cost_clm = {"COST06RELAMIPCT","COST06RELAMICAT","COST06RELPOVPCT","COST06RELPOVCAT","COST06RELFMRPCT","COST06RELFMRCAT"
                    ,"COST08RELAMIPCT","COST08RELAMICAT","COST08RELPOVPCT","COST08RELPOVCAT","COST08RELFMRPCT","COST08RELFMRCAT",
                    "COST12RELAMIPCT","COST12RELAMICAT","COST12RELPOVPCT","COST12RELPOVCAT","COST12RELFMRPCT","COST12RELFMRCAT",
                    "COSTMedRELAMIPCT","COSTMedRELAMICAT","COSTMedRELPOVPCT","COSTMedRELPOVCAT","COSTMedRELFMRPCT","COSTMedRELFMRCAT"};

    private static String [] fmt_clm = {"FMTZADEQ","FMTMETRO3","FMTBUILT","FMTSTRUCTURETYPE","FMTBEDRMS","FMTOWNRENT","FMTCOST06RELPOVCAT",
                    "FMTCOST08RELPOVCAT","FMTCOST12RELPOVCAT","FMTCOSTMEDRELPOVCAT","FMTINCRELPOVCAT","FMTCOST06RELFMRCAT",
                    "FMTCOST08RELFMRCAT","FMTCOST12RELFMRCAT","FMTCOSTMEDRELFMRCAT","FMTINCRELFMRCAT","FMTCOST06RELAMICAT",
                    "FMTCOST08RELAMICAT","FMTCOST12RELAMICAT","FMTCOSTMEDRELAMICAT","FMTINCRELAMICAT","FMTASSISTED","FMTBURDEN",
                    "FMTREGION","FMTSTATUS"};
    /**
     * 构建命名空间
     */
    public static void initNamespace() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        NamespaceDescriptor ndesc = NamespaceDescriptor.create(NS).build();
        admin.createNamespace(ndesc);
        admin.close();
        conn.close();

    }


    /**
     * 初始化表
     */
    public static void init(String TABLENAME,String INFO,String COST,String FMT) throws IOException {
        //获取配置与链接
        conf = HBaseConfiguration.create();
        conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();

        //表描述器
        HTableDescriptor hdesc = new HTableDescriptor(TableName.valueOf(TABLENAME));

        //列描述其   info
        HColumnDescriptor infohcdesc = new HColumnDescriptor(INFO);
        hdesc.addFamily(infohcdesc);

        //列描述其   cost
        HColumnDescriptor costhcdesc = new HColumnDescriptor(COST);
        hdesc.addFamily(costhcdesc);

        //列描述其   fmt
        HColumnDescriptor fmtcosthcdesc = new HColumnDescriptor(FMT);
        hdesc.addFamily(fmtcosthcdesc);

        admin.createTable(hdesc);

        admin.close();
        conn.close();

    }

    /**
     * 读取并插入数据数据
     */
    public static List<List<Put>> readData(String path) throws IOException {

        conf = HBaseConfiguration.create();
        conn = ConnectionFactory.createConnection(conf);
        List<List<Put>> list = new ArrayList<>();
        List<Put> l = new ArrayList<>();

        File file = new File(path);
        //流
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
        BufferedReader in = new BufferedReader(isr);
        String s;
      // Table table = conn.getTable(TableName.valueOf(TABLENAME));
        while((s = in.readLine())!=null){
            String[] split = s.split(",");
            //长度判断
            if(split.length!=(info_clm.length+cost_clm.length+fmt_clm.length)){
                System.out.println("数据有误！");
                return null;
            }
            String rowKey = split[2].replaceAll("\'","")+"_"+split[0].replaceAll("\'","");
            Put put = new Put(Bytes.toBytes(rowKey));
            for(int i=0;i<split.length;i++){
                String word = split[i];
                //往info添加数据
                if (i < info_clm.length) {
                    put.addColumn(Bytes.toBytes(INFO), Bytes.toBytes(info_clm[i]), Bytes.toBytes(word.replace("\'", "")));
                } else if (i < info_clm.length + cost_clm.length) {
                    put.addColumn(Bytes.toBytes(COST), Bytes.toBytes(cost_clm[i - info_clm.length]), Bytes.toBytes(word.replace("\'", "")));
                } else {
                    put.addColumn(Bytes.toBytes(FMT), Bytes.toBytes(fmt_clm[i - info_clm.length - cost_clm.length]), Bytes.toBytes(word.replace("\'", "")));
                }
                l.add(put);
                if(l.size()==3000){
                    list.add(l);
                    l = new ArrayList<>();
                }
            }
        }
//        for (List list1 : list) {
//           System.out.println(list.size());
//            table.put(list1);
//        }

       // table.close();
        list.add(l);
        isr.close();
        in.close();
        return list;

    }

    /**
     * put
     * @throws IOException
     */
    public static void insertToHbase(String TABLENAME,String path) throws IOException {
        conf = HBaseConfiguration.create();
        conn = ConnectionFactory.createConnection(conf);

        Table table = conn.getTable(TableName.valueOf(TABLENAME));
        List<List<Put>> list = readData(path);

        for (List<Put> puts : list) {
            System.out.println("===="+puts.size());
            table.put(puts);
        }
        table.close();


    }

    public static void main(String[] args) throws IOException {

        String path = "d:/thads2013n.csv";
        long t1 = System.currentTimeMillis();
        insertToHbase("thads:201352",path);
        long t2 = System.currentTimeMillis();
        System.out.println((t2-t1)/1000);
//        List<List<Put>> lists = readData(path);
//
//        insertToHbase(lists,"thads:201352");

//        initNamespace();
//       init("thads:201352",INFO,COST,FMT);


        //插数据S
//        conf = HBaseConfiguration.create();
//        conn = ConnectionFactory.createConnection(conf);
       // Table table = conn.getTable(TableName.valueOf("thads:2013"));
//        String path = "d:/thads2013n.csv";
//
//        long t1 = System.currentTimeMillis();
//
//            readData("thads:201352", path);
//        long t2 = System.currentTimeMillis();
//        System.out.println((t2-t1)/1000);
//        for (List<Put> list : lists) {
//            table.put(list);
//        }
//        table.close();
    }





}
