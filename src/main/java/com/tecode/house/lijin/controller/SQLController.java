package com.tecode.house.lijin.controller;

import com.tecode.house.d01.service.Analysis;
import com.tecode.house.lijin.test.db.TestAnalysis;

/**
 * 分析并保存数据到SQL
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public class SQLController {

    /**
     * 分析并保存数据
     *
     * @return 结果
     */
    public String start() {
        Analysis basicHouseNum = new TestAnalysis();
        basicHouseNum.analysis("thads:2013");
        return null;
    }

    public static void main(String[] args) {
        new SQLController().start();
    }


}
