package com.yunkouan.lpid.biz.screenscompare.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkou.common.util.BeanMapper;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageModel;
import com.yunkouan.lpid.biz.screenscompare.service.ScreensCompareHistoryService;
import com.yunkouan.lpid.persistence.dao.ScreenscompareDao;
import com.yunkouan.lpid.persistence.entity.RunningPackage;

/**
 * <P>Title: csc_sh</P>
 * <P>Description:同屏比对结果历史查询service实现类 </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年8月28日-上午9:59:33</P>
 * @author yangli
 * @version 1.0.0
 */
@Transactional
@Service("screensCompareHistoryService")
public class ScreensCompareHistoryServiceImpl implements ScreensCompareHistoryService {
	@Autowired
	private ScreenscompareDao screenscompareDao;
	
	/**
	 * 通过epc号码查询包裹信息
	 */
	@Override
	public RunningPackageModel getRunningPackageModelByEPC(String epcNo) {
		RunningPackage runningPackage = screenscompareDao.getRunningPackageByEPC(epcNo);
		if (runningPackage == null) return null;
		RunningPackageModel runningPackageModels = BeanMapper.map(runningPackage, RunningPackageModel.class);
		return runningPackageModels;
	}

	@Override
	public RunningPackageModel getRunningPackageModelById(int id) {
		RunningPackage runningPackage = screenscompareDao.getRunningPackageById(id);
		RunningPackageModel runningPackageModels = BeanMapper.map(runningPackage, RunningPackageModel.class);
		// 设置RunningPackage对象为null,防止生成json对象时出现互相关联二导致json数据无限大显示不出来.
		List<XrayImageModel> xrayImageModels = runningPackageModels.getXrayImages();
		for (int i = 0; !xrayImageModels.isEmpty() && i < xrayImageModels.size(); i++) {
			xrayImageModels.get(i).setRunningPackage(null);
		}
		return runningPackageModels;
	}
	
}
