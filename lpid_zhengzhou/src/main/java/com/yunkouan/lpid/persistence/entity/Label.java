package com.yunkouan.lpid.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="label")
@NamedQuery(name="Label.findAll", query="SELECT l FROM Label l")
public class Label extends AbstractEntity  {
	private Integer id;
	/**
	 * 标签总数
	 */
	private Integer total;
	/**
	 * 剩余数量
	 */
	private Integer balance;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
}
