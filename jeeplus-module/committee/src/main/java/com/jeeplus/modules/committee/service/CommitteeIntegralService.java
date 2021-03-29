/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.service;

import java.util.List;

import com.jeeplus.modules.committee.entity.CommitteeIntegral;
import com.jeeplus.modules.committee.entity.IntegralDetail;
import com.jeeplus.modules.committee.mapper.CommitteeIntegralMapper;
import com.jeeplus.modules.committee.mapper.IntegralDetailMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jeeplus.common.utils.StringUtils;


/**
 * 委员积分Service
 * @author tangxin
 * @version 2021-03-20
 */
@Service("committeeIntegralService")
@Transactional(readOnly = true)
public class CommitteeIntegralService extends CrudService<CommitteeIntegralMapper, CommitteeIntegral> {

	@Autowired
	private IntegralDetailMapper integralDetailMapper;

	public CommitteeIntegral get(String id) {
		CommitteeIntegral committeeIntegral = super.get(id);
		committeeIntegral.setIntegralDetailList(integralDetailMapper.findList(new IntegralDetail(committeeIntegral)));
		return committeeIntegral;
	}

	public List<CommitteeIntegral> findList(CommitteeIntegral committeeIntegral) {
		return super.findList(committeeIntegral);
	}

	public Page<CommitteeIntegral> findPage(Page<CommitteeIntegral> page, CommitteeIntegral committeeIntegral) {
		page = super.findPage(page, committeeIntegral);
		for(CommitteeIntegral integral:page.getList()){
			User user = UserUtils.get(integral.getCommitteeId());
			integral.setCommittee(user.getLoginName());
			integral.setOffice(user.getOffice().getName());
			integral.setIntegral(integralDetailMapper.countIntegral(integral.getCommitteeId()));
		}
		return page;
	}

	@Transactional(readOnly = false)
	public void save(CommitteeIntegral committeeIntegral) {
		super.save(committeeIntegral);
		for (IntegralDetail integralDetail : committeeIntegral.getIntegralDetailList()){
			if (integralDetail.getId() == null){
				continue;
			}
			if (IntegralDetail.DEL_FLAG_NORMAL.equals(integralDetail.getDelFlag())){
				if (StringUtils.isBlank(integralDetail.getId())){
					integralDetail.setCi(committeeIntegral);
					integralDetail.preInsert();
					integralDetailMapper.insert(integralDetail);
				}else{
					integralDetail.preUpdate();
					integralDetailMapper.update(integralDetail);
				}
			}else{
				integralDetailMapper.delete(integralDetail);
			}
		}
	}


	@Transactional(readOnly = false)
	public void saveDetail(String committeeId,Integer integralType,Integer integral){
		//查询主记录
		CommitteeIntegral committeeIntegral = new CommitteeIntegral();
		committeeIntegral.setCommitteeId(committeeId);
		List<CommitteeIntegral> committeeIntegrals = super.findList(committeeIntegral);
		if(committeeIntegrals.size()>0){
			CommitteeIntegral ci = committeeIntegrals.get(0);
			IntegralDetail qDetail = new IntegralDetail();
			qDetail.setCi(ci);
			qDetail.setIntegralType(integralType);
			List<IntegralDetail> details = integralDetailMapper.findList(qDetail);
			if(details.size()>0){
				IntegralDetail detail = details.get(0);
				detail.setIntegral(detail.getIntegral()+integral);
				integralDetailMapper.update(detail);
			} else {
				IntegralDetail detail = new IntegralDetail();
				detail.setCi(ci);
				detail.setIntegralType(integralType);
				detail.setIntegral(integral);
				detail.preInsert();
				integralDetailMapper.insert(detail);
			}
		} else {
			committeeIntegral.setOrg(UserUtils.get(committeeId).getOffice().getId());
			super.save(committeeIntegral);
			IntegralDetail detail = new IntegralDetail();
			detail.setCi(committeeIntegral);
			detail.setIntegralType(integralType);
			detail.setIntegral(integral);
			detail.preInsert();
			integralDetailMapper.insert(detail);
		}
	}


	@Transactional(readOnly = false)
	public void delete(CommitteeIntegral committeeIntegral) {
		super.delete(committeeIntegral);
		integralDetailMapper.delete(new IntegralDetail(committeeIntegral));
	}

}