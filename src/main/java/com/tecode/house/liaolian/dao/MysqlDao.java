package com.tecode.house.liaolian.dao;

import java.util.List;

/**
 * Created by Administrator on 2018/12/6.
 */
public interface MysqlDao<T> {
    /**
     * 查询表中所有数据
     * @return	查询到的所有数据
     */
    List<T> findAll();

    /**
     * 根据单条件查询数据
     * @param column	列名
     * @param value	值
     * @return	查询到的数据
     */
    List<T> findByColumn(String column, String value);

    /**
     * 多条件查询
     * @param columns	查询项
     * @param values	查询值
     * @return	查询到的数据
     */
    List<T> findByColums(String[] columns, String[] values);

    /**
     * 插入数据
     * @param t	插入的数据
     * @return	成功的条数
     */
    int insert(T t);

    /**
     * 插入多条数据
     * @param ts 数据项
     * @return 成功的条数
     */
    int insert(List<T> ts);

    /**
     * 更新数据
     * @param t		新对象
     * @return	成功的条数
     */
    int update(T t);

    /**
     * 更新多条数据
     * @param ts	新对象
     * @return	成功的条数
     */
    int update(List<T> ts);

    /**
     * 删除数据
     * @param t	要删除的数据
     * @return	成功的条数
     */
    int delect(T t);
}
