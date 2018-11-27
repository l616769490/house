package com.tecode.house.lijun.test;

import com.tecode.table.TablePost;
import com.tecode.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 表格请求接口测试
 */
@Controller
public class TestTable {

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
