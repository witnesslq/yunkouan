package com.yunkouan.lpid.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the log_type database table.
 * 
 */
@Entity
@Table(name="log_type")
@NamedQuery(name="LogType.findAll", query="SELECT l FROM LogType l")
public class LogType  extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String description;
	private String name;

	public LogType() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(length=200)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(length=100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}