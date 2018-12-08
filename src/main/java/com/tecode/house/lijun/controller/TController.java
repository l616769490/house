package com.tecode.house.lijun.controller;

import com.tecode.house.lijun.showSerivce.TableSerivce;
import com.tecode.house.lijun.showSerivce.impl.TableSerivceImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TController {



    TableSerivce tableSerivce = new TableSerivceImpl();
    @ResponseBody
    @RequestMapping(value = "/cost_table", method = RequestMethod.POST)
    public Table cost(TablePost tablePost) {

        return tableSerivce.getTableForCost(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/price_table", method = RequestMethod.POST)
    public Table price(TablePost tablePost){
            Table table = tableSerivce.getTableForPrice(tablePost.getPage(), tablePost.getYear());
            return table;


    }

    @ResponseBody
    @RequestMapping(value = "/income_table", method = RequestMethod.POST)
    public Table income(TablePost tablePost){
        return tableSerivce.getTableForIncome(tablePost);
    }

}
