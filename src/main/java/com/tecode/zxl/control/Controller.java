package com.tecode.zxl.control;

import com.tecode.zxl.server.MaketPriceServer;
import com.tecode.zxl.server.impl.ServerImpl;

public class Controller {


    public static void main(String[] args) {
        MaketPriceServer mps=new ServerImpl();
        mps.getValue();
    }
}
