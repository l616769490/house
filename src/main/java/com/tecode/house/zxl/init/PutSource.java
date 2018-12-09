package com.tecode.house.zxl.init;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PutSource {
    private static Configuration conf = HBaseConfiguration.create();
   private static  int count=0;
    public static void main(String[] args)  {

        try {
            long before=System.currentTimeMillis();
            System.out.println("导入数据ing......");
//            put();
            put2011();
            long after=System.currentTimeMillis();
            System.out.println("导入数据完毕，用时： "+new SimpleDateFormat("mm分ss秒").format(after-before));
        } catch (Exception e) {
            System.out.println(count);
            e.printStackTrace();
        }

    }


    private static void put() throws IOException {
        FileInputStream in=new FileInputStream("F:/zxl/项目2/thads2013n.csv");
        InputStreamReader isr=new InputStreamReader(in);
        BufferedReader br=new BufferedReader(isr);



        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf("2013"));
        List<Put> list=new ArrayList<>();
        while(br.ready()) {
            String[] s=br.readLine().split(",");

            String rowkey=s[2].replaceAll("\'", "")+"_"+s[0].replaceAll("\'", "");

            Put put = new Put(Bytes.toBytes(rowkey));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"), Bytes.toBytes(s[0].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("AGE1"), Bytes.toBytes(s[1].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("METRO3"), Bytes.toBytes(s[2].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("REGION"), Bytes.toBytes(s[3].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("LMED"), Bytes.toBytes(s[4].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("FMR"), Bytes.toBytes(s[5].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("L30"), Bytes.toBytes(s[6].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("L50"), Bytes.toBytes(s[7].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("L80"), Bytes.toBytes(s[8].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("IPOV"), Bytes.toBytes(s[9].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("BEDRMS"), Bytes.toBytes(s[10].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("BUILT"), Bytes.toBytes(s[11].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("STATUS"), Bytes.toBytes(s[12].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("TYPE"), Bytes.toBytes(s[13].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("VALUE"), Bytes.toBytes(s[14].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("VACANCY"), Bytes.toBytes(s[15].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("TENURE"), Bytes.toBytes(s[16].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("NUNITS"), Bytes.toBytes(s[17].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ROOMS"), Bytes.toBytes(s[18].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("WEIGHT"), Bytes.toBytes(s[19].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("PER"), Bytes.toBytes(s[20].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"), Bytes.toBytes(s[21].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ZADEQ"), Bytes.toBytes(s[22].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC"), Bytes.toBytes(s[23].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("STRUCTURETYPE"), Bytes.toBytes(s[24].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("OWNRENT"), Bytes.toBytes(s[25].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("UTILITY"), Bytes.toBytes(s[26].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("OTHERCOST"), Bytes.toBytes(s[27].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("COST06"), Bytes.toBytes(s[28].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("COST12"), Bytes.toBytes(s[29].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("COST08"), Bytes.toBytes(s[30].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("COSTMED"), Bytes.toBytes(s[31].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("TOTSAL"), Bytes.toBytes(s[32].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ASSISTED"), Bytes.toBytes(s[33].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("GLMED"), Bytes.toBytes(s[34].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("GL30"), Bytes.toBytes(s[35].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("GL50"), Bytes.toBytes(s[36].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("GL80"), Bytes.toBytes(s[37].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("APLMED"), Bytes.toBytes(s[38].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ABL30"), Bytes.toBytes(s[39].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ABL50"), Bytes.toBytes(s[40].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ABL80"), Bytes.toBytes(s[41].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ABLMED"), Bytes.toBytes(s[42].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("BURDEN"), Bytes.toBytes(s[43].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("INCRELAMIPCT"), Bytes.toBytes(s[44].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("INCRELAMICAT"), Bytes.toBytes(s[45].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("INCRELPOVPCT"), Bytes.toBytes(s[46].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("INCRELPOVCAT"), Bytes.toBytes(s[47].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("INCRELFMRPCT"), Bytes.toBytes(s[48].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("INCRELFMRCAT"), Bytes.toBytes(s[49].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST06RELAMIPCT"), Bytes.toBytes(s[50].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST06RELAMICAT"), Bytes.toBytes(s[51].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST06RELPOVPCT"), Bytes.toBytes(s[52].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST06RELPOVCAT"), Bytes.toBytes(s[53].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST06RELFMRPCT"), Bytes.toBytes(s[54].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST06RELFMRCAT"), Bytes.toBytes(s[55].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST08RELAMIPCT"), Bytes.toBytes(s[56].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST08RELAMICAT"), Bytes.toBytes(s[57].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST08RELPOVPCT"), Bytes.toBytes(s[58].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST08RELPOVCAT"), Bytes.toBytes(s[59].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST08RELFMRPCT"), Bytes.toBytes(s[60].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST08RELFMRCAT"), Bytes.toBytes(s[61].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST12RELAMIPCT"), Bytes.toBytes(s[62].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST12RELAMICAT"), Bytes.toBytes(s[63].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST12RELPOVPCT"), Bytes.toBytes(s[64].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST12RELPOVCAT"), Bytes.toBytes(s[65].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST12RELFMRPCT"), Bytes.toBytes(s[66].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COST12RELFMRCAT"), Bytes.toBytes(s[67].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COSTMedRELAMIPCT"), Bytes.toBytes(s[68].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COSTMedRELAMICAT"), Bytes.toBytes(s[69].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COSTMedRELPOVPCT"), Bytes.toBytes(s[70].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COSTMedRELPOVCAT"), Bytes.toBytes(s[71].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COSTMedRELFMRPCT"), Bytes.toBytes(s[72].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("cost"), Bytes.toBytes("COSTMedRELFMRCAT"), Bytes.toBytes(s[73].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTZADEQ"), Bytes.toBytes(s[74].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTMETRO3"), Bytes.toBytes(s[75].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTBUILT"), Bytes.toBytes(s[76].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTSTRUCTURETYPE"), Bytes.toBytes(s[77].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTBEDRMS"), Bytes.toBytes(s[78].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTOWNRENT"), Bytes.toBytes(s[79].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST06RELPOVCAT"), Bytes.toBytes(s[80].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST08RELPOVCAT"), Bytes.toBytes(s[81].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST12RELPOVCAT"), Bytes.toBytes(s[82].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOSTMEDRELPOVCAT"), Bytes.toBytes(s[83].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTINCRELPOVCAT"), Bytes.toBytes(s[84].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST06RELFMRCAT"), Bytes.toBytes(s[85].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST08RELFMRCAT"), Bytes.toBytes(s[86].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST12RELFMRCAT"), Bytes.toBytes(s[87].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOSTMEDRELFMRCAT"), Bytes.toBytes(s[88].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTINCRELFMRCAT"), Bytes.toBytes(s[89].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST06RELAMICAT"), Bytes.toBytes(s[90].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST08RELAMICAT"), Bytes.toBytes(s[91].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOST12RELAMICAT"), Bytes.toBytes(s[92].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTCOSTMEDRELAMICAT"), Bytes.toBytes(s[93].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTINCRELAMICAT"), Bytes.toBytes(s[94].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTASSISTED"), Bytes.toBytes(s[95].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTBURDEN"), Bytes.toBytes(s[96].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTREGION"), Bytes.toBytes(s[97].replaceAll("\'", "")));

            put.addColumn(Bytes.toBytes("fmt"), Bytes.toBytes("FMTSTATUS"), Bytes.toBytes(s[98].replaceAll("\'", "")));

            list.add(put);
            add(list,br,table);

        }
            table.close();
            conn.close();







    }

    private static void put2011() throws IOException {
        FileInputStream in=new FileInputStream("F:/zxl/项目2/thads2011.txt");
        InputStreamReader isr=new InputStreamReader(in);
        BufferedReader br=new BufferedReader(isr);



        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf("2011"));
        List<Put> list=new ArrayList<>();
        String[] title=br.readLine().split(",");
        while(br.ready()) {

                String[] s=br.readLine().split(",");
                String rowkey=s[2].replaceAll("\'","")+"_"+s[0].replaceAll("\'","");
                Put put=new Put(Bytes.toBytes(rowkey));
                               for(int i=0;i<s.length;i++){

                    String value=s[i].replaceAll("\'","");
                    String t=title[i];
                    if(t.length()>6&&t.startsWith("COST")){
                        put.addColumn(Bytes.toBytes("cost"),Bytes.toBytes(t),Bytes.toBytes(value));
                    }else if(t.startsWith("FMT")){
                        put.addColumn(Bytes.toBytes("fmt"),Bytes.toBytes(t),Bytes.toBytes(value));
                    }else {
                        put.addColumn(Bytes.toBytes("info"),Bytes.toBytes(t),Bytes.toBytes(value));
                    }




                }



            list.add(put);

            add(list,br,table);

        }

        table.close();
        conn.close();
    }

    private static void add(List<Put> list,BufferedReader br,Table table) throws IOException {

        if(list.size()==1000) {
            table.put(list);
            list.clear();
        }else if(!br.ready()&&list.size()>0){
            table.put(list);
            list.clear();
        }
    }



}

