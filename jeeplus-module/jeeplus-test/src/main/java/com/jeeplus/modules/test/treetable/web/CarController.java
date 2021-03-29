/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.treetable.web;

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
import com.jeeplus.modules.test.treetable.entity.Car;
import com.jeeplus.modules.test.treetable.service.CarService;

/**
 * 车辆Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/treetable/car")
public class CarController extends BaseController {

	@Autowired
	private CarService carService;
	
	@ModelAttribute
	public Car get(@RequestParam(required=false) String id) {
		Car entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = carService.get(id);
		}
		if (entity == null){
			entity = new Car();
		}
		return entity;
	}
	
	/**
	 * 车辆列表数据
	 */
	@RequiresPermissions("test:treetable:car:list")
	@GetMapping("list")
	public AjaxJson list(Car car, HttpServletRequest request, HttpServletResponse response) {
		Page<Car> page = carService.findPage(new Page<Car>(request, response), car);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取车辆数据
	 */
	@RequiresPermissions(value={"test:treetable:car:view","test:treetable:car:add","test:treetable:car:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(Car car) {
		return AjaxJson.success().put("car", car);
	}

	/**
	 * 保存车辆
	 */
	@RequiresPermissions(value={"test:treetable:car:add","test:treetable:car:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(Car car, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(car);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		carService.save(car);//保存
		return AjaxJson.success("保存车辆成功");
	}


	/**
	 * 批量删除车辆
	 */
	@RequiresPermissions("test:treetable:car:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			carService.delete(new Car(id));
		}
		return AjaxJson.success("删除车辆成功");
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:treetable:car:export")
    @GetMapping("export")
    public AjaxJson exportFile(Car car, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "车辆"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Car> page = carService.findPage(new Page<Car>(request, response, -1), car);
    		new ExportExcel("车辆", Car.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出车辆记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:treetable:car:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Car> list = ei.getDataList(Car.class);
			for (Car car : list){
				try{
					carService.save(car);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车辆记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条车辆记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入车辆失败！失败信息："+e.getMessage());
		}
    }
	
	/**
	 * 下载导入车辆数据模板
	 */
	@RequiresPermissions("test:treetable:car:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "车辆数据导入模板.xlsx";
    		List<Car> list = Lists.newArrayList();
    		new ExportExcel("车辆数据", Car.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}