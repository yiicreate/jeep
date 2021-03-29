/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.test.moments.entity.Share;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.moments.entity.Support;
import com.jeeplus.modules.test.moments.mapper.SupportMapper;

/**
 * 委员圈点赞Service
 * @author lh
 * @version 2021-03-10
 */
@Service
@Transactional(readOnly = true)
public class SupportService extends CrudService<SupportMapper, Support> {

	public Support get(String id) {
		return super.get(id);
	}
	
	public List<Support> findList(Support support) {
		return super.findList(support);
	}
	
	public Page<Support> findPage(Page<Support> page, Support support) {
		return super.findPage(page, support);
	}
	
	@Transactional(readOnly = false)
	public void save(Support support) {
		super.save(support);
	}
	
	@Transactional(readOnly = false)
	public void delete(Support support) {
		super.delete(support);
	}

	@Transactional(readOnly = false)
	public void deleteByShare(Share share) {
		mapper.deleteByShare(share);
	}

	public List<Support> findListByShare(Share share){
		List<Support> supports = mapper.findListByShare(share);
		Map<String,List<Support>> map = new HashMap<>();
		List<Support> list = new ArrayList<>();
		Map<String,List<Support>> map1= new HashMap<>();
		User user = UserUtils.getUser();
		for (Support s:supports) {
			s.setIsCurrent(user.getId().equals(s.getCreateBy())?"1":"0");
			if(s.getType() == 1){
				list.add(s);
				map.put("dz",list);
			}else{
				if(s.getPid().equals("0")){
					List<Support> a = new ArrayList<>();
					a.add(s);
					map1.put(s.getId(),a);
				}else{
					List<Support> b = new ArrayList<>();
					if(!(map1.get(s.getPid())==null)){
						b = map1.get(s.getPid());
					}
					b.add(s);
					map1.put(s.getPid(),b);
				}
			}
		}

		List<Support> list1 = new ArrayList<>();
		if(map.get("dz")!=null){
			list1.addAll(map.get("dz"));
		}
		for (String b: map1.keySet()) {
			list1.addAll(map1.get(b));
		}
		return list1;
	}



	
}