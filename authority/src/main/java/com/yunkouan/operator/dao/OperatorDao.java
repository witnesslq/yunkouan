package com.yunkouan.operator.dao;

import java.util.List;

import com.yunkouan.operator.entity.Operator;

public interface OperatorDao {
	/**
	 * 添加操作员
	 * @param operator
	 * @return
	 */
	public Operator addOperator(Operator operator);
	/**
	 * 更新操作员
	 * @param operator
	 * @return
	 */
	public Operator updateOperator(Operator operator);
	/**
	 * 查询操作员
	 * @return
	 */
	public List<Operator> getOperators();
	/**
	 * 移除操作员
	 * @param operator
	 */
	public void removeOperator(Operator operator);
}
