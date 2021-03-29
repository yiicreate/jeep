/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.manytomany.web;

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
import com.jeeplus.modules.test.manytomany.entity.Course;
import com.jeeplus.modules.test.manytomany.service.CourseService;

/**
 * 课程Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/manytomany/course")
public class CourseController extends BaseController {

	@Autowired
	private CourseService courseService;

	@ModelAttribute
	public Course get(@RequestParam(required=false) String id) {
		Course entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = courseService.get(id);
		}
		if (entity == null){
			entity = new Course();
		}
		return entity;
	}

	/**
	 * 课程列表数据
	 */
	@RequiresPermissions("test:manytomany:course:list")
	@GetMapping("list")
	public AjaxJson list(Course course, HttpServletRequest request, HttpServletResponse response) {
		Page<Course> page = courseService.findPage(new Page<Course>(request, response), course);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取课程数据
	 */
	@RequiresPermissions(value={"test:manytomany:course:view","test:manytomany:course:add","test:manytomany:course:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(Course course) {
		return AjaxJson.success().put("course", course);
	}

	/**
	 * 保存课程
	 */
	@RequiresPermissions(value={"test:manytomany:course:add","test:manytomany:course:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(Course course, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(course);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		courseService.save(course);//保存
		return AjaxJson.success("保存课程成功");
	}


	/**
	 * 批量删除课程
	 */
	@RequiresPermissions("test:manytomany:course:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			courseService.delete(new Course(id));
		}
		return AjaxJson.success("删除课程成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:manytomany:course:export")
    @GetMapping("export")
    public AjaxJson exportFile(Course course, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "课程"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Course> page = courseService.findPage(new Page<Course>(request, response, -1), course);
    		new ExportExcel("课程", Course.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出课程记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:manytomany:course:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Course> list = ei.getDataList(Course.class);
			for (Course course : list){
				try{
					courseService.save(course);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条课程记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条课程记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入课程失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入课程数据模板
	 */
	@RequiresPermissions("test:manytomany:course:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "课程数据导入模板.xlsx";
    		List<Course> list = Lists.newArrayList();
    		new ExportExcel("课程数据", Course.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}