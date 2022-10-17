package com.xxxx.crm.base;

import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * BaseMapper 基本方法定义
 * 262626
 */

public interface BaseMapper <T,ID>{
    /**
     * 添加记录返回行数
     */
    public Integer insertSelective(T entity) throws DataAccessException;

    /**
     * 添加记录返回主键
     */
    public Integer insertHasKey(T entity) throws DataAccessException;
    /**
     * 批量添加
     */
    public Integer insertBatch(List<T> entities) throws DataAccessException;

    /**
     * 根据ID  查询详情
     */
    public T selectByPrimaryKey(ID id) throws DataAccessException;

    /**
     * 多条件查询
     * @param baseQuery
     * @return
     */
    public List<T> selectByParams(BaseQuery baseQuery) throws DataAccessException;

    /**
     * 更新单条记录
     */

    public Integer updateByPrimaryKeySelective(T entity) throws DataAccessException;
    /**
     * 批量更新
     */
    public Integer updateBatch(List<T> entities) throws DataAccessException;
    /**
     * 删除单条记录
     */
    public Integer deleteByPrimaryKey(ID id) throws DataAccessException;

    /**
     * 批量删除
     */
    public Integer deleteBatch(ID[] ids) throws DataAccessException;
}
