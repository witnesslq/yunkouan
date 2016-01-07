package com.yunkouan.lpid.persistence.dao.impl;

import org.springframework.stereotype.Repository;

import com.yunkouan.lpid.persistence.dao.TraceDao;
import com.yunkouan.lpid.persistence.entity.TraceTime;

@Repository(value="traceDao")
public class TraceDaoImpl extends GenericJpaDaoImpl implements TraceDao{

	@Override
	public void saveTraceTime(TraceTime traceTime) {
		super.save(traceTime);
	}
}
