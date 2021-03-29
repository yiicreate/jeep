/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.books.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.books.entity.ListenRead;

/**
 * 在线听书看书MAPPER接口
 * @author lc
 * @version 2021-03-16
 */
@Mapper
@Repository
public interface ListenReadMapper extends BaseMapper<ListenRead> {

}