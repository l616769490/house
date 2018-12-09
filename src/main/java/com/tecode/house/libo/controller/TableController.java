package com.tecode.house.libo.controller;

import com.tecode.house.libo.Service.InsertTable;
import com.tecode.house.libo.Service.ServiceImpl.InsertToTable;
import com.tecode.house.libo.test.TestService;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
public class TableController {


    InsertTable insertTable = new InsertToTable();

    /**
     * 自住、租赁
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/test-table-self", method = RequestMethod.POST)
    public Table rentTable(@RequestBody TablePost tablePost) {
//        if (tablePost == null) {
//            return testService.getTable();
//        }
        return insertTable.insertSelfTable(tablePost);
    }

    /**
     * 自住、租赁
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/test-table-room", method = RequestMethod.POST)
    public Table roomTable(@RequestBody TablePost tablePost) {
//        if (tablePost == null) {
//            return testService.getTable();
//        }
        return insertTable.insertRoomTable(tablePost);
    }




    /**
     * 自住、租赁
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/test-table-single", method = RequestMethod.POST)
    public Table singleTable(@RequestBody TablePost tablePost) {
//        if (tablePost == null) {
//            return testService.getTable();
//        }
        return insertTable.insertSingleTable(tablePost);
    }





}
