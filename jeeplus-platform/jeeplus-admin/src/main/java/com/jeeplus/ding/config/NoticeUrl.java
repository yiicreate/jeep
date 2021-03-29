package com.jeeplus.ding.config;

/**
 * @author: lh
 * @date: 2021/3/16
 */


public class NoticeUrl {
    /**
     * 会议通知地址
     */
    public static final String URL_HY = "";

    /**
     * 微应用基础url
     * dingtalk://dingtalkclient/action/openapp?corpid=免登企业corpId&container_type=work_platform&app_id=0_{应用agentid}&redirect_type=jump&redirect_url=跳转url
     */
    public static final String BASE_URl = "dingtalk://dingtalkclient/action/openapp?corpid=#{corpid}&container_type=work_platform&app_id=#{app_id}&redirect_type=jump&redirect_url=#{url}";
}
