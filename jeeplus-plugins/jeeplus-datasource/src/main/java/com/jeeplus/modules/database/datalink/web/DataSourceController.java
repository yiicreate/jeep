/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datalink.web;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.database.datalink.entity.DataSource;
import com.jeeplus.modules.database.datalink.entity.DataSourceTree;
import com.jeeplus.modules.database.datalink.jdbc.DBPool;
import com.jeeplus.modules.sys.utils.DictUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.jeeplus.modules.database.datalink.service.DataSourceService;

/**
 * 数据库连接Controller
 *
 * @author 刘高峰
 * @version 2018-08-06
 */
@RestController
@RequestMapping(value = "/database/datalink/dataSource")
public class DataSourceController extends BaseController {

    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private DatabaseIdProvider databaseIdProvider;

    @ModelAttribute
    public DataSource get(@RequestParam(value = "id", required = false) String id) {
        DataSource entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = dataSourceService.get(id);
        }
        if (entity == null) {
            entity = new DataSource();
        }
        return entity;
    }

    /**查询实体
     */
    @RequiresPermissions(value = {"database:datalink:dataSource:view", "database:datalink:dataSource:add", "database:datalink:dataSource:edit"}, logical = Logical.OR)
    @RequestMapping(value = "queryById")
    public AjaxJson queryById(DataSource dataSource) {
        return AjaxJson.success().put("dataSource", dataSource);
    }

    @RequiresPermissions("database:datalink:dataSource:list")
    @RequestMapping(value = "list")
    public AjaxJson list(DataSource dataSource, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DataSource> page = dataSourceService.findPage(new Page<DataSource>(request, response), dataSource);
        return AjaxJson.success().put("page", page);
    }

    @RequestMapping(value = "treeData")
    public AjaxJson treeData(HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<DataSource> list = dataSourceService.findList(new DataSource());
        HashSet<String> set = new HashSet();
        for (int i = 0; i < list.size(); i++) {
            DataSource e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("parent", e.getHost());
            map.put("text", e.getName());
            map.put("type", "db");
            mapList.add(map);
            set.add(e.getHost());
        }
        for (String host : set) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", host);
            map.put("parent", "#");
            map.put("text", host);
            map.put("type", "host");
            Map<String, Object> state = Maps.newHashMap();
            state.put("opened", true);
            map.put("state", state);
            mapList.add(map);
        }
        return AjaxJson.success().put("treeData", mapList);
    }

    @RequestMapping(value = "treeData2")
    public AjaxJson treeData2(HttpServletResponse response) {
        List<DataSourceTree> treelist = Lists.newArrayList();
        List<DataSourceTree> treeRoots = Lists.newArrayList();
        String dbType ;
        try{
            dbType = databaseIdProvider.getDatabaseId(SpringContextHolder.getBean(javax.sql.DataSource.class));

        }catch (Exception e){
            dbType = "mysql";
        }
        treeRoots.add(new DataSourceTree("master-parent","默认数据源","0","","host", true));

        treelist.add(new DataSourceTree("master","本地数据库","master-parent","master","db", dbType));

        List<DataSource> list = dataSourceService.findList(new DataSource());
        HashSet<String> set = new HashSet();
        for (int i = 0; i < list.size(); i++) {
            DataSource e = list.get(i);
            treelist.add(new DataSourceTree(e.getId(),e.getName(),e.getHost(),e.getEnName(),"db", e.getType()));
            set.add(e.getHost());
        }
        for (String host : set) {
            treeRoots.add(new DataSourceTree(host,host,"0","","host", true));
        }
        List rootTree = getRootTree(treeRoots,treelist);
        return AjaxJson.success().put("treeData", rootTree);
    }



    private List<DataSourceTree> getRootTree(List<DataSourceTree> rootTrees, List<DataSourceTree> list) {
        List<DataSourceTree> trees = Lists.newArrayList();
        for (DataSourceTree root : rootTrees) {
                trees.add(getChildOfTree(root, list));
        }
        return trees;
    }

    private DataSourceTree getChildOfTree(DataSourceTree area, List<DataSourceTree> areaList) {
        area.setChildren(Lists.newArrayList());
        for (DataSourceTree child : areaList) {
                if (child.getParentId().equals(area.getId())) {
                    area.getChildren().add(getChildOfTree(child, areaList));
                }
        }
        return area;
    }

    /**
     * 保存数据库连接
     */
    @RequiresPermissions(value = {"database:datalink:dataSource:add", "database:datalink:dataSource:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(DataSource dataSource, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(dataSource);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        String oldName = "";
        if(StringUtils.isNotBlank(dataSource.getId())){
             oldName = dataSourceService.get(dataSource.getId()).getEnName();
        }

        String driver = DictUtils.getDictValue(dataSource.getType(), "db_driver", "mysql");
        dataSource.setDriver(driver);
        dataSource.setUrl(dataSourceService.toUrl(dataSource.getType(), dataSource.getHost(), Integer.valueOf(dataSource.getPort()), dataSource.getDbname()));
        //新增或编辑表单保存
        dataSourceService.save(dataSource);//保存
        if(StringUtils.isNotBlank(oldName)){
            DBPool.getInstance().destroy(oldName);
        }
        DBPool.getInstance().create(dataSource);
        j.setSuccess(true);
        j.setMsg("保存数据库连接成功");
        return j;
    }


    /**
     * 批量删除数据库连接
     */
    @RequiresPermissions("database:datalink:dataSource:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(@RequestParam(value = "ids", required = false)String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            DataSource dataSource = dataSourceService.get(id);
            dataSourceService.delete(dataSource);
            DBPool.getInstance().destroy(dataSource.getEnName());
        }
        j.setMsg("删除数据库连接成功");
        return j;
    }




    /**
     * 验证数据库唯一key是否存在
     * @param oldEnName
     * @param enName
     * @return
     */
    @RequestMapping(value = "checkEnName")
    public AjaxJson checkLoginName(@RequestParam(value = "oldEnName", required = false)String oldEnName, @RequestParam(value = "enName", required = false)String enName) {
        if (enName !=null && enName.equals(oldEnName)) {
            return AjaxJson.success();
        } else if (enName !=null && dataSourceService.findUniqueByProperty("enName", enName) == null) {
            return AjaxJson.success();
        }
        return AjaxJson.error("数据库连接英文名已存在");
    }
        /**
         * 测试数据源是否可用
         *
         * @param type     数据库类型
         * @param host
         * @param port
         * @param dbname
         * @param username
         * @param password
         * @return
         */
    @RequestMapping("/test")
    public AjaxJson test(@RequestParam(value = "type", required = false)String type, @RequestParam(value = "host", required = false)String host, @RequestParam(value = "port", required = false)Integer port, @RequestParam(value = "dbname", required = false)String dbname, @RequestParam(value = "username", required = false)String username, @RequestParam(value = "password", required = false)String password) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(type) || StringUtils.isBlank(host) || StringUtils.isBlank(dbname) || StringUtils.isBlank(username)) {
            j.setSuccess(false);
            j.setMsg("配置信息不全");
            return j;
        }
        if (DBPool.getInstance().test(dataSourceService.getDriver(type), dataSourceService.toUrl(type, host, port, dbname), username, password)) {
            j.setSuccess(true);
            j.setMsg("连接成功");
            return j;
        } else {
            j.setSuccess(false);
            j.setMsg("连接失败");
            return j;
        }
    }

}
