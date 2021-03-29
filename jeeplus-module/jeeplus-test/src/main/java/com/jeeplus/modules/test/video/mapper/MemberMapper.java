/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.video.mapper;

import com.jeeplus.modules.test.video.entity.Live;
import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.video.entity.Member;

import java.util.List;

/**
 * 参与人MAPPER接口
 * @author lh
 * @version 2021-03-17
 */
@Mapper
@Repository
public interface MemberMapper extends BaseMapper<Member> {
    public List<Member> findListByLive(Live live);

    public void deleteByLive(Live live);
}