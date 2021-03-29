/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.entity;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.Collections3;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.service.UserService;

/**
 * 垃圾箱
 * @author 刘高峰
 * @version 2020-08-28
 */
public class MailTrash extends DataEntity<MailTrash> {

	private static final long serialVersionUID = 1L;
	private String status;		// 邮件类型
	private User sender;		// 发件人
	private String receiverIds;		// 收件人
	private Date sendtime;		// 发送时间
	private Mail mail;		// 邮件id

	public MailTrash() {
		super();
	}

	public MailTrash(String id){
		super(id);
	}



	@ExcelField(title="发件人", fieldType=User.class, value="sender.name", align=2, sort=2)
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	@ExcelField(title="收件人", align=2, sort=3)
	public String getReceiverIds() {
		return receiverIds;
	}

	public void setReceiverIds(String receiverIds) {
		this.receiverIds = receiverIds;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发送时间", align=2, sort=4)
	public Date getSendtime() {
		return sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	@ExcelField(title="邮件id", fieldType=Mail.class, value="mail.title", align=2, sort=5)
	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	/**
	 * 获取收件人用户Name
	 *
	 * @return
	 */
	public String getReceiverNames() {
		if(StringUtils.isBlank(receiverIds)){
			return "";
		}
		List receiverList = Lists.newArrayList();
		for (String id : StringUtils.split(receiverIds, ",")) {

			receiverList.add(SpringContextHolder.getBean(UserService.class).get(id));
		}
		return Collections3.extractToString(receiverList, "name", ",");
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
