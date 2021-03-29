/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.service;

import java.util.List;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.test.moments.entity.Share;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.moments.entity.Remind;
import com.jeeplus.modules.test.moments.mapper.RemindMapper;

/**
 * 提醒人Service
 * @author lh
 * @version 2021-03-18
 */
@Service
@Transactional(readOnly = true)
public class RemindService extends CrudService<RemindMapper, Remind> {

	public Remind get(String id) {
		return super.get(id);
	}
	
	public List<Remind> findList(Remind remind) {
		return super.findList(remind);
	}
	
	public Page<Remind> findPage(Page<Remind> page, Remind remind) {
		return super.findPage(page, remind);
	}
	
	@Transactional(readOnly = false)
	public void save(Remind remind) {
		super.save(remind);
	}
	
	@Transactional(readOnly = false)
	public void delete(Remind remind) {
		super.delete(remind);
	}

	@Transactional(readOnly = false)
	public void saveList(User user,String share_id) {
		Remind remind = new Remind();
		remind.setName(user.getName());
		remind.setShareId(share_id);
		remind.setUser(user.getId());
		super.save(remind);
	}

	@Transactional(readOnly = false)
	public void deleteByShare(Share share) {
		mapper.deleteByShare(share);
	}

	public List<Remind> findListByShare(Share share){
		return mapper.findListByShare(share);
	};
}