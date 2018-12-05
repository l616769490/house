package com.tecode.house.zouchao.controller;

import com.tecode.house.lijin.test.TestService;
import com.tecode.house.zouchao.showSerivce.TableSerivce;
import com.tecode.house.zouchao.showSerivce.TableSerivceImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TableController {



    TableSerivce tableSerivce = new TableSerivceImpl();
    @ResponseBody
    @RequestMapping(value = "/rent_table", method = RequestMethod.POST)
    public Table rentTable(TablePost tablePost) {
        if (tablePost.getSearches().isEmpty()) {
            Table table = tableSerivce.getTable(tablePost.getPage(), tablePost.getYear());
            return table;
        }
        return tableSerivce.getTable(tablePost);
    }
}
