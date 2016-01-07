package com.yunkouan.lpid.biz.screenscompare.service;

import java.util.List;

import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.ScreenscompareQueryModel;
import com.yunkouan.lpid.persistence.entity.RunningPackage;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月01日-04时29分21秒</P>
 * @author yangli
 * @version 1.0.0
 */
public interface ScreenscompareService {
	/**
	 * 保存包裹信息
	 * @param model
	 */
	void saveRunningPackage(RunningPackageModel model);
	
	/**
	 * 查询包裹的总条数
	 * 
	 * @param queryModel
	 * @return 
	 * @since 1.0.0
	 * 2014年12月15日-上午9:38:33
	 */
    public int findRunningPackageCount(ScreenscompareQueryModel queryModel);
    
    /**
	 * 分页查询包裹
	 * 
	 * @param queryModel
	 * @return 
	 * @since 1.0.0
	 * 2014年12月15日-上午9:38:33
	 */
    public List<RunningPackage> findAllRunningPackage(int firstResult, int pageSize, ScreenscompareQueryModel screenscompareModel);
}