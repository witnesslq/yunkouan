package com.yunkouan.operator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.operator.dao.OperatorDao;
import com.yunkouan.operator.entity.Operator;
import com.yunkouan.operator.service.OperatorService;
import com.yunkouan.operator.vo.OperatorVO;

@Transactional
@Service(value="operatorService")
public class OperatorServiceImpl implements OperatorService {
	@Autowired
	private OperatorDao operatorDao;
	
	@Override
	public void addOperator(OperatorVO operatorVO) {
		Operator operator = new Operator();
		operator.setLoginName(operatorVO.getLoginName());
		operator.setNickname(operatorVO.getNickname());
		operator.setPassword(operatorVO.getPassword());
		operator.setDescription(operatorVO.getDescription());
		operator.setCreateTime(operatorVO.getCreateTime());
		operatorDao.addOperator(operator);
	}

	@Override
	public void modifyOperator(OperatorVO operatorVO) {
		
	}

	@Override
	public void removeOperator(OperatorVO operatorVO) {
		
	}

	@Override
	public List<OperatorVO> getOperatorVOs() {
		return null;
	}
}
