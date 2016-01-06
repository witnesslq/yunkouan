package com.yunkouan.biz.operator.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.biz.operator.service.OperatorService;
import com.yunkouan.biz.operator.vo.OperatorVO;

@Controller
@RequestMapping(value="/operator")
public class OperatorController {
	@Autowired
	private OperatorService operatorService;
	
	@RequestMapping(value="/listOperators")
	@ResponseBody
	public List<OperatorVO> listOperators(){
		List<OperatorVO> operators = new ArrayList<>();
		operators = operatorService.getOperatorVOs();
		return operators;
	}
	
	/**
	 * 添加操作员
	 * @return
	 */
	@RequestMapping(value="/addOperator")
	public String addOperator(OperatorVO operatorVO){
		operatorService.addOperator(operatorVO);
		return "success";
	}
	
	public String modifyOperator(OperatorVO operatorVO){
		operatorService.modifyOperator(operatorVO);
		return "success";
	}
}
