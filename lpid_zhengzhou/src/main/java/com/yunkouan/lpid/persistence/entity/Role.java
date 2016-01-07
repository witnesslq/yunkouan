package com.yunkouan.lpid.persistence.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name="role")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role extends AbstractEntity  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date createTime;
	private String description;// 角色描述
	private String name;//角色名称
	private Integer status;// 状态(0:禁用;1:启用) 
	private Set<Operator> operators;
	private Set<Menu> menus;

	public Role() {
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


	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(length=256)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(length=32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	//bi-directional many-to-many association to Operator
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="roles")
	public Set<Operator> getOperators() {
		return this.operators;
	}

	public void setOperators(Set<Operator> operators) {
		this.operators = operators;
	}


	//bi-directional many-to-many association to Menu
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},  fetch = FetchType.LAZY)
    @JoinTable(name = "role_menu", 
        joinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) }, 
        inverseJoinColumns = { @JoinColumn(name = "menu_id", nullable = false, updatable = false) })
	public Set<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

}