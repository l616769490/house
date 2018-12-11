package com.tecode.house.azhangzhou.controller;



import com.tecode.house.azhangzhou.showService.ShowTables;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ZZTableController {

    @Autowired
    ShowTables showTables;

    /**
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/basic-vacancy-table", method = RequestMethod.POST)
    public Table vacancyTable(@RequestBody TablePost tablePost) {
        /*if (tablePost == null) {
            return showTables.showHouseDutyTable(null);
        }*/
        return showTables.showVacancyTable(tablePost);
    }
    /**
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/city-singleBuilding-table", method = RequestMethod.POST)
    public Table singleBuildingTable(@RequestBody TablePost tablePost) {
        /*if (tablePost == null) {
            return showTables.showHouseDutyTable(null);
        }*/
        return showTables.showSingleBuildingTable(tablePost);
    }
    /**
     * @return 请求结果
     */
    @ResponseBody
    @RequestMapping(value = "/city-houseDuty-table", method = RequestMethod.POST)
    public Table testTable(@RequestBody TablePost tablePost) {
        /*if (tablePost == null) {
            return showTables.showHouseDutyTable(null);
        }*/
        return showTables.showHouseDutyTable(tablePost);
    }
}
