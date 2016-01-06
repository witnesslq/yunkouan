package com.yunkouan.operator.service;

import java.util.List;

import com.yunkouan.operator.vo.OperatorVO;

public interface OperatorService {
	void addOperator(OperatorVO operatorVO);
	
	void modifyOperator(OperatorVO operatorVO);
	
	void removeOperator(OperatorVO operatorVO);
	
	List<OperatorVO> getOperatorVOs(); 
}
