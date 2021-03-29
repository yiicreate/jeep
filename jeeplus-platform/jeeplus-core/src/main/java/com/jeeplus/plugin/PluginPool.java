package com.jeeplus.plugin;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.SpringContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class PluginPool {
    public PluginPool(){
    }
    private List<IPlugin> plugins = Lists.newArrayList();

    public List<IPlugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<IPlugin> plugins) {
        this.plugins = plugins;
    }

    public void addPlugin(IPlugin plugin){
        this.plugins.add(plugin);
    }
    public void addPlugin(ExPlugin plugin){
        this.plugins.add(plugin);
    }

    public static PluginPool getInstance(){
      return   SpringContextHolder.getBean(PluginPool.class);
    }
}
