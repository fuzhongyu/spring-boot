package com.fzy.core.base;

import java.util.List;

/**
 * dao基类
 *
 * @author Fucai
 * @date 2018/3/19
 */
public interface BaseDao<T> {


    /**
     * 查询数据列表
     * @param entity
     * @return
     */
     List<T> findList(T entity);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    T get(Long id);


    /**
     * 插入数据
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int batchInsert(List<T> list);


    /**
     * 更新数据
     * @param entity
     * @return
     */
    int update(T entity);


    /**
     * 删除数据（逻辑删除，更新del_flag字段为false）
     * @param entity
     * @return
     */
     int delete(T entity);

    /**
     * 删除数据（非逻辑删除）
     * @param id
     * @return
     */
     int del(Long id);

}
