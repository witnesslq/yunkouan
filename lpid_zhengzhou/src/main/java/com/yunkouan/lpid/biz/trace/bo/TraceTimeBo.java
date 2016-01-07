package com.yunkouan.lpid.biz.trace.bo;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yunkouan.lpid.persistence.entity.TraceTime;

@Component(value="traceTimeBo")
public class TraceTimeBo {
	private Map<String,TraceTime> traceTimes = new HashMap<>(5);

	public Map<String, TraceTime> getTraceTimes() {
		return traceTimes;
	}

	public void setTraceTimes(Map<String, TraceTime> traceTimes) {
		this.traceTimes = traceTimes;
	}
}
