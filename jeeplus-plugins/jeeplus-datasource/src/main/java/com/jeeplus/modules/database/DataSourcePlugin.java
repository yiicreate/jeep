package com.jeeplus.modules.database;

import com.jeeplus.modules.database.datalink.entity.DataSource;
import com.jeeplus.modules.database.datalink.jdbc.DBPool;
import com.jeeplus.modules.database.datalink.service.DataSourceService;
import com.jeeplus.plugin.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "ds.plugin")
@PropertySource(value={"classpath:datasource-plugin.properties"}, encoding = "UTF-8")
public class DataSourcePlugin  extends Plugin {
    private String name;
    private String version;
    private String icon;
    private String site;
    private String description;
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    javax.sql.DataSource dataSource;

    @Autowired
    DBPool dbPool;

    @Override
    public void init(){
        //添加系统默认主数据源
        DBPool.getInstance().addMaster(dataSource);
        //获取数据库中定义的数据源
        List<DataSource> list = dataSourceService.findAllList(new DataSource());
        for (DataSource dataLink : list) {
            try {
                dbPool.create(dataLink.getEnName(), dataLink.getDriver(), HtmlUtils.htmlUnescape(dataLink.getUrl()), dataLink.getUsername(), dataLink.getPassword());
            } catch (Exception e) {
                System.out.println("【" + dataLink.getName() + "】数据库连接失败");
            }
        }

        System.out.println(this.getName()+"插件加载完成!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

