/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.mapper;

import com.jeeplus.modules.committee.entity.CommitteeIntegral;
import com.jeeplus.core.persistence.BaseMapper;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

/**
 * 委员积分MAPPER接口
 * @author tangxin
 * @version 2021-03-20
 */
@Mapper
@Repository
public interface CommitteeIntegralMapper extends BaseMapper<CommitteeIntegral> {
	
}