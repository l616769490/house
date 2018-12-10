package com.tecode.house.zzliuhao.control;

import com.tecode.pagelist.Group;
import com.tecode.pagelist.PageList;
import com.tecode.pagelist.Report;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/12/6.
 */
@Controller
public class PackagePage {
    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public PageList testPage() {
        return new PageList()
                .addYear(2011).addYear(2013)
                .addGroup(new Group("基础分析")
                        .addReport(new Report("建筑结构类型分析", "http://www.baidu.com"))

                ).addGroup(new Group("城市规模分析")
                        .addReport(new Report("税务分析", "http://www.baidu.com"))
                );
    }
}
