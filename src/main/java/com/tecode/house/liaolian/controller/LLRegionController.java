package com.tecode.house.liaolian.controller;

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
public class LLRegionController {
    @Autowired
    private SelectMysqlServer basicsRoomsNumServer;

    @ResponseBody
    @RequestMapping(value = "/basics_family_money", method = RequestMethod.POST)
    public Option basicsRoomsNum(Integer year) {
        return basicsRoomsNumServer.select(year, "基础-家庭收入分析");
    }
}
