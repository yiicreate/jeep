/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.test.moments.entity.Share;
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
import com.jeeplus.modules.test.moments.entity.Support;
import com.jeeplus.modules.test.moments.service.SupportService;

/**
 * 委员圈点赞Controller
 * @author lh
 * @version 2021-03-10
 */
@RestController
@RequestMapping(value = "/test/moments/support")
@Api(tags = "委员圈")
public class SupportController extends BaseController {

	@Autowired
	private SupportService supportService;

	@ModelAttribute
	public Support get(@RequestParam(required=false) String id) {
		Support entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supportService.get(id);
		}
		if (entity == null){
			entity = new Support();
		}
		return entity;
	}

	/**
	 * 委员圈点赞列表数据
	 */
	@RequiresPermissions("test:moments:support:list")
	@GetMapping("list")
	public AjaxJson list(Support support, HttpServletRequest request, HttpServletResponse response) {
		Page<Support> page = supportService.findPage(new Page<Support>(request, response), support);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取委员圈点赞数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(Support support) {
		return AjaxJson.success().put("support", support);
	}

	/**
	 * 保存委员圈点赞
	 */
	@PostMapping("save")
	@ApiOperation(value = "委员圈点赞评论")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "shareId", value = "委员圈主表id", required = true),
			@ApiImplicitParam(name = "pid", value = "点赞传0，评论传0，回复传评论的主id", required = true),
			@ApiImplicitParam(name = "type", value = "点赞传1，评论传2", required = true),
			@ApiImplicitParam(name = "content", value = "内容")
	})
	public AjaxJson save(Support support, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(support);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		supportService.save(support);//保存
		return AjaxJson.success("保存委员圈点赞成功");
	}


	/**
	 * 批量删除委员圈点赞
	 */
	@DeleteMapping("delete")
	@ApiOperation(value = "删除点赞评论")
	public AjaxJson delete(String ids) {
		Support support = supportService.get(ids);
		if(!support.getCurrentUser().getId().equals(support.getCreateBy().getId())){
			return AjaxJson.error("没有删除权限");
		}
		supportService.delete(support);
		return AjaxJson.success("删除委员圈点赞成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:moments:support:export")
    @GetMapping("export")
    public AjaxJson exportFile(Support support, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "委员圈点赞"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Support> page = supportService.findPage(new Page<Support>(request, response, -1), support);
    		new ExportExcel("委员圈点赞", Support.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出委员圈点赞记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:moments:support:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Support> list = ei.getDataList(Support.class);
			for (Support support : list){
				try{
					supportService.save(support);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条委员圈点赞记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条委员圈点赞记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入委员圈点赞失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入委员圈点赞数据模板
	 */
	@RequiresPermissions("test:moments:support:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "委员圈点赞数据导入模板.xlsx";
    		List<Support> list = Lists.newArrayList();
    		new ExportExcel("委员圈点赞数据", Support.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}