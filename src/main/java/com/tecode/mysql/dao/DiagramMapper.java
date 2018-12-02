package com.tecode.mysql.dao;

import com.tecode.mysql.bean.Diagram;
import com.tecode.mysql.bean.DiagramExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DiagramMapper {
    long countByExample(DiagramExample example);

    int deleteByExample(DiagramExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Diagram record);

    int insertSelective(Diagram record);

    List<Diagram> selectByExample(DiagramExample example);

    Diagram selectByPrimaryKey(Integer id);

    List<Diagram> selectByReportKey(Integer id);

    int updateByExampleSelective(@Param("record") Diagram record, @Param("example") DiagramExample example);

    int updateByExample(@Param("record") Diagram record, @Param("example") DiagramExample example);

    int updateByPrimaryKeySelective(Diagram record);

    int updateByPrimaryKey(Diagram record);
}