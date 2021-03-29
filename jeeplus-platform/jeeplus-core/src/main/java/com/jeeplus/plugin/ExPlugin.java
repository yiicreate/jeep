package com.jeeplus.plugin;

import org.springframework.boot.ApplicationArguments;

public class ExPlugin extends Plugin implements IPlugin{

    public ExPlugin(String name, String version, String icon, String site, String description){
       super(name,version,icon,site,description);
    }
    @Override
    public void init() {

    }


    @Override
    public void run(ApplicationArguments args) throws Exception {


        //获取数据库中定义的数据源
        init();
        pluginPool.getPlugins().add(this);
    }
}
