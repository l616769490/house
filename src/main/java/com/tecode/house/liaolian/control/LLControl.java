package com.tecode.house.liaolian.control;

import com.tecode.house.d01.service.Analysis;
import com.tecode.house.liaolian.server.LLMaketPriceServer;
import com.tecode.house.liaolian.server.impl.LLServerImpl;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class LLControl {
    @Autowired
    private static LLMaketPriceServer mps =new LLServerImpl();

    private static Analysis  as=new LLServerImpl();

    public static void main(String[] args) {
        if(as.analysis("thads:2013")){
           // System.out.println("分析数据成功");
        }else{
            //System.out.println("分析数据失败");
        }
    }


    /**
     * 统计数据，然后写入Mysql
     *
     * @param year 要统计的结果
     * @return 结果写入Mysql是否成功
     */
    @ResponseBody
    @RequestMapping(value = "/ll-control", method = RequestMethod.POST)
    public Map<String, Object> init(String year) {
        boolean b =as.analysis(year);
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
    @RequestMapping(value = "/ll_value_table", method = RequestMethod.POST)
    public Table valueTable(@RequestBody TablePost tablePost) {

        return mps.getValueTable(tablePost);
    }


    /**
     * 展示家庭人数的表格
     *
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ll_person_table", method = RequestMethod.POST)
    public Table personTable(@RequestBody TablePost tablePost) {

        return mps.getPersonTable(tablePost);
    }

    /**
     * 展示家庭收入的表格
     *
     * @param tablePost
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ll_income_table", method = RequestMethod.POST)
    public Table incomeTable(@RequestBody TablePost tablePost) {

        return mps.getIncomeTable(tablePost);
    }


}
