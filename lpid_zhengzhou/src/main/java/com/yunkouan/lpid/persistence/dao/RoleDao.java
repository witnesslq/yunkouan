package com.yunkouan.lpid.persistence.dao;

import java.util.List;

import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Role;

/**
 * <P>Title: csc_sh</P>
 * <P>Description:角色管理 </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午2:39:38</P>
 * @author yangli
 * @version 1.0.0
 */
public interface RoleDao extends GenericDao{
    /**
     * 查询所有的角色
     * 
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午2:41:22
     */
    public List<Role> findRoles();
    
    /**
     * 查询所有的menu
     * 
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午2:41:19
     */
    public List<Menu> findMenus();
    
    /**
     * 增加角色
     * 
     * @param role
     * @since 1.0.0
     * 2014年9月25日-下午2:41:15
     */
    public void addRole(Role role);
    
    /**
     * 删除角色
     * 
     * @param roleId 
     * @since 1.0.0
     * 2014年9月25日-下午2:41:03
     */
    public void deleteRole(int roleId);
}
