package com.tecode.house.azouchao.controller;

import com.tecode.house.azouchao.showSerivce.TableSerivce;
import com.tecode.house.azouchao.showSerivce.TableSerivceImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ZCTableController {


    private  TableSerivce tableSerivce = new TableSerivceImpl();

    @ResponseBody
    @RequestMapping(value = "/rent_table", method = RequestMethod.POST)
    public Table rentTable(@RequestBody  TablePost tablePost) {

        return tableSerivce.getTableForRent(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/priceByBuild_table", method = RequestMethod.POST)
    public Table PriceByBuild(@RequestBody TablePost tablePost) {
        return tableSerivce.getTableForPrice(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/roomsByBuild_table", method = RequestMethod.POST)
    public Table RoomByBuild(@RequestBody TablePost tablePost) {
        return tableSerivce.getTableForRoom(tablePost);
    }

}
