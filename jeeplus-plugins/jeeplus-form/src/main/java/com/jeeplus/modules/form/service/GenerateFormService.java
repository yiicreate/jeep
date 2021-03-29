/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.form.service;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.persistence.dialect.Dialect;
import com.jeeplus.core.persistence.interceptor.SQLHelper;
import com.jeeplus.database.datasource.annotation.DS;
import com.jeeplus.modules.database.datalink.jdbc.DBPool;
import com.jeeplus.modules.form.dto.Column;
import com.jeeplus.modules.form.dto.Relation;
import com.jeeplus.modules.form.entity.Form;
import com.jeeplus.modules.form.utils.FormJsonUtils;
import com.jeeplus.modules.form.utils.FormOracleTableBuilder;
import com.jeeplus.modules.form.utils.FormTableBuilder;
import com.jeeplus.modules.sys.entity.DataRule;
import com.jeeplus.modules.sys.service.AreaService;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据表单Service
 *
 * @author 刘高峰
 * @version 2019-12-24
 */
@Service
@Transactional(readOnly = true)
public class GenerateFormService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AreaService areaService;

    /**
     * 更新表单
     *
     * @param data
     * @return
     */
    @Transactional(readOnly = false)
    @DS("#form.dataSource.enName")
    public void executeUpdate(Form form, JSONObject data, HashMap map) {
        //保存表单数据
        FormTableBuilder formTableBuilder;
        String enName = form.getDataSource().getEnName();
        String dbType = FormTableBuilder.getJdbcType(enName);
        JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(enName);
        if (dbType.equals("oracle")) {
            formTableBuilder = new FormOracleTableBuilder(jdbcTemplate, form.getTableName());
        } else {
            formTableBuilder = new FormTableBuilder(jdbcTemplate, form.getTableName());
        }
        List<Relation> relations = FormJsonUtils.newInstance().getRelations(form.getSource ());
        if(relations.size() == 0) { // 如果是单表
            formTableBuilder.executeUpdate(form.getTableName(), data, map);
        }else{//如果是多表
            // 主表
            Map mainMap = new HashMap();
            Map mainData = new HashMap();
            for (Object name : data.keySet()) {
                if (!name.toString().contains("->")) {
                    mainMap.put(name, map.get(name));
                    mainData.put(name, data.get(name));
                }

            }
            formTableBuilder.executeUpdate(form.getTableName(), mainData, mainMap);
            // 多表关联
            for (Relation relation : relations) {
                if("table".equals(relation.getType())){
                    continue;
                }
                Map childMap = new HashMap();
                Map dataMap = new HashMap();
                for (Object name : data.keySet()) {
                    if (name.toString().startsWith(relation.getChildTableName() + "->")) {
                        childMap.put(name.toString().substring((relation.getChildTableName() + "->").length()), map.get(name));
                        dataMap.put(name.toString().substring((relation.getChildTableName() + "->").length()), data.get(name));
                    }
                }
                String foreignKey = relation.getForeignKey().split("\\.")[1];
                childMap.put(foreignKey, "input");
                dataMap.put(foreignKey, data.get(relation.getPrimaryKey()));

                int count = formTableBuilder.executeQueryCountByForeignKey(relation.getChildTableName(), foreignKey, dataMap.get(foreignKey).toString());
                if(count == 0){
                    formTableBuilder.executeInsert(relation.getChildTableName(), dataMap, childMap);
                }else {
                    formTableBuilder.executeUpdateByForeignKey(relation.getChildTableName(), dataMap, childMap, foreignKey);
                }
            }



            // 子表
            for (Relation relation : relations) {
                if("table".equals(relation.getType())){
                    String foreignKey = relation.getForeignKey().split("\\.")[1];
                    for (Object name : data.keySet()) {
                        if (name.toString().equals("childTable->"+relation.getChildTableName())) {
                            JSONArray childArray = data.getJSONArray("childTable->" + relation.getChildTableName());
                            List<Column> fieldArra = FormJsonUtils.newInstance().getFieldsByTable(form.getSource (), "childTable->"+relation.getChildTableName());
                            Map childMap = new HashMap();
                            for(Column column: fieldArra){
                                childMap.put(column.getModel().split("->")[1], column.getType());
                            }
                            // 统一设置外键
                            childMap.put(foreignKey, "input");
                            String foreignValue = data.get(relation.getPrimaryKey()).toString();

                            // 删除所有数据
                            formTableBuilder.executeDeleteByForeignKey(relation.getChildTableName(), foreignValue, foreignKey);

                            // 逐条插入子表
                            for(int i=0; i<childArray.size(); i++){
                                JSONObject columnData = childArray.getJSONObject(i);
                                Map dataMap = new HashMap();
                                dataMap.put(foreignKey, foreignValue);
                                for(Column column: fieldArra){
                                    dataMap.put(column.getModel().split("->")[1], columnData.get(column.getModel()));
                                }
                                formTableBuilder.executeInsert(relation.getChildTableName(), dataMap, childMap);
                            }

                        }
                    }
                }
            }

        }
    }

    /**
     * 保存表单
     *
     * @param data
     * @return
     */
    @Transactional(readOnly = false)
    @DS("#form.dataSource.enName")
    public void executeInsert(Form form, JSONObject data, Map map) {
        //保存表单数据
        FormTableBuilder formTableBuilder;
        String enName = form.getDataSource().getEnName();
        String dbType = FormTableBuilder.getJdbcType(enName);
        JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(enName);
        if (dbType.equals("oracle")) {
            formTableBuilder = new FormOracleTableBuilder(jdbcTemplate, form.getTableName());
        } else {
            formTableBuilder = new FormTableBuilder(jdbcTemplate, form.getTableName());
        }

        List<Relation> relations = FormJsonUtils.newInstance().getRelations(form.getSource ());
        if(relations.size() == 0){ // 如果是单表
            for (Object name : data.keySet()) {
                if (map.get(name) == null)
                    continue;
            }
            formTableBuilder.executeInsert(form.getTableName(), data, map);
        }else { // 如果是多表关联
            // 主表
            Map mainMap = new HashMap();
            Map mainData = new HashMap();
            for (Object name : data.keySet()) {
                if (!name.toString().contains("->")) {
                    mainMap.put(name, map.get(name));
                    mainData.put(name, data.get(name));
                }
            }
            String id = formTableBuilder.executeInsert(form.getTableName(), mainData, mainMap);
            // 多表关联
            for (Relation relation : relations) {
                if("table".equals(relation.getType())){// 排除子表
                    continue;
                }
                Map childMap = new HashMap();
                Map dataMap = new HashMap();
                for (Object name : data.keySet()) {
                    if (name.toString().startsWith(relation.getChildTableName() + "->")) {
                        childMap.put(name.toString().substring((relation.getChildTableName() + "->").length()), map.get(name));
                        dataMap.put(name.toString().substring((relation.getChildTableName() + "->").length()), data.get(name));
                    }
                }
                String foreignKey = relation.getForeignKey().split("\\.")[1];
                if (relation.getPrimaryKey().equals("id")) {
                    childMap.put(foreignKey, "input");
                    dataMap.put(foreignKey, id);
                } else {
                    childMap.put(foreignKey, "input");
                    dataMap.put(foreignKey, data.get(relation.getPrimaryKey()));
                }
                formTableBuilder.executeInsert(relation.getChildTableName(), dataMap, childMap);
            }

            // 子表
            for (Relation relation : relations) {
                if("table".equals(relation.getType())){
                    String foreignKey = relation.getForeignKey().split("\\.")[1];
                    for (Object name : data.keySet()) {
                        if (name.toString().equals("childTable->"+relation.getChildTableName())) {
                            JSONArray childArray = data.getJSONArray("childTable->" + relation.getChildTableName());
                            List<Column> fieldArra = FormJsonUtils.newInstance().getFieldsByTable(form.getSource (), "childTable->"+relation.getChildTableName());
                            Map childMap = new HashMap();
                            for(Column column: fieldArra){
                                childMap.put(column.getModel().split("->")[1], column.getType());
                            }
                            String foreignValue;
                            // 统一设置外键
                            if (relation.getPrimaryKey().equals("id")) {
                                childMap.put(foreignKey, "input");
                                foreignValue = id;
                            } else {
                                childMap.put(foreignKey, "input");
                                foreignValue = data.get(relation.getPrimaryKey()).toString();
                            }

                            // 逐条插入子表
                            for(int i=0; i<childArray.size(); i++){
                                JSONObject columnData = childArray.getJSONObject(i);
                                Map dataMap = new HashMap();
                                dataMap.put(foreignKey, foreignValue);
                                for(Column column: fieldArra){
                                    dataMap.put(column.getModel().split("->")[1], columnData.get(column.getModel()));
                                }
                                formTableBuilder.executeInsert(relation.getChildTableName(), dataMap, childMap);
                            }

                        }
                    }
                }
            }
        }


    }


    /**
     * 删除表单内容
     *
     * @return
     */
    @Transactional(readOnly = false)
    @DS("#form.dataSource.enName")
    public void executeDelete(Form form, String ids) {
        FormTableBuilder formTableBuilder;
        String enName = form.getDataSource().getEnName();
        String dbType = FormTableBuilder.getJdbcType(enName);
        JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(enName);
        if (dbType.equals("oracle")) {
            formTableBuilder = new FormOracleTableBuilder(jdbcTemplate, form.getTableName());
        } else {
            formTableBuilder = new FormTableBuilder(jdbcTemplate, form.getTableName());
        }
        List<Relation> relations = FormJsonUtils.newInstance().getRelations(form.getSource ());
        if(relations.size() == 0){ // 如果是单表
            formTableBuilder.executeDelete(form.getTableName(), ids);
        }else { // 如果是多表关联
            // 子表
            for (Relation relation : relations) {
                String keys = "";
                if(!relation.getPrimaryKey().equalsIgnoreCase("id")){
                    String querySql = "select "+relation.getPrimaryKey()+ " from " + form.getTableName() + " where id in ( ";
                    String idArra = "";
                    for (String id : ids.split(",")) {
                        idArra = idArra + "'" + id + "',";
                    }
                    idArra = idArra.substring(0, idArra.length() - 1);
                    querySql = querySql + idArra + " ) ";

                    List<Map<String, Object>> mapList  = jdbcTemplate.queryForList(querySql);
                    for (Map map : mapList){
                        String key = map.get(relation.getPrimaryKey()).toString();
                        keys = keys + key + ",";
                    }
                    keys = keys.substring(0, keys.length()-1);
                }else{
                    keys = ids;
                }
                formTableBuilder.executeDeleteByForeignKey(relation.getChildTableName(), keys, relation.getForeignKey().split("\\.")[1]);
            }

            // 主表
            formTableBuilder.executeDelete(form.getTableName(), ids);

        }

    }

    /**
     * 查询表单内容
     *
     * @return
     */
    @Transactional(readOnly = true)
    @DS("#form.dataSource.enName")
    public Map executeQueryById(Form form, String id) {
        FormTableBuilder formTableBuilder;
        String enName = form.getDataSource().getEnName();
        String dbType = FormTableBuilder.getJdbcType(enName);
        JdbcTemplate jdbcTemplate = DBPool.getInstance().getDataSource(enName);
        if (dbType.equals("oracle")) {
            formTableBuilder = new FormOracleTableBuilder(jdbcTemplate, form.getTableName());
        } else {
            formTableBuilder = new FormTableBuilder(jdbcTemplate, form.getTableName());
        }

        List<Relation> relations = FormJsonUtils.newInstance().getRelations(form.getSource ());
        if(relations.size() == 0){ // 如果是单表
            return formTableBuilder.executeQueryById(form.getTableName(), id);
        }else { // 如果是多表关联
            String originalSql = "select ";
            List<Column> fields = FormJsonUtils.newInstance().getFields(form.getSource ());
            HashMap<String, String> typeMap = new HashMap();
            for (Column column : fields) {
                typeMap.put(column.getModel(), column.getType());
            }
            for (Column field : fields) {
                if(field.getModel().startsWith("childTable->")){
                    continue;
                }
                if(field.getModel().contains("->")){
                    originalSql = originalSql + field.getModel().replace ("->", ".") + " as '" + field.getModel() + "',";
                }else{
                    originalSql = originalSql + form.getTableName()+ "."+ field.getModel() + " as '" + field.getModel() + "',";
                }
            }
            originalSql = originalSql.substring(0, originalSql.length() - 1);
            originalSql = originalSql + " from " + form.getTableName();
            // 多表关联关系
            for (Relation relation : relations) {
                if("table".equals(relation.getType())){
                    continue;
                }
                originalSql = originalSql + " left join " + relation.getChildTableName() + " on " + form.getTableName() + "." + relation.getPrimaryKey()
                        + " = "  + relation.getForeignKey();
            }
            originalSql = originalSql + " where " + form.getTableName() + ".id = '" + id + "'";
            Map map = jdbcTemplate.queryForMap(originalSql);
            map.put("id", id);// 主键存储。
            //处理日期
            for (Object key : map.keySet()) {
                if ("date".equals(typeMap.get(key)) || "time".equals(typeMap.get(key))) {
                    if (map.get(key) != null) {
                        if(map.get (key) instanceof  Timestamp){
                            long time = ((Timestamp) map.get (key)).getTime ()/1000;
                            map.put (key, DateUtils.long2string (time));
                        }
                    }
                }

            }


            // 子表
            for (Relation relation : relations) {
                if("table".equals(relation.getType())){
                    String foreignKey = relation.getForeignKey().split("\\.")[1];

                    String childSql = "select ";
                    List<Column> childTableFieldArra = FormJsonUtils.newInstance().getFieldsByTable(form.getSource (), "childTable->"+relation.getChildTableName());
                    for(Column column: childTableFieldArra){
                        childSql = childSql + column.getModel().split("->")[1] + " as '" + column.getModel() + "',";
                    }
                    childSql = childSql.substring(0, childSql.length()-1);
                    childSql = childSql + " from "+ relation.getChildTableName() +" where "+foreignKey+" = '"+map.get(relation.getPrimaryKey())+"'";
                    List<Map<String, Object>> childList = jdbcTemplate.queryForList(childSql);
                    //处理日期
                    HashMap<String, String> childTableTypeMap = new HashMap();
                    for (Column column : childTableFieldArra) {
                        childTableTypeMap.put(column.getModel(), column.getType());
                    }
                    for (Map<String, Object> childMap : childList) {
                        for (String key : childMap.keySet()) {
                            if ("date".equals(childTableTypeMap.get(key)) || "time".equals(childTableTypeMap.get(key))) {
                                if (childMap.get(key) != null) {
                                    if(childMap.get (key) instanceof  Timestamp){
                                        long time = ((Timestamp) childMap.get (key)).getTime ()/1000;
                                        childMap.put (key, DateUtils.long2string (time));
                                    }
                                }
                            }

                        }
                    }

                    map.put("childTable->"+relation.getChildTableName()+"", JSON.toJSONString(childList));


                }
            }
            return map;
        }

    }

    @DS("#form.dataSource.enName")
    public Page<Map> executeFindPage(Page page, Form form, String params) {
        String originalSql = "";
            List<Relation> relations = FormJsonUtils.newInstance().getRelations(form.getSource ());
        if (relations.size() == 0) {// 包含子表
            originalSql = "select * from " + form.getTableName() + " ";
        } else {// 如果是多表关联
            originalSql = "select "+form.getTableName()+".id,";
            List<Column> fields = FormJsonUtils.newInstance().getFields(form.getSource ());
            for (Column field : fields) {
                if(field.getModel().startsWith("childTable->")){//排除子表
                    continue;
                }
                if(field.getModel().contains("->")){
                    originalSql = originalSql + field.getModel().replace ("->", ".") + " as '" + field.getModel() + "',";
                }else{
                    originalSql = originalSql + form.getTableName()+ "." + field.getModel() + " as '" + field.getModel() + "',";
                }
            }
            originalSql = originalSql.substring(0, originalSql.length() - 1);
            originalSql = originalSql + " from " + form.getTableName();
            // 子表

            for (Relation relation : relations) {
                if("table".equals(relation.getType())){// 排除子表
                    continue;
                }
                originalSql = originalSql + " left join " + relation.getChildTableName() + " on " + form.getTableName() + "." + relation.getPrimaryKey()
                        + " = "  + relation.getForeignKey();
            }
        }

        Map<String, String> paramsMap = JSON.parseObject(params, Map.class);
        List<Column> fieldArra = FormJsonUtils.newInstance().getFields(form.getSource ());
        HashMap<String, String> typeMap = new HashMap();
        for (Column column : fieldArra) {
            typeMap.put(column.getModel(), column.getType());
        }
        if (paramsMap.size() > 0) {
            if (StringUtils.isNotBlank(dataRuleFilter(form))) {
                originalSql = originalSql + " where 1=1 " + dataRuleFilter(form) + "  and   ";
            } else {
                originalSql = originalSql + " where ";
            }
            for (String key : paramsMap.keySet()) {
                if(StringUtils.isNotBlank(String.valueOf(paramsMap.get(key)))){
                    originalSql = originalSql + " " + key + " like " + "'%" + String.valueOf(paramsMap.get(key)) + "%' and   ";
                }
            }
            originalSql = originalSql.substring(0, originalSql.length() - 6);
        } else if (StringUtils.isNotBlank(dataRuleFilter(form))) {
            originalSql = originalSql + " where 1=1 " + dataRuleFilter(form);
        }
        if (StringUtils.isNotBlank(page.getOrderBy())) {
            originalSql = originalSql + " order by " + page.getOrderBy();
        }
        Dialect dialect = FormTableBuilder.getDialect();
        final String countSql = "select count(1) from (" + dialect.getCountString(originalSql) + ") tmp_count";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        page.setCount(count);
        String pageSql = SQLHelper.generatePageSql(originalSql, page, dialect);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(pageSql);

        for (Map<String, Object> map : mapList) {
            for (String key : map.keySet()) {
                if ("user".equals(typeMap.get(key))) {
                    if (map.get(key) != null && UserUtils.get(map.get(key).toString()) != null) {
                        map.put(key, UserUtils.get(map.get(key).toString()).getName());
                    }
                }
                if ("office".equals(typeMap.get(key))) {
                    if (map.get(key) != null && officeService.get(map.get(key).toString()) != null) {
                        map.put(key, officeService.get(map.get(key).toString()).getName());
                    }
                }
                if ("area".equals(typeMap.get(key))) {
                    if (map.get(key) != null && areaService.get(map.get(key).toString()) != null) {
                        map.put(key, areaService.get(map.get(key).toString()).getName());
                    }
                }
                if ("date".equals(typeMap.get(key)) || "time".equals(typeMap.get(key))) {
                    if (map.get(key) != null) {
                        if(map.get (key) instanceof  Timestamp){
                            long time = ((Timestamp) map.get (key)).getTime ()/1000;
                            map.put (key, DateUtils.long2string (time));
                        }
                    }
                }

            }
        }
        page.setCount(count);
        page.setList(mapList);
        return page;
    }

    public static String dataRuleFilter(Form form) {
        if (UserUtils.getUser() == null || StringUtils.isBlank(UserUtils.getUser().getId())) {
            return "";
        }
        form.setCurrentUser(UserUtils.getUser());
        List<DataRule> dataRuleList = UserUtils.getDataRuleList();
        // 如果是超级管理员，则不过滤数据
        if (dataRuleList.size() == 0) {
            return "";
        }
        // 数据范围
        StringBuilder sqlString = new StringBuilder();
        for (DataRule dataRule : dataRuleList) {
            if (form.getTableName().equals(dataRule.getClassName())) {
                sqlString.append(dataRule.getDataScopeSql());
            }
        }

        return sqlString.toString();

    }

}
