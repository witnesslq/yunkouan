package com.yunkouan.lpid.persistence.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunkou.common.util.CollentionUtil;
import com.yunkouan.lpid.persistence.dao.GenericDao;
import com.yunkouan.lpid.persistence.entity.Entity;


/**
 * DAO基类接口JPA方式实现类
 * 
 * @author yangli
 *
 */
public class GenericJpaDaoImpl implements GenericDao {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private EntityManager entityManager;

	@PersistenceContext(name="entityManagerFactory")
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public void setLogger(final Logger logger) {
		this.logger = logger;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public <E extends Entity<K>, K> void delete(final E entity, final Class<E> clazz) {
		if (entity == null) {
			return;
		}

		/* Make sure entity is managed before deleting */
		E managedEntity = entity;
		if (!this.getEntityManager().contains(entity)) {
			managedEntity = this.getEntityManager().find(clazz, entity.getId());
		}

		this.getEntityManager().remove(managedEntity);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public <E extends Entity<K>, K> void delete(final K id, final Class<E> clazz) {
		if (id == null) {
			return;
		}
		final E entity = this.getEntityManager().find(clazz, id);
		if (entity != null) {
			this.getEntityManager().remove(entity);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public <E extends Entity<K>, K> E find(final K id, final Class<E> clazz) {
		return this.getEntityManager().find(clazz, id);
	}

	@Override
	@Transactional(readOnly = true)
	public <E extends Entity<K>, K> List<E> findAll(final Class<E> clazz) {
		final CriteriaBuilder builder = this.getCriteriaBuilder();
		final CriteriaQuery<E> criteriaQuery = builder.createQuery(clazz);
		criteriaQuery.from(clazz);

		return this.find(criteriaQuery);
	}

	@Override
	@Transactional(readOnly = true)
	public <E extends Entity<K>, K> E load(final K id, final Class<E> clazz) {
		final E entity = this.getEntityManager().find(clazz, id);
		if (entity == null) {
			throw new NoResultException();
		}

		return entity;
	}

	@Override
	@Transactional
	public <E extends Entity<K>, K> E save(final E entity) {
		final E mergedEntity = this.getEntityManager().merge(entity);
		return mergedEntity;
	}
	
	@Override
    @Transactional
	public <E extends Entity<K>, K> E update(E entity) {
	    final E mergedEntity = this.getEntityManager().merge(entity);

        return mergedEntity;
	}

	@Override
	@Transactional(readOnly = true)
	public <E extends Entity<K>, K> long getCount(final Class<E> clazz) {
		final CriteriaBuilder builder = this.getCriteriaBuilder();
		final CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		final Root<E> from = criteriaQuery.from(clazz);

		final Expression<Long> count = builder.count(from);

		criteriaQuery.select(count);

		return this.findSingle(criteriaQuery).longValue();
	}
	
	/**
	 * @see com.yunkouan.lpid.commons.dao.GenericDao#getCount(java.lang.String, java.lang.Object[])
	 */
	@Override
	@Transactional(readOnly = true)
	public int getCount(String jpql, Object... params) {
        StringBuffer countHQL = new StringBuffer(" select count(*) ");
        countHQL.append(jpql.substring(jpql.toLowerCase().indexOf("from")));
        Query query = entityManager.createQuery(countHQL.toString());
        this.setQueryParameterValues(query, params);
        
        int count = 0;
        try {
            String temp = query.getSingleResult().toString();
            count = Integer.valueOf(temp);
        } catch (final NoResultException e) {
            count = 0;
        }
        
        return count;
	}
	
	/**
	 * @see com.yunkouan.lpid.commons.dao.GenericDao#update(java.lang.String, java.lang.Object[])
	 */
	@Override
    public boolean update(String updateJPQL, Object... params) {
	    return this.execute(updateJPQL, params);
	}
    
	@Override
	public boolean delete(String deleteJPQL, Object... params){
	    return this.execute(deleteJPQL, params);
	}
	
    @Override
    public <E extends Entity<K>, K> List<E> findByNamedQuery(final String jpql, Object... params){
        Query query = getEntityManager().createQuery(jpql);
        this.setQueryParameterValues(query, params);
        return (List<E>) query.getResultList();
    }

    @Override
    public <E extends Entity<K>, K> List<E> findByNamedQuery(final String jpql, Map<String, ? extends Object> params){
        Query query = getEntityManager().createQuery(jpql);
        for(Map.Entry<String, ?> entry: params.entrySet()){
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return (List<E>) query.getResultList();
    }
    
    @Override
    public <E extends Entity<K>, K> List<E> findByNamedQuery(final String jpql, int firstResult, int maxResults, Object... params) {
        Query query = getEntityManager().createQuery(jpql);
        this.setQueryParameterValues(query, params);
        query.setMaxResults(maxResults);
        query.setFirstResult(firstResult);
        
        return (List<E>) query.getResultList();
    }

	/**
	 * Gets the criteria builder of the entity manager.
	 */
	protected CriteriaBuilder getCriteriaBuilder() {
		return this.getEntityManager().getCriteriaBuilder();
	}

	/**
	 * Find all entities by the given {@link CriteriaQuery}.
	 */
	protected <V> List<V> find(final CriteriaQuery<V> criteriaQuery) {
		return this.getEntityManager().createQuery(criteriaQuery).getResultList();
	}

	/**
	 * 根据原生SQL进行查询
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return 
	 * @since 1.0.0
	 * 2014年10月16日-下午4:49:17
	 */
    public <E extends Entity<K>, K> List<E> findBySQLQuery(final String sql, final Class<E> clazz, Object... params){
        Query query = getEntityManager().createNativeQuery(sql, clazz);
        this.setQueryParameterValues(query, params);
        
        return (List<E>) query.getResultList();
    }

	/**
	 * Finds all entities by the given {@link CriteriaQuery} and limit the result set.
	 * 
	 * @param firstResult
	 *            Position of the first result to retrive.
	 * @param maxResults
	 *            The maximum number of results to retrieve.
	 */
	protected <V> List<V> find(final CriteriaQuery<V> criteriaQuery, final int firstResult, final int maxResults) {
		final TypedQuery<V> query = this.getEntityManager().createQuery(criteriaQuery);
		query.setMaxResults(maxResults);
		query.setFirstResult(firstResult);

		return query.getResultList();
	}

	/**
	 * Find a single entity by the given {@link CriteriaQuery}, throws an Exception if there is no or more than one
	 * result.
	 */
	protected <V> V findSingle(final CriteriaQuery<V> criteriaQuery) {
		return this.getEntityManager().createQuery(criteriaQuery).getSingleResult();
	}

	/**
	 * Finds a single entity by the given {@link CriteriaQuery}, throws an Exception if there is more than one result.
	 * 
	 * @return The found entity or null if there was none found.
	 */
	public <V> V findSingleOrNull(final CriteriaQuery<V> criteriaQuery) {
		try {
			return this.getEntityManager().createQuery(criteriaQuery).getSingleResult();
		} catch (final NoResultException e) {
			return null;
		}
	}

	/**
	 * Finds the first entity by the given {@link CriteriaQuery} or null if no result was found.
	 * 
	 * @param criteriaQuery
	 *            Query to execute.
	 * @return The found entity or null if there was none found.
	 */
	protected <V> V findFirstOrNull(CriteriaQuery<V> criteriaQuery) {
		List<V> results = this.find(criteriaQuery, 1);
		return CollentionUtil.getFirstElement(results);
	}
	
	public <V extends Entity<K>, K> V findFirstOrNull(final String jpql, Object... params) {
        List<V> results = (List<V>)this.findByNamedQuery(jpql, params);
        return CollentionUtil.getFirstElement(results);
    }
	
    protected <V> List<V> find(final CriteriaQuery<V> criteriaQuery, final int maxResults) {
        final TypedQuery<V> query = this.getEntityManager().createQuery(criteriaQuery);
        query.setMaxResults(maxResults);

        return query.getResultList();
    } 
	
	/**
	 * 直接执行JPQL，比如update、delete语句
	 * 
	 * @param execuetJPQL JPQL语言语句，比如  <code>update User o set o.userName = ?  where o.id = ?</code>
	 * @param parameterArray 参数集合
	 * @return 成功返回true，失败返回false
	 */
    private final boolean execute(String execuetJPQL, Object... params) {
        Query query = entityManager.createQuery(execuetJPQL);
        this.setQueryParameterValues(query, params);
        return query.executeUpdate() > 0 ? true : false;
    }
    
    /**
     * 给query语句传参数
     * 
     * @param query query查询对象
     * @param params 参数集合
     */
    private final void setQueryParameterValues(Query query, Object... params) {
        if (params == null || params.length == 0) {
            return;
        }
        int index = 1;
        for(Object param: params){
            query.setParameter(index, param);
            index++;
        }
    }

}
