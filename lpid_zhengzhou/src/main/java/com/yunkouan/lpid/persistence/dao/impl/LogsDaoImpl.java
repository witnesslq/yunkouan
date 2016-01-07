package com.yunkouan.lpid.persistence.dao.impl;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.Tuple;
import com.yunkou.common.util.TwoTuple;
import com.yunkouan.lpid.biz.system.logs.bo.LogsModelQuery;
import com.yunkouan.lpid.persistence.dao.LogsDao;
import com.yunkouan.lpid.persistence.entity.Log;

@Repository("logsDaoImpl")
public class LogsDaoImpl extends GenericJpaDaoImpl implements LogsDao{

	@Override
	public void addLogInfo(Log log) {
		super.save(log);
	}

	@Override
	public List<Log> findAllLogInfo(int firstResult, int pageSize,
			LogsModelQuery queryModel) {
	    return super.findByNamedQuery(getJpql(queryModel).first, firstResult, pageSize, 
	            getJpql(queryModel).second.toArray());
	}

    @Override
    public int findLogInfoCount(LogsModelQuery queryModel) {
        return super.getCount(getJpql(queryModel).first, getJpql(queryModel).second.toArray());
    }
    
    private TwoTuple<String, List<Object>> getJpql(LogsModelQuery queryModel) {
        //查询jpql语句
        StringBuffer jpql = new StringBuffer(
                  "select log from Log log "
                + " left join log.logType logType"
                + " left join log.operator operator "
                + " where 1 = 1 ");
        //参数
        List<Object> params = new ArrayList<Object>();
        
        //日志类型
        if(queryModel.getLogTypeSelect() != -1) {
            jpql.append(" and logType.id = ? ");
            params.add(queryModel.getLogTypeSelect());
        }
        //用户名
        if(isNotEmpty(queryModel.getOperatorName())) {
            jpql.append(" and operator.loginName like ? ");
            params.add("%" + queryModel.getOperatorName() + "%");
        }
        
        //查询时间开始
        if(isNotEmpty(queryModel.getStartTime())) {
            jpql.append(" and log.createTime >= ? ");
            params.add(Timestamp.valueOf(DateTimeUtil.getDateBeginTime(queryModel.getStartTime())));
        }
        
        //查询时间结束
        if(isNotEmpty(queryModel.getEndTime())) {
            jpql.append(" and log.createTime <= ? ");
            params.add(Timestamp.valueOf(DateTimeUtil.getDateEndTime(queryModel.getEndTime())));
        }
        
        //日志内容
        if(isNotEmpty(queryModel.getContent())) {
            jpql.append(" and log.content like ? ");
            params.add("%" + queryModel.getContent() + "%");
        }
        
        jpql.append(" order by log.createTime desc");
        
        return Tuple.tuple(jpql.toString(), params);
    }

}
