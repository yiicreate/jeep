/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.video.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.video.entity.Live;

import java.util.List;

/**
 * 直播MAPPER接口
 * @author lh
 * @version 2021-03-17
 */
@Mapper
@Repository
public interface LiveMapper extends BaseMapper<Live> {
}