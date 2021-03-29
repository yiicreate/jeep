/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.video.service;

import java.util.List;

import com.jeeplus.ding.utils.TimeUtil;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.test.video.entity.Live;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.video.entity.Member;
import com.jeeplus.modules.test.video.mapper.MemberMapper;

/**
 * 参与人Service
 * @author lh
 * @version 2021-03-17
 */
@Service
@Transactional(readOnly = true)
public class MemberService extends CrudService<MemberMapper, Member> {

	public Member get(String id) {
		return super.get(id);
	}
	
	public List<Member> findList(Member member) {
		return super.findList(member);
	}
	
	public Page<Member> findPage(Page<Member> page, Member member) {
		return super.findPage(page, member);
	}
	
	@Transactional(readOnly = false)
	public void save(Member member) {
		super.save(member);
	}
	
	@Transactional(readOnly = false)
	public void delete(Member member) {
		super.delete(member);
	}

	@Transactional(readOnly = false)
	public void saveByUser(User user, Live live){
		Member member = new Member();
		member.setUserId(user.getId());
		member.setUid(user.getUserId());
		member.setIsIn("0");
		member.setLiveId(live.getId());
		member.setLaunch("0");
		if(live.getCreateBy().getId().equals(user.getId())){
			member.setLaunch("1");
		}
		super.save(member);
	}


	public List<Member> findListByLive(Live live) {
		return mapper.findListByLive(live);
	}

	public void deleteByLive(Live live) {
		mapper.deleteByLive(live);
	}
}