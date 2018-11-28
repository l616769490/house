package com.tecode.house.jianchenfei.hbasedata;

import com.tecode.house.jianchenfei.utils.HBaseUtil;

/**
 * Created by Administrator on 2018/11/28.
 */
public class DataToHbase {
    public static void main(String[] args) {
        HBaseUtil.createTable("thads:2011","info","cost","fmt");
        HBaseUtil.createTable("thads:2013","info","cost","fmt");
    }

}
