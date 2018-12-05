package com.tecode.house.test;

import com.tecode.table.*;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public Table getTable(TablePost tablePost) {
        return new Table().setYear(2011).setPage(new Page().setThisPage(5).addData(2).addData(3).addData(4).addData(5).addData(6))
                .addTop("数据1").addTop("数据2")
                .addSearchs(new Search().setTitle("按地区").addValue("东部地区").addValue("西部地区"))
                .addData(new Row().addRow("asa").addRow("sds"))
                .addData(new Row().addRow("123").addRow("232"))
                .addData(new Row().addRow("dfd").addRow("234"));
    }

    @Override
    public Table getTable() {

        return new Table().setYear(2011).setPage(new Page().setThisPage(1).addData(1).addData(2).addData(3).addData(4).addData(5))
                .addTop("数据1").addTop("数据2")
                .addSearchs(new Search().setTitle("按地区").addValue("东部地区").addValue("西部地区"))
                .addData(new Row().addRow("asa").addRow("sds"))
                .addData(new Row().addRow("123").addRow("232"))
                .addData(new Row().addRow("dfd").addRow("234"));

    }
}
