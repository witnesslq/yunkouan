package com.yunkouan.lpid.biz.system.home.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.lpid.biz.system.home.service.HomeService;
import com.yunkouan.lpid.biz.system.logs.bo.LogTypeCode;
import com.yunkouan.lpid.biz.system.logs.service.LogsService;
import com.yunkouan.lpid.persistence.dao.HomeDAO;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;

@Transactional
@Service("homeService")
public class HomeServiceImpl implements HomeService{
	public static Logger logger = Logger.getLogger(HomeServiceImpl.class);
	
	@Autowired
	private HomeDAO homeDAO;
	@Autowired
	private LogsService logsService;
	
	public List<Menu> findMenuList(Operator operator) {
		return homeDAO.findMenuList(operator);
	}
	
	@Override
	public int updateOperatePassword(Operator operator, String currentPassword,
			String newPassword) {
		
	    String message = "[操作员>密码修改]: " + "登录名：" + operator.getLoginName();
		logger.info(message);
		
		//写操作日志
		logsService.insertLog(operator, LogTypeCode.OPEREATOR, message);
		return homeDAO.updateOperatePassword(operator, currentPassword, newPassword);
	}

}
