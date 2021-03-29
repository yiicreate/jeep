/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.onetomany.web;

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
import com.jeeplus.modules.test.onetomany.entity.TestDataMainForm;
import com.jeeplus.modules.test.onetomany.service.TestDataMainFormService;

/**
 * 票务代理Controller
 * @author liugf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/onetomany/testDataMainForm")
public class TestDataMainFormController extends BaseController {

	@Autowired
	private TestDataMainFormService testDataMainFormService;

	@ModelAttribute
	public TestDataMainForm get(@RequestParam(required=false) String id) {
		TestDataMainForm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testDataMainFormService.get(id);
		}
		if (entity == null){
			entity = new TestDataMainForm();
		}
		return entity;
	}

	/**
	 * 票务代理列表数据
	 */
	@RequiresPermissions("test:onetomany:testDataMainForm:list")
	@GetMapping("list")
	public AjaxJson list(TestDataMainForm testDataMainForm, HttpServletRequest request, HttpServletResponse response) {
		Page<TestDataMainForm> page = testDataMainFormService.findPage(new Page<TestDataMainForm>(request, response), testDataMainForm);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取票务代理数据
	 */
	@RequiresPermissions(value={"test:onetomany:testDataMainForm:view","test:onetomany:testDataMainForm:add","test:onetomany:testDataMainForm:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(TestDataMainForm testDataMainForm) {
		return AjaxJson.success().put("testDataMainForm", testDataMainForm);
	}

	/**
	 * 保存票务代理
	 */
	@RequiresPermissions(value={"test:onetomany:testDataMainForm:add","test:onetomany:testDataMainForm:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(TestDataMainForm testDataMainForm, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testDataMainForm);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		testDataMainFormService.save(testDataMainForm);//保存
		return AjaxJson.success("保存票务代理成功");
	}


	/**
	 * 批量删除票务代理
	 */
	@RequiresPermissions("test:onetomany:testDataMainForm:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testDataMainFormService.delete(new TestDataMainForm(id));
		}
		return AjaxJson.success("删除票务代理成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:onetomany:testDataMainForm:export")
    @GetMapping("export")
    public AjaxJson exportFile(TestDataMainForm testDataMainForm, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "票务代理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestDataMainForm> page = testDataMainFormService.findPage(new Page<TestDataMainForm>(request, response, -1), testDataMainForm);
    		new ExportExcel("票务代理", TestDataMainForm.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出票务代理记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:onetomany:testDataMainForm:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestDataMainForm> list = ei.getDataList(TestDataMainForm.class);
			for (TestDataMainForm testDataMainForm : list){
				try{
					testDataMainFormService.save(testDataMainForm);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条票务代理记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条票务代理记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入票务代理失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入票务代理数据模板
	 */
	@RequiresPermissions("test:onetomany:testDataMainForm:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "票务代理数据导入模板.xlsx";
    		List<TestDataMainForm> list = Lists.newArrayList();
    		new ExportExcel("票务代理数据", TestDataMainForm.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}