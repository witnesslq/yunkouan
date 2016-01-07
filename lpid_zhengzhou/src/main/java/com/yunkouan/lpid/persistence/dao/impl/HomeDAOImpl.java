package com.yunkouan.lpid.persistence.dao.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.yunkouan.lpid.persistence.dao.HomeDAO;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;

/**
 * 
 * <P>Title: csc_sh</P>
 * <P>Description: 首页</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年10月16日-下午6:25:57</P>
 * @author yangli
 * @version 1.0.0
 */
@Repository("homeDao")
public class HomeDAOImpl extends GenericJpaDaoImpl implements HomeDAO{
    
    @Override
    public int updateOperatePassword(Operator operator, String currentPassword, String newPassword){
    	String jpql = "from Operator operator "
    			+ " where operator.id = ? "
    			+ "  and operator.password = ?"
    			+ "  and operator.status = 1";
    	try {
    	    //查询登录的操作员
	    	Operator updateOperator = super.findFirstOrNull(jpql, new Object[] {operator.getId(), 
	    	        DigestUtils.md5Hex(currentPassword)});
	    	
	    	if(updateOperator == null) {
	    		return 2;
	    	}
	    	
	    	updateOperator.setPassword(DigestUtils.md5Hex(newPassword));
	    	super.update(updateOperator);
    	} catch (Exception e) {
    	    logger.error("修改用户密码时发生错误", e);
    		return 3;
    	}
    	
    	return 1;
    }
    
    @Override
    public List<Menu> findMenuList(Operator operator) {
    	//一级菜单
    	String menuLevelFirstSql = 
    			  "select distinct "
    			+ " bm.ID, "
    			+ " bm.NAME, "
    			+ " bm.LINK_URL,"
    			+ " bm.MENU_LEVEL, "
    			+ " bm.MENU_ORDER, "
    			+ " bm.MENU_TYPE, "
    			+ " bm.MENU_ICON, "
    			+ " bm.PARENT_ID "  
    			+ " from OPERATOR bo inner join OPERATOR_ROLE bor on bo.ID =  bor.OPERATOR_ID " 
    			+ "     inner join ROLE br on bor.ROLE_ID = br.ID " 
    			+ " 	inner join ROLE_MENU brm on br.ID = brm.ROLE_ID " 
    			+ "     inner join MENU bm on brm.MENU_ID = bm.ID " 
    			+ " where bo.STATUS = 1 " 
    			+ "   and bo.ID = ? " 
    			+ "   and bm.PARENT_ID is NULL"
    			+ "  order by bm.MENU_ORDER" ;
    	
    	//二级菜单
    	String menuLevelSecondSql = 
    			"select distinct "
    			+ " bm.ID, "
				+ " bm.NAME, "
				+ " bm.LINK_URL, "
				+ " bm.MENU_LEVEL, "
				+ " bm.MENU_ORDER, "
				+ " bm.MENU_TYPE, "
				+ " bm.MENU_ICON, "
				+ " bm.PARENT_ID " 
				+ " from OPERATOR bo inner join OPERATOR_ROLE bor on bo.ID = bor.OPERATOR_ID " 
				+ "     inner join ROLE br on bor.ROLE_ID = br.ID " 
				+ " 	inner join ROLE_MENU brm on br.ID = brm.ROLE_ID " 
				+ "     inner join MENU bm on brm.MENU_ID = bm.ID " 
				+ " where bo.STATUS = 1 " 
				+ "   and bo.ID = ?"
				+ "   and bm.PARENT_ID = ?"
    			+ "  order by bm.MENU_ORDER" ;
    	
    	//一级菜单
    	List<Menu> menuLevelOneList = super.findBySQLQuery(menuLevelFirstSql, Menu.class, 
    			new Object[]{operator.getId()});
    	if(CollectionUtils.isEmpty(menuLevelOneList)) {
    		return null;
    	}
    	
    	/* 
    	 * 查询二级菜单 
    	 * 
    	 * TODO 
    	 * 遍历一级菜单查询多次，待重构，做一次查询，现在的观点是将menu给平铺开，
    	 * 即一条记录中同时保存一、二、三级的菜单，查询后然后再进行组织数据，数据格式自己定义，组成tree的形式
    	 *  
    	 */
    	for(Menu menu : menuLevelOneList) {
    		List<Menu> menuLevelSecondList = super.findBySQLQuery(menuLevelSecondSql, Menu.class, 
    				new Object[] {operator.getId(), menu.getId()});
            //结果集转换成Set
    		Set<Menu> set = new TreeSet<Menu>();
    		for(Menu menusecond : menuLevelSecondList) {
    			set.add(menusecond);
    		}
    		menu.setMenus(set);
    	}
    	return menuLevelOneList;
    }
    
}
