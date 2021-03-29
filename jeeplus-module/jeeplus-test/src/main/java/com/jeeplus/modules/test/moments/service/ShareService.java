/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.comm.service.ComFilesService;
import com.jeeplus.modules.test.moments.entity.Remind;
import com.jeeplus.modules.test.moments.entity.Support;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.moments.entity.Share;
import com.jeeplus.modules.test.moments.mapper.ShareMapper;

import static com.jeeplus.modules.test.comm.config.FilesType.SHARE_FILE;

/**
 * 委员圈Service
 * @author lh
 * @version 2021-03-10
 */
@Service
@Transactional(readOnly = true)
public class ShareService extends CrudService<ShareMapper, Share> {

	@Autowired
	private SupportService supportService;

	@Autowired
	private ComFilesService comFilesService;

	@Autowired
	private RemindService remindService;

	@Autowired
	private UserService userService;

	public Share get(String id) {
		Share share = super.get(id);
		return share;
	}

	public Share getAndSup(String id) {
		Share share = super.get(id);
		List<Support> supports = supportService.findListByShare(share);
		User user = UserUtils.getUser();
		share.setIsCurrent(user.getId().equals(share.getCreateBy())?"1":"0");
		share.setSupports(supports);
		share.setComFiles(comFilesService.findListBySourceAOwnerId(new ComFiles(share.getId(),SHARE_FILE)));
		share.setReminds(remindService.findListByShare(share));
		if(!StringUtils.isBlank(share.getInvis())){
			share.setInvisUsers(userService.findListByIds(share.getInvis()));
		}
		if(!StringUtils.isBlank(share.getVis())&&!share.getVis().equals("all")){
			share.setVisUsers(userService.findListByIds(share.getVis()));
		}

		return share;
	}

	public Page<Share> findPageBySup(Page<Share> page, Share share) {
		User user = UserUtils.getUser();
		share.setDataScope(" and ((FIND_IN_SET( "+user.getId()+", a.vis ) AND a.invis IS NULL )  OR a.vis = 'all' " +
				"OR ( ! FIND_IN_SET( "+user.getId()+", a.invis ) AND a.vis IS NULL ))");
		Page<Share> page1 = super.findPage(page, share);
		List<Share> list = page1.getList();
		for (Share s: list) {
			s.setIsCurrent(user.getId().equals(s.getCreateBy())?"1":"0");
			s.setSupports(supportService.findListByShare(s));
			s.setComFiles(comFilesService.findListBySourceAOwnerId(new ComFiles(s.getId(),SHARE_FILE)));
			s.setReminds(remindService.findListByShare(s));
		}
		return page1;
	}

	public List<Share> findList(Share share) {
		return super.findList(share);
	}

	public Page<Share> findPage(Page<Share> page, Share share) {
		return super.findPage(page, share);
	}

	@Transactional(readOnly = false)
	public void save(Share share) {
		super.save(share);
	}

	@Transactional(readOnly = false)
	public void delete(Share share) {
		super.delete(share);
		supportService.deleteByShare(share);
		ComFiles comFiles = new ComFiles(share.getId(),SHARE_FILE);
		comFilesService.deleteBySource(comFiles);
	}
}