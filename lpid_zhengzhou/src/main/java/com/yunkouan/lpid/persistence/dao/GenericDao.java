package com.yunkouan.lpid.persistence.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;

import com.yunkouan.lpid.persistence.entity.Entity;


/**
 * DAO基类接口
 * 
 * @author yangli
 *
 */
public interface GenericDao{

    /**
     * 保存实体
     * 
     * @param entity
     * @return
     */
	<E extends Entity<K>, K> E save(E entity);
	
	/**
	 * 更新实体
	 * 
	 * @param entity
	 * @return
	 */
	<E extends Entity<K>, K> E update(E entity);

	/**
	 * 根据实体类删除实体
	 * 
	 * @param entity 实体对象
	 * @param clazz 实体类名称
	 */
	<E extends Entity<K>, K> void delete(final E entity, final Class<E> clazz);

	/**
	 * 根据实体类的主键值删除实体
	 * 
	 * @param id 实体对象主键值
	 * @param clazz 实体类名称
	 */
	<E extends Entity<K>, K> void delete(final K id, final Class<E> clazz);

	/**
	 * 根据实体类的主键和名称查询实体
	 * 
	 * @param id 实体对象主键值
	 * @param clazz 实体类名称
	 * @return 实体对象
	 */
	<E extends Entity<K>, K> E find(final K id, final Class<E> clazz);

	/**
	 * 根据实体类名称查询所有实体对象
	 * 
	 * @param clazz 实体类名称
	 * @return 所有实体对象
	 */
	<E extends Entity<K>, K> List<E> findAll(final Class<E> clazz);

	/**
	 * 根据实体类的主键和名称load实体,想哈希不到的话，抛出异常
	 * 
	 * @param id 实体对象主键值
	 * @param clazz 实体类名称
	 * @return 实体对象
	 */
	<E extends Entity<K>, K> E load(final K id, final Class<E> clazz);

	/**
	 * 根据实体类对象查询出总记录个数
	 * 
	 * @param clazz 实体对象
	 * @return 总记录个数
	 */
	<E extends Entity<K>, K> long getCount(final Class<E> clazz);
	
	/**
     * 根据jpql语句，查询出总记录个数
     * 
     * @param jpql
     * @param params
     * @return 
     * @since 1.0.0
     * 2014年10月15日-上午10:58:47
     */
    int getCount(String jpql, Object... params);
	
	/**
	  * 执行JPQL语句，更新实体
	  * 
	  * @param updateJPQL JPQL语句  <code>update User o set o.userName = ? where o.id = ?</code>
	  * params 预处理参数 <code>new Object{"张三",1} ; </code>
	  */
	boolean update(String updateJPQL, Object... params);
	
	/**
	 * 执行JPQL语句，删除实体
	 * 
	 * @param deleteJPQL JPQL语句  <code>delete User o where o.userName = ? and o.id = ?</code>
	 * params 预处理参数 <code>new Object{"张三",1} ; </code>
	 */
	boolean delete(String deleteJPQL, Object... params);
	
	/**
	 * JPQL方式查询
	 * 
	 * @param jpql jpql语句
	 * @param params 可变化的参数集合
	 * @return 实体结果集
	 */
    <E extends Entity<K>, K> List<E> findByNamedQuery(final String jpql, Object... params);
    
    /**
     * JPQL方式分页查询
     * 
     * @param jpql jpql语句
     * @param params 可变化的参数集合
     * @return 实体结果集
     */
    <E extends Entity<K>, K> List<E> findByNamedQuery(final String jpql, int firstResult, int maxResults, Object... params);

    /**
     * JPQL方式查询
     * 
     * @param jpql jpql语句
     * @param params Map参数集合
     * @return 实体结果集
     */
    <E extends Entity<K>, K> List<E> findByNamedQuery(final String jpql, Map<String, ? extends Object> params);
    
    /**
     * 查询单个对象（首个）
     * @param criteriaQuery
     * @return 
     * @since 1.0.0
     * 2015-1-14-上午11:07:07
    */
    <V> V findSingleOrNull(final CriteriaQuery<V> criteriaQuery);
    

    /**
     * 查询单个对象（首个）
     * @param jpql
     * @param params
     * @return 
     * @since 1.0.0
     * 2015-1-14-上午11:08:41
    */
    <V extends Entity<K>, K> V findFirstOrNull(final String jpql, Object... params);

}
