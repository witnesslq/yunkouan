package com.yunkouan.lpid.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the param_config database table.
 * 
 */
@Entity
@Table(name="param_config")
@NamedQuery(name="ParamConfig.findAll", query="SELECT p FROM ParamConfig p")public class ParamConfig extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String value;

	public ParamConfig() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ParamConfig [name=" + name + ", value=" + value + "]";
	}

}