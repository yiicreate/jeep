/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.pic.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.pic.entity.TestPic;

/**
 * 图片管理MAPPER接口
 * @author lgf
 * @version 2021-01-05
 */
@Mapper
@Repository
public interface TestPicMapper extends BaseMapper<TestPic> {

}