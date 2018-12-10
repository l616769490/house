package com.tecode.house.lijun.controller;

import com.tecode.house.lijun.sSerivce.TSerivce;
import com.tecode.house.lijun.sSerivce.impl.TSerivceImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TController {



    TSerivce tableSerivce = new TSerivceImpl();
    @ResponseBody
    @RequestMapping(value = "/cost_table", method = RequestMethod.POST)
    public Table cost(@RequestBody TablePost tablePost) {

        return tableSerivce.getTableForCost(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/price_table", method = RequestMethod.POST)
    public Table price(@RequestBody TablePost tablePost){
            Table table = tableSerivce.getTablePrice(tablePost.getPage(), tablePost.getYear());
            return table;


    }

    @ResponseBody
    @RequestMapping(value = "/income_table", method = RequestMethod.POST)
    public Table income(@RequestBody TablePost tablePost){
        return tableSerivce.getTableForIncome(tablePost);
    }

}
