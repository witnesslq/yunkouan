package com.yunkouan.lpid.biz.system.logs.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.lpid.biz.system.logs.bo.LogTypeCode;
import com.yunkouan.lpid.biz.system.logs.bo.LogsModelQuery;
import com.yunkouan.lpid.biz.system.logs.service.LogsService;
import com.yunkouan.lpid.persistence.dao.LogsDao;
import com.yunkouan.lpid.persistence.entity.Log;
import com.yunkouan.lpid.persistence.entity.LogType;
import com.yunkouan.lpid.persistence.entity.Operator;
/**
 * <P>Title: csc_sh</P>
 * <P>Description: log日志管理</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午2:42:47</P>
 * @author  yangli
 * @version 1.0.0
 */
@Service("logsService")
public class LogsServiceImpl implements LogsService{
	@Autowired
	private LogsDao logsDao;
	

    @Override
    public List<LogType> findLogType() {
        return logsDao.findAll(LogType.class);
    }

	@Override
	public void addLogInfo(Log log) {
		logsDao.addLogInfo(log);
	}

	@Override
	public List<Log> findAllLogInfo(int firstResult, int pageSize,
			LogsModelQuery queryModel) {
		return logsDao.findAllLogInfo(firstResult, pageSize, queryModel);
	}

    @Override
    public int findLogInfoCount(LogsModelQuery queryModel) {
        return logsDao.findLogInfoCount(queryModel);
    }

    @Override
    public void insertLog(Operator operator, LogTypeCode type, String desc) {
        //获取log类型
        LogType logType = logsDao.find(type.getCode(), LogType.class);
        
        Log log = new Log();
        log.setLogType(logType);
        log.setOperator(operator);
        log.setContent(desc);
        log.setCreateTime(new Date());
        
        logsDao.save(log);
    }

}
