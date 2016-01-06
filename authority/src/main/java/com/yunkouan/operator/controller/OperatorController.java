package com.yunkouan.operator.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunkouan.operator.service.OperatorService;
import com.yunkouan.operator.vo.OperatorVO;

@Controller
@RequestMapping(value="/operator")
public class OperatorController {
	@Autowired
	private OperatorService operatorService;
	
	@RequestMapping(value="/testSuccess")
	public String testSuccess(String s){
		System.out.println("testsuccess");
		System.out.println(s);
		return "success";
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
}
