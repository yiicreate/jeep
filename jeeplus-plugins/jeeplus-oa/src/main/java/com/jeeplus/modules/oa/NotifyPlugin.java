package com.jeeplus.modules.oa;

import com.jeeplus.plugin.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "notify.plugin")
@PropertySource(value={"classpath:notify-plugin.properties"}, encoding = "UTF-8")
public class NotifyPlugin extends Plugin {



    public void init(){
        System.out.println(this.getName()+"插件加载完成!");
    }
}
