/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.extension.entity.NodeSetting;

/**
 * 节点设置MAPPER接口
 * @author 刘高峰
 * @version 2021-01-11
 */
@Mapper
@Repository
public interface NodeSettingMapper extends BaseMapper<NodeSetting> {
    public NodeSetting queryByDefIdAndTaskId(@Param("processDefId") String processDefId, @Param("taskDefId") String taskDefId);

    public NodeSetting queryByKey(@Param("processDefId") String processDefId, @Param("taskDefId") String taskDefId ,@Param ("key")String key);

    public void deleteByDefIdAndTaskId(NodeSetting nodeSetting);

    public void deleteByProcessDefId(NodeSetting nodeSetting);

}
