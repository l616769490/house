package com.tecode.house.lijun.controller;

import com.tecode.house.azouchao.showSerivce.TableSerivce;
import com.tecode.house.azouchao.showSerivce.TableSerivceImpl;
import com.tecode.house.lijin.service.TableServer;
import com.tecode.house.lijun.sSerivce.TSerivce;
import com.tecode.house.lijun.sSerivce.impl.TSerivceImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TController {

    @Autowired
    TableServer tableServer;

    private TableSerivce tableSerivce1= new TableSerivceImpl();
    TSerivce tableSerivce = new TSerivceImpl();

    /**
     * 房屋费用
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/cost_table", method = RequestMethod.POST)
    public Table cost(@RequestBody TablePost tablePost) {

        return tableSerivce.getTableForCost(tablePost);
       // return tableSerivce1.getTableForRent(tablePost);
        //return tableServer.getTable("基础-房间数分析", tablePost, LJTableController.class.getResource("/table/basics-rooms.xml").getPath());


    }
    /**
     * 水电费用
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shuidian_table", method = RequestMethod.POST)
    public Table utility(@RequestBody TablePost tablePost) {

        return tableSerivce.getTableForCost(tablePost);
    }


    /**
     * 其他费用
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/other_table", method = RequestMethod.POST)
    public Table other(@RequestBody TablePost tablePost) {

        return tableSerivce.getTableForCost(tablePost);
    }


    /**
     * 总共费用
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/total_table", method = RequestMethod.POST)
    public Table total(@RequestBody TablePost tablePost) {

        return tableSerivce.getTableForCost(tablePost);
    }

    /**
     * 住房价格
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/price_value_table", method = RequestMethod.POST)
    public Table priceValue(@RequestBody TablePost tablePost){
            Table table = tableSerivce.getTablePrice(tablePost.getPage(), tablePost.getYear());
            return table;
    }

    /**
     * 住房租金
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/price_frm_table", method = RequestMethod.POST)
    public Table priceFRM(@RequestBody TablePost tablePost){
        Table table = tableSerivce.getTablePrice(tablePost.getPage(), tablePost.getYear());
        return table;
    }


    /**
     * 家庭收入
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/age_income_table", method = RequestMethod.POST)
    public Table income(@RequestBody TablePost tablePost){
        return tableSerivce.getTableForIncome(tablePost);
    }

}
