package com.jeeplus.modules.sys.utils;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.sys.service.SysConfigService;

public class ConfigUtils {

    public static String getProductName(){
        return SpringContextHolder.getBean(SysConfigService.class).get("1").getProductName();
    }

    public static String getLogo(){
        return SpringContextHolder.getBean(SysConfigService.class).get("1").getLogo();
    }

    public static String getDefaultTheme(){
        return SpringContextHolder.getBean(SysConfigService.class).get("1").getDefaultTheme();
    }

    public static String getHomeUrl(){
        return SpringContextHolder.getBean(SysConfigService.class).get("1").getHomeUrl();
    }
}
