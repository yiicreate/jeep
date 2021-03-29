/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.video.service;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.ding.io.DingEvent;
import com.jeeplus.ding.utils.DingUtil;
import com.jeeplus.ding.utils.TimeUtil;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.test.comm.entity.ComDing;
import com.jeeplus.modules.test.comm.service.ComDingService;
import com.jeeplus.modules.test.video.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.video.entity.Live;
import com.jeeplus.modules.test.video.mapper.LiveMapper;

/**
 * 直播Service
 * @author lh
 * @version 2021-03-17
 */
@Service
@Transactional(readOnly = true)
public class LiveService extends CrudService<LiveMapper, Live> {
	public static  final String dingType = "EVENT";
	public static  final String sourceType = "LIVE";

	@Autowired
	private MemberService memberService;
	@Autowired
	private DingUtil dingUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private ComDingService comDingService;

	public Live get(String id) {
		Live live =  super.get(id);
		List<Member> list = memberService.findListByLive(live);
		live.setMembers(list);
		return live;
	}
	
	public List<Live> findList(Live live) {
		return super.findList(live);
	}
	
	public Page<Live> findPage(Page<Live> page, Live live) {
		return super.findPage(page, live);
	}
	
	@Transactional(readOnly = false)
	public void save(Live live) {
		super.save(live);
		if("9".equals(live.getStep())){
			cancelEvent(live);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Live live) {
		super.delete(live);
		memberService.deleteByLive(live);
	}

	/**
	 * 获取当前列表
	 * @param live
	 * @return
	 */
	public Page<Live> findListByCurrentPage(Page<Live> page, Live live){
		dataRuleFilter(live);
		live.setPage(page);
		User user = UserUtils.getUser();
		live.setDataScope(" and a.step != 9 and a.create_by = '"+user.getId()+"'");
		page.setOrderBy("a.step");
		page.setList(mapper.findList(live));
		return page;
	};

	public Map<String,Object> sendEvent(Live live){
		do{
			if(StringUtils.isBlank(live.getPlanStart())||StringUtils.isBlank(live.getPlanEnd())){
				break;
			}
			DingEvent dingEvent = new DingEvent(live.getTitle(),live.getIntroduction(),TimeUtil.getUnixTime(live.getPlanStart()),TimeUtil.getUnixTime(live.getPlanEnd()));
			List<Member> users = live.getMembers();
			if(users == null ||users.size()==0){
				users = this.get(live.getId()).getMembers();
			}
			for (Member u:users) {
				if(!StringUtils.isBlank(u.getUid())){
					dingEvent.setAttendee(u.getUid());
				}
			}
			User user = live.getCreateBy();
			if(StringUtils.isBlank(user.getUserId())){
				user = userService.get(live.getCreateBy());
			}
			dingEvent.setOrganizer(user.getUserId());
			Map<String,Object> map =  dingUtil.sendEvent(dingEvent);
			return map;
		}while (false);
		return null;
	}

	public void cancelEvent(Live live){
		do{
			ComDing comDing = comDingService.getBySource(sourceType,live.getId());
			if(comDing == null){
				break;
			}
			comDing.setDelFlag("1");
			comDingService.save(comDing);
			dingUtil.cancelEvent(comDing.getDingId());
		}while (false);
	}
}