package com.yunkouan.lpid.biz.system.operator.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yunkou.common.util.JsonUtil;
import com.yunkouan.lpid.biz.system.logs.bo.LogTypeCode;
import com.yunkouan.lpid.biz.system.logs.service.LogsService;
import com.yunkouan.lpid.biz.system.operator.bo.OperatorModel;
import com.yunkouan.lpid.biz.system.operator.dto.OperatorDTO;
import com.yunkouan.lpid.biz.system.operator.service.OperatorService;
import com.yunkouan.lpid.persistence.dao.OperatorDao;
import com.yunkouan.lpid.persistence.dao.RoleDao;
import com.yunkouan.lpid.persistence.entity.Operator;
import com.yunkouan.lpid.persistence.entity.Role;

@Transactional
@Service("operatorService")
public class OperatorServiceImpl implements OperatorService {

	public static Logger logger = Logger.getLogger(OperatorServiceImpl.class);
	@Autowired
    private OperatorDao operatorDao;
	@Autowired
    private RoleDao roleDao;
	@Autowired
	private LogsService logsService;
	
    @Override
    public int findAllOperatorCount(OperatorModel operatorModel) {
        return operatorDao.findAllOperatorCount(operatorModel);
    }
    
    @Override
    public List<Operator> findAllOperator(int firstResult, int pageSize,
            OperatorModel queryModel) {
        return operatorDao.findAllOperator(firstResult, pageSize, queryModel);
    }
    
    @Override
    public void addOperator(OperatorModel operatorModel) {
        Set<Role> roleList = getRoleJson(operatorModel);
        //新建立操作员对象
        Operator operator = new Operator();
        operator.setName(operatorModel.getName());
        operator.setLoginName(operatorModel.getLoginName());
        operator.setPassword(DigestUtils.md5Hex(operatorModel.getPassword()));
        operator.setCreateTime(new Date());
        operator.setEmail(operatorModel.getEmail());
        operator.setRoles(roleList);
        operator.setChannel(operatorModel.getChannel());
        operator.setStatus(1);
        
        operatorDao.save(operator);
        //TODO 写操作日志
    }
    
    @Override
    public void updateOperator(OperatorModel operatorModel) {
        Operator operator = operatorDao.find(operatorModel.getId(), Operator.class);
        operator.setLoginName(operatorModel.getLoginName());
        operator.setName(operatorModel.getName());
        operator.setEmail(operatorModel.getEmail());
        operator.setChannel(operatorModel.getChannel());
        operatorDao.update(operator);
        
        //TODO 写操作日志 
    }
    
    @Override
    public void updateOperatorPassword(OperatorModel operatorModel) {
        Operator operator = operatorDao.find(operatorModel.getId(), Operator.class);
        operator.setPassword(DigestUtils.md5Hex(operatorModel.getPassword()));//重置密码
        operatorDao.update(operator);
        //写操作日志
		logsService.insertLog(operator, LogTypeCode.OPEREATOR, "[操作员>密码修改]: " + "登录名：" + operator.getLoginName());
    }
    
    /**
     * 将页面中传输的角色的json字符串转换成对象
     * @return
     */
    private Set<Role> getRoleJson(OperatorModel operatorModel) {
        try {
            return JsonUtil.getInstance().fromJson(operatorModel.getRoleArray(), 
                    new TypeToken<Set<Role>>() {}.getType());
        } catch (JsonSyntaxException e) {
            logger.error("角色的json字符串转换成对象失败", e);
            //TODO 添加exception
            throw e;
        }
    }

    @Override
    public List<Role> findRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getOperatorRoleTree(int operatorId) {
        List<Role> roleList = roleDao.findRoles();
        Operator operator = operatorDao.find(operatorId, Operator.class);
        Set<Role> roleSet = operator.getRoles();
        
        StringBuffer roleTree = new StringBuffer("[");
        if(CollectionUtils.isNotEmpty(roleList)) {
            //拼接角色菜单
            for (Role role : roleList) {
                roleTree.append("{\"id\":\"").append(role.getId()).append("\", \"name\":\"").
                append(role.getName()).append(hasMenu(role.getId(), roleSet)).append(", \"child\":[]").append("},");
            }
        }
        
        return StringUtils.removeEnd(roleTree.toString(), ",") + "]";
    }

    @Override
    public boolean updateOperatorRole(int operatorId, String roleArray) {
        try {
            Set<Role> newRole = getRoleJson(roleArray);
            //获取原来的role，清除
            Operator operator = operatorDao.find(operatorId, Operator.class);
            operator.setRoles(null);
            operatorDao.update(operator);
            
            //设置新设置的role
            operator.setRoles(newRole);
            operatorDao.update(operator);
            //TODO 写操作日志
        } catch (Exception e) {
            logger.error("修改角色的权限发生错误", e);
            return false;
        }        
        return true;
    }
    
    /**
     * 将页面中传输的role的json字符串转换成对象
     * 
     * @param roleModel
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午4:55:20
     */
    private Set<Role> getRoleJson(String roleArray) {
        Set<Role> roles = new HashSet<Role>();
        try {
            Set<Role> jsonTransformSet = JsonUtil.getInstance().fromJson(roleArray, 
                    new TypeToken<Set<Role>>() {}.getType());
            //TODO 不做查询操作的话，在更新的时候，会把数据库中的role数据更新。
            if(CollectionUtils.isNotEmpty(jsonTransformSet)) {
                for (Role role : jsonTransformSet) {
                    Role selectRole = roleDao.find(role.getId(), Role.class);
                    roles.add(selectRole);
                }
            }
            return roles;
            
        } catch (JsonSyntaxException e) {
            logger.error("角色的json字符串转换成对象失败", e);
            //TODO throw new ManageException
            throw e;
        }
        
    }

    @Override
    public Set<OperatorDTO> findOperatorRoles(int operatorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteOperator(int operatorId) {
        Operator operator = operatorDao.find(operatorId, Operator.class);
        operator.setStatus(2);//删除操作员
        operatorDao.update(operator);
    }
    
    /**
     * 判断角色是否拥有该角色
     * 
     * @param roleId
     * @param roleSet
     * @return 
     * @since 1.0.0
     * 2014年9月26日-下午7:02:35
     */
    private String hasMenu(int roleId, Set<Role> roleSet) {
        //如果没有权限直接返回
        if(CollectionUtils.isEmpty(roleSet)) {
            return "\", \"check\":false";
        }
        for (Role role : roleSet) {
            if(role == null) continue;
            if(role.getId() == roleId){
                return "\", \"check\":true";
            }
        }
        
        return "\", \"check\":false";
    }

	@Override
	public int checkLoginName(String loginName) {
		return operatorDao.getCount("from Operator o where o.status = 1 and o.loginName = ?", loginName);
	}
    
}
