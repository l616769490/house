package com.tecode.house.zhangzhou.hbaseDao.impl;

import com.tecode.house.zhangzhou.hbaseDao.HBaseDao;
import com.tecode.house.zhangzhou.zzUtil.HBaseUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HBaseDaoImpl implements HBaseDao {
    private Connection conn = HBaseUtil.setConn();
    private final String NS = "thads";
//    private final String TB1 = "2011";
//    private final String TB2 = "2013";
    private final String ROWKEY = "CONTROL";
    private final byte[] CD1 = Bytes.toBytes("info");
    private final byte[] CD2 = Bytes.toBytes("cost");
    private final byte[] CD3 = Bytes.toBytes("fmt");
    private final String[] INFO_CLUMS = new String[]{"AGE1","METRO3","REGION","LMED","FMR","L30","L50","L80",
                                    "IPOV","BEDRMS","BUILT","STATUS","TYPE","VALUE","VACANCY","TENURE",
                                    "NUNITS","ROOMS","WEIGHT","PER","ZINC2","ZADEQ","ZSMHC","STRUCTURETYPE",
                                    "OWNRENT","UTILITY","OTHERCOST","COST06","COST12","COST08","COSTMED",
                                    "TOTSAL","ASSISTED","GLMED","GL30","GL50","GL80","APLMED","ABL30", "ABL50",
                                    "ABL80","ABLMED","BURDEN","INCRELAMIPCT","INCRELAMICAT","INCRELPOVPCT",
                                    "INCRELPOVCAT","INCRELFMRPCT","INCRELFMRCAT"};
    private final String[] COST_CLUMS = new String[]{"COST06RELAMIPCT","COST06RELAMICAT","COST06RELPOVPCT",
                                    "COST06RELPOVCAT","COST06RELFMRPCT","COST06RELFMRCAT","COST08RELAMIPCT",
                                    "COST08RELAMICAT","COST08RELPOVPCT","COST08RELPOVCAT","COST08RELFMRPCT",
                                    "COST08RELFMRCAT","COST12RELAMIPCT","COST12RELAMICAT","COST12RELPOVPCT",
                                    "COST12RELPOVCAT","COST12RELFMRPCT","COST12RELFMRCAT","COSTMedRELAMIPCT",
                                    "COSTMedRELAMICAT","COSTMedRELPOVPCT","COSTMedRELPOVCAT",
                                    "COSTMedRELFMRPCT","COSTMedRELFMRCAT"};
    private final String[] FMT_CLUMS = new String[]{"FMTZADEQ","FMTMETRO3","FMTBUILT","FMTSTRUCTURETYPE","FMTBEDRMS",
                                    "FMTOWNRENT","FMTCOST06RELPOVCAT","FMTCOST08RELPOVCAT","FMTCOST12RELPOVCAT",
                                    "FMTCOSTMEDRELPOVCAT","FMTINCRELPOVCAT","FMTCOST06RELFMRCAT","FMTCOST08RELFMRCAT",
                                    "FMTCOST12RELFMRCAT","FMTCOSTMEDRELFMRCAT","FMTINCRELFMRCAT","FMTCOST06RELAMICAT",
                                    "FMTCOST08RELAMICAT","FMTCOST12RELAMICAT","FMTCOSTMEDRELAMICAT","FMTINCRELAMICAT",
                                    "FMTASSISTED","FMTBURDEN","FMTREGION","FMTSTATUS"};
    @Override
    public void create(String tableName) {
        try {
            Admin admin = conn.getAdmin();
            NamespaceDescriptor npDesc = NamespaceDescriptor.create(NS).build();
            admin.createNamespace(npDesc);
            HTableDescriptor htDesc = new HTableDescriptor(TableName.valueOf(tableName));
            HColumnDescriptor hcDesc1 = new HColumnDescriptor(CD1);
            HColumnDescriptor hcDesc2 = new HColumnDescriptor(CD2);
            HColumnDescriptor hcDesc3 = new HColumnDescriptor(CD3);
            htDesc.addFamily(hcDesc1).addFamily(hcDesc2).addFamily(hcDesc3);
            admin.createTable(htDesc);
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(String tableName) {
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(ROWKEY));
            List<Put> list = HBaseUtil.putFile(put,CD1,CD2,CD3,INFO_CLUMS,COST_CLUMS,FMT_CLUMS);
            table.put(list);
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
