package com.yunkouan.lpid.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.yunkouan.lpid.biz.system.operator.bo.OperatorModel;
import com.yunkouan.lpid.persistence.dao.OperatorDao;
import com.yunkouan.lpid.persistence.entity.Operator;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 操作员</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午9:16:18</P>
 * @author yangli
 * @version 1.0.0
 */
@Repository("operatorDao")
public class OperatorDaoImpl extends GenericJpaDaoImpl implements OperatorDao{

    @Override
    public int findAllOperatorCount(OperatorModel operatorModel) {
        CriteriaBuilder criteriaBuilder = this.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Operator> from = criteriaQuery.from(Operator.class);
        addPredicate(criteriaBuilder, criteriaQuery, from, operatorModel);

        Expression<Long> count = criteriaBuilder.count(from);

        criteriaQuery.select(count);

        return super.findSingle(criteriaQuery).intValue();
    }

    @Override
    public List<Operator> findAllOperator(int firstResult, int pageSize, 
            OperatorModel queryModel) {
        //组织动态查询
        CriteriaBuilder criteriaBuilder = super.getCriteriaBuilder();
        CriteriaQuery<Operator> criteriaQuery = criteriaBuilder.createQuery(Operator.class);
        Root<Operator> from = criteriaQuery.from(Operator.class);
        criteriaQuery.select(from);
        criteriaQuery.distinct(true);
        
        addPredicate(criteriaBuilder, criteriaQuery, from, queryModel);
        
        return super.find(criteriaQuery, firstResult, pageSize);
    }
    
    /**
     * 添加查询条件
     * 
     * @param criteriaBuilder
     * @param criteriaQuery
     * @param from
     * @param queryModel
     */
    private <T> void addPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, 
            Root<Operator> from, OperatorModel operatorModel) {
        List<Predicate> criteria = new ArrayList<Predicate>();
        
        criteria.add(criteriaBuilder.equal(from.get("status").as(String.class), 1));
        
        if(criteria.size() == 1) {
            criteriaQuery.where(criteria.get(0));
        } else if(criteria.size() > 1) {
            criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[0])));
        }
        
        //根据创建时间倒叙排列
        List<Order> orders = new ArrayList<Order>();
        orders.add(criteriaBuilder.asc(from.get("loginName")));
        criteriaQuery.orderBy(orders);
    }
    
}
