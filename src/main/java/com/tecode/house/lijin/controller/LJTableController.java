package com.tecode.house.lijin.controller;

import com.tecode.house.lijin.service.TableServer;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
@Controller
public class LJTableController {
    @Autowired
    TableServer tableServer;

    /**
     * 获取基础-房间数分析详细数据
     *
     * @param tablePost 前台搜索数据
     * @return 表格
     */
    @ResponseBody
    @RequestMapping(value = "/basics_rooms_num_table", method = RequestMethod.POST)
    public Table basicsRoomsNumTable(@RequestBody TablePost tablePost) {
        return tableServer.getTable("基础-房间数分析", tablePost, LJTableController.class.getResource("/table/basics-rooms.xml").getPath());
    }

    @ResponseBody
    @RequestMapping(value = "/region_zinc2_num_table", method = RequestMethod.POST)
    public Table regionZinc2NumTable(@RequestBody TablePost tablePost) {

        return tableServer.getTable("按区域-家庭收入分析", tablePost, LJTableController.class.getResource("/table/region-zinx2.xml").getPath());
    }

    @ResponseBody
    @RequestMapping(value = "/region_zsmhc_num_table", method = RequestMethod.POST)
    public Table regionZsmhcNumTable(@RequestBody TablePost tablePost) {

        return tableServer.getTable("按区域-房产税分析", tablePost, LJTableController.class.getResource("/table/region-zsmhc.xml").getPath());
    }
}
