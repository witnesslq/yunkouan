package com.yunkouan.lpid.biz.system.config.service;

import java.util.List;

import com.yunkouan.lpid.persistence.entity.ParamConfig;


/**
 * <P>Title: ppoiq</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月18日-下午4:05:15</P>
 * @author  yangli
 * @version 1.0.0
 */
public interface ConfigService {
	/**
	 * 查询配置参数信息
	 * 
	 * @return 
	 * @since 1.0.0
	 * 2014年12月18日-下午4:23:53
	 */
	public List<ParamConfig> findConfigs();
	
	/**
	 * 保存更新配置参数信息
	 * 
	 * @param list 
	 * @since 1.0.0
	 * 2014年12月18日-下午4:24:03
	 */
	public void updateConfigs(List<ParamConfig> list);
	
	/**
	 * 更新剩余标签数量
	 */
	public void updateLabelRemain();
	
	/**
	 * 查询配置参数对象
	 * @param prams
	 * @return
	 */
	public ParamConfig findConfig(Object...prams);
}
