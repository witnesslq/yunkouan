package com.yunkouan.lpid.biz.login.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.lpid.biz.login.service.LoginService;
import com.yunkouan.lpid.persistence.dao.LoginDao;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;
    
    @Override
    public Operator findOperator(String loginName, String password) {
        
        return loginDao.findOperator(loginName, password);
    }

    @Override
    public List<Menu> findAuthMenuList(Operator operator) {
        return loginDao.findAuthMenuList(operator);
    }

}
