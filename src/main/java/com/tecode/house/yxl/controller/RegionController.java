package com.tecode.house.yxl.controller;

import com.tecode.echarts.Option;
import com.tecode.house.lijin.service.SelectMysqlServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/12/5.
 */
@Controller
public class RegionController {
    @Autowired
    private SelectMysqlServer basicsRoomsNumServer;

    @ResponseBody
    @RequestMapping(value = "/basics_year", method = RequestMethod.POST)
    public Option basicsRoomsNum(Integer year) {
        return basicsRoomsNumServer.select(year, "基础-建成年份分析");
    }
}
