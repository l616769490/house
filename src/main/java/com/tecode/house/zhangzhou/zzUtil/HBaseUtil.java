package com.tecode.house.zhangzhou.zzUtil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zz
 * HBase工具类，用于获得hbase连接和关闭连接
 */
public class HBaseUtil {
    private static Configuration conf;
    private  static final Connection conn;
    static{
        conf = HBaseConfiguration.create();
        try {
            conn = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection setConn(){
        return conn;
    }

    public static void closeConn(){
        try {
            conn.close();
        } catch (IOException e) {
            System.out.println("关闭连接失败");
            e.printStackTrace();
        }
    }

    public static List<List<Put>> putFile(byte[] info,byte[] cost,byte[] fmt, String[] infoClums,String[] costClums,String[] fmtClums,String path) throws IOException {
        List<List<Put>> re = new ArrayList<>();
        List<Put> list = new ArrayList<>();
        FileInputStream fis = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;
        while (br.ready()) {
            line = br.readLine();
            String[] spls = line.split(",");
            if (spls.length != (infoClums.length + costClums.length + fmtClums.length)) {
                System.out.println("导入数据出错！");
                return null;
            }
            Put put = new Put(Bytes.toBytes(spls[2].replace("\'", "") + "_" + spls[0].replace("\'", "")));
            for(int i=0;i<spls.length;i++){
                if(i<infoClums.length){
                    put.addColumn(info,Bytes.toBytes(infoClums[i]),Bytes.toBytes(spls[i].replace("\'","")));
                }else if(i<infoClums.length+costClums.length){
                    put.addColumn(cost,Bytes.toBytes(costClums[i-infoClums.length]),Bytes.toBytes(spls[i].replace("\'","")));
                }else {
                    put.addColumn(fmt,Bytes.toBytes(fmtClums[i-infoClums.length-costClums.length]),Bytes.toBytes(spls[i].replace("\'","")));
                }
            }
            list.add(put);
            if(list.size()==1000){
                re.add(list);
                //System.out.println("put:"+list.size());
                list=new ArrayList<>();
            }
        }
        re.add(list);
        br.close();
        fis.close();
        return re;
    }

}
