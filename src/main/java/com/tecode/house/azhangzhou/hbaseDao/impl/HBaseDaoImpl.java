package com.tecode.house.azhangzhou.hbaseDao.impl;

import com.tecode.house.azhangzhou.hbaseDao.HBaseDao;
import com.tecode.house.azhangzhou.zzUtil.HBaseUtil;
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

/**
 * @author zhangzhou
 * HBase数据库的操作类
 */
public class HBaseDaoImpl implements HBaseDao {
    private Connection conn = HBaseUtil.setConn();
    //命名空间
    private final String NS = "thads";
    //info列族
    private final byte[] CD1 = Bytes.toBytes("info");
    //cost列族
    private final byte[] CD2 = Bytes.toBytes("cost");
    //fmt列族
    private final byte[] CD3 = Bytes.toBytes("fmt");
    //info列族的列字段
    private final String[] INFO_CLUMS = new String[]{"CONTROL","AGE1","METRO3","REGION","LMED","FMR","L30","L50","L80",
                                    "IPOV","BEDRMS","BUILT","STATUS","TYPE","VALUE","VACANCY","TENURE",
                                    "NUNITS","ROOMS","WEIGHT","PER","ZINC2","ZADEQ","ZSMHC","STRUCTURETYPE",
                                    "OWNRENT","UTILITY","OTHERCOST","COST06","COST12","COST08","COSTMED",
                                    "TOTSAL","ASSISTED","GLMED","GL30","GL50","GL80","APLMED","ABL30", "ABL50",
                                    "ABL80","ABLMED","BURDEN","INCRELAMIPCT","INCRELAMICAT","INCRELPOVPCT",
                                    "INCRELPOVCAT","INCRELFMRPCT","INCRELFMRCAT"};
    //cost列族的列字段
    private final String[] COST_CLUMS = new String[]{"COST06RELAMIPCT","COST06RELAMICAT","COST06RELPOVPCT",
                                    "COST06RELPOVCAT","COST06RELFMRPCT","COST06RELFMRCAT","COST08RELAMIPCT",
                                    "COST08RELAMICAT","COST08RELPOVPCT","COST08RELPOVCAT","COST08RELFMRPCT",
                                    "COST08RELFMRCAT","COST12RELAMIPCT","COST12RELAMICAT","COST12RELPOVPCT",
                                    "COST12RELPOVCAT","COST12RELFMRPCT","COST12RELFMRCAT","COSTMedRELAMIPCT",
                                    "COSTMedRELAMICAT","COSTMedRELPOVPCT","COSTMedRELPOVCAT",
                                    "COSTMedRELFMRPCT","COSTMedRELFMRCAT"};
    //fmt列族的列字段
    private final String[] FMT_CLUMS = new String[]{"FMTZADEQ","FMTMETRO3","FMTBUILT","FMTSTRUCTURETYPE","FMTBEDRMS",
                                    "FMTOWNRENT","FMTCOST06RELPOVCAT","FMTCOST08RELPOVCAT","FMTCOST12RELPOVCAT",
                                    "FMTCOSTMEDRELPOVCAT","FMTINCRELPOVCAT","FMTCOST06RELFMRCAT","FMTCOST08RELFMRCAT",
                                    "FMTCOST12RELFMRCAT","FMTCOSTMEDRELFMRCAT","FMTINCRELFMRCAT","FMTCOST06RELAMICAT",
                                    "FMTCOST08RELAMICAT","FMTCOST12RELAMICAT","FMTCOSTMEDRELAMICAT","FMTINCRELAMICAT",
                                    "FMTASSISTED","FMTBURDEN","FMTREGION","FMTSTATUS"};

    /**
     * 创建命名空间
     * @return 返回true：创建成功，false：创建失败
     */
    @Override
    public boolean createNameSpace() {
        Admin admin = null;
        try {
            admin = conn.getAdmin();
            NamespaceDescriptor build = NamespaceDescriptor.create(NS).build();
            admin.createNamespace(build);
            admin.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 创建表
     * @param tableName 表格名称
     * @return true：成功 false：失败
     */
    @Override
    public boolean create(String tableName) {
        try {
            Admin admin = conn.getAdmin();
            HTableDescriptor htDesc = new HTableDescriptor(TableName.valueOf(tableName));
            HColumnDescriptor hcDesc1 = new HColumnDescriptor(CD1);
            HColumnDescriptor hcDesc2 = new HColumnDescriptor(CD2);
            HColumnDescriptor hcDesc3 = new HColumnDescriptor(CD3);
            htDesc.addFamily(hcDesc1).addFamily(hcDesc2).addFamily(hcDesc3);
            admin.createTable(htDesc);
            admin.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 插入数据
     * @param tableName 表明
     * @param path 文件路径
     */
    @Override
    public boolean insert(String tableName,String path) {
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            List<List<Put>> list = HBaseUtil.putFile(CD1,CD2,CD3,INFO_CLUMS,COST_CLUMS,FMT_CLUMS,path);
            //System.out.println("list:"+list.size());
            for (List<Put> puts : list) {
                System.out.println("======"+puts.size());
                table.put(puts);
            }
            table.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
