package com.jeeplus.ding.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lh
 * @date: 2021/3/8
 */

@Configuration
@Getter
public class DingConf {
    @Value("${ding.app_key}")
    private  String APP_KEY;

    @Value("${ding.app_secret}")
    private  String APP_SECRET;

    @Value("${ding.agent_id}")
    private  Long AGENT_ID;

    @Value("${ding.corp_id}")
    private  String CORP_ID;

    @Value("${ding.web_path}")
    private  String PATH;
}
