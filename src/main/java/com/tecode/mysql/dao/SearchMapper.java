package com.tecode.mysql.dao;

import com.tecode.mysql.bean.Search;
import com.tecode.mysql.bean.SearchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SearchMapper {
    long countByExample(SearchExample example);

    int deleteByExample(SearchExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Search record);

    int insertSelective(Search record);

    List<Search> selectByExample(SearchExample example);

    Search selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Search record, @Param("example") SearchExample example);

    int updateByExample(@Param("record") Search record, @Param("example") SearchExample example);

    int updateByPrimaryKeySelective(Search record);

    int updateByPrimaryKey(Search record);
}