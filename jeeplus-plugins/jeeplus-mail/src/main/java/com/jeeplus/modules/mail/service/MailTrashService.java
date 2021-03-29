/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.service;

import java.util.List;

import com.jeeplus.modules.mail.entity.MailCompose;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.mail.entity.MailTrash;
import com.jeeplus.modules.mail.mapper.MailTrashMapper;

/**
 * 邮件Service
 * @author 刘高峰
 * @version 2020-08-28
 */
@Service
@Transactional(readOnly = true)
public class MailTrashService extends CrudService<MailTrashMapper, MailTrash> {

	public MailTrash get(String id) {
		return super.get(id);
	}

	public List<MailTrash> findList(MailTrash mailTrash) {
		return super.findList(mailTrash);
	}

	public Page<MailTrash> findPage(Page<MailTrash> page, MailTrash mailTrash) {
		return super.findPage(page, mailTrash);
	}

	@Transactional(readOnly = false)
	public void save(MailTrash mailTrash) {
		super.save(mailTrash);
	}

	@Transactional(readOnly = false)
	public void delete(MailTrash mailTrash) {
		super.delete(mailTrash);
	}

	public int getCount(MailTrash mailTrash) {
		return mapper.getCount(mailTrash);
	}

}
