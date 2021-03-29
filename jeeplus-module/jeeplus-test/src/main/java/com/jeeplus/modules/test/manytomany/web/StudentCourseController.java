/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.manytomany.web;

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
import com.jeeplus.modules.test.manytomany.entity.StudentCourse;
import com.jeeplus.modules.test.manytomany.service.StudentCourseService;

/**
 * 学生课程记录Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/manytomany/studentCourse")
public class StudentCourseController extends BaseController {

	@Autowired
	private StudentCourseService studentCourseService;

	@ModelAttribute
	public StudentCourse get(@RequestParam(required=false) String id) {
		StudentCourse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = studentCourseService.get(id);
		}
		if (entity == null){
			entity = new StudentCourse();
		}
		return entity;
	}

	/**
	 * 学生课程记录列表数据
	 */
	@RequiresPermissions("test:manytomany:studentCourse:list")
	@GetMapping("list")
	public AjaxJson list(StudentCourse studentCourse, HttpServletRequest request, HttpServletResponse response) {
		Page<StudentCourse> page = studentCourseService.findPage(new Page<StudentCourse>(request, response), studentCourse);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取学生课程记录数据
	 */
	@RequiresPermissions(value={"test:manytomany:studentCourse:view","test:manytomany:studentCourse:add","test:manytomany:studentCourse:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(StudentCourse studentCourse) {
		return AjaxJson.success().put("studentCourse", studentCourse);
	}

	/**
	 * 保存学生课程记录
	 */
	@RequiresPermissions(value={"test:manytomany:studentCourse:add","test:manytomany:studentCourse:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(StudentCourse studentCourse, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(studentCourse);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		studentCourseService.save(studentCourse);//保存
		return AjaxJson.success("保存学生课程记录成功");
	}


	/**
	 * 批量删除学生课程记录
	 */
	@RequiresPermissions("test:manytomany:studentCourse:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			studentCourseService.delete(new StudentCourse(id));
		}
		return AjaxJson.success("删除学生课程记录成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:manytomany:studentCourse:export")
    @GetMapping("export")
    public AjaxJson exportFile(StudentCourse studentCourse, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "学生课程记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<StudentCourse> page = studentCourseService.findPage(new Page<StudentCourse>(request, response, -1), studentCourse);
    		new ExportExcel("学生课程记录", StudentCourse.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出学生课程记录记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:manytomany:studentCourse:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<StudentCourse> list = ei.getDataList(StudentCourse.class);
			for (StudentCourse studentCourse : list){
				try{
					studentCourseService.save(studentCourse);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生课程记录记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条学生课程记录记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入学生课程记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入学生课程记录数据模板
	 */
	@RequiresPermissions("test:manytomany:studentCourse:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "学生课程记录数据导入模板.xlsx";
    		List<StudentCourse> list = Lists.newArrayList();
    		new ExportExcel("学生课程记录数据", StudentCourse.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}