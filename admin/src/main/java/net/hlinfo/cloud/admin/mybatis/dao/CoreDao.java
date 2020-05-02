package net.hlinfo.cloud.admin.mybatis.dao;

import java.util.List;

public interface CoreDao {
    /**
     * 查询列表，有条件
     * @param mybitsSqlId
     * @param object
     * @return list
     */
    public abstract List queryList(String mybitsSqlId, Object object);

    /**
     * 查询列表，无条件
     * @param mybitsSqlId
     * @return list
     */
    public abstract List queryList(String mybitsSqlId);

    /**
     * 查询数量，无条件
     * @param mybitsSqlId
     * @return int
     */
    public abstract int queryCount(String mybitsSqlId);

    /**
     * 查询数量，有条件
     * @param mybitsSqlId
     * @param object
     * @return int
     */
    public abstract int queryCount(String mybitsSqlId, Object object);

    /**
     * 查询一条数据，无条件
     * @param mybitsSqlId
     * @return Object
     */
    public abstract Object queryOne(String mybitsSqlId);

    /**
     * 查询一条数据，有条件
     * @param mybitsSqlId
     * @param object
     * @return Object
     */
    public abstract Object queryOne(String mybitsSqlId, Object object);

    /**
     * 新增
     * @param mybitsSqlId
     * @param object
     * @return
     */
    public abstract boolean save(String mybitsSqlId, Object object);

    /**
     * 修改
     * @param mybitsSqlId
     * @param object
     * @return
     */
    public abstract boolean update(String mybitsSqlId, Object object);
}
