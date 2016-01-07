package com.yunkouan.lpid.biz.system.role.bo;


/**
 * 
 * <P>Title: csc_sh</P>
 * <P>Description: 角色Model</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午4:03:50</P>
 * @author yangli
 * @version 1.0.0
 */
public class RoleModel {
    //提供给前台menu树显示用
    private String emnuTree;
    //角色名称
    private String roleName;
    //角色描述
    private String roleDescribe;
    //选择的menu菜单
    private String menuArray;
    //角色Id
    private int roleId;
    //是否被删除(0:禁用;1:启用) 
    private Integer status ;
    
    public String getEmnuTree() {
        return emnuTree;
    }

    public void setEmnuTree(String emnuTree) {
        this.emnuTree = emnuTree;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescribe() {
        return roleDescribe;
    }

    public void setRoleDescribe(String roleDescribe) {
        this.roleDescribe = roleDescribe;
    }

    public String getMenuArray() {
        return menuArray;
    }

    public void setMenuArray(String menuArray) {
        this.menuArray = menuArray;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
