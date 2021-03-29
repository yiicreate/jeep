/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.tree.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.TreeMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.tree.entity.TestTree;

/**
 * 组织机构MAPPER接口
 * @author lgf
 * @version 2021-01-05
 */
@Mapper
@Repository
public interface TestTreeMapper extends TreeMapper<TestTree> {
	
}