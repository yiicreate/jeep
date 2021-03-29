/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.grid.web;

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
import com.jeeplus.modules.test.grid.entity.TestContinent;
import com.jeeplus.modules.test.grid.service.TestContinentService;

/**
 * 洲Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/grid/testContinent")
public class TestContinentController extends BaseController {

	@Autowired
	private TestContinentService testContinentService;

	@ModelAttribute
	public TestContinent get(@RequestParam(required=false) String id) {
		TestContinent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testContinentService.get(id);
		}
		if (entity == null){
			entity = new TestContinent();
		}
		return entity;
	}

	/**
	 * 洲列表数据
	 */
	@RequiresPermissions("test:grid:testContinent:list")
	@GetMapping("list")
	public AjaxJson list(TestContinent testContinent, HttpServletRequest request, HttpServletResponse response) {
		Page<TestContinent> page = testContinentService.findPage(new Page<TestContinent>(request, response), testContinent);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取洲数据
	 */
	@RequiresPermissions(value={"test:grid:testContinent:view","test:grid:testContinent:add","test:grid:testContinent:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(TestContinent testContinent) {
		return AjaxJson.success().put("testContinent", testContinent);
	}

	/**
	 * 保存洲
	 */
	@RequiresPermissions(value={"test:grid:testContinent:add","test:grid:testContinent:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(TestContinent testContinent, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testContinent);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		testContinentService.save(testContinent);//保存
		return AjaxJson.success("保存洲成功");
	}


	/**
	 * 批量删除洲
	 */
	@RequiresPermissions("test:grid:testContinent:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testContinentService.delete(new TestContinent(id));
		}
		return AjaxJson.success("删除洲成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:grid:testContinent:export")
    @GetMapping("export")
    public AjaxJson exportFile(TestContinent testContinent, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "洲"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestContinent> page = testContinentService.findPage(new Page<TestContinent>(request, response, -1), testContinent);
    		new ExportExcel("洲", TestContinent.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出洲记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:grid:testContinent:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestContinent> list = ei.getDataList(TestContinent.class);
			for (TestContinent testContinent : list){
				try{
					testContinentService.save(testContinent);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条洲记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条洲记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入洲失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入洲数据模板
	 */
	@RequiresPermissions("test:grid:testContinent:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "洲数据导入模板.xlsx";
    		List<TestContinent> list = Lists.newArrayList();
    		new ExportExcel("洲数据", TestContinent.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}