package com.yunkouan.lpid.biz.system.role.service.impl;

import static org.apache.commons.lang3.StringUtils.removeEnd;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yunkou.common.util.JsonUtil;
import com.yunkouan.lpid.biz.system.role.bo.RoleModel;
import com.yunkouan.lpid.biz.system.role.dto.MenuDTO;
import com.yunkouan.lpid.biz.system.role.service.RoleService;
import com.yunkouan.lpid.persistence.dao.RoleDao;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Role;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 角色管理</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午2:36:30</P>
 * @author yangli
 * @version 1.0.0
 */
@Transactional
@Service("roleService")
public class RoleServiceImpl implements RoleService{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    RoleDao roleDao;
    @Override
    public List<Role> findRoles() {
        return roleDao.findRoles();
    }

    @Override
    public List<Menu> findMenus() {
        return roleDao.findMenus();
    }

    @Override
    public String getMenuTree(int roleId) {
        List<Menu> menuList = this.findMenus();
        
        Role role = roleDao.find(roleId, Role.class);
        Set<Menu> menuSet = role.getMenus();
        
        StringBuffer menuTree = new StringBuffer("[");
        if(CollectionUtils.isNotEmpty(menuList)) {
            //拼接一级菜单
            for (Menu menuFirst : menuList) {
                menuTree.append("{\"id\":\"").append(menuFirst.getId()).append("\", \"name\":\"").
                append(menuFirst.getName()).append(hasMenu(menuFirst.getId(), menuSet)).
                append(", \"child\":[");
                
                //拼接二级菜单
                if(CollectionUtils.isNotEmpty(menuFirst.getMenus())) {
                    StringBuffer innerBuffer = new StringBuffer();
                    for (Menu menuSecond : menuFirst.getMenus()) {
                        innerBuffer.append("{\"id\":\"").append(menuSecond.getId()).append("\", \"name\":\"").
                        append(menuSecond.getName()).append(hasMenu(menuSecond.getId(), menuSet)).append("},");
                    }
                    
                    menuTree.append(removeEnd(innerBuffer.toString(), ",")).append("]");
                } else {
                    menuTree.append("]");
                }
                menuTree.append("},");
            }
        }
        //去掉最后一个","号
        return removeEnd(menuTree.toString(), ",") + "]";
    }
    
    /**
     * 判断角色是否拥有该权限
     * 
     * @param menuId
     * @param menuSet
     * @return 
     * @since 1.0.0
     * 2014年9月26日-下午7:02:35
     */
    private String hasMenu(int menuId, Set<Menu> menuSet) {
        //如果没有权限直接返回
        if(CollectionUtils.isEmpty(menuSet)) {
            return "\", \"check\":false";
        }
        for (Menu menu : menuSet) {
            if(menu == null) continue;
            if(menu.getId() == menuId){
                return "\", \"check\":true";
            }
        }
        
        return "\", \"check\":false";
    }

    @Override
    public void addRole(RoleModel roleModel) {
        Role role = new Role();
        role.setName(roleModel.getRoleName());
        role.setDescription(roleModel.getRoleDescribe());
        role.setCreateTime(new Date());
        role.setStatus(roleModel.getStatus());
        roleDao.addRole(role);
        
        //TODO 写操作日志
    }

    @Override
    public Set<MenuDTO> findRolesMenu(int roleId) {
        Set<MenuDTO> busiMenuDTOSet = new HashSet<MenuDTO>();
        Role role = roleDao.find(roleId, Role.class);
        Set<Menu> menuSet = role.getMenus();
        //ajax前端只需要MenuId
        for(Menu menu: menuSet) {
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setMenuId(menu.getId());
            busiMenuDTOSet.add(menuDTO);
        }
        return busiMenuDTOSet;
    }

    @Override
    public void updateRole(RoleModel roleModel) {
        
        //获取原来的menu
        Role role = roleDao.find(roleModel.getRoleId(), Role.class);
        role.setName(roleModel.getRoleName());
        role.setDescription(roleModel.getRoleDescribe());
        roleDao.update(role);
        
        //TODO 写操作日志
        
    }
    
    @Override
    public boolean updateAuth(int roleId, String menuArray) {
        try {
            
            Set<Menu> newMenu = getMenuJson(menuArray);
            //获取原来的menu，清除
            Role role = roleDao.find(roleId, Role.class);
            role.setMenus(null);
            roleDao.update(role);
            
            //设置新设置的menu
            role.setMenus(newMenu);
            roleDao.update(role);
            //TODO 写操作日志
        } catch (Exception e) {
            logger.error("修改角色的权限发生错误", e);
            return false;
        }
        
        return true;
        
    }

    @Override
    public void deleteRole(int roleId) {
        roleDao.deleteRole(roleId);
        
//        String logDetail= "角色名称：" + 
//                roleModel.getRoleName() + ";"
//                + "描述：" + roleModel.getRoleDescribe();
        logger.info("[系统管理>角色管理>删除角色，角色Id]：" + roleId);
        //写操作日志
        //logService.insertLog(LogTypeCode.SYS, "删除角色",logDetail);
    }
    
    /**
     * 将页面中传输的menu的json字符串转换成对象
     * 
     * @param roleModel
     * @return 
     * @since 1.0.0
     * 2014年9月25日-下午4:55:20
     */
    private Set<Menu> getMenuJson(String menuArray) {
        Set<Menu> menus = new HashSet<Menu>();
        try {
            Set<Menu> jsonTransformSet = JsonUtil.getInstance().fromJson(menuArray, 
                    new TypeToken<Set<Menu>>() {}.getType());
            //TODO 不做查询操作的话，在更新的时候，会把数据库中的menu数据更新。
            if(CollectionUtils.isNotEmpty(jsonTransformSet)) {
                for (Menu menu : jsonTransformSet) {
                    Menu selectMenu = roleDao.find(menu.getId(), Menu.class);
                    menus.add(selectMenu);
                }
            }
            return menus;
            
        } catch (JsonSyntaxException e) {
            logger.error("权限的json字符串转换成对象失败", e);
            //TODO throw new ManageException
            throw e;
        }
        
    }

	@Override
	public int checkRoleName(String roleName) {
		return roleDao.getCount("from Role where name = ?", roleName);
	}

}
