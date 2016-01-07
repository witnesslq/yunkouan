package com.yunkouan.lpid.biz.label.service;

import com.yunkouan.lpid.persistence.entity.Label;

public interface LabelService {
	/**
	 * 设置标签总数
	 */
	public void setTotal(Label label);
	
	/**
	 * 获取标签对象
	 * @return
	 */
	public Label findLabel();
	
	/**
	 * 更新标签剩余数量
	 */
	public void updateBalance(Label label);
}
