package com.yunkouan.lpid.biz.system.config.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.lpid.biz.system.config.service.ConfigService;
import com.yunkouan.lpid.persistence.dao.ConfigDao;
import com.yunkouan.lpid.persistence.entity.ParamConfig;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月18日-下午4:05:50</P>
 * @author  yangli
 * @version 1.0.0
 */
@Service("configService")
@Transactional
public class ConfigServiceImpl implements ConfigService {
	@Autowired
	private ConfigDao configDao;
	
	/**
	 * @see com.yunkouan.lpid.module.system.config.service.ConfigService#findConfigs()
	 */
	@Override
	public List<ParamConfig> findConfigs() {
		return configDao.findConfigs();
	}

	/**
	 * @see com.yunkouan.lpid.module.system.config.service.ConfigService#updateConfigs(java.util.List)
	 */
	@Override
	public void updateConfigs(List<ParamConfig> list) {
		configDao.updateConfigs(list);
	}

	@Override
	public void updateLabelRemain() {
		configDao.updateLabelRemain();
	}

	@Override
	public ParamConfig findConfig(Object... prams) {
		return configDao.findConfig(prams);
	}
	
}
