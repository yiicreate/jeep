/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.form.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.database.datalink.jdbc.DBPool;
import com.jeeplus.modules.form.dto.Column;
import com.jeeplus.modules.form.entity.DataTable;
import com.jeeplus.modules.form.entity.DataTableColumn;
import com.jeeplus.modules.form.entity.Form;
import com.jeeplus.modules.form.service.FormService;
import com.jeeplus.modules.form.utils.FormJsonUtils;
import com.jeeplus.modules.form.utils.FormOracleTableBuilder;
import com.jeeplus.modules.form.utils.FormTableBuilder;
import com.jeeplus.modules.sys.entity.DataRule;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.service.DataRuleService;
import com.jeeplus.modules.sys.service.MenuService;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表单Controller
 * @author 刘高峰
 * @version 2019-12-24
 */
@RestController
@RequestMapping(value = "/form/make")
public class MakeFormController extends BaseController {

	@Autowired
	private FormService formService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MenuService menuService;
	@Autowired
	private DataRuleService dataRuleService;
	@ModelAttribute
	public Form get(@RequestParam(required=false) String id) {
		Form entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = formService.get(id);
		}
		if (entity == null){
			entity = new Form();
		}
		return entity;
	}

	/**
	 * 数据表单列表数据
	 */
	@RequiresPermissions("form:make:list")
	@GetMapping("list")
	public AjaxJson list(Form form, HttpServletRequest request, HttpServletResponse response) {
		Page<Form> page = formService.findPage(new Page<Form>(request, response), form);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取数据表单数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(Form form) {
		return AjaxJson.success().put("form", form);
	}

	/**
	 * 保存数据表单
	 */
	@RequiresPermissions(value={"form:make:add","form:make:edit"},logical=Logical.OR)
	@PostMapping("saveFormSource")
	public AjaxJson save(Form form,  Model model) throws Exception{
		if(StringUtils.isEmpty(form.getTableName())){
			form.setTableName(FormTableBuilder.getTablePrefix() + System.currentTimeMillis() + RandomUtils.nextInt(2));
		}
		String enName = form.getDataSource().getEnName();
		String dbType = FormTableBuilder.getJdbcType(enName);
		JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(enName);
		FormTableBuilder formTableBuilder;
		if(dbType.equals("oracle")){
			formTableBuilder = new FormOracleTableBuilder(jdbcTemplate, form.getTableName());
		}else {
			formTableBuilder = new FormTableBuilder(jdbcTemplate, form.getTableName());
		}
		List<Column> fieldArra = FormJsonUtils.newInstance().getFields(form.getSource ());
		if(form.getAutoCreate().equals("1")){ //自动建表，需要同步表结构
			formTableBuilder.syncTable(fieldArra);
		}
		//新增或编辑表单保存
		if(StringUtils.isBlank(form.getVersion())){
			form.setVersion("1");
		}else{
			form.setVersion(String.valueOf(Integer.valueOf(form.getVersion())+1));
		}
		formService.save(form);//保存
		return AjaxJson.success("保存数据表单成功");
	}

	/**
	 * 保存数据表单
	 */
	@RequiresPermissions(value={"form:make:add","form:make:edit"},logical=Logical.OR)
	@PostMapping("saveBasicInfo")
	public AjaxJson saveBasicInfo(Form form) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(form);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		if(StringUtils.isEmpty(form.getTableName())){
			form.setTableName(FormTableBuilder.getTablePrefix() + System.currentTimeMillis() + RandomUtils.nextInt(2));
		}
		String enName = form.getDataSource().getEnName();
		String dbType = FormTableBuilder.getJdbcType(enName);
		JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(enName);
		FormTableBuilder formTableBuilder;
		if(dbType.equals("oracle")){
			formTableBuilder = new FormOracleTableBuilder(jdbcTemplate, form.getTableName());
		}else {
			formTableBuilder = new FormTableBuilder(jdbcTemplate, form.getTableName());
		}
		if(StringUtils.isBlank(form.getId()) && form.getAutoCreate().equals("1")){
			formTableBuilder.createTable();
		}
		//新增或编辑表单保存
		if(StringUtils.isBlank(form.getVersion())){
			form.setVersion("1");
		}else{
			form.setVersion(String.valueOf(Integer.valueOf(form.getVersion())+1));
		}
		formService.save(form);//保存
		return AjaxJson.success("保存数据表单成功");
	}

	/**
	 * 编码唯一性验证（数据库中不存在）
	 */
	@GetMapping("validateKey")
	public AjaxJson validateKey(String key) {
		Form form = formService.findUniqueByProperty("code", key);
		if (form == null) {
			return AjaxJson.success().put("noExist", true);
		} else {
			return AjaxJson.success().put("noExist", false);
		}
	}

	/**
	 * 表名唯一性验证（数据库中不存在）
	 */
	@GetMapping("validateTableExist")
	public AjaxJson validateTableExist(DataTable dataTable) {
		DataTable dataTable1 = formService.findTableByName(dataTable);
		if (dataTable1 == null) {
			return AjaxJson.success().put("noExist", true);
		} else {
			return AjaxJson.success().put("noExist", false);
		}
	}


	@RequestMapping("getTableList")
	public AjaxJson getTableList( DataTable dataTable) {


		// 获取物理表列表
		List<DataTable> tableList = formService
				.findTableListFormDb(dataTable);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", tableList);
		map.put("total", tableList.size());
		return AjaxJson.success().putMap(map);
	}

	@RequestMapping("getTableColumnList")
	public AjaxJson getTableColumnList( DataTable dataTable) {


		// 获取物理表列表
		List<DataTableColumn> tableColumnList = formService
				.findTableColumnList(dataTable);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", tableColumnList);
		map.put("total", tableColumnList.size());
		return AjaxJson.success().putMap(map);
	}

	/**
	 * 批量删除数据表单
	 */
	@RequiresPermissions("form:make:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Form form = formService.get(id);
			//需要删除表
			if (StringUtils.isNotBlank(form.getTableName()) && form.getAutoCreate().equals("1")) {
				String enName = form.getDataSource().getEnName();
				String dbType = FormTableBuilder.getJdbcType(enName);
				JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(enName);
				FormTableBuilder formTableBuilder;
				if(dbType.equals("oracle")){
					formTableBuilder = new FormOracleTableBuilder(jdbcTemplate, form.getTableName());
				}else {
					formTableBuilder = new FormTableBuilder(jdbcTemplate, form.getTableName());
				}
				formTableBuilder.dropTable();
			}
			formService.delete(form);

		}
		return AjaxJson.success("删除数据表单成功");
	}
	/*
	 *部署
	 */
	@RequiresPermissions("form:make:deploy")
	@RequestMapping("createMenu")
	public AjaxJson createMenu(@RequestParam("formId")String formId, Menu menu) {
		if (jeePlusProperites.isDemoMode()) {
			return AjaxJson.error("演示模式，不允许操作！");
		}

		// 获取排序号，最末节点排序号+30
		if (StringUtils.isBlank(menu.getId())) {
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(menu.getId())) {
				List<Menu> list = Lists.newArrayList();
				List<Menu> sourcelist = menuService.findAllMenu();
				Menu.sortList(list, sourcelist, menu.getParentId(), false);
				if (list.size() > 0) {
					menu.setSort(list.get(list.size() - 1).getSort() + 30);
				}
			}
		}

		Form form = formService.get(formId);
		Menu topMenu = formService.createMenu(form, menu);
		if(menu.getDataRuleList() != null){
			for(DataRule dataRule: menu.getDataRuleList()){
				dataRule.setMenuId(topMenu.getId());
				dataRuleService.save(dataRule);
			}
		}

		return AjaxJson.success("创建菜单'" + form.getName() + "'成功<br/>");
	}

}
