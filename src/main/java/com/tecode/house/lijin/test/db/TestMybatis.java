package com.tecode.house.lijin.test.db;

import com.tecode.mysql.bean.Dimension;
import com.tecode.mysql.dao.DiagramMapper;
import com.tecode.mysql.dao.DimensionMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

/**
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
@Controller
public class TestMybatis {

    @Autowired
    private DimensionMapper dimensionMapper;


    @ResponseBody
    @RequestMapping(value = "/test-mybatis", method = RequestMethod.GET)
    public void test() {
        Dimension dimension = dimensionMapper.selectByPrimaryKey(1);
        System.out.println(dimension);
    }
}
