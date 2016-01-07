package com.yunkouan.lpid.persistence.dao;

import java.util.List;

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
public interface ScreenscompareDao extends GenericDao{
	
	/**
	 * @detail 查询包裹数据
	 * @param firstResult
	 * @param pageSize
	 * @param queryModel
	 * @return
	 */
	List<RunningPackage> findRunningPackages(int firstResult, int pageSize, ScreenscompareQueryModel queryModel);
	
	/**
	 * 查询包裹数据的总条数
	 * 
	 * @param queryModel
	 * @return 
	 * @since 1.0.0
	 * 2014年10月15日-上午11:05:35
	 */
	public int findRunningPackageCount(ScreenscompareQueryModel queryModel);
	
	/**
	 * 通过epc号码查询包裹信息
	 * @param epcNo
	 * @return
	 */
	public RunningPackage getRunningPackageByEPC(String epcNo);
	
	/**
	 * 通过id查询包裹信息
	 * @param id
	 * @return
	 */
	public RunningPackage getRunningPackageById(int id);
}
