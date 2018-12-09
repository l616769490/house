package com.tecode.house.dengya.controller;

import com.tecode.house.dengya.showservice.TableService;
import com.tecode.house.dengya.showservice.impl.TableServiceImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TableController {
    TableService tableService = new TableServiceImpl();
    @ResponseBody
    @RequestMapping(value = "/units_table", method = RequestMethod.POST)
    public Table unitsTable(@RequestBody  TablePost tablePost){

        return tableService.getTableForUnits(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/priceByCity_table", method = RequestMethod.POST)
    public Table priceByCity(@RequestBody TablePost tablePost){

        return tableService.getTableForPrice(tablePost);
    }

}
