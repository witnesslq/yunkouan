package com.yunkouan.lpid.biz.system.operator.service;

import java.util.List;
import java.util.Set;

import com.yunkouan.lpid.biz.system.operator.bo.OperatorModel;
import com.yunkouan.lpid.biz.system.operator.dto.OperatorDTO;
import com.yunkouan.lpid.persistence.entity.Operator;
import com.yunkouan.lpid.persistence.entity.Role;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 操作员管理</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午9:47:53</P>
 * @author yangli
 * @version 1.0.0
 */
public interface OperatorService {
    
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
    
    /**
     * 查询所有的role
     * 
     * @return 操作员的角色
     * @since 1.0.0
     * 2014年9月25日-下午4:06:36
     */
    public List<Role> findRoles();
    
    /**
     * 返回role树的json串
     * 
     * [
     *   { id:1, pId:0, name:"随意勾选 1"},
     *   { id:2, pId:0, name:"随意勾选 2", checked:true},
     * ];
     * @return 返回的形式如下
     * @since 1.0.0
     * 2014年9月25日-下午9:50:58
     */
    public String getOperatorRoleTree(int operatorId);
    
    /**
     * 添加操作员
     * 
     * @param operatorModel 
     * @since 1.0.0
     * 2014年9月25日-下午9:50:54
     */
    public void addOperator(OperatorModel operatorModel);
    
    /**
     * 修改操作员
     * 
     * @param operatorModel 
     * @since 1.0.0
     * 2014年9月25日-下午9:50:50
     */
    public void updateOperator(OperatorModel operatorModel);
  
    /**
     * 修改操作员的角色
     * 
     * @param operatorId 操作员Id
     * @param operatorArray 该操作员的角色
     * @since 1.0.0
     * 2014年9月25日-下午4:06:18
     */
    public boolean updateOperatorRole(int operatorId, String roleArray);
    
    /**
     * 获取操作员的角色
     * 
     * @param operatorId
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午4:06:22
     */
    public Set<OperatorDTO> findOperatorRoles(int operatorId);
    
    /**
     * 修改操作员密码
     * 
     * @param operatorModel 
     * @since 1.0.0
     * 2014年9月25日-下午9:50:46
     */
    public void updateOperatorPassword(OperatorModel operatorModel);
    
    /**
     * 删除操作员
     * 
     * @param operatorModel 
     * @since 1.0.0
     * 2014年9月25日-下午9:50:39
     */
    public void deleteOperator(int operatorId);

    /**
     * 检查操作员的用户名是否重复
     * 
     * @param loginName
     * @return
     */
	public int checkLoginName(String loginName);
}
