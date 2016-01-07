package com.yunkouan.lpid.commons.exception;

import com.yunkouan.lpid.commons.model.EnumCode;


/**
 * <P>Title: ppoiq</P>
 * <P>Description: 异常信息编码</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月20日-下午4:34:44</P>
 * @author yangli
 * @version 1.0.0
 */
public enum ExceptionCode implements EnumCode {
	
    /**综合查询*/
    
    
    /**系统管理*/
    //角色管理
    System_AddEditRole_Menu_ParseJsonError(8101),//权限的json字符串转换成对象失败
    //操作员管理
    System_AddEditOperator_Role_ParseJsonError(8201),//角色的json字符串转换成对象失败
    
    Other_Error(999999);//其他未知错误
	
	private final int number;

	private ExceptionCode(int number) {
		this.number = number;
	}
	
	@Override
	public int getNumber() {
		return number;
	}

}
