package com.tecode.house.zhangzhou.zzUtil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.ArrayList;
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

    public static List<Put> putFile(Put put, byte[] info,byte[] cost,byte[] fmt, String[] infoClums,String[] costClums,String[] fmtClums) throws IOException {
        List<Put> list = new ArrayList<>();
        FileInputStream fis = new FileInputStream("e:/american/thads2011.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = br.readLine();
        String[] spls = line.split(",");
        if(spls.length!=(infoClums.length+costClums.length+fmtClums.length)){
            System.out.println("导入数据出错！");
            return null;
        }
        for (int i=0;i<spls.length;i++) {
            String newWord = spls[i].replace("\'","");
            if(i<infoClums.length){
                Put putInfo = put.addColumn(info, Bytes.toBytes(infoClums[i]), Bytes.toBytes(newWord));
                list.add(putInfo);
            }else if(i<(infoClums.length+costClums.length)){
                Put putCost = put.addColumn(cost,Bytes.toBytes(costClums[i]),Bytes.toBytes(newWord));
                list.add(putCost);
            }else {
                Put putFmt = put.addColumn(fmt,Bytes.toBytes(fmtClums[i]),Bytes.toBytes(newWord));
                list.add(putFmt);
            }
        }
        br.close();
        fis.close();
        return list;
    }

}
