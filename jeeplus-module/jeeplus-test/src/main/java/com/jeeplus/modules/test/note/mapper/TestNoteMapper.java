/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.note.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.note.entity.TestNote;

/**
 * 富文本测试MAPPER接口
 * @author liugf
 * @version 2021-01-05
 */
@Mapper
@Repository
public interface TestNoteMapper extends BaseMapper<TestNote> {

}