package com.jeeplus.modules.form.utils;


import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.dialect.Dialect;
import com.jeeplus.core.persistence.dialect.db.*;
import com.jeeplus.modules.database.datalink.jdbc.DBPool;
import com.jeeplus.modules.form.dto.Column;
import com.jeeplus.modules.form.entity.Form;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 表单数据表生成
 */
public class FormTableBuilder {
    private static String tablePrefix = "jp_form_";
    private String tableName = "";
    private JdbcTemplate jdbcTemplate;

    public FormTableBuilder(JdbcTemplate jdbcTemplate, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
    }

    public static String getTablePrefix() {
        return tablePrefix;
    }

    /*
        获取数据库类型
     */
    public static String getJdbcType(String enName){
        String dbType;
        try{
            dbType = SpringContextHolder.getBean(DatabaseIdProvider.class).getDatabaseId(DBPool.getInstance().getDataSource(enName).getDataSource());
        }catch (Exception e){
            dbType = "mysql";
        }
        return dbType;
    }

    /*
       获取方言
     */
    public static Dialect getDialect() {
        Dialect dialect = null;
        String dbType ;
        try{
            dbType = SpringContextHolder.getBean(DatabaseIdProvider.class).getDatabaseId(SpringContextHolder.getBean(DataSource.class));
        }catch (Exception e){
            dbType = "mysql";
        }

        if ("db2".equals(dbType)){
            dialect = new DB2Dialect();
        }else if("derby".equals(dbType)){
            dialect = new DerbyDialect();
        }else if("h2".equals(dbType)){
            dialect = new H2Dialect();
        }else if("hsql".equals(dbType)){
            dialect = new HSQLDialect();
        }else if("mysql".equals(dbType)){
            dialect = new MySQLDialect();
        }else if("oracle".equals(dbType)){
            dialect = new OracleDialect();
        }else if("postgre".equals(dbType)){
            dialect = new PostgreSQLDialect();
        }else if("mssql".equals(dbType) || "sqlserver".equals(dbType)){
            dialect = new SQLServerDialect();
        }else if("sybase".equals(dbType)){
            dialect = new SybaseDialect();
        }
        if (dialect == null) {
            throw new RuntimeException("mybatis dialect error.");
        }
        return dialect;
    }
    /**
     * 删除表格
     *
     * @return
     */
    public boolean dropTable() {
        if(StringUtils.isNotBlank(tableName)){
        jdbcTemplate.execute(String.format("drop table if exists %s", tableName));
        }
        return true;
    }


    public String executeInsert(String tableName, Map data, Map map) {
        //保存表单数据
        String uuid = IdGen.uuid();
        if (StringUtils.isNotBlank(tableName)) {
            //遍历参数，组装sql语句
            String insertSql = "insert into " + tableName + "(";
            String insertSqlValue = " values(";
            for (Object name : data.keySet()) {
                if (map.get(name) == null)
                    continue;
                insertSql += "" + name + ",";
                if (map.get(name).equals("slider")
                        || map.get(name).equals("number")
                        || map.get(name).equals("rate")
                        || map.get(name).equals("switch")) {
                    insertSqlValue += "" + data.get(name) + ",";
                } else {
                    insertSqlValue += "'" + data.get(name) + "',";
                }
            }
            User user = UserUtils.getUser();
            if (StringUtils.isNotBlank(user.getId())){
                if(this.getColumns().contains("create_by") && map.get("create_by") == null){
                    insertSql += "create_by,";
                    insertSqlValue += "'" + user.getId() + "',";
                }
                if(this.getColumns().contains("update_by") && map.get("update_by") == null){
                    insertSql += "update_by,";
                    insertSqlValue += "'" + user.getId() + "',";
                }
            }
            if(this.getColumns().contains("create_date") && map.get("create_date") == null) {
                insertSql += "create_date,";
                insertSqlValue += "'" + formatDate(new Date()) + "',";
            }
            if(this.getColumns().contains("update_date") && map.get("update_date") == null) {
                insertSql += "update_date,";
                insertSqlValue += "'" + formatDate(new Date()) + "',";
            }
            if(this.getColumns().contains("del_flag") && map.get("del_flag") == null) {
                insertSql += "del_flag,";
                insertSqlValue += "'0',";
            }
            insertSql += "id)" + insertSqlValue + "'" + uuid + "')";

            jdbcTemplate.execute(insertSql);
        }
        return uuid;
    }


    public String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //从前端或者自己模拟一个日期格式，转为String即可
        return format.format(date);
    }

    /**
     * 删除表单内容
     *
     * @return
     */
    public void executeDelete(String tableName, String ids) {
        if (StringUtils.isNotBlank(tableName)) {
            String deleteSql = "delete from " + tableName + " where id in ( ";
            String idArra = "";
            for (String id : ids.split(",")) {
                idArra = idArra + "'" + id + "',";
            }
            idArra = idArra.substring(0, idArra.length() - 1);
            deleteSql = deleteSql + idArra + " ) ";
            jdbcTemplate.execute(deleteSql);
        }
    }

    /**
     * 删除外键删除表单内容
     *
     * @return
     */
    public void executeDeleteByForeignKey(String tableName, String ids, String foreignKey) {
        if (StringUtils.isNotBlank(tableName)) {
            String deleteSql = "delete from " + tableName + " where "+foreignKey+" in ( ";
            String idArra = "";
            for (String id : ids.split(",")) {
                idArra = idArra + "'" + id + "',";
            }
            idArra = idArra.substring(0, idArra.length() - 1);
            deleteSql = deleteSql + idArra + " ) ";
            jdbcTemplate.execute(deleteSql);
        }
    }
    /*
    查询表单内容
     */
    public int executeQueryCountByForeignKey(String tableName, String foreignKey, String foreignValue){
        String countSql = "select count(1)  from "+tableName+" where "+foreignKey+" = '"+foreignValue+"'";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        return count;
    }

    /*
      查询表单内容
    */
    public Map executeQueryById(String tableName, String id){
        String originalSql = "select * from "+tableName+" where id = '"+id+"'";
        Map map = jdbcTemplate.queryForMap(originalSql);
        return map;
    }
    /**
     * 更新表单
     *
     * @param data
     * @return
     */
    public void executeUpdate(String tableName, Map data, Map map) {
        //保存表单数据
        if (StringUtils.isNotBlank(tableName)) {
            String updateSql = "update " + tableName + " set ";
            boolean needUpdate = false;
            for (Object name : data.keySet()) {
                if (name.equals("id") || map.get(name) == null)
                    continue;
                if (map.get(name).equals("slider")
                        || map.get(name).equals("number")
                        || map.get(name).equals("rate")
                        || map.get(name).equals("switch")) {
                    updateSql = updateSql + " " + name + " = " + data.get(name) + ",";
                } else {
                    updateSql = updateSql + " " + name + " = '" + data.get(name) + "',";
                }
                needUpdate = true;
            }
            User user = UserUtils.getUser();
            if (StringUtils.isNotBlank(user.getId()) && map.get("update_by") == null){
                if(this.getColumns().contains("update_by")){
                    updateSql = updateSql + "  update_by  = '" + user.getId() + "',";
                }
            }
            if(this.getColumns().contains("update_date") && map.get("update_date") == null){
                updateSql = updateSql + "  update_date  = '" + formatDate(new Date()) + "',";
            }
            updateSql = updateSql.substring(0, updateSql.length() - 1);
            updateSql = updateSql + " where id = '" + data.get("id").toString() + "'";
            if (needUpdate) {
                jdbcTemplate.execute(updateSql);
            }
        }
    }

    /**
     * 通过外键更新
     *
     * @param data
     * @return
     */
    public void executeUpdateByForeignKey(String tableName, Map data, Map map, String foreignKey) {
        //保存表单数据
        if (StringUtils.isNotBlank(tableName)) {
            String updateSql = "update " + tableName + " set ";
            boolean needUpdate = false;
            for (Object name : data.keySet()) {
                if (name.equals("id") || name.equals(foreignKey) || map.get(name) == null)
                    continue;
                if (map.get(name).equals("slider")
                        || map.get(name).equals("number")
                        || map.get(name).equals("rate")
                        || map.get(name).equals("switch")) {
                    updateSql = updateSql + " " + name + " = " + data.get(name) + ",";
                } else {
                    updateSql = updateSql + " " + name + " = '" + data.get(name) + "',";
                }
                needUpdate = true;
            }
            User user = UserUtils.getUser();
            if (StringUtils.isNotBlank(user.getId())){
                if(this.getColumns().contains("update_by")){
                    updateSql = updateSql + "  update_by  = '" + user.getId() + "',";
                }
            }
            if(this.getColumns().contains("update_date")){
                updateSql = updateSql + "  update_date  = '" + formatDate(new Date()) + "',";
            }
            updateSql = updateSql.substring(0, updateSql.length() - 1);
            updateSql = updateSql + " where "+foreignKey+" = '" + data.get(foreignKey) + "'";
            if (needUpdate) {
                jdbcTemplate.execute(updateSql);
            }
        }
    }

    public boolean createTable() {
        jdbcTemplate.execute(String.format("CREATE TABLE IF NOT EXISTS %s(`id` varchar(32) primary key comment '主键', `create_by` varchar(64) comment '创建人', `create_date` datetime comment '创建日期' , `update_by` varchar(64) comment '更新者', `update_date` datetime comment '更新日期' )", tableName));
        return true;
    }

    public HashSet getColumns () {
        HashSet columns = new HashSet();
        String sql = String.format("select * from   %s", tableName);
        String[] str= jdbcTemplate.queryForRowSet(sql).getMetaData().getColumnNames();
        for(int i = 0;i<str.length;i++){
            columns.add(str[i]);
        }
        return columns;
    }

    public  void modifyColumn(List<Column> columnList){
        HashSet<String> newColumns = new HashSet();
        HashMap<String, String> map = new HashMap();
        HashMap<String, String> map2 = new HashMap();
        HashSet<String> oldColumns = getColumns();
        for(Column column:columnList){
            newColumns.add(column.getModel());
            map.put(column.getModel(),column.getType());
            map2.put(column.getModel(), column.getName());
        }
        for(String oldColumn: oldColumns){
            if(!newColumns.contains(oldColumn)
                    && !oldColumn.equals("id")
                    && !oldColumn.equals("create_by")
                    && !oldColumn.equals("create_date")
                    && !oldColumn.equals("update_by")
                    && !oldColumn.equals("update_date")){
                jdbcTemplate.execute("alter table `"+tableName+"` drop column `"+oldColumn+"`" );
            }
        }
        for(String newColumn : newColumns){
           if(!oldColumns.contains(newColumn)){
               String type = map.get(newColumn);
               String sql = "alter table `"+tableName+"` add column `"+newColumn+"`";
               if(type.equals("slider") || type.equals("number")  || type.equals("rate") ){
                   sql +=  " integer";
               }else if(type.equals("switch")){
                   sql +=  " boolean";
               }else if(type.equals("textarea") || type.equals("table") || type.equals("imgupload") || type.equals("fileupload") || type.equals("editor")){
                   sql +=  " text";
               }else{
                   sql +=  " text";
               }
               sql += " comment '"+ map2.get(newColumn) +"'";
               jdbcTemplate.execute(sql);
           }
        }
    }



    /**
     * 创建表格
     *
     * @return
     */
    public boolean syncTable(List<Column> columnList) {
        if (this.createTable()) {
            this.modifyColumn(columnList);
        }
        return true;
    }

    public String getTableName() {
        return tableName;
    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
