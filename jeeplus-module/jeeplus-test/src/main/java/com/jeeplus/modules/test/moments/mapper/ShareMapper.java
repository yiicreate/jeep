/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.moments.entity.Share;

/**
 * 委员圈MAPPER接口
 * @author lh
 * @version 2021-03-10
 */
@Mapper
@Repository
public interface ShareMapper extends BaseMapper<Share> {
	
}