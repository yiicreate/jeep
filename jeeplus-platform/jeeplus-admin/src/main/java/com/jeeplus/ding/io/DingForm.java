package com.jeeplus.ding.io;

import com.dingtalk.api.request.OapiMessageMassSendRequest;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import lombok.Getter;

import javax.swing.text.StringContent;

/**
 * @author: lh
 * @date: 2021/3/16
 */

@Getter
public class DingForm {
    private OapiWorkrecordAddRequest.FormItemVo vo;
    public DingForm(String title, String content){
        vo = new OapiWorkrecordAddRequest.FormItemVo();
        vo.setTitle(title);
        vo.setContent(content);
    }
}
