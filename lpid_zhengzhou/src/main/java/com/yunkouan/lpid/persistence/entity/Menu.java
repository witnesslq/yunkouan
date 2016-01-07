package com.yunkouan.lpid.persistence.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@Table(name="menu")
@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m")
public class Menu extends AbstractEntity implements Comparable<Menu> {
	private static final long serialVersionUID = 1L;
	private int id;
	private String linkUrl;
	private int menuLevel;
	private int menuOrder;
	private int menuType;//菜单类型(1:菜单;2:链接[没有菜单名称])
	private String name;
	private String menuIcon;
	private Menu menu;
	private Set<Menu> menus;
	private Set<Role> roles;

	public Menu() {
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


	@Column(name="link_url", length=256)
	public String getLinkUrl() {
		return this.linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}


	@Column(name="menu_level")
	public int getMenuLevel() {
		return this.menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}


	@Column(name="menu_order")
	public int getMenuOrder() {
		return this.menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}


	@Column(name="menu_type")
	public int getMenuType() {
		return this.menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}


	@Column(length=32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    @Column(name="menu_icon", length=100)
    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

	//bi-directional many-to-one association to Menu
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}


	//bi-directional many-to-one association to Menu
	@OneToMany(fetch = FetchType.LAZY, mappedBy="menu")
	public Set<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

	public Menu addMenus(Menu menus) {
		getMenus().add(menus);
		menus.setMenu(this);

		return menus;
	}

	public Menu removeMenus(Menu menus) {
		getMenus().remove(menus);
		menus.setMenu(null);

		return menus;
	}


	//bi-directional many-to-many association to Role
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="menus")
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
   @Override
    public int compareTo(Menu anotherMenu) {
        int thisOrder = this.getMenuOrder();
        int anotherOrder = anotherMenu.getMenuOrder();
        return thisOrder < anotherOrder ? -1 : (thisOrder == anotherOrder ? 0 : 1);
    }

}