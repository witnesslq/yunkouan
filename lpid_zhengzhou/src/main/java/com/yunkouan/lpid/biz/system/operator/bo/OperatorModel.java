package com.yunkouan.lpid.biz.system.operator.bo;

import java.util.Date;
import java.util.Set;

import com.yunkouan.lpid.biz.system.logs.bo.LogsModel;
import com.yunkouan.lpid.biz.system.role.bo.RoleModel;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 操作员管理</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午9:22:51</P>
 * @author yangli
 * @version 1.0.0
 */
public class OperatorModel {
	private Integer id;
    private String name;//操作员名字
    private String loginName;//操作员登录名
    private String email;//操作员邮箱
    private String password;//密码
    private String repeatPassword;//再次输入密码
    private String channel;//操作员所属频道
    private String operatorCode;//官员代码 
    private Date lastLoginTime;
    private Set<LogsModel> logs;
	private Set<RoleModel> roles;

    public Set<LogsModel> getLogs() {
		return logs;
	}
	public void setLogs(Set<LogsModel> logs) {
		this.logs = logs;
	}
	public Set<RoleModel> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleModel> roles) {
		this.roles = roles;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	//提供给前台角色树显示用
  	private String roleTree;
	//选中的角色菜单
	private String roleArray;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getRoleTree() {
		return roleTree;
	}
	public void setRoleTree(String roleTree) {
		this.roleTree = roleTree;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepeatPassword() {
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	public String getRoleArray() {
		return roleArray;
	}
	public void setRoleArray(String roleArray) {
		this.roleArray = roleArray;
	}
	public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String officialCode) {
		this.operatorCode = officialCode;
	}
}