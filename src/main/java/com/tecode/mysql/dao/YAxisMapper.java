package com.tecode.mysql.dao;

import com.tecode.mysql.bean.YAxis;
import com.tecode.mysql.bean.YAxisExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface YAxisMapper {
    long countByExample(YAxisExample example);

    int deleteByExample(YAxisExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(YAxis record);

    int insertSelective(YAxis record);

    List<YAxis> selectByExample(YAxisExample example);

    YAxis selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") YAxis record, @Param("example") YAxisExample example);

    int updateByExample(@Param("record") YAxis record, @Param("example") YAxisExample example);

    int updateByPrimaryKeySelective(YAxis record);

    int updateByPrimaryKey(YAxis record);
}