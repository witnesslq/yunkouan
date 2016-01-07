package com.yunkouan.lpid.persistence.dao;

import java.util.List;

import com.yunkouan.lpid.biz.system.operator.bo.OperatorModel;
import com.yunkouan.lpid.persistence.entity.Operator;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 操作员</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午9:15:37</P>
 * @author yangli
 * @version 1.0.0
 */
public interface OperatorDao extends GenericDao{

    /**
     * 操作员总数
     * 
     * @param operatorModel 页面查询对象
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午9:26:44
     */
    public int findAllOperatorCount(OperatorModel operatorModel);
    
    /**
     * 操作员查询
     * 
     * @param firstResult 查询起始位置
     * @param pageSize 返回查询记录数
     * @param queryModel 查询请求参数
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午9:43:42
     */
    public List<Operator> findAllOperator(int firstResult, int pageSize, 
            OperatorModel queryModel);
    
}
