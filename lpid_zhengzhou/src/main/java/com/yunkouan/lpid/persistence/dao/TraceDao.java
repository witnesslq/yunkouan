package com.yunkouan.lpid.persistence.dao;

import com.yunkouan.lpid.persistence.entity.TraceTime;

public interface TraceDao  extends GenericDao {
	void saveTraceTime(TraceTime traceTime);
}
