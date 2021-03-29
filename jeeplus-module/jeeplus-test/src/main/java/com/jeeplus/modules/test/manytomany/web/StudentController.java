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
import com.jeeplus.modules.test.manytomany.entity.Student;
import com.jeeplus.modules.test.manytomany.service.StudentService;

/**
 * 学生Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/manytomany/student")
public class StudentController extends BaseController {

	@Autowired
	private StudentService studentService;

	@ModelAttribute
	public Student get(@RequestParam(required=false) String id) {
		Student entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = studentService.get(id);
		}
		if (entity == null){
			entity = new Student();
		}
		return entity;
	}

	/**
	 * 学生列表数据
	 */
	@RequiresPermissions("test:manytomany:student:list")
	@GetMapping("list")
	public AjaxJson list(Student student, HttpServletRequest request, HttpServletResponse response) {
		Page<Student> page = studentService.findPage(new Page<Student>(request, response), student);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取学生数据
	 */
	@RequiresPermissions(value={"test:manytomany:student:view","test:manytomany:student:add","test:manytomany:student:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(Student student) {
		return AjaxJson.success().put("student", student);
	}

	/**
	 * 保存学生
	 */
	@RequiresPermissions(value={"test:manytomany:student:add","test:manytomany:student:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(Student student, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(student);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		studentService.save(student);//保存
		return AjaxJson.success("保存学生成功");
	}


	/**
	 * 批量删除学生
	 */
	@RequiresPermissions("test:manytomany:student:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			studentService.delete(new Student(id));
		}
		return AjaxJson.success("删除学生成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:manytomany:student:export")
    @GetMapping("export")
    public AjaxJson exportFile(Student student, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "学生"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Student> page = studentService.findPage(new Page<Student>(request, response, -1), student);
    		new ExportExcel("学生", Student.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出学生记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:manytomany:student:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Student> list = ei.getDataList(Student.class);
			for (Student student : list){
				try{
					studentService.save(student);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条学生记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入学生失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入学生数据模板
	 */
	@RequiresPermissions("test:manytomany:student:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "学生数据导入模板.xlsx";
    		List<Student> list = Lists.newArrayList();
    		new ExportExcel("学生数据", Student.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}