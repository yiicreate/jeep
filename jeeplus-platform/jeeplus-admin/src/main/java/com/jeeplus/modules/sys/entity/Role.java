/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeeplus.config.swagger.IgnoreSwaggerParameter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.jeeplus.config.properties.JeePlusProperites;
import com.jeeplus.core.persistence.DataEntity;
/**
 * 角色Entity
 * @author jeeplus
 * @version 2016-12-05
 */
public class Role extends DataEntity<Role> {

	private static final long serialVersionUID = 1L;
	private String name; 	// 角色名称
	private String enname;	// 英文名称
	private String oldName; 	// 原角色名称
	private String oldEnname;	// 原英文名称
	private String sysData; 		//是否是系统数据
	private String useable; 		//是否是可用
	@IgnoreSwaggerParameter
	private User user;		// 根据用户ID查询角色列表

	private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表
	private List<DataRule> dataRuleList = Lists.newArrayList(); // 数据范围


	public Role() {
		super();
		this.useable= JeePlusProperites.YES;
	}

	public Role(String id){
		super(id);
	}

	public Role(User user) {
		this();
		this.user = user;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public String getSysData() {
		return sysData;
	}

	public void setSysData(String sysData) {
		this.sysData = sysData;
	}

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=100)
	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldEnname() {
		return oldEnname;
	}

	public void setOldEnname(String oldEnname) {
		this.oldEnname = oldEnname;
	}

	@JsonIgnore
	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	@JsonIgnore
	public List<String> getMenuIdList() {
		List<String> menuIdList = Lists.newArrayList();
		for (Menu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		menuList = Lists.newArrayList();
		for (String menuId : menuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			menuList.add(menu);
		}
	}

	public String getMenuIds() {
		return StringUtils.join(getMenuIdList(), ",");
	}

	public void setMenuIds(String menuIds) {
		menuList = Lists.newArrayList();
		if (menuIds != null){
			String[] ids = StringUtils.split(menuIds, ",");
			setMenuIdList(Lists.newArrayList(ids));
		}
	}


	/**
	 * 获取权限字符串列表
	 */
	public List<String> getPermissions() {
		List<String> permissions = Lists.newArrayList();
		for (Menu menu : menuList) {
			if (menu.getPermission()!=null && !"".equals(menu.getPermission())){
				permissions.add(menu.getPermission());
			}
		}
		return permissions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonIgnore
	public List<DataRule> getDataRuleList() {
		return dataRuleList;
	}

	public void setDataRuleList(List<DataRule> dataRuleList) {
		this.dataRuleList = dataRuleList;
	}

	@JsonIgnore
	public List<String> getDataRuleIdList() {
		List<String> dataRuleIdList = Lists.newArrayList();
		for (DataRule dataRule : dataRuleList) {
			dataRuleIdList.add(dataRule.getId());
		}
		return dataRuleIdList;
	}

	public void setDataRuleIdList(List<String> dataRuleIdList) {
		dataRuleList = Lists.newArrayList();
		for (String dataRuleId : dataRuleIdList) {
			DataRule dataRule = new DataRule();
			dataRule.setId(dataRuleId);
			dataRuleList.add(dataRule);
		}
	}

	public String getDataRuleIds() {
		return StringUtils.join(getDataRuleIdList(), ",");
	}

	public void setDataRuleIds(String dataRuleIds) {
		dataRuleList = Lists.newArrayList();
		if (dataRuleIds != null){
			String[] ids = StringUtils.split(dataRuleIds, ",");
			setDataRuleIdList(Lists.newArrayList(ids));
		}
	}

}
