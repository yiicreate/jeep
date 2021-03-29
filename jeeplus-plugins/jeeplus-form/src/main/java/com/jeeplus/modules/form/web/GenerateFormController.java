/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.form.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.form.dto.Column;
import com.jeeplus.modules.form.entity.Form;
import com.jeeplus.modules.form.service.FormService;
import com.jeeplus.modules.form.service.GenerateFormService;
import com.jeeplus.modules.form.utils.ExcelUtils;
import com.jeeplus.modules.form.utils.FormJsonUtils;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表单Controller
 * @author 刘高峰
 * @version 2019-12-24
 */
@RestController
@RequestMapping(value = "/form/generate")
public class GenerateFormController extends BaseController {

	@Autowired
	private FormService formService;
	@Autowired
	private GenerateFormService generateFormService;

	/**
	 * 保存数据表单
	 */
	@PostMapping("save")
	public AjaxJson add(String formId, @RequestParam("data") String data) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		Form form = formService.get(formId);
		JSONObject jData = JSONObject.fromObject(data);
		List<Column> fieldArra = FormJsonUtils.newInstance().getFields(form.getSource ());
		HashMap<String, String> map = new HashMap();
		for(Column column: fieldArra){
			map.put(column.getModel(), column.getType());
		}
		if(jData.get("id") != null && StringUtils.isNotBlank(jData.getString("id"))){
			generateFormService.executeUpdate(form, jData, map);

		}else {
			generateFormService.executeInsert(form, jData, map);
		}
		return AjaxJson.success("保存数据成功");
	}




	@RequestMapping(value = "/list")
	public AjaxJson dataList(@RequestParam("formId") String formId, @RequestParam("params") String params,  HttpServletRequest request, HttpServletResponse response) throws Exception{
		Form form = formService.get(formId);
		Page page = generateFormService.executeFindPage(new Page<Form>(request, response), form, params);
		return AjaxJson.success().put("page", page);
	}


	/**
	 * 批量删除数据表单
	 */
	@DeleteMapping("delete")
	public AjaxJson executeDelete(String formId, String ids) {
		Form form = formService.get(formId);
		generateFormService.executeDelete(form, ids);
		return AjaxJson.success("删除数据成功");
	}

	/**
	 * 查询
	 */
	@GetMapping("queryById")
	public AjaxJson executeQueryById(String formId, @RequestParam String id) {
		Form form = formService.get(formId);
		Map map = generateFormService.executeQueryById(form, id);
		return AjaxJson.success().put("obj", map);
	}

	/**
	 * 导出excel文件
	 */
	@RequestMapping("export")
	public AjaxJson exportFile(@RequestParam("formId") String formId, @RequestParam("params") String params, HttpServletRequest request, HttpServletResponse response) {
		Form form = formService.get(formId);
		try {
			String fileName = form.getName() + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Map> page = generateFormService.executeFindPage(new Page<Map>(request, response), form, params);

			String title = form.getName();
			List<String> header = Lists.newArrayList();
			List<String> keys = Lists.newArrayList();
			List<Column> fieldArra = FormJsonUtils.newInstance().getFields(form.getSource ());
			for (Column column : fieldArra) {
				header.add(column.getName());
				keys.add(column.getModel());
			}
			ExportExcel exportExcel = new ExportExcel(title, header);
			for (Map<String, Object> map : page.getList()) {
				int column = 0;
				Row row = exportExcel.addRow();
				for (String key: keys) {
					exportExcel.addCell(row, column, map.get(key));
					column++;
				}
			}

			exportExcel.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			return AjaxJson.error("导出" + form.getName() + "记录失败！失败信息：" + e.getMessage());
		}
	}

	/**
	 * 导入Excel数据
	 */
	@PostMapping("import")
	public AjaxJson importFile(@RequestParam("formId") String formId,  @RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		Form form = formService.get(formId);
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			for (int i = ei.getDataRowNum(); i < ei.getLastDataRowNum(); i++) {
				Row row = ei.getRow(i);
				try {

					JSONObject jData = new JSONObject();
					List<Column> fieldArra = FormJsonUtils.newInstance().getFields(form.getSource ());
					HashMap<String, String> map = new HashMap();
					for (int j = 0; j < fieldArra.size(); j++) {
						Column column = fieldArra.get(j);
						if (column.getType().equals("area")) {
							Area area = UserUtils.getByAreaName(row.getCell(j).toString());
							if (area != null) {
								jData.put(column.getModel(), area.getId());
							} else {
								jData.put(column.getModel(), "");
							}

						} else if (column.getType().equals("user")) {
							User user = UserUtils.getByUserName(row.getCell(j).toString());
							if (user != null) {
								jData.put(column.getModel(), user.getId());
							} else {
								jData.put(column.getModel(), "");
							}
						} else if (column.getType().equals("office")) {
							Office office = UserUtils.getByOfficeName(row.getCell(j).toString());
							if (office != null) {
								jData.put(column.getModel(), office.getId());
							} else {
								jData.put(column.getModel(), "");
							}
						} else {
							jData.put(column.getModel(), ExcelUtils.getCellString(row.getCell(j)));
						}
						map.put(column.getModel(), column.getType());
					}
					generateFormService.executeInsert(form, jData, map);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条"+form.getName()+"记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条"+form.getName()+"记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入"+form.getName()+"失败！失败信息："+e.getMessage());
		}
	}

	/**
	 * 下载导入表单数据模板
	 */
	@RequestMapping("import/template")
	public AjaxJson importFileTemplate(String formId, HttpServletResponse response) {
		Form form = formService.get(formId);
		try {
			String title = form.getName();
			String fileName = title + "数据导入模板.xlsx";
			List<String> header = Lists.newArrayList();
			List<Column> fieldArra = FormJsonUtils.newInstance().getFields(form.getSource ());
			for (Column column : fieldArra) {
				header.add(column.getName());
			}
			ExportExcel exportExcel = new ExportExcel(title, header);

			exportExcel.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
	}



}
