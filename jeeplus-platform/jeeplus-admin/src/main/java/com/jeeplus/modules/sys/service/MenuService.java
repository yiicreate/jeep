/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.core.service.TreeService;
import com.jeeplus.modules.sys.entity.DataRule;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.mapper.MenuMapper;
import com.jeeplus.modules.sys.utils.LogUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 菜单.
 *
 * @author jeeplus
 * @version 2016-12-05
 */
@Service
@Transactional(readOnly = true)
public class MenuService extends TreeService<MenuMapper, Menu> {

    @Autowired
    private DataRuleService dataRuleService;

    public Menu get(String id) {
        return super.get(id);
    }

    public List<Menu> findAllMenu() {
        return UserUtils.getMenuList();
    }

    public List<Menu> getChildren(String parentId) {
        return super.getChildren(parentId);
    }

    @Transactional(readOnly = false)
    public void saveMenu(Menu menu) {
        // 获取父节点实体
        menu.setParent(this.get(menu.getParent().getId()));
        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = menu.getParentIds();
        // 设置新的父节点串
        menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");
        // 保存或更新实体
        super.save(menu);
        // 更新子节点 parentIds
        Menu m = new Menu();
        m.setParentIds("%," + menu.getId() + ",%");
        List<Menu> list = mapper.findByParentIdsLike(m);
        for (Menu e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
            mapper.updateParentIds(e);
        }
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
        UserUtils.removeCache(UserUtils.CACHE_TOP_MENU);
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }

    @Transactional(readOnly = false)
    public void updateSort(Menu menu) {
        mapper.updateSort(menu);
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
        UserUtils.removeCache(UserUtils.CACHE_TOP_MENU);
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }

    @Transactional(readOnly = false)
    public void delete(Menu menu) {
        // 解除菜单角色关联
        List<Map<String, Object>> mrlist = mapper.execSelectSql(
                "SELECT distinct a.menu_id as id FROM sys_role_menu a left join sys_menu menu on a.menu_id = menu.id WHERE a.menu_id ='"
                        + menu.getId() + "' OR menu.parent_ids LIKE  '%," + menu.getId() + ",%'");
        for (Map<String, Object> mr : mrlist) {
            mapper.deleteMenuRole(mr.get("id").toString());
        }
        // 删除菜单关联的数据权限数据，以及解除角色数据权限关联
        List<Map<String, Object>> mdlist = mapper.execSelectSql(
                "SELECT distinct a.id as id FROM sys_datarule a left join sys_menu menu on a.menu_id = menu.id WHERE a.menu_id ='"
                        + menu.getId() + "' OR menu.parent_ids LIKE  '%," + menu.getId() + ",%'");
        for (Map<String, Object> md : mdlist) {
            DataRule dataRule = new DataRule(md.get("id").toString());
            dataRuleService.delete(dataRule);
        }

        mapper.delete(menu);
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_TOP_MENU);
        UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }


}
