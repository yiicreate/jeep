/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.comm.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.comm.entity.ComDing;

/**
 * 钉钉推送MAPPER接口
 * @author lh
 * @version 2021-03-22
 */
@Mapper
@Repository
public interface ComDingMapper extends BaseMapper<ComDing> {
    public ComDing getBySource(String sourceType,String source);
}