package com.yunkouan.lpid.biz.system.role.service;

import java.util.List;
import java.util.Set;

import com.yunkouan.lpid.biz.system.role.bo.RoleModel;
import com.yunkouan.lpid.biz.system.role.dto.MenuDTO;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Role;

/**
 * <P>Title: csc_sh</P>
 * <P>Description:角色管理 </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午2:35:24</P>
 * @author yangli
 * @version 1.0.0
 */
public interface RoleService {
    /**
     * 查询所有的角色
     * 
     * @return 角色列表
     * @since 1.0.0
     * 2014年9月25日-下午4:06:39
     */
    public List<Role> findRoles();
    
    /**
     * 查询所有的menu
     * 
     * @return 角色menu
     * @since 1.0.0
     * 2014年9月25日-下午4:06:36
     */
    public List<Menu> findMenus();
    
    /**
     * 返回menu树的json串
     * 
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午4:06:31
     */
    public String getMenuTree(int roleId);
    
    /**
     * 添加角色
     * 
     * @param roleModel 
     * @since 1.0.0
     * 2014年9月25日-下午4:06:26
     */
    public void addRole(RoleModel roleModel);
    
    /**
     * 获取角色的menu
     * 
     * @param roleId
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午4:06:22
     */
    public Set<MenuDTO> findRolesMenu(int roleId);
    
    /**
     * 修改角色
     * 
     * @param roleModel 
     * @since 1.0.0
     * 2014年9月25日-下午4:06:18
     */
    public void updateRole(RoleModel roleModel);
    
    /**
     * 修改角色的权限
     * 
     * @param roleId 角色Id
     * @param menuArray 该角色的权限
     * @since 1.0.0
     * 2014年9月25日-下午4:06:18
     */
    public boolean updateAuth(int roleId, String menuArray);
    
    /**
     * 删除角色
     * 
     * @param roleId 角色id 
     * @since 1.0.0
     * 2014年9月25日-下午4:06:14
     */
    public void deleteRole(int roleId);

    /**
     * 检查角色名称是否重复
     * @param roleName
     * @return
     */
	public int checkRoleName(String roleName);
}
