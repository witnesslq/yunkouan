package com.yunkouan.lpid.persistence.dao.impl;

import org.springframework.stereotype.Repository;

import com.yunkouan.lpid.persistence.dao.LabelDao;
import com.yunkouan.lpid.persistence.entity.Label;

@Repository(value="labelDao")
public class LabelDaoImpl extends GenericJpaDaoImpl implements LabelDao {

	@Override
	public void setTotal(Label label) {
		super.save(label);
	}

	@Override
	public Label findLabel() {
		String jpql = "SELECT l FROM Label l";
		return super.findFirstOrNull(jpql, null);
	}

	@Override
	public void updateBalance(Label label) {
		super.update(label);
	}
}
