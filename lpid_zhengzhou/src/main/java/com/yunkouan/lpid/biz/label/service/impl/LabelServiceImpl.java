package com.yunkouan.lpid.biz.label.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.lpid.biz.label.service.LabelService;
import com.yunkouan.lpid.persistence.dao.LabelDao;
import com.yunkouan.lpid.persistence.entity.Label;

@Transactional
@Service(value="labelService")
public class LabelServiceImpl implements LabelService {
	@Autowired
	private LabelDao labelDao;
	@Override
	public void setTotal(Label label) {
		labelDao.setTotal(label);
	}

	@Override
	public Label findLabel() {
		return labelDao.findLabel();
	}

	@Override
	public void updateBalance(Label label) {
		labelDao.update(label);
	}

}
