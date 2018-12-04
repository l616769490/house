package com.tecode.zxl.server.impl;



import com.tecode.zxl.dao.MySQLDao;
import com.tecode.zxl.dao.impl.MySQLDaoImpl;
import com.tecode.zxl.server.MaketPriceServer;



public class ServerImpl implements MaketPriceServer {

   private MySQLDao sd=new MySQLDaoImpl();

    @Override
    public void getValue() {

       if(sd.into()){
           System.out.println("插入数据成功");
       }else{
           System.out.println("插入数据失败");
       }
    }

}
