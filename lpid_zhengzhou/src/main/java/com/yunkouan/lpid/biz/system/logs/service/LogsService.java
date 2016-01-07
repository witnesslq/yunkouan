package com.yunkouan.lpid.biz.system.logs.service;

import java.util.List;

import com.yunkouan.lpid.biz.system.logs.bo.LogTypeCode;
import com.yunkouan.lpid.biz.system.logs.bo.LogsModelQuery;
import com.yunkouan.lpid.persistence.entity.Log;
import com.yunkouan.lpid.persistence.entity.LogType;
import com.yunkouan.lpid.persistence.entity.Operator;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午2:41:00</P>
 * @author  yangli
 * @version 1.0.0
 */
public interface LogsService  {
    
    /**
     * 查询
     * 
     * @return 
     * @since 1.0.0
     * 2014年10月15日-下午5:59:23
     */
    public List<LogType> findLogType();
    
    /**
     * 写log操作
     * 
     * @param operator 操作员 
     * @param type log类型
     * @param desc log的具体描述
     * @since 1.0.0
     * 2014年10月16日-下午3:45:35
     */
    public void insertLog(Operator opetrator, LogTypeCode type, String desc);
    
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
     * @param logsModel
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
