package com.yunkouan.lpid.biz.screenscompare.service.impl;

import static java.lang.Math.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkou.common.util.BeanMapper;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.ScreenscompareQueryModel;
import com.yunkouan.lpid.biz.screenscompare.service.ScreenscompareService;
import com.yunkouan.lpid.persistence.dao.ScreenscompareDao;
import com.yunkouan.lpid.persistence.entity.RunningPackage;
import com.yunkouan.lpid.persistence.entity.XrayImage;


/**
 * <P>Title: ppoiq</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月01日-04时29分21秒</P>
 * @author yangli
 * @version 1.0.0
 */
@Transactional
@Service("screenscompareService") 
public class ScreenscompareServiceImpl implements ScreenscompareService {
	@Autowired
	private ScreenscompareDao screenscompareDao;

	@Override
	public void saveRunningPackage(RunningPackageModel model) {
		RunningPackage runningPackage = BeanMapper.map(model, RunningPackage.class);
		screenscompareDao.save(runningPackage);
	}

	@Override
	public int findRunningPackageCount(ScreenscompareQueryModel queryModel) {
		return screenscompareDao.findRunningPackageCount(queryModel);
	}

	@Override
	public List<RunningPackage> findAllRunningPackage(int firstResult,
			int pageSize, ScreenscompareQueryModel screenscompareModel) {
		return screenscompareDao.findRunningPackages(firstResult, pageSize, screenscompareModel);
	}
}
