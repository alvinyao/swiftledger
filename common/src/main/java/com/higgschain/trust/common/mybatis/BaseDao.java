package com.higgschain.trust.common.mybatis;

import java.util.List;

/**
 * The interface Base dao.
 *
 * @param <T> the type parameter
 * @Description:
 * @author: pengdi
 */
public interface BaseDao<T> {

    /**
     * Add int.
     *
     * @param t the t
     * @return the int
     */
    public int add(T t);

    /**
     * Delete int.
     *
     * @param id the id
     * @return the int
     */
    public int delete(Object id);

    /**
     * Query by count int.
     *
     * @param t the t
     * @return the int
     */
    public int queryByCount(T t);

    /**
     * Query by list list.
     *
     * @param t the t
     * @return the list
     */
    public List<T> queryByList(T t);

    /**
     * Query by id t.
     *
     * @param id the id
     * @return the t
     */
    public T queryById(Object id);
}
