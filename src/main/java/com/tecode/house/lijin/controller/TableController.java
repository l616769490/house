package com.tecode.house.lijin.controller;

import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public class TableController {
    @ResponseBody
    @RequestMapping(value = "/basic_rooms_num_table", method = RequestMethod.POST)
    public Table basicsRoomsNumTable(@RequestParam(required = false) TablePost tablePost) {



        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/region_zinc2_num_table", method = RequestMethod.POST)
    public Table regionZinc2NumTable(@RequestParam(required = false) TablePost tablePost) {
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/region_zsmhc_num_table", method = RequestMethod.POST)
    public Table regionZsmhcNumTable(@RequestParam(required = false) TablePost tablePost) {
        return null;
    }
}
