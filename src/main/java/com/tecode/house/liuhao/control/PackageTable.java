package com.tecode.house.liuhao.control;

import com.tecode.house.liuhao.server.getTableServersDao;
import com.tecode.house.liuhao.server.impl.GetTableServersDaoImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/12/7.
 */
@Controller
public class PackageTable {
    private getTableServersDao tableserver = new GetTableServersDaoImpl();
    @ResponseBody
    @RequestMapping(value = "/CitySize_Tax_Avg_bar_table", method = RequestMethod.POST)
    public Table getCityTable(@RequestBody TablePost tablePost){
        return tableserver.getCityTax(tablePost);
    }

    /**\
     * 建筑结构类型柱状图
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/basics_structuretype_num_bar_table", method = RequestMethod.POST)
    public Table getStructuceTypeTable(@RequestBody TablePost tablePost){
        return tableserver.getStructureType(tablePost);
    }
}
