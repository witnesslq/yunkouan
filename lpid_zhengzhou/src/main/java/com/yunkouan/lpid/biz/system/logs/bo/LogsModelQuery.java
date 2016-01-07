package com.yunkouan.lpid.biz.system.logs.bo;



public class LogsModelQuery {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	/**
	 * 创建时间
	 */
	private String startTime;
	/**
	 * 创建时间
	 */
	private String endTime;
	/**
	 * 描述
	 */
	private String content;
	/**
	 * 日志类型
	 */
	private int logTypeSelect;
	/**
	 * 操作员
	 */
	private String operatorName;
	
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public int getLogTypeSelect() {
        return logTypeSelect;
    }
    public void setLogTypeSelect(int logTypeSelect) {
        this.logTypeSelect = logTypeSelect;
    }
    public String getOperatorName() {
        return operatorName;
    }
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
	
}
