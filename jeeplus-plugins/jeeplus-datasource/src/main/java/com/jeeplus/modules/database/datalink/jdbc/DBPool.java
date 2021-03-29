package com.jeeplus.modules.database.datalink.jdbc;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.database.datalink.entity.DataSource;
import com.jeeplus.modules.database.datalink.service.DataSourceService;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.jeeplus.database.datasource.DynamicRoutingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * 数据库池
 * 存放外部数据库链接
 */
@Component
public class DBPool {


    @Autowired
    private  DataSourceService dataSourceService;
    //存放所有的外部数据库链接
    private Map<String, JdbcTemplate> dbMap = new HashMap<>();

    private DBPool() {
    }

    public static DBPool getInstance() {
        return SpringContextHolder.getBean(DBPool.class);
    }

    /**
     * 测试配置文件是否可用
     *
     * @param driver   数据库驱动
     * @param url      数据库链接
     * @param username 数据库用户
     * @param password 数据库密码
     * @return
     */
    public boolean test(String driver, String url, String username, String password) {

        try {
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDriverClassName(driver);
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(basicDataSource);
            if(driver.toLowerCase().contains("oracle")){
                jdbcTemplate.queryForList("select 1 from dual");
            }else {
                jdbcTemplate.queryForList("select 1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 创建数据源，并存放到池中
     *
     * @param enName      数据源名称（不可重复）
     * @param driver   数据库驱动
     * @param url      数据库链接
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public DBPool create(String enName, String driver, String url, String username, String password)  {
        if (!dbMap.containsKey(enName)) {
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDriverClassName(driver);
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            basicDataSource.setTestWhileIdle(true);
            basicDataSource.setTimeBetweenEvictionRunsMillis(60000);
            basicDataSource.setMinEvictableIdleTimeMillis(1800000);
            DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
            dynamicRoutingDataSource.addDataSource(enName,basicDataSource);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(basicDataSource);
            dbMap.put(enName, jdbcTemplate);
        }
        return this;
    }

    public DBPool addMaster(javax.sql.DataSource dataSource) {
        try{
            String dbName = dataSource.getConnection().getMetaData().getDatabaseProductName();
        }catch (Exception e){

        }

        if (!dbMap.containsKey("master")) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            dbMap.put("master", jdbcTemplate);
        }
        return this;
    }

    public DBPool create(DataSource dataSource) {
        if (!dbMap.containsKey(dataSource.getEnName())) {
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDriverClassName(dataSource.getDriver());
            basicDataSource.setUrl(HtmlUtils.htmlUnescape(dataSource.getUrl()));
            basicDataSource.setUsername(dataSource.getUsername());
            basicDataSource.setPassword(dataSource.getPassword());
            DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
            dynamicRoutingDataSource.addDataSource(dataSource.getEnName(),basicDataSource);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(basicDataSource);
            dbMap.put(dataSource.getEnName(), jdbcTemplate);
        }
        return this;
    }
    public DBPool create(String key, JdbcTemplate dataSource) {
        dbMap.put(key, dataSource);
        return this;
    }

    /**
     * 获取数据源
     *
     * @param enName
     * @return
     */
    public JdbcTemplate getDataSource(String enName) {
        JdbcTemplate jdbcTemplate = dbMap.get(enName);
        if (jdbcTemplate == null) {
            DataSource dataSource = dataSourceService.get(enName);
            if (dataSource == null) {
                return null;
            }
            return create(enName, dataSource.getDriver(), HtmlUtils.htmlUnescape(dataSource.getUrl()), dataSource.getUsername(), dataSource.getPassword()).getDataSource(enName);
        } else {
            return jdbcTemplate;
        }
    }



    /**
     * 销毁指定数据源
     *
     * @param enName
     */
    public void destroy(String enName) {
        JdbcTemplate pool = dbMap.get(enName);
        if (pool != null) {
            dbMap.remove(enName);
            DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
            dynamicRoutingDataSource.removeDataSource(enName);
            try {
                pool.getDataSource().getConnection().close();
            } catch (SQLException e) {

            }


        }
    }

    /**
     * 销毁所有数据源
     */
    public void destroyAll() {
        for (String s : dbMap.keySet()) {
            destroy(s);
        }
        dbMap.clear();
    }

    /**
     * 数据源数量
     *
     * @return
     */
    public int size() {
        return dbMap.size();
    }
}
