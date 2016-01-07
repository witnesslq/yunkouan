package com.yunkouan.lpid.persistence.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunkouan.lpid.persistence.dao.LoginDao;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;

@Repository("loginDao")
public class LoginDaoImpl extends GenericJpaDaoImpl implements LoginDao {

    /**
     * @see com.yunkouan.lpid.persistence.login.dao.impl.LoginDao#findOperator(java.lang.String, java.lang.String)
     */
    @Override
    public Operator findOperator(String loginName, String password) {
        String jpql = " from Operator as operator "
                + " where operator.loginName = ? "
                + "  and operator.password = ? "
                + "  and operator.status = 1 ";
        
        return super.findFirstOrNull(jpql, new Object[] {loginName, password});
    }
    
    /**
     * @see com.yunkouan.lpid.persistence.login.dao.impl.LoginDao#findAuthMenuList(com.yunkouan.lpid.commons.entity.Operator)
     */
    @Override
    public List<Menu> findAuthMenuList(Operator operator) {
        //查询所有的菜单项
        String allUrlSql = 
            " select distinct "
            + " bm.id , "
            + " bm.name , "
            + " bm.link_url ,"
            + " bm.menu_level , "
            + " bm.menu_order , "
            + " bm.parent_id,  "
            + " bm.menu_type,  "
            + " bm.menu_icon  "
            + " from menu bm inner join "
            + "  ( select distinct brm.menu_id "
            + "     from operator_role bor inner join role_menu brm "
            + "      on bor.role_id = brm.role_id "
            + "    where bor.operator_id = ? "
            + "  ) menufirstandsecond " //内部查询获取该用户的一二级菜单
            + " on bm.id = menufirstandsecond.menu_id  "
            + "    or (bm.parent_id = menufirstandsecond.menu_id and bm.menu_type=2) " //查询出所有的一、二级菜单和二级菜单下的链接
            + "  order by bm.menu_level,bm.menu_order" ;
        
        return super.findBySQLQuery(allUrlSql, Menu.class, new Object[]{operator.getId()});
    }

}
