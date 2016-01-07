package com.yunkouan.lpid.biz.login.service;

import java.util.List;

import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;



/**
 * <P>Title: csc_sh</P>
 * <P>Description: 登录service接口</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年8月28日-上午9:57:42</P>
 * @author yangli
 * @version 1.0.0
 */
public interface LoginService {
    /**
     * 查询系统中的操作员
     * 
     * @param loginName
     * @param password
     * @return 
     * @since 1.0.0
     * 2014年10月13日-上午10:52:55
     */
    public Operator findOperator(String loginName, String password);
    
    /**
     * 查询该用户所有的权限，包括一、二级菜单和在该菜单下的所有操作的url
     * 
     * @param operator 用户
     * @return 用户所有的权限
     */
    public List<Menu> findAuthMenuList(Operator operator);
}
