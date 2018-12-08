package com.tecode.house.libo.test;

import com.tecode.house.libo.Service.InsertTable;
import com.tecode.house.libo.Service.ServiceImpl.InsertToTable;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 表格请求接口测试
 */
@Controller
public class TestTable {


    private InsertTable i = new InsertToTable();

    /**
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/basics_rooms_num_table", method = RequestMethod.POST)
    public Table testTable(@RequestBody TablePost tablePost) {
        return i.insertSelfTable(tablePost);
    }
}
