package com.tecode.house.chenyong.dao.impl;

import com.tecode.house.chenyong.dao.ImportIntoData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImportIntoDataImpl implements ImportIntoData {

    private static final String INFO = "info";
    private static final String COST = "cost";
    private static final String FMT = "fmt";
    String[] info = {"CONTROL", "AGE1", "METRO3", "REGION", "LMED", "FMR", "L30", "L50", "L80", "IPOV",
            "BEDRMS", "BUILT", "STATUS", "TYPE", "VALUE", "VACANCY", "TENURE", "NUNITS",
            "ROOMS", "WEIGHT", "PER", "ZINC2", "ZADEQ", "ZSMHC", "STRUCTURETYPE", "OWNRENT", "UTILITY",
            "OTHERCOST", "COST06", "COST12", "COST08", "COSTMED", "TOTSAL", "ASSISTED", "GLMED",
            "GL30", "GL50", "GL80", "APLMED", "ABL30", "ABL50", "ABL80", "ABLMED", "BURDEN", "INCRELAMIPCT",
            "INCRELAMICAT", "INCRELPOVPCT", "INCRELPOVCAT", "INCRELFMRPCT", "INCRELFMRCAT"};
    String[] cost = {"COST06RELAMIPCT", "COST06RELAMICAT", "COST06RELPOVPCT", "COST06RELPOVCAT", "COST06RELFMRPCT",
            "COST06RELFMRCAT", "COST08RELAMIPCT", "COST08RELAMICAT", "COST08RELPOVPCT", "COST08RELPOVCAT",
            "COST08RELFMRPCT", "COST08RELFMRCAT", "COST12RELAMIPCT", "COST12RELAMICAT", "COST12RELPOVPCT",
            "COST12RELPOVCAT", "COST12RELFMRPCT", "COST12RELFMRCAT", "COSTMedRELAMIPCT", "COSTMedRELAMICAT",
            "COSTMedRELPOVPCT", "COSTMedRELPOVCAT", "COSTMedRELFMRPCT", "COSTMedRELFMRCAT"};
    String[] fmt = {"FMTZADEQ", "FMTMETRO3", "FMTBUILT", "FMTSTRUCTURETYPE", "FMTBEDRMS", "FMTOWNRENT", "FMTCOST06RELPOVCAT",
            "FMTCOST08RELPOVCAT", "FMTCOST12RELPOVCAT", "FMTCOSTMEDRELPOVCAT", "FMTINCRELPOVCAT", "FMTCOST06RELFMRCAT",
            "FMTCOST08RELFMRCAT", "FMTCOST12RELFMRCAT", "FMTCOSTMEDRELFMRCAT", "FMTINCRELFMRCAT", "FMTCOST06RELAMICAT",
            "FMTCOST08RELAMICAT", "FMTCOST12RELAMICAT", "FMTCOSTMEDRELAMICAT", "FMTINCRELAMICAT", "FMTASSISTED", "FMTASSISTED",
            "FMTREGION", "FMTSTATUS"};
    private static Configuration conf;

    static {
        conf = HBaseConfiguration.create();
    }

    @Override
    public void readData() {
        List<String[]> list = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream("F:\\houseData\\thads2013n.csv"));
            BufferedReader br = new BufferedReader(isr);
            //一行一行的读取数据
            while (br.ready()) {
                String line = br.readLine();
                //替换掉字段中含有'的部分，然后按逗号切分
                String[] split = line.replace("\'", "").split(",");
                list.add(split);
                if (list.size() == 100) {
                    insertIntoData(list);
                    list.clear();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable(String tableName) throws IOException {
        //创建链接
        Connection conn = ConnectionFactory.createConnection(conf);
        System.out.println("建表中，不要打扰");
        Admin admin = conn.getAdmin();
        //构建表的描述器
        HTableDescriptor hd = new HTableDescriptor(TableName.valueOf(tableName));
        //构建列族的描述器
        HColumnDescriptor hcd = new HColumnDescriptor(INFO);
        HColumnDescriptor chcd = new HColumnDescriptor(COST);
        HColumnDescriptor fhcd = new HColumnDescriptor(FMT);
        //把列族绑定到表描述器中
        hd.addFamily(hcd).addFamily(chcd).addFamily(fhcd);
        admin.createTable(hd);
        admin.close();
        conn.close();
    }

    @Override
    public void insertIntoData(List<String[]> list) throws IOException {
        List<Put> lists = new ArrayList<>();
        //创建链接
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = table = conn.getTable(TableName.valueOf("thads:2013"));
        for (String[] str : list) {
            for (int i = 0; i < str.length; i++) {
                String rowkey = str[2] + "_" + str[0];
                Put put = new Put(Bytes.toBytes(rowkey));
                if (i < info.length) {
                    put.addColumn(Bytes.toBytes(INFO), Bytes.toBytes(info[i]), Bytes.toBytes(str[i]));
                } else if (info.length <= i && i < (cost.length + info.length)) {
                    put.addColumn(Bytes.toBytes(COST), Bytes.toBytes(cost[i-info.length]), Bytes.toBytes(str[i]));
                } else {
                    put.addColumn(Bytes.toBytes(FMT), Bytes.toBytes(fmt[i-(info.length+cost.length)]), Bytes.toBytes(str[i]));
                }
                lists.add(put);
            }
        }
        table.put(lists);
        table.close();
    }
}
