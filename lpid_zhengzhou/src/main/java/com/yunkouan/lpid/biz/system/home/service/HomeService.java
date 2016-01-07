package com.yunkouan.lpid.biz.system.home.service;

import java.util.List;

import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;


/**
 * 
 * <P>Title: csc_sh</P>
 * <P>Description: 首页面Service</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年10月16日-下午6:45:47</P>
 * @author yangli
 * @version 1.0.0
 */
public interface HomeService {
	/**
	 * 查询一个用户的菜单列表
	 * 
	 * @param operator 用户
	 * @return 菜单列表，总共分两级
	 * @since 1.0.0
	 * 2014年10月16日-下午6:47:06
	 */
    public List<Menu> findMenuList(Operator operator);
    
	/**
	 * 更改密码
	 * 
	 * @param operator 操作员 
	 * @param currentPassword 原来的密码
	 * @param newPassword 修改的密码
	 * @return 1：成功  2：原密码输入有误 3: 更新时发生错误
	 * @since 1.0.0
	 * 2014年10月16日-下午6:46:47
	 */
    public int updateOperatePassword(Operator operator, String currentPassword, String newPassword);
    
}
