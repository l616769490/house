package com.tecode.house.jianchenfei.controllor;


import com.tecode.house.jianchenfei.service.TableSerivce;
import com.tecode.house.jianchenfei.service.serviceImpl.TableServiceImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class JCFTableController {


    private  TableSerivce tableSerivce = new TableServiceImpl();

    @ResponseBody
    @RequestMapping(value = "/per_table", method = RequestMethod.POST)
    public Table perTable(@RequestBody  TablePost tablePost) {

        return tableSerivce.getTablePer(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/build_rate_table", method = RequestMethod.POST)
    public Table ratefrombuild(@RequestBody TablePost tablePost) {
        return tableSerivce.getTableRate(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/region_single_table", method = RequestMethod.POST)
    public Table singlefromregion(@RequestBody TablePost tablePost) {
        return tableSerivce.getTableSingle(tablePost);
    }

}
