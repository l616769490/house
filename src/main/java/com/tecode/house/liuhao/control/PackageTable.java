package com.tecode.house.liuhao.control;

import com.tecode.house.liuhao.server.getTableServersDao;
import com.tecode.house.liuhao.server.impl.GetTableServersDaoImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/12/7.
 */
public class PackageTable {
    private getTableServersDao tableserver = new GetTableServersDaoImpl();
    @ResponseBody
    @RequestMapping(value = "/CitySize_Tax_Avg_bar_table", method = RequestMethod.POST)
    public Table getCityTable(TablePost tablePost){
        return tableserver.getCityTax(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/basics_structuretype_num_bar_table", method = RequestMethod.POST)
    public Table getStructuceTypeTable(TablePost tablePost){
        return tableserver.getStructureType(tablePost);
    }
}
