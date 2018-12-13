package com.tecode.house.libo.controller;

import com.tecode.house.libo.Service.InsertTable;
import com.tecode.house.libo.Service.ServiceImpl.InsertToTable;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
public class LBTableController {


    InsertTable insertTable = new InsertToTable();

    /**
     * 自住、租赁
     * @return 请求结果
     */


    @ResponseBody
    @RequestMapping(value = "/libo_self_table", method = RequestMethod.POST)
    public Table rentTable(@RequestBody TablePost tablePost) {
//        if (tablePost == null) {
//            return testService.getTable();
//        }
        return insertTable.insertSelfTable(tablePost);
    }

    /**
     *房间数卧室数
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/libo_room_table", method = RequestMethod.POST)
    public Table roomTable(@RequestBody TablePost tablePost) {
//        if (tablePost == null) {
//            return testService.getTable();
//        }
        Table t = insertTable.insertRoomTable(tablePost);
        return t;
    }




    /**
     * 独栋建筑
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/libo_single_table", method = RequestMethod.POST)
    public Table singleTable(@RequestBody TablePost tablePost) {
//        if (tablePost == null) {
//            return testService.getTable();
//        }
        return insertTable.insertSingleTable(tablePost);
    }





}
