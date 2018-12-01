package com.tecode.mysql.dao;

import com.tecode.mysql.bean.Legend;
import com.tecode.mysql.bean.LegendExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LegendMapper {
    long countByExample(LegendExample example);

    int deleteByExample(LegendExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Legend record);

    int insertSelective(Legend record);

    List<Legend> selectByExample(LegendExample example);

    Legend selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Legend record, @Param("example") LegendExample example);

    int updateByExample(@Param("record") Legend record, @Param("example") LegendExample example);

    int updateByPrimaryKeySelective(Legend record);

    int updateByPrimaryKey(Legend record);
}