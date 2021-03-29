/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.committee.entity.StandingCommittee;

/**
 * 委员相关功能MAPPER接口
 * @author tangxin
 * @version 2021-03-24
 */
@Mapper
@Repository
public interface StandingCommitteeMapper extends BaseMapper<StandingCommittee> {
	
}