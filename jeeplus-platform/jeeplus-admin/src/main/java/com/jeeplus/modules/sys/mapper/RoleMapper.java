/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.modules.sys.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色MAPPER接口
 * @author jeeplus
 * @version 2016-12-05
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

	public Role getByName(Role role);

	public Role getByEnname(Role role);

	/**
	 * 查询角色的所有无下属菜单ID
	 * @param id
	 * @return
	 */
	public List<String> queryAllNotChildrenMenuId(String id);
	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);

	public int insertRoleMenu(Role role);

	/**
	 * 维护角色与数据权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleDataRule(Role role);

	public int insertRoleDataRule(Role role);

}
