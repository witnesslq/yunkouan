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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the operator database table.
 * 
 */
@Entity
@Table(name="operator")
@NamedQuery(name="Operator.findAll", query="SELECT o FROM Operator o")
public class Operator extends AbstractEntity  {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date createTime;
	private int isAdmin;
	private Date lastLoginTime;
	private String loginName;
	private String name;
	private String operatorCode;
	private String password;
	private String email;
	private String channel;
    private int status;
	private Date updateTime;
	private Set<Log> logs;
	private Set<Role> roles;

	public Operator() {
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


	@Column(name="is_admin")
	public int getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_login_time")
	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}


	@Column(name="login_name", length=45)
	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	@Column(length=45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name="operator_code", length=45)
	public String getOperatorCode() {
		return this.operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}


	@Column(length=45)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(length=100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Column(length=45)
	public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	//bi-directional many-to-one association to Log
	@OneToMany(fetch = FetchType.LAZY, mappedBy="operator")
	public Set<Log> getLogs() {
		return this.logs;
	}

	public void setLogs(Set<Log> logs) {
		this.logs = logs;
	}

	public Log addLog(Log log) {
		getLogs().add(log);
		log.setOperator(this);

		return log;
	}

	public Log removeLog(Log log) {
		getLogs().remove(log);
		log.setOperator(null);

		return log;
	}


	//bi-directional many-to-many association to Role
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "operator_role", 
        joinColumns = { @JoinColumn(name = "operator_id", nullable = false, updatable = false) }, 
        inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	@Override
	public String toString() {
		return "Operator [id=" + id + ", createTime=" + createTime + ", isAdmin=" + isAdmin + ", lastLoginTime="
				+ lastLoginTime + ", loginName=" + loginName + ", name=" + name + ", operatorCode=" + operatorCode
				+ ", password=" + password + ", email=" + email + ", channel=" + channel + ", status=" + status
				+ ", updateTime=" + updateTime + "]";
	}
}