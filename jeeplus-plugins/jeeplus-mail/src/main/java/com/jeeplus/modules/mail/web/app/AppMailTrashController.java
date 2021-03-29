/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.web.app;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.mail.entity.MailTrash;
import com.jeeplus.modules.mail.service.MailTrashService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 垃圾箱Controller
 * @author 刘高峰
 * @version 2020-08-28
 */
@RestController
@RequestMapping(value = "/app/mail/mailTrash")
public class AppMailTrashController extends BaseController {

	@Autowired
	private MailTrashService mailTrashService;

	@ModelAttribute
	public MailTrash get(@RequestParam(required=false) String id) {
		MailTrash entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mailTrashService.get(id);
		}
		if (entity == null){
			entity = new MailTrash();
		}
		return entity;
	}

	/**
	 * 垃圾邮件列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(MailTrash mailTrash, HttpServletRequest request, HttpServletResponse response) {
		mailTrash.setCreateBy (UserUtils.getUser ());
		Page<MailTrash> page = mailTrashService.findPage(new Page<MailTrash>(request, response), mailTrash);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取邮件数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(MailTrash mailTrash) {
		return AjaxJson.success().put("mailTrash", mailTrash);
	}

	/**
	 * 保存邮件
	 */
	@PostMapping("save")
	public AjaxJson save(MailTrash mailTrash, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(mailTrash);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		mailTrashService.save(mailTrash);//保存
		return AjaxJson.success("保存邮件成功");
	}


	/**
	 * 批量删除垃圾邮件
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mailTrashService.delete(new MailTrash(id));
		}
		return AjaxJson.success("删除邮件成功");
	}



}
