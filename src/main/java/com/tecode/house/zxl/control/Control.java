package com.tecode.house.zxl.control;

import com.tecode.table.Table;
import com.tecode.table.TablePost;
import com.tecode.house.zxl.server.MaketPriceServer;
import com.tecode.house.zxl.server.impl.ServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class Control {
    private  static MaketPriceServer mps=new ServerImpl();
    public static void main(String[] args) {
        mps.getValue();
    }

    @Autowired

    @ResponseBody
    @RequestMapping(value = "/zxl-control", method = RequestMethod.POST)
    public void init() {



    }



    @ResponseBody
    @RequestMapping(value = "/zxl-valuetable", method = RequestMethod.POST)
    public Table testTable(@RequestParam(required = false) TablePost tablePost) {
      if (tablePost == null) {
        return mps.getTable();
    }
        return mps.getTable(tablePost);
}


}
