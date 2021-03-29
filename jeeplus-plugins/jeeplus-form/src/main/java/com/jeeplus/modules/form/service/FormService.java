/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.form.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.database.datasource.annotation.DS;
import com.jeeplus.modules.form.entity.DataTable;
import com.jeeplus.modules.form.entity.DataTableColumn;
import com.jeeplus.modules.form.entity.Form;
import com.jeeplus.modules.form.mapper.FormMapper;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 数据表单Service
 * @author 刘高峰
 * @version 2019-12-24
 */
@Service
@Transactional(readOnly = true)
public class FormService extends CrudService<FormMapper, Form> {
	@Autowired
	private MenuService menuService;
	public Form get(String id) {
		return super.get(id);
	}

	public List<Form> findList(Form form) {
		return super.findList(form);
	}

	public Page<Form> findPage(Page<Form> page, Form form) {
		return super.findPage(page, form);
	}

	@Transactional(readOnly = false)
	public void save(Form form) {
		super.save(form);
	}

	@Transactional(readOnly = false)
	public void delete(Form form) {
		super.delete(form);
	}

	/**
	 * 获取物理数据表列表
	 *
	 * @param dataTable
	 * @return
	 */
	@Transactional(readOnly = false)
	@DS("#dataTable.dataSource.enName")
	public List<DataTable> findTableListFormDb(DataTable dataTable) {
		return mapper.findTableList(dataTable);
	}

	@Transactional(readOnly = false)
	@DS("#dataTable.dataSource.enName")
	public DataTable findTableByName(DataTable dataTable) {
		return mapper.findTableByName(dataTable);
	}

	@Transactional(readOnly = false)
	@DS("#dataTable.dataSource.enName")
	public List<DataTableColumn> findTableColumnList(DataTable dataTable) {
		return mapper.findTableColumnList(dataTable);
	}


	@Transactional(readOnly = false)
	public Menu createMenu(Form form, Menu topMenu) {
		String permissionPrefix = "form:"+form.getTableName();
		String url = "/form/GenerateList?title="+form.getName()+"&id="+form.getId();
		topMenu.setHref(url);
		topMenu.setIsShow("1");
		topMenu.setType("1");
		topMenu.setPermission(permissionPrefix + ":list");
		menuService.saveMenu(topMenu);

		Menu addMenu = new Menu();
		addMenu.setName("新建");
		addMenu.setIsShow("0");
		addMenu.setType("2");
		addMenu.setSort(30);
		addMenu.setPermission(permissionPrefix + ":add");
		addMenu.setParent(topMenu);
		menuService.saveMenu(addMenu);

		Menu delMenu = new Menu();
		delMenu.setName("删除");
		delMenu.setIsShow("0");
		delMenu.setType("2");
		delMenu.setSort(60);
		delMenu.setPermission(permissionPrefix + ":del");
		delMenu.setParent(topMenu);
		menuService.saveMenu(delMenu);

		Menu editMenu = new Menu();
		editMenu.setName("修改");
		editMenu.setIsShow("0");
		editMenu.setType("2");
		editMenu.setSort(90);
		editMenu.setPermission(permissionPrefix + ":edit");
		editMenu.setParent(topMenu);
		menuService.saveMenu(editMenu);

		Menu viewMenu = new Menu();
		viewMenu.setName("查看");
		viewMenu.setIsShow("0");
		viewMenu.setType("2");
		viewMenu.setSort(120);
		viewMenu.setPermission(permissionPrefix + ":view");
		viewMenu.setParent(topMenu);
		menuService.saveMenu(viewMenu);

		Menu importMenu = new Menu();
		importMenu.setName("导入");
		importMenu.setIsShow("0");
		importMenu.setType("2");
		importMenu.setSort(150);
		importMenu.setPermission(permissionPrefix + ":import");
		importMenu.setParent(topMenu);
		menuService.saveMenu(importMenu);

		Menu exportMenu = new Menu();
		exportMenu.setName("导出");
		exportMenu.setIsShow("0");
		exportMenu.setType("2");
		exportMenu.setSort(180);
		exportMenu.setPermission(permissionPrefix + ":export");
		exportMenu.setParent(topMenu);
		menuService.saveMenu(exportMenu);
		return topMenu;

	}


}
