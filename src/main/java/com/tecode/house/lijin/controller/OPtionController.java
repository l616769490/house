package com.tecode.house.lijin.controller;

import com.tecode.echarts.Option;
import com.tecode.house.lijin.service.SelectMysqlServer;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 版本：2018/12/4 V1.0
 * 成员：李晋
 */
@Controller
public class OPtionController {

    @Autowired
    private SelectMysqlServer basicsRoomsNumServer;

    @ResponseBody
    @RequestMapping(value = "/basics_rooms_num", method = RequestMethod.POST)
    public Option basicsRoomsNum(Integer year) {
        Option option = basicsRoomsNumServer.select(year, "基础-房间数分析");
        return option;
    }

    @ResponseBody
    @RequestMapping(value = "/basic_rooms_num_table", method = RequestMethod.POST)
    public Table basicsRoomsNumTable(@RequestParam(required = false) TablePost tablePost) {
        return null;
    }
}
