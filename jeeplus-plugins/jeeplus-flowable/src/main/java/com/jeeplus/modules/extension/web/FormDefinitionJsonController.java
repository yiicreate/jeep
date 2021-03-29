/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.modules.extension.entity.FormDefinitionJson;
import com.jeeplus.modules.extension.service.FormDefinitionJsonService;

/**
 * 流程表单Controller
 * @author 刘高峰
 * @version 2020-02-02
 */
@RestController
@RequestMapping(value = "/extension/formDefinitionJson")
public class FormDefinitionJsonController extends BaseController {

	@Autowired
	private FormDefinitionJsonService formDefinitionJsonService;

	public FormDefinitionJson get(String id) {
		FormDefinitionJson entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = formDefinitionJsonService.get(id);
		}
		if (entity == null){
			entity = new FormDefinitionJson();
		}
		return entity;
	}

	/**
	 * 流程表单列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(FormDefinitionJson formDefinitionJson, HttpServletRequest request, HttpServletResponse response) {
		Page<FormDefinitionJson> page = formDefinitionJsonService.findPage(new Page<FormDefinitionJson>(request, response), formDefinitionJson);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取流程表单数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(FormDefinitionJson formDefinitionJson) {
		formDefinitionJson = this.get(formDefinitionJson.getId());
		return AjaxJson.success().put("formDefinitionJson", formDefinitionJson);
	}

	/**
	 * 根据Id获取流程表单数据
	 */
	@PostMapping("updatePrimary")
	public AjaxJson updatePrimary(FormDefinitionJson formDefinitionJson) {
		formDefinitionJson = this.get(formDefinitionJson.getId());
		//其余版本更新为非主版本
		FormDefinitionJson formDefinitionJson1 = new FormDefinitionJson();
		formDefinitionJson1.setIsPrimary("0");
		formDefinitionJson1.setFormDefinitionId(formDefinitionJson.getFormDefinitionId());
		formDefinitionJsonService.updatePrimary(formDefinitionJson1);

		formDefinitionJson.setIsPrimary("1");
		formDefinitionJsonService.save(formDefinitionJson);
		return AjaxJson.success("设置主表单成功!");
	}

	/**
	 * 保存流程表单
	 */
	@PostMapping("save")
	public AjaxJson save(FormDefinitionJson formDefinitionJson, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(formDefinitionJson);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		if(StringUtils.isBlank(formDefinitionJson.getId())){//新增
			formDefinitionJson.setVersion(0);
			formDefinitionJson.setIsPrimary("1"); //设置为主版本
		}else {//更新
			FormDefinitionJson old = this.get(formDefinitionJson.getId());
			if("1".equals(old.getStatus())){//已发布, 如果表单已经发布，不可修改，只能发布为新版本
				formDefinitionJson.setId("");//发布新版本
				formDefinitionJson.setVersion(formDefinitionJsonService.getMaxVersion(formDefinitionJson)+1);
			}
			formDefinitionJson.setIsPrimary("1");//设置为主版本

			//其余版本更新为非主版本
			FormDefinitionJson formDefinitionJson1 = new FormDefinitionJson();
			formDefinitionJson1.setIsPrimary("0");
			formDefinitionJson1.setFormDefinitionId(formDefinitionJson.getFormDefinitionId());
			formDefinitionJsonService.updatePrimary(formDefinitionJson1);
		}
		formDefinitionJsonService.save(formDefinitionJson);//保存
		return AjaxJson.success("保存流程表单成功");
	}


	/**
	 * 批量删除流程表单
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			formDefinitionJsonService.delete(formDefinitionJsonService.get(id));
		}
		return AjaxJson.success("删除流程表单成功");
	}

	/**
	 * 导出excel文件
	 */
    @GetMapping("export")
    public AjaxJson exportFile(FormDefinitionJson formDefinitionJson, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "流程表单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FormDefinitionJson> page = formDefinitionJsonService.findPage(new Page<FormDefinitionJson>(request, response, -1), formDefinitionJson);
    		new ExportExcel("流程表单", FormDefinitionJson.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出流程表单记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FormDefinitionJson> list = ei.getDataList(FormDefinitionJson.class);
			for (FormDefinitionJson formDefinitionJson : list){
				try{
					formDefinitionJsonService.save(formDefinitionJson);
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
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "流程表单数据导入模板.xlsx";
    		List<FormDefinitionJson> list = Lists.newArrayList();
    		new ExportExcel("流程表单数据", FormDefinitionJson.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}
