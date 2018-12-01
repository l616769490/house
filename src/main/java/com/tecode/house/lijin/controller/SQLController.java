package com.tecode.house.lijin.controller;

import com.tecode.house.d01.service.Analysis;
import com.tecode.house.lijin.hbase.BasicsHouseNum;
import com.tecode.house.lijin.service.MysqlServer;
import com.tecode.house.lijin.service.impl.BasicsRoomsNumServer;
import com.tecode.house.lijin.test.TestAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 分析并保存数据到SQL
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
@Controller
public class SQLController {

    /**
     * 分析并保存数据
     *
     * @return 结果
     */
    @ResponseBody
    @RequestMapping(value = "/analysis", method = RequestMethod.GET)
    public String start() {
        Analysis basicHouseNum = new TestAnalysis();
        basicHouseNum.analysis("thads:2013");
        return null;
    }

    public static void main(String[] args) {
        new SQLController().start();
    }


}
