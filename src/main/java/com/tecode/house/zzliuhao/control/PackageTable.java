package com.tecode.house.zzliuhao.control;

import com.tecode.house.zzliuhao.server.getTableServersDao;
import com.tecode.house.zzliuhao.server.impl.GetTableServersDaoImpl;
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
    @RequestMapping(value = "/cityTax_table", method = RequestMethod.POST)
    public Table getCityTable(TablePost tablePost){
        return tableserver.getCityTax(tablePost);
    }

    @ResponseBody
    @RequestMapping(value = "/structureType_table", method = RequestMethod.POST)
    public Table getStructuceTypeTable(TablePost tablePost){
        return tableserver.getStructureType(tablePost);
    }
}
