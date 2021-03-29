/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.validation.web;

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
import com.jeeplus.modules.test.validation.entity.TestValidation;
import com.jeeplus.modules.test.validation.service.TestValidationService;

/**
 * 测试校验功能Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/validation/testValidation")
public class TestValidationController extends BaseController {

	@Autowired
	private TestValidationService testValidationService;

	@ModelAttribute
	public TestValidation get(@RequestParam(required=false) String id) {
		TestValidation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testValidationService.get(id);
		}
		if (entity == null){
			entity = new TestValidation();
		}
		return entity;
	}

	/**
	 * 测试校验列表数据
	 */
	@RequiresPermissions("test:validation:testValidation:list")
	@GetMapping("list")
	public AjaxJson list(TestValidation testValidation, HttpServletRequest request, HttpServletResponse response) {
		Page<TestValidation> page = testValidationService.findPage(new Page<TestValidation>(request, response), testValidation);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取测试校验数据
	 */
	@RequiresPermissions(value={"test:validation:testValidation:view","test:validation:testValidation:add","test:validation:testValidation:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(TestValidation testValidation) {
		return AjaxJson.success().put("testValidation", testValidation);
	}

	/**
	 * 保存测试校验
	 */
	@RequiresPermissions(value={"test:validation:testValidation:add","test:validation:testValidation:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(TestValidation testValidation, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testValidation);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		testValidationService.save(testValidation);//保存
		return AjaxJson.success("保存测试校验成功");
	}


	/**
	 * 批量删除测试校验
	 */
	@RequiresPermissions("test:validation:testValidation:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testValidationService.delete(new TestValidation(id));
		}
		return AjaxJson.success("删除测试校验成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:validation:testValidation:export")
    @GetMapping("export")
    public AjaxJson exportFile(TestValidation testValidation, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "测试校验"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestValidation> page = testValidationService.findPage(new Page<TestValidation>(request, response, -1), testValidation);
    		new ExportExcel("测试校验", TestValidation.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出测试校验记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:validation:testValidation:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestValidation> list = ei.getDataList(TestValidation.class);
			for (TestValidation testValidation : list){
				try{
					testValidationService.save(testValidation);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条测试校验记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条测试校验记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入测试校验失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入测试校验数据模板
	 */
	@RequiresPermissions("test:validation:testValidation:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "测试校验数据导入模板.xlsx";
    		List<TestValidation> list = Lists.newArrayList();
    		new ExportExcel("测试校验数据", TestValidation.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}