package com.tecode.house.chenyong.controller;


import com.tecode.house.chenyong.service.EncapsulationIntoTable;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/12/9.
 */
@Controller
public class EncapsulationIntoTableController {

    EncapsulationIntoTable eit = new EncapsulationIntoTable();

    @ResponseBody
    @RequestMapping(value = "/ageAnalysis_table", method = RequestMethod.POST)
    public Table getIntoTableAge(@RequestBody TablePost tablePost){
        return eit.intoTableAge(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/roomsAnalysisByAge_table", method = RequestMethod.POST)
    public Table getIntoTableRooms(@RequestBody TablePost tablePost){
        return eit.intoTableRooms(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/utilityAnalysisByAge_table", method = RequestMethod.POST)
    public Table getIntoTableUtility(@RequestBody TablePost tablePost){
        return eit.intoTableUtility(tablePost);
    }

}
