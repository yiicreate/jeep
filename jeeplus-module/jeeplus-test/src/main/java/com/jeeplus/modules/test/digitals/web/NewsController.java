/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.digitals.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSON;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.comm.service.ComFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import com.jeeplus.modules.test.digitals.entity.News;
import com.jeeplus.modules.test.digitals.service.NewsService;

import static com.jeeplus.modules.test.comm.config.FilesType.NEWS_FILE;

/**
 * 数字化办公-新闻宣传Controller
 * @author lh
 * @version 2021-03-12
 */
@RestController
@RequestMapping(value = "/test/digitals/news")
@Api(tags = "新闻宣传")
public class NewsController extends BaseController {

	@Autowired
	private NewsService newsService;

	@Autowired
	private ComFilesService comFilesService;

	@ModelAttribute
	public News get(@RequestParam(required=false) String id) {
		News entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = newsService.get(id);
		}
		if (entity == null){
			entity = new News();
		}
		return entity;
	}

	/**
	 * 数字化办公-新闻宣传列表数据
	 */
//	@RequiresPermissions("test:digitals:news:list")
	@GetMapping("list")
	@ApiOperation("管理员宣传列表")
	public AjaxJson list(News news, HttpServletRequest request, HttpServletResponse response) {
		Page<News> page = newsService.findPage(new Page<News>(request, response), news);
		return AjaxJson.success().put("page",page);
	}

	@GetMapping("releaseList")
	@ApiOperation("已发布宣传列表")
	public AjaxJson listRelease(News news, HttpServletRequest request, HttpServletResponse response) {
		news.setStep("1");
		Page<News> page = newsService.findPage(new Page<News>(request, response), news);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取数字化办公-新闻宣传数据
	 */
//	@RequiresPermissions(value={"test:digitals:news:view","test:digitals:news:add","test:digitals:news:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	@ApiOperation("新闻详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",value = "新闻主键",required = true),
	})
	public AjaxJson queryById(News news) {
		return AjaxJson.success().put("news", news);
	}

	/**
	 * 保存数字化办公-新闻宣传
	 */
//	@RequiresPermissions(value={"test:digitals:news:add","test:digitals:news:edit"},logical=Logical.OR)
	@PostMapping("save")
	@ApiOperation("新闻保存")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "news",value = "新闻内容相关",required = true),
//			@ApiImplicitParam(name = "files",value = "附件",required = true),
	})
	public AjaxJson save(News news, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(news);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		newsService.save(news);//保存
//		if(!StringUtils.isBlank(files)){
//			List<ComFiles> comFilesList = JSON.parseArray(files, ComFiles.class);
//			comFilesService.save(comFilesList,NEWS_FILE,news.getId());
//		}

		return AjaxJson.success("保存数字化办公-新闻宣传成功");
	}

	/**
	 * 保存数字化办公-新闻宣传
	 */
//	@RequiresPermissions(value={"test:digitals:news:add","test:digitals:news:edit"},logical=Logical.OR)
	@PostMapping("release")
	@ApiOperation("新闻发布OR不发布")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",value = "主键",required = true),
			@ApiImplicitParam(name = "step",value = "发布1，不发布0",required = true),
	})
	public AjaxJson release(News news, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(news);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		newsService.save(news);//保存
		return AjaxJson.success("成功");
	}


	/**
	 * 批量删除数字化办公-新闻宣传
	 */
//	@RequiresPermissions("test:digitals:news:del")
	@DeleteMapping("delete")
	@ApiOperation("新闻删除")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			newsService.delete(new News(id));
		}
		return AjaxJson.success("删除数字化办公-新闻宣传成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:digitals:news:export")
    @GetMapping("export")
    public AjaxJson exportFile(News news, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "数字化办公-新闻宣传"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<News> page = newsService.findPage(new Page<News>(request, response, -1), news);
    		new ExportExcel("数字化办公-新闻宣传", News.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出数字化办公-新闻宣传记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:digitals:news:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<News> list = ei.getDataList(News.class);
			for (News news : list){
				try{
					newsService.save(news);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条数字化办公-新闻宣传记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条数字化办公-新闻宣传记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入数字化办公-新闻宣传失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入数字化办公-新闻宣传数据模板
	 */
	@RequiresPermissions("test:digitals:news:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "数字化办公-新闻宣传数据导入模板.xlsx";
    		List<News> list = Lists.newArrayList();
    		new ExportExcel("数字化办公-新闻宣传数据", News.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}