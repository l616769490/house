package com.tecode.mysql.dao;

import com.tecode.mysql.bean.XAxis;
import com.tecode.mysql.bean.XAxisExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface XAxisMapper {
    long countByExample(XAxisExample example);

    int deleteByExample(XAxisExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XAxis record);

    int insertSelective(XAxis record);

    List<XAxis> selectByExample(XAxisExample example);

    XAxis selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XAxis record, @Param("example") XAxisExample example);

    int updateByExample(@Param("record") XAxis record, @Param("example") XAxisExample example);

    int updateByPrimaryKeySelective(XAxis record);

    int updateByPrimaryKey(XAxis record);
}