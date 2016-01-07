package com.yunkouan.lpid.biz.label.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.lpid.biz.label.service.LabelService;
import com.yunkouan.lpid.persistence.entity.Label;

@Controller
@RequestMapping(value="/label")
public class LabelController {
	@Autowired
	private LabelService labelService;
	
	@RequestMapping("/setTotal")
	@ResponseBody
	public void setTotal(Label label) {
		Label labelBo = labelService.findLabel();
		if(labelBo == null) {
			labelBo = new Label();
		} 
		labelBo.setTotal(label.getTotal());
		labelBo.setBalance(label.getTotal());
		labelService.setTotal(labelBo);
	}
}
