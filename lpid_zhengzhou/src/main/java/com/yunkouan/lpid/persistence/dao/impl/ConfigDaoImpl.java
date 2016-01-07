package com.yunkouan.lpid.persistence.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.yunkouan.lpid.persistence.dao.ConfigDao;
import com.yunkouan.lpid.persistence.entity.ParamConfig;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月18日-下午4:03:34</P>
 * @author  yangli
 * @version 1.0.0
 */
@Repository("configDao")
public class ConfigDaoImpl extends GenericJpaDaoImpl implements ConfigDao{
	/**
	 * @see com.yunkouan.lpid.module.system.config.dao.ConfigDao#findConfigs()
	 */
	@Override
	public List<ParamConfig> findConfigs() {
		 return super.findAll(ParamConfig.class);
	}

	/**
	 * @see com.yunkouan.lpid.module.system.config.dao.ConfigDao#saveConfigs(java.util.List)
	 */
	@Override
	public void updateConfigs(List<ParamConfig> list) {
		EntityManager entityManager = super.getEntityManager();
		Query query = entityManager.createQuery("update ParamConfig pc set pc.value= :paramValue where pc.name = :paramName");
		for(ParamConfig config:list){
//			super.update(hsql, config.getValue(),config.getName());
			query.setParameter("paramName", config.getName());
			query.setParameter("paramValue", config.getValue());
		}
		query.executeUpdate();
	}

	@Override
	public void updateLabelRemain() {
		ParamConfig param = super.findFirstOrNull("FROM ParamConfig pc WHERE pc.name='labelRemain'");
		if(param == null) return ;
		int remain = Integer.valueOf(param.getValue());
		param.setValue(String.valueOf(remain-1));
		super.update(param);
	}
	
	/**
	 * 查询配置参数对象
	 * @param prams
	 * @return
	 */
	public ParamConfig findConfig(Object...prams) {
		String jpql = "FROM ParamConfig pc WHERE pc.name=?";
		ParamConfig paramConfig = super.findFirstOrNull(jpql, prams);
		return paramConfig;
	}
}
