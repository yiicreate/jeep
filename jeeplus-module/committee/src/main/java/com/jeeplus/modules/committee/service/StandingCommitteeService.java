/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.committee.entity.StandingCommittee;
import com.jeeplus.modules.committee.mapper.StandingCommitteeMapper;
import com.jeeplus.modules.committee.entity.CommitteeDetail;
import com.jeeplus.modules.committee.mapper.CommitteeDetailMapper;

/**
 * 委员相关功能Service
 * @author tangxin
 * @version 2021-03-24
 */
@Service
@Transactional(readOnly = true)
public class StandingCommitteeService extends CrudService<StandingCommitteeMapper, StandingCommittee> {

	@Autowired
	private CommitteeDetailMapper committeeDetailMapper;

	public StandingCommittee get(String id) {
		StandingCommittee standingCommittee = super.get(id);
		standingCommittee.setCommitteeDetailList(committeeDetailMapper.findList(new CommitteeDetail(standingCommittee)));
		return standingCommittee;
	}

	public List<StandingCommittee> findList(StandingCommittee standingCommittee) {
		return super.findList(standingCommittee);
	}

	public Page<StandingCommittee> findPage(Page<StandingCommittee> page, StandingCommittee standingCommittee) {
		return super.findPage(page, standingCommittee);
	}

	@Transactional(readOnly = false)
	public void save(StandingCommittee standingCommittee) {
		super.save(standingCommittee);
		for (CommitteeDetail committeeDetail : standingCommittee.getCommitteeDetailList()){
			if (committeeDetail.getId() == null){
				continue;
			}
			if (CommitteeDetail.DEL_FLAG_NORMAL.equals(committeeDetail.getDelFlag())){
				if (StringUtils.isBlank(committeeDetail.getId())){
					committeeDetail.setStanding(standingCommittee);
					committeeDetail.preInsert();
					committeeDetailMapper.insert(committeeDetail);
				}else{
					committeeDetail.preUpdate();
					committeeDetailMapper.update(committeeDetail);
				}
			}else{
				committeeDetailMapper.delete(committeeDetail);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(StandingCommittee standingCommittee) {
		super.delete(standingCommittee);
		committeeDetailMapper.delete(new CommitteeDetail(standingCommittee));
	}

}