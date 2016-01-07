package com.yunkouan.lpid.persistence.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.yunkouan.lpid.persistence.dao.RoleDao;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Role;

/**
 * <P>Title: csc_sh</P>
 * <P>Description:角色管理 </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午2:40:38</P>
 * @author yangli
 * @version 1.0.0
 */
@Repository("roleDao")
public class RoleDaoImpl 
    extends GenericJpaDaoImpl implements RoleDao {

    @Override
    public List<Role> findRoles() {
//        return findAll(Role.class);
    	return super.findBySQLQuery("SELECT * from Role r WHERE r.status=1", Role.class);
    }

    @Override
    public List<Menu> findMenus() {
        //一级菜单
        String menuFirstLevelHql = " from Menu m "
                + " where m.menuLevel = 1 "
                + "  and m.menuType = 1 "
                + " order by m.menuOrder ";
        //二级菜单
        String menuSecondLevelSecondHql = " from Menu m "
                + " where m.menuLevel = 2 "
                + "  and m.menuType = 1 "
                + "  and m.menu.id = ? "
                + " order by m.menuOrder";
        
        
        List<Menu> menuFirstLevelList = super.findByNamedQuery(menuFirstLevelHql, new Object[]{});
        if(CollectionUtils.isEmpty(menuFirstLevelList)) {
            return null;
        }
        //查询二级菜单
        for(Menu menu : menuFirstLevelList) {
            List<Menu> menuLevelSecondList = super.findByNamedQuery(menuSecondLevelSecondHql, new Object[]{menu.getId()});
            //TODO list to set 
            Set<Menu> set = new HashSet<Menu>();
            for(Menu menusecond : menuLevelSecondList) {
                set.add(menusecond);
            }
            menu.setMenus(set);
        }
        
        return menuFirstLevelList;
    }

    @Override
    public void addRole(Role role) {
//        if(CollectionUtils.isNotEmpty(menuSet)) {
//            for (Menu menu : menuSet) {
//                if(menu == null) continue;
//                Menu busiMenu = super.find(menu.getId(), Menu.class);
//                role.getMenus().add(busiMenu);
//            }
//        }
        super.save(role);
    }

    @Override
    public void deleteRole(int roleId) {
    	String sql = "update Role r SET r.status=0 WHERE r.id=?";
        super.update(sql, Integer.valueOf(roleId));
    	//delete(roleId, Role.class);
    }

}
