/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.mapper;

import com.jeeplus.modules.test.moments.entity.Share;
import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.moments.entity.Support;

import java.util.List;

/**
 * 委员圈点赞MAPPER接口
 * @author lh
 * @version 2021-03-10
 */
@Mapper
@Repository
public interface SupportMapper extends BaseMapper<Support> {
    public List<Support> findListByShare(Share share);

    public int deleteByShare(Share share);
}