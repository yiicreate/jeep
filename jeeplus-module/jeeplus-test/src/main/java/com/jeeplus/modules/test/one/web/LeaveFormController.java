/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.one.web;

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
import com.jeeplus.modules.test.one.entity.LeaveForm;
import com.jeeplus.modules.test.one.service.LeaveFormService;

/**
 * 请假表单Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/one/leaveForm")
public class LeaveFormController extends BaseController {

	@Autowired
	private LeaveFormService leaveFormService;

	@ModelAttribute
	public LeaveForm get(@RequestParam(required=false) String id) {
		LeaveForm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = leaveFormService.get(id);
		}
		if (entity == null){
			entity = new LeaveForm();
		}
		return entity;
	}

	/**
	 * 请假表单列表数据
	 */
	@RequiresPermissions("test:one:leaveForm:list")
	@GetMapping("list")
	public AjaxJson list(LeaveForm leaveForm, HttpServletRequest request, HttpServletResponse response) {
		Page<LeaveForm> page = leaveFormService.findPage(new Page<LeaveForm>(request, response), leaveForm);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取请假表单数据
	 */
	@RequiresPermissions(value={"test:one:leaveForm:view","test:one:leaveForm:add","test:one:leaveForm:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(LeaveForm leaveForm) {
		return AjaxJson.success().put("leaveForm", leaveForm);
	}

	/**
	 * 保存请假表单
	 */
	@RequiresPermissions(value={"test:one:leaveForm:add","test:one:leaveForm:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(LeaveForm leaveForm, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(leaveForm);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		leaveFormService.save(leaveForm);//保存
		return AjaxJson.success("保存请假表单成功");
	}


	/**
	 * 批量删除请假表单
	 */
	@RequiresPermissions("test:one:leaveForm:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			leaveFormService.delete(new LeaveForm(id));
		}
		return AjaxJson.success("删除请假表单成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:one:leaveForm:export")
    @GetMapping("export")
    public AjaxJson exportFile(LeaveForm leaveForm, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "请假表单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<LeaveForm> page = leaveFormService.findPage(new Page<LeaveForm>(request, response, -1), leaveForm);
    		new ExportExcel("请假表单", LeaveForm.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出请假表单记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:one:leaveForm:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LeaveForm> list = ei.getDataList(LeaveForm.class);
			for (LeaveForm leaveForm : list){
				try{
					leaveFormService.save(leaveForm);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条请假表单记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条请假表单记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入请假表单失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入请假表单数据模板
	 */
	@RequiresPermissions("test:one:leaveForm:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "请假表单数据导入模板.xlsx";
    		List<LeaveForm> list = Lists.newArrayList();
    		new ExportExcel("请假表单数据", LeaveForm.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}