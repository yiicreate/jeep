/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.grid.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.jeeplus.modules.test.grid.entity.TestCountry;
import com.jeeplus.modules.test.grid.service.TestCountryService;

/**
 * 国家Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/grid/testCountry")
public class TestCountryController extends BaseController {

	@Autowired
	private TestCountryService testCountryService;

	@ModelAttribute
	public TestCountry get(@RequestParam(required=false) String id) {
		TestCountry entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testCountryService.get(id);
		}
		if (entity == null){
			entity = new TestCountry();
		}
		return entity;
	}

	/**
	 * 国家列表数据
	 */
	@RequiresPermissions("test:grid:testCountry:list")
	@GetMapping("list")
	public AjaxJson list(TestCountry testCountry, HttpServletRequest request, HttpServletResponse response) {
		Page<TestCountry> page = testCountryService.findPage(new Page<TestCountry>(request, response), testCountry);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取国家数据
	 */
	@RequiresPermissions(value={"test:grid:testCountry:view","test:grid:testCountry:add","test:grid:testCountry:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(TestCountry testCountry) {
		return AjaxJson.success().put("testCountry", testCountry);
	}

	/**
	 * 保存国家
	 */
	@RequiresPermissions(value={"test:grid:testCountry:add","test:grid:testCountry:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(TestCountry testCountry, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testCountry);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		testCountryService.save(testCountry);//保存
		return AjaxJson.success("保存国家成功");
	}


	/**
	 * 批量删除国家
	 */
	@RequiresPermissions("test:grid:testCountry:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testCountryService.delete(new TestCountry(id));
		}
		return AjaxJson.success("删除国家成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:grid:testCountry:export")
    @GetMapping("export")
    public AjaxJson exportFile(TestCountry testCountry, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "国家"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestCountry> page = testCountryService.findPage(new Page<TestCountry>(request, response, -1), testCountry);
    		new ExportExcel("国家", TestCountry.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出国家记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:grid:testCountry:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestCountry> list = ei.getDataList(TestCountry.class);
			for (TestCountry testCountry : list){
				try{
					testCountryService.save(testCountry);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条国家记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条国家记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入国家失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入国家数据模板
	 */
	@RequiresPermissions("test:grid:testCountry:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "国家数据导入模板.xlsx";
    		List<TestCountry> list = Lists.newArrayList();
    		new ExportExcel("国家数据", TestCountry.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}