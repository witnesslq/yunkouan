package com.yunkouan.lpid.persistence.dao;

import com.yunkouan.lpid.persistence.entity.Label;

public interface LabelDao extends GenericDao {
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
