/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.shop.web;

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
import com.jeeplus.modules.test.shop.entity.Goods;
import com.jeeplus.modules.test.shop.service.GoodsService;

/**
 * 商品Controller
 * @author liugf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/shop/goods")
public class GoodsController extends BaseController {

	@Autowired
	private GoodsService goodsService;

	@ModelAttribute
	public Goods get(@RequestParam(required=false) String id) {
		Goods entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = goodsService.get(id);
		}
		if (entity == null){
			entity = new Goods();
		}
		return entity;
	}

	/**
	 * 商品列表数据
	 */
	@RequiresPermissions("test:shop:goods:list")
	@GetMapping("list")
	public AjaxJson list(Goods goods, HttpServletRequest request, HttpServletResponse response) {
		Page<Goods> page = goodsService.findPage(new Page<Goods>(request, response), goods);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取商品数据
	 */
	@RequiresPermissions(value={"test:shop:goods:view","test:shop:goods:add","test:shop:goods:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(Goods goods) {
		return AjaxJson.success().put("goods", goods);
	}

	/**
	 * 保存商品
	 */
	@RequiresPermissions(value={"test:shop:goods:add","test:shop:goods:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(Goods goods, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(goods);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		goodsService.save(goods);//保存
		return AjaxJson.success("保存商品成功");
	}


	/**
	 * 批量删除商品
	 */
	@RequiresPermissions("test:shop:goods:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			goodsService.delete(new Goods(id));
		}
		return AjaxJson.success("删除商品成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:shop:goods:export")
    @GetMapping("export")
    public AjaxJson exportFile(Goods goods, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "商品"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Goods> page = goodsService.findPage(new Page<Goods>(request, response, -1), goods);
    		new ExportExcel("商品", Goods.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出商品记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:shop:goods:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Goods> list = ei.getDataList(Goods.class);
			for (Goods goods : list){
				try{
					goodsService.save(goods);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商品记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条商品记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入商品失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入商品数据模板
	 */
	@RequiresPermissions("test:shop:goods:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "商品数据导入模板.xlsx";
    		List<Goods> list = Lists.newArrayList();
    		new ExportExcel("商品数据", Goods.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}