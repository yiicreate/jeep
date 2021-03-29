/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datalink.entity;


import com.jeeplus.core.persistence.DataEntity;

/**
 * 数据库连接Entity
 *
 * @author 刘高峰
 * @version 2018-08-06
 */
public class DataSource extends DataEntity<DataSource> {

    private static final long serialVersionUID = 1L;
    private String name;        // 数据库名称
    private String enName;
    private String type;        // 数据库类型
    private String driver;        // 数据库驱动
    private String host;        // 主机地址
    private String port;        // 端口
    private String dbname;        // 数据库名
    private String url;        // 数据库连接
    private String username;        // 数据库用户名
    private String password;        // 数据库密码
    private String invokes;        // 调用次数

    public DataSource() {
        super();
    }

    public DataSource(String id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInvokes() {
        return invokes;
    }

    public void setInvokes(String invokes) {
        this.invokes = invokes;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }
}
