/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.treetable.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.TreeMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.treetable.entity.CarKind;

/**
 * 车系MAPPER接口
 * @author lgf
 * @version 2021-01-05
 */
@Mapper
@Repository
public interface CarKindMapper extends TreeMapper<CarKind> {
	
}