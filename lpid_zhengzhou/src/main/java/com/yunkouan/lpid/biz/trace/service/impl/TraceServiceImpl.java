package com.yunkouan.lpid.biz.trace.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.lpid.biz.trace.service.TraceService;
import com.yunkouan.lpid.persistence.dao.TraceDao;
import com.yunkouan.lpid.persistence.entity.TraceTime;

@Transactional
@Service(value="traceService")
public class TraceServiceImpl implements TraceService{
	@Autowired
	private TraceDao traceDao;
	
	@Override
	public void saveTraceTime(TraceTime traceTime) {
		traceDao.saveTraceTime(traceTime);
	}
}
