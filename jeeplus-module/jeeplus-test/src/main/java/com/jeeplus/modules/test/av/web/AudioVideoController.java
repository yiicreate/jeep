/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.av.web;

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
import com.jeeplus.modules.test.av.entity.AudioVideo;
import com.jeeplus.modules.test.av.service.AudioVideoService;

/**
 * 音频视频Controller
 * @author lc
 * @version 2021-03-19
 */
@RestController
@RequestMapping(value = "/av/audioVideo")
public class AudioVideoController extends BaseController {

	@Autowired
	private AudioVideoService audioVideoService;

	@ModelAttribute
	public AudioVideo get(@RequestParam(required=false) String id) {
		AudioVideo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = audioVideoService.get(id);
		}
		if (entity == null){
			entity = new AudioVideo();
		}
		return entity;
	}

	/**
	 * 音频视频列表数据
	 */
	@RequiresPermissions("av:audioVideo:list")
	@GetMapping("list")
	public AjaxJson list(AudioVideo audioVideo, HttpServletRequest request, HttpServletResponse response) {
		Page<AudioVideo> page = audioVideoService.findPage(new Page<AudioVideo>(request, response), audioVideo);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取音频视频数据
	 */
	@RequiresPermissions(value={"av:audioVideo:view","av:audioVideo:add","av:audioVideo:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(AudioVideo audioVideo) {
		return AjaxJson.success().put("audioVideo", audioVideo);
	}

	/**
	 * 保存音频视频
	 */
	@RequiresPermissions(value={"av:audioVideo:add","av:audioVideo:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(AudioVideo audioVideo, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(audioVideo);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		audioVideoService.save(audioVideo);//保存
		return AjaxJson.success("保存音频视频成功");
	}


	/**
	 * 批量删除音频视频
	 */
	@RequiresPermissions("av:audioVideo:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			audioVideoService.delete(new AudioVideo(id));
		}
		return AjaxJson.success("删除音频视频成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("av:audioVideo:export")
    @GetMapping("export")
    public AjaxJson exportFile(AudioVideo audioVideo, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "音频视频"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AudioVideo> page = audioVideoService.findPage(new Page<AudioVideo>(request, response, -1), audioVideo);
    		new ExportExcel("音频视频", AudioVideo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出音频视频记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("av:audioVideo:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AudioVideo> list = ei.getDataList(AudioVideo.class);
			for (AudioVideo audioVideo : list){
				try{
					audioVideoService.save(audioVideo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条音频视频记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条音频视频记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入音频视频失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入音频视频数据模板
	 */
	@RequiresPermissions("av:audioVideo:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "音频视频数据导入模板.xlsx";
    		List<AudioVideo> list = Lists.newArrayList();
    		new ExportExcel("音频视频数据", AudioVideo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}