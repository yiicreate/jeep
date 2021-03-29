/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import java.util.List;

import com.jeeplus.core.persistence.TreeMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.modules.sys.entity.Menu;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 菜单MAPPER接口
 * @author jeeplus
 * @version 2017-05-16
 */
@Mapper
@Repository
public interface MenuMapper extends TreeMapper<Menu> {

	public List<Menu> findByUserId(Menu menu);

	public void deleteMenuRole(@Param("menu_id") String menu_id);

	public void deleteMenuDataRule(@Param("menu_id") String menu_id);

	public List<Menu> findAllDataRuleList(Menu menu);

}
