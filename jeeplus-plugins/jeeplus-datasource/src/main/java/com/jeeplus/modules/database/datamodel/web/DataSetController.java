/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.database.datalink.entity.DataSource;
import com.jeeplus.modules.database.datalink.jdbc.DBPool;
import com.jeeplus.modules.database.datalink.service.DataSourceService;
import com.jeeplus.modules.database.datamodel.entity.DataSet;
import com.jeeplus.modules.database.datamodel.service.DataMetaService;
import com.jeeplus.modules.database.datamodel.service.DataParamService;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.jeeplus.modules.database.datamodel.service.DataSetService;

/**
 * 数据模型Controller
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
@RestController
@RequestMapping(value = "/database/datamodel/dataSet")
public class DataSetController extends BaseController {

    @Autowired
    private DataSetService dataSetService;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private DataMetaService dataMetaService;
    @Autowired
    private DataParamService dataParamService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ModelAttribute
    public DataSet get(@RequestParam(value = "id", required = false) String id) {
        DataSet entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = dataSetService.get(id);
        }
        if (entity == null) {
            entity = new DataSet();
        }
        return entity;
    }


    /**
     * 数据模型列表数据
     */
    @RequiresPermissions("database:datamodel:dataSet:list")
    @GetMapping(value = "list")
    public AjaxJson data(DataSet dataSet, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DataSet> page = dataSetService.findPage(new Page<DataSet>(request, response), dataSet);
        return AjaxJson.success().put("page", page);
    }



    /**
     * 查看，增加，编辑数据模型表单页面
     * params:
     * mode: add, edit, view, 代表三种模式的页面
     */
    @RequiresPermissions(value = {"database:datamodel:dataSet:view", "database:datamodel:dataSet:add", "database:datamodel:dataSet:edit"}, logical = Logical.OR)
    @GetMapping(value = "queryById")
    public AjaxJson queryById(DataSet dataSet) {
        dataSet = dataSetService.detail(dataSet.getId());
        return AjaxJson.success().put("dataSet", dataSet);
    }


    /**
     * 保存数据模型
     */
    @RequiresPermissions(value = {"database:datamodel:dataSet:add", "database:datamodel:dataSet:edit"}, logical = Logical.OR)
    @PostMapping(value = "save")
    public AjaxJson save(DataSet dataSet, Model model) throws Exception {
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(dataSet);
        if (StringUtils.isNotBlank(errMsg)) {
            return AjaxJson.error(errMsg);
        }
        //新增或编辑表单保存
        dataSetService.save(dataSet);//保存
        return AjaxJson.success("保存数据模型成功");
    }


    /**
     * 批量删除数据模型
     */
    @RequiresPermissions("database:datamodel:dataSet:del")
    @DeleteMapping(value = "delete")
    public AjaxJson delete(@RequestParam(value = "ids", required = false)String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        StringBuffer msg = new StringBuffer();
        for (String id : idArray) {
            List <Map<String, Object>>  list  = jdbcTemplate.queryForList("select name from plugin_echarts where model_id = '" + id +"'");
            if(list.size() > 0 ){
                j.setSuccess(false);
                String name = "";
                for(Map<String, Object> map : list){
                    name = name +" ["+map.get("name").toString()+"]";
                }
                msg.append("数据模型 ["+dataSetService.get(id).getName()+"] 已被图表"+name+" 使用，无法删除。请先删除图表，再删除该数据模型。<br/>");
            }else {
                j.setSuccess(true);
                dataMetaService.deleteByDsId(id);
                dataParamService.deleteByDataSetId(id);
                msg.append("删除数据模型 ["+dataSetService.get(id).getName()+"] 成功。<br/>");
                dataSetService.delete(dataSetService.get(id));

            }

        }
        j.setMsg(msg.toString());
        return j;
    }


    /**
     * 执行sql 获取表结构
     */

    @PostMapping(value = "getMeta")
    public AjaxJson query(@RequestParam(value = "db", required = false)String db, @RequestParam(value = "sql", required = false)String sql, @RequestParam(value = "field[]", required = false)String[] field, @RequestParam(value = "defaultValue[]", required = false)String[] defaultValue) throws IOException, SQLException {
        List columnList = new ArrayList();

        DataSource dataSource = dataSourceService.get(db);
        if (dataSource == null) {
            return null;
        }

        JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(dataSource.getEnName());
        sql = dataSetService.mergeSql(sql, field, defaultValue);
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        int count = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= count; i++) {
            Map map = new HashMap();
            map.put("name", rs.getMetaData().getColumnName(i));
            map.put("type", rs.getMetaData().getColumnTypeName(i));
            map.put("label", rs.getMetaData().getColumnLabel(i));
            columnList.add(map);
        }

        return AjaxJson.success().put("columnList", columnList);
    }

    /**
     * 执行sql,预览数据
     *
     * @param sql
     * @return
     */
    @PostMapping(value = "/exec")
    public AjaxJson exec(@RequestParam(value = "db", required = false)String db, @RequestParam(value = "sql", required = false)String sql, @RequestParam(value = "field[]", required = false)String[] field, @RequestParam(value = "defaultValue[]", required = false)String[] defaultValue) {
        AjaxJson j = new AjaxJson();
        try {
            DataSource dataSource = dataSourceService.get(db);
            if (dataSource == null) {
                j.setSuccess(false);
                j.setMsg("数据库链接不存在!");
                return j;
            }

            JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(dataSource.getEnName());
            if (sql.contains("delete") || sql.contains("update")) {
                j.setSuccess(false);
                j.setMsg("只允许查询操作!");
                return j;
            };
            sql = dataSetService.mergeSql(sql, field, defaultValue);

            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            //数据格式转换
            JSONArray data = dataSetService.toJSON(list);

            //返回结果
            j.put("html", dataSetService.toHTML(data));
            j.put("json", list);
            j.put("xml", dataSetService.toXML(data));
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getCause().getLocalizedMessage());
            return j;
        }
    }

    /**
     * 执行sql,预览数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getData/{id}/{type}")
    public AjaxJson getData(@PathVariable("id") String id, @PathVariable("type") String type,  HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        Enumeration<String> names = request.getParameterNames();
        DataSet dataSet = dataSetService.get(id);
        try {
            DataSource dataSource = dataSourceService.get(dataSet.getDb());
            if (dataSource == null) {
                j.setSuccess(false);
                j.setMsg("数据库链接不存在!");
                return j;
            }
            JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(dataSource.getEnName());

            Map paramsMap = dataParamService.getParamsForMap(get(id));
            while(names.hasMoreElements()){
                String param = names.nextElement().toString();
                paramsMap.put(param, request.getParameter(param));
            }

            String sql = dataSetService.mergeSql(get(id).getSqlcmd(), paramsMap);
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            //数据格式转换
            JSONArray data = dataSetService.toJSON(list);

            if("html".equals(type)){
                j.put("result", dataSetService.toHTML(data));
            }else if("xml".equals(type)){
                j.put("result", dataSetService.toXML(data));
            }else {
                j.put("result", list);
            }
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getCause().getLocalizedMessage());
            return j;
        }
    }



}
