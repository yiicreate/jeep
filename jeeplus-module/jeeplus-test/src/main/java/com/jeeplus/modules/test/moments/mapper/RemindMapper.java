/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.mapper;

import com.jeeplus.modules.test.moments.entity.Share;
import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.moments.entity.Remind;

import java.util.List;

/**
 * 提醒人MAPPER接口
 * @author lh
 * @version 2021-03-18
 */
@Mapper
@Repository
public interface RemindMapper extends BaseMapper<Remind> {
    public List<Remind> findListByShare(Share share);

    public int deleteByShare(Share share);
}