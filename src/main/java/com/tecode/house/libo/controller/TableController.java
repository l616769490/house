package com.tecode.house.libo.controller;

import com.tecode.house.libo.test.TestService;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class TableController {

    @Autowired
    TestService testService;

    /**
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/test-table", method = RequestMethod.POST)
    public Table testTable(@RequestParam(required = false) TablePost tablePost) {
        if (tablePost == null) {
            return testService.getTable();
        }
        return testService.getTable(tablePost);
    }
}
