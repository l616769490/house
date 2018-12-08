package com.tecode.house.lijin.test;

import com.tecode.house.lijin.service.TableServer;
import com.tecode.table.Search;
import com.tecode.table.TablePost;
import com.tecode.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格请求接口测试
 */
@Controller
public class TestTable {

    @Autowired
    TestService testService;

    @Autowired
    TableServer tableServer;

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


    @ResponseBody
    @RequestMapping(value = "/test-table2", method = RequestMethod.POST)
    public Table testTable2(@RequestBody TablePost tablePost) {
//        tableServer.getTable("基础-房间数分析", tablePost, TestTable.class.getResource("/table/basics-rooms.xml").getPath());
        return tableServer.getTable("基础-房间数分析", tablePost, TestTable.class.getResource("/table/basics-rooms.xml").getPath());
    }

    @ResponseBody
    @RequestMapping(value = "/test-tp", method = RequestMethod.POST)
    public TablePost testTablePost() {
        List<Search> searches = new ArrayList<>();
        Search search1 = new Search();
        search1.setTitle("房间数");
        List<String> l1 = new ArrayList<>();
        l1.add("1");
        search1.setValues(l1);
        searches.add(search1);

        Search search2 = new Search();
        search2.setTitle("卧室数");
        List<String> l2 = new ArrayList<>();
        l2.add("1");
        search2.setValues(l2);
        searches.add(search2);

        TablePost tp = new TablePost();
        tp.setPage(2);
        tp.setYear(2013);
        tp.setSearches(searches);
        return tp;
    }

    @RequestMapping(value = "/table")
    public String upload() {
        return "table";
    }

}
