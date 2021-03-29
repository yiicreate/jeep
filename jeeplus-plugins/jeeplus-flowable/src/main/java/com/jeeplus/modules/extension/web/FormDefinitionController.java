/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.extension.entity.FormDefinitionJson;
import com.jeeplus.modules.extension.service.FormDefinitionJsonService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.extension.entity.FormDefinition;
import com.jeeplus.modules.extension.service.FormDefinitionService;

/**
 * 流程表单Controller
 * @author 刘高峰
 * @version 2020-02-02
 */
@RestController
@RequestMapping(value = "/extension/formDefinition")
public class FormDefinitionController extends BaseController {

	@Autowired
	private FormDefinitionService formDefinitionService;
	@Autowired
	private FormDefinitionJsonService formDefinitionJsonService;

	@ModelAttribute
	public FormDefinition get(@RequestParam(required=false) String id) {
		FormDefinition entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = formDefinitionService.get(id);
		}
		if (entity == null){
			entity = new FormDefinition();
		}
		return entity;
	}

	/**
	 * 流程表单列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(FormDefinition formDefinition, HttpServletRequest request, HttpServletResponse response) {
		Page<FormDefinition> page = formDefinitionService.findPage(new Page<FormDefinition>(request, response), formDefinition);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取流程表单数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(FormDefinition formDefinition) {
		return AjaxJson.success().put("formDefinition", formDefinition);
	}

	/**
	 * 根据Id获取流程表单数据
	 */
	@GetMapping("queryByJsonId")
	public AjaxJson queryByJsonId(String jsonId) {
		FormDefinitionJson formDefinitionJson = formDefinitionJsonService.get(jsonId);
		if(formDefinitionJson != null){
			FormDefinition formDefinition = formDefinitionService.get(formDefinitionJson.getFormDefinitionId());
			formDefinition.setFormDefinitionJson(formDefinitionJson);
			return AjaxJson.success().put("formDefinition", formDefinition);
		}else{
			return AjaxJson.error("流程关联的动态表单版本已经被删除!");
		}

	}
	/**
	 * 保存流程表单
	 */
	@RequiresPermissions(value={"extension:formDefinition:add","extension:formDefinition:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(FormDefinition formDefinition, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(formDefinition);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		formDefinitionService.save(formDefinition);//保存
		return AjaxJson.success("保存流程表单成功").put("formDefinition", formDefinition);
	}


	/**
	 * 批量删除流程表单
	 */
	@RequiresPermissions("extension:formDefinition:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			FormDefinition formDefinition = new FormDefinition(id);
			formDefinitionService.delete(formDefinition);
		}
		return AjaxJson.success("删除流程表单成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("extension:formDefinition:export")
    @GetMapping("export")
    public AjaxJson exportFile(FormDefinition formDefinition, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "流程表单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FormDefinition> page = formDefinitionService.findPage(new Page<FormDefinition>(request, response, -1), formDefinition);
    		new ExportExcel("流程表单", FormDefinition.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出流程表单记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("extension:formDefinition:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FormDefinition> list = ei.getDataList(FormDefinition.class);
			for (FormDefinition formDefinition : list){
				try{
					formDefinitionService.save(formDefinition);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条流程表单记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条流程表单记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入流程表单失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入流程表单数据模板
	 */
	@RequiresPermissions("extension:formDefinition:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "流程表单数据导入模板.xlsx";
    		List<FormDefinition> list = Lists.newArrayList();
    		new ExportExcel("流程表单数据", FormDefinition.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}
