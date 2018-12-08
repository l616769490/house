package com.tecode.house.liuhao.utils;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.nio.file.Path;

/**
 * Created by Administrator on 2018/11/28.
 */
public class DataToHbase {
    public void readFile() throws IOException {
        String[] INFO ={"CONTROL","AGE1","METRO3","REGION","LMED","FMR","L30","L50","L80","IPOV","BEDRMS","BUILT","STATUS","TYPE","VALUE",
                "VACANCY","TENURE","NUNITS", "ROOMS","WEIGHT","PER","ZINC2","ZADEQ","ZSMHC","STRUCTURETYPE","OWNRENT","UTILITY",
                "OTHERCOST","COST06","COST12","COST08","COSTMED","TOTSAL","ASSISTED", "GLMED","GL30","GL50","GL80","APLMED","ABL30",
                "ABL50","ABL80","ABLMED","BURDEN","INCRELAMIPCT","INCRELAMICAT","INCRELPOVPCT","INCRELPOVCAT","INCRELFMRPCT",
                "INCRELFMRCAT"};
        String[] COST ={"COST06RELAMIPCT","COST06RELAMICAT","COST06RELPOVPCT","COST06RELPOVCAT","COST06RELFMRPCT","COST06RELFMRCAT",
                "COST08RELAMIPCT","COST08RELAMICAT","COST08RELPOVPCT","COST08RELPOVCAT","COST08RELFMRPCT","COST08RELFMRCAT",
                "COST12RELAMIPCT","COST12RELAMICAT","COST12RELPOVPCT","COST12RELPOVCAT","COST12RELFMRPCT", "COST12RELFMRCAT",
                "COSTMedRELAMIPCT","COSTMedRELAMICAT","COSTMedRELPOVPCT","COSTMedRELPOVCAT","COSTMedRELFMRPCT","COSTMedRELFMRCAT",
                };
        String[] FMT ={"FMTZADEQ","FMTMETRO3","FMTBUILT","FMTSTRUCTURETYPE","FMTBEDRMS","FMTOWNRENT",
                "FMTCOST06RELPOVCAT","FMTCOST08RELPOVCAT","FMTCOST12RELPOVCAT","FMTCOSTMEDRELPOVCAT","FMTINCRELPOVCAT",
                "FMTCOST06RELFMRCAT", "FMTCOST08RELFMRCAT","FMTCOST12RELFMRCAT","FMTCOSTMEDRELFMRCAT","FMTINCRELFMRCAT",
                "FMTCOST06RELAMICAT", "FMTCOST08RELAMICAT","FMTCOST12RELAMICAT","FMTCOSTMEDRELAMICAT","FMTINCRELAMICAT","FMTASSISTED",
                "FMTBURDEN","FMTREGION","FMTSTATUS"};

        HBaseUtil t = new HBaseUtil();
        String tablename = "2013";
        String[] family = {"INFO","COST","FMT"};

        boolean newtable = t.createTable(tablename, family);
        System.out.println(newtable);
        File file = new File("d://thads2013n.csv");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
        BufferedReader in = new BufferedReader(isr);

        Connection connection = HBaseUtil.getConnection();
        // 获取表名
        TableName tn = TableName.valueOf(tablename);
        // 获取表
        Table table = connection.getTable(tn);
        String line;
        while((line=in.readLine())!=null){

            String[] split = line.replaceAll("\'","").split(",");
            String row = split[2]+"_"+split[0];
            Put put = new Put(Bytes.toBytes(row));
            for (int i = 0; i < split.length; i++) {
                if(i<INFO.length){
                    put.addColumn(Bytes.toBytes("INFO"),Bytes.toBytes(INFO[i]),Bytes.toBytes(split[i]));
                }else if(INFO.length<=i&&i<(INFO.length+COST.length)){
                    put.addColumn(Bytes.toBytes("COST"),Bytes.toBytes(COST[i-INFO.length]),Bytes.toBytes(split[i]));
                }else {
                    put.addColumn(Bytes.toBytes("FMT"),Bytes.toBytes(FMT[i-INFO.length-COST.length]),Bytes.toBytes(split[i]));
                }
            }
            table.put(put);

        }

    }
}
