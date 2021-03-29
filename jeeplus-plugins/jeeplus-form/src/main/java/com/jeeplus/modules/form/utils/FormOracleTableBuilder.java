package com.jeeplus.modules.form.utils;


import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.form.dto.Column;
import com.jeeplus.modules.form.entity.Form;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * 表单数据表生成
 */
public class FormOracleTableBuilder extends FormTableBuilder {

    public FormOracleTableBuilder(JdbcTemplate jdbcTemplate, String tableName) {
        super(jdbcTemplate, tableName);
    }

    /**
     * 删除表格
     *
     * @return
     */
    public boolean dropTable() {
            getJdbcTemplate().execute("DECLARE\n" +
                    "  num NUMBER;\n" +
                    "BEGIN\n" +
                    "  SELECT COUNT(1)\n" +
                    "    INTO num\n" +
                    "    FROM USER_TABLES\n" +
                    "   WHERE TABLE_NAME = UPPER('"+getTableName()+"');\n" +
                    "  IF num > 0 THEN\n" +
                    "    EXECUTE IMMEDIATE 'DROP TABLE "+getTableName()+"';\n" +
                    "  END IF;\n" +
                    "END;");
        return true;
    }



    public boolean createTable() {
            getJdbcTemplate().execute("declare\n" +
                    "      num   number;\n" +
                    "begin\n" +
                    "    select count(1) into num from user_tables where table_name = upper('"+getTableName()+"') ;\n" +
                    "    if num = 0 then\n" +
                    "        execute immediate 'CREATE TABLE  "+getTableName()+"(\"id\" varchar(32) primary key, \"create_by\" varchar(64), \"create_date\" date , \"update_by\" varchar(64), \"update_date\" date )' ;\n" +
                    "    end if;\n" +
                    "end;");
        return true;
    }

    public String executeInsert(Form form, Map data, Map map) {
        //保存表单数据
        String uuid = IdGen.uuid();
        if (StringUtils.isNotBlank(form.getTableName())) {
            //遍历参数，组装sql语句
            String insertSql = "insert into " + form.getTableName() + "(";
            String insertSqlValue = " values(";
            for (Object name : data.keySet()) {
                insertSql += "\"" + name + "\",";
                if (map.get(name).equals("slider")
                        || map.get(name).equals("number")
                        || map.get(name).equals("rate")) {
                    insertSqlValue += "" + data.get(name) + ",";
                } else {
                    insertSqlValue += "'" + data.get(name) + "',";
                }
            }
            User user = UserUtils.getUser();
            if (StringUtils.isNotBlank(user.getId())){
                insertSql += "\"create_by\",";
                insertSqlValue += "'" + user.getId() + "',";
                insertSql += "\"update_by\",";
                insertSqlValue += "'" + user.getId() + "',";
            }
            insertSql += "\"create_date\",";
            insertSqlValue += "" + new Date() + ",";
            insertSql += "\"update_date\",";
            insertSqlValue += "" + new Date() + ",";
            insertSql += "\"id\")" + insertSqlValue + "'" + uuid + "')";

            getJdbcTemplate().execute(insertSql);
        }
        return uuid;

    }

    /**
     * 删除表单内容
     *
     * @return
     */
    public void executeDelete(Form form, String ids) {
        if (StringUtils.isNotBlank(form.getTableName())) {
            String deleteSql = "delete from " + form.getTableName() + " where \"id\" in ( ";
            String idArra = "";
            for (String id : ids.split(",")) {
                idArra = idArra + "'" + id + "',";
            }
            idArra = idArra.substring(0, idArra.length() - 1);
            deleteSql = deleteSql + idArra + " ) ";
            getJdbcTemplate().execute(deleteSql);
        }
    }

    /*
        查询表单内容
     */
    public Map executeQueryById(Form form, String id){
        String originalSql = "select * from "+form.getTableName()+" where \"id\" = '"+id+"'";
        Map map = getJdbcTemplate().queryForMap(originalSql);
        return map;
    }
    /**
     * 更新表单
     *
     * @param data
     * @return
     */
    public void executeUpdate(Form form, Map data, HashMap map) {
        //保存表单数据
        if (StringUtils.isNotBlank(form.getTableName())) {
            String updateSql = "update " + form.getTableName() + " set ";
            boolean needUpdate = false;
            for (Object name : data.keySet()) {
                if (name.equals("id"))
                    continue;
                if (map.get(name).equals("slider")
                        || map.get(name).equals("number")
                        || map.get(name).equals("rate")) {
                    updateSql = updateSql + " \"" + name + "\" = " + data.get(name) + ",";
                } else {
                    updateSql = updateSql + "\"" + name + "\" = '" + data.get(name) + "',";
                }
                needUpdate = true;
            }
            User user = UserUtils.getUser();
            if (StringUtils.isNotBlank(user.getId())){
                updateSql = updateSql + " \"update_by\"  = '" + user.getId() + "',";
            }
            updateSql = updateSql + " \"update_date\"  = " + new Date() + ",";

            updateSql = updateSql.substring(0, updateSql.length() - 1);
            updateSql = updateSql + " where \"id\" = '" + data.get("id").toString() + "'";
            if (needUpdate) {
                getJdbcTemplate().execute(updateSql);
            }
        }
    }

    public  void modifyColumn(List<Column> columnList){
        HashSet<String> newColumns = new HashSet();
        HashMap<String, String> map = new HashMap();
        HashSet<String> oldColumns = getColumns();
        for(Column column:columnList){
            newColumns.add(column.getModel());
            map.put(column.getModel(),column.getType());
        }
        for(String oldColumn: oldColumns){
            if(!newColumns.contains(oldColumn)
                    && !oldColumn.equals("id")
                    && !oldColumn.equals("create_by")
                    && !oldColumn.equals("create_date")
                    && !oldColumn.equals("update_by")
                    && !oldColumn.equals("update_date")){
                getJdbcTemplate().execute("alter table "+getTableName()+" drop column "+oldColumn+"" );
            }
        }
        for(String newColumn : newColumns){
           if(!oldColumns.contains(newColumn)){
               String type = map.get(newColumn);
               String sql = "alter table "+getTableName()+" add \""+newColumn+"\"";
               if(type.equals("slider") || type.equals("number")  || type.equals("rate") ){
                   sql +=  " integer";
               }else if(type.equals("textarea") || type.equals("table") || type.equals("imgupload") || type.equals("fileupload") || type.equals("editor")){
                   sql +=  " clob";
               }else{
                   sql +=  " varchar(512)";
               }
               getJdbcTemplate().execute(sql);
           }
        }
    }

}
