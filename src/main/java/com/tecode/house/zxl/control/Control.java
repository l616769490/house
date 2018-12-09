package com.tecode.house.zxl.control;

import com.tecode.table.Table;
import com.tecode.table.TablePost;
import com.tecode.house.zxl.server.MaketPriceServer;
import com.tecode.house.zxl.server.impl.ServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
public class Control {
    @Autowired
    private static MaketPriceServer mps = new ServerImpl();

    public static void main(String[] args) {
        mps.intoMysql("2013");
    }


    /**
     * 统计数据，然后写入Mysql
     *
     * @param year 要统计的结果
     * @return 结果写入Mysql是否成功
     */
    @ResponseBody
    @RequestMapping(value = "/zxl-control", method = RequestMethod.POST)
    public Map<String, Object> init(String year) {
        boolean b = mps.intoMysql(year);
        Map<String, Object> map = new HashMap<>();
        if (b) {
            map.put("success", true);
            map.put("msg", "统计数据写入Mysql成功");
        } else {
            map.put("success", false);
            map.put("msg", "统计数据写入Mysql失败");
        }
        return map;

    }


    /**
     * 展示市场价的表格
     *
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/zxl-valuetable", method = RequestMethod.POST)
    public Table valueTable(@RequestBody TablePost tablePost) {
        if (tablePost == null) {
            return mps.getValueTable();
        }
        return mps.getValueTable(tablePost);
    }


    /**
     * 展示家庭人数的表格
     *
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/zxl-persontable", method = RequestMethod.POST)
    public Table personTable(@RequestBody TablePost tablePost) {
        if (tablePost == null) {
            return mps.getPersonTable();
        }
        return mps.getPersonTable(tablePost);
    }

    /**
     * 展示家庭收入的表格
     *
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/zxl-incometable", method = RequestMethod.POST)
    public Table incomeTable(@RequestBody TablePost tablePost) {
        if (tablePost == null) {
            return mps.getIncomeTable();
        }
        return mps.getIncomeTable(tablePost);
    }


}