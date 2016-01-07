package com.yunkouan.lpid.biz.screenscompare.service;

import java.util.List;

import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 同屏比对结果历史查询service接口</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年8月28日-上午9:57:42</P>
 * @author yangli
 * @version 1.0.0
 */
public interface ScreensCompareHistoryService {
	public RunningPackageModel getRunningPackageModelByEPC(String epcNo);
	
	public RunningPackageModel getRunningPackageModelById(int id);
}
