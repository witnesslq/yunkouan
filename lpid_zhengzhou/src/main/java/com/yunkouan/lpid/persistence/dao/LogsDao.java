package com.yunkouan.lpid.persistence.dao;

import java.util.List;

import com.yunkouan.lpid.biz.system.logs.bo.LogsModelQuery;
import com.yunkouan.lpid.persistence.entity.Log;
/**
 * <P>Title: csc_sh</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午2:40:40</P>
 * @author  yangli
 * @version 1.0.0
 */
public interface LogsDao extends GenericDao {
	/**
	 * 添加日志信息
	 * 
	 * @param log 
	 * @since 1.0.0
	 * 2014年9月25日-下午2:52:07
	 */
	public void addLogInfo(Log log);
	
	/**
	 * 查询日志的总条数
	 * 
	 * @param queryModel
	 * @return 
	 * @since 1.0.0
	 * 2014年10月15日-上午11:05:35
	 */
	public int findLogInfoCount(LogsModelQuery queryModel);
	
	/**
	 * 查询日志信息的结果集
	 * 
	 * @param firstResult
	 * @param pageSize
	 * @param queryModel
	 * @return 
	 * @since 1.0.0
	 * 2014年9月25日-下午2:52:57
	 */
	public List<Log> findAllLogInfo(int firstResult, int pageSize, LogsModelQuery queryModel);
}
