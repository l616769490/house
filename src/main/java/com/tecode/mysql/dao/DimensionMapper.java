package com.tecode.mysql.dao;

import com.tecode.mysql.bean.Dimension;
import com.tecode.mysql.bean.DimensionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DimensionMapper {
    long countByExample(DimensionExample example);

    int deleteByExample(DimensionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Dimension record);

    int insertSelective(Dimension record);

    List<Dimension> selectByExample(DimensionExample example);

    Dimension selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Dimension record, @Param("example") DimensionExample example);

    int updateByExample(@Param("record") Dimension record, @Param("example") DimensionExample example);

    int updateByPrimaryKeySelective(Dimension record);

    int updateByPrimaryKey(Dimension record);
}