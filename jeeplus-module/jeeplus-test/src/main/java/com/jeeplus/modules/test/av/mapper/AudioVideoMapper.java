/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.av.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.av.entity.AudioVideo;

/**
 * 音频视频MAPPER接口
 * @author lc
 * @version 2021-03-19
 */
@Mapper
@Repository
public interface AudioVideoMapper extends BaseMapper<AudioVideo> {

}