package com.jeeplus.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

@JsonIgnoreProperties(value={"$$beanFactory","beanPostProcessors"})
public  abstract class Plugin implements IPlugin {

    private String name;
    private String version;
    private String icon;
    private String site;
    private String description;
    @Autowired
    PluginPool pluginPool;
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getIcon() {
        return this.icon;
    }

    @Override
    public String getSite() {
        return this.site;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }


    public Plugin(){

    }
    public Plugin(String name, String version, String icon, String site, String description){
        this.name = name;
        this.version = version;
        this.icon = icon;
        this.site = site;
        this.description = description;
    }





    @Override
    public void run(ApplicationArguments args) throws Exception {


        //获取数据库中定义的数据源
        init();
        pluginPool.getPlugins().add(this);
    }
}
