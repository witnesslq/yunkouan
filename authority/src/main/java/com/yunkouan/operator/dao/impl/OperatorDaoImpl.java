package com.yunkouan.operator.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.yunkouan.operator.dao.OperatorDao;
import com.yunkouan.operator.entity.Operator;

@Repository(value="operatorDao")
public class OperatorDaoImpl implements OperatorDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	public Operator addOperator(Operator operator){
		entityManager.persist(operator);
		return operator;
	}
	
	public Operator updateOperator(Operator operator){
		return entityManager.merge(operator);
	}
	
	public List<Operator> getOperators(){
		List<Operator> operators = null; 
		Query query = entityManager.createNamedQuery("Operator.findAll");
		operators = query.getResultList();
		return operators;
	}
	
	public void removeOperator(Operator operator){
		entityManager.remove(operator);
	}
}
