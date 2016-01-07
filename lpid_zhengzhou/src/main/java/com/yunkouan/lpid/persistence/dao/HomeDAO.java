package com.yunkouan.lpid.persistence.dao;

import java.util.List;

import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;


public interface HomeDAO extends GenericDao {
	
	/**
	 * 更改密码
	 * 
	 * @param operator 操作员
	 * @param currentPassword 原来的密码
	 * @param newPassword 修改的密码
	 * @return 1：成功  2：原密码输入有误  3: 更新时发生错误
	 * @since 1.0.0
	 * 2014年10月16日-下午6:24:20
	 */
    public int updateOperatePassword(Operator operator, String currentPassword, String newPassword);
    
	/**
	 * 查询一个用户的菜单列表
	 * 
	 * @param operator 用户
	 * 
	 * @return 菜单列表，总共分两级
	 */
    public List<Menu> findMenuList(Operator operator);
    
}
