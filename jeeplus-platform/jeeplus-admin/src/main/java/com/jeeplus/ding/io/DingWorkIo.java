package com.jeeplus.ding.io;

import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.jeeplus.ding.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lh
 * @date: 2021/3/10
 */

@Setter
@Getter
public class DingWorkIo implements Serializable {
    public DingWorkIo(String userId,String title,OapiWorkrecordAddRequest.FormItemVo formItemVo,String url){
        this.userId = userId;
        this.title = title;
        List<OapiWorkrecordAddRequest.FormItemVo> list = new ArrayList<>();
        list.add(formItemVo);
        this.formItemVoList = list;
        this.url = url;
        createTime = TimeUtil.getUnixTime();
    }

    public DingWorkIo(String userId,String title,List<OapiWorkrecordAddRequest.FormItemVo> formItemVo,String url){
        this.userId = userId;
        this.title = title;
        this.formItemVoList = formItemVo;
        this.url = url;
        createTime = TimeUtil.getUnixTime();
    }


    public DingWorkIo(){
        createTime = TimeUtil.getUnixTime();
    }

    public DingWorkIo(String userId,String title){
        createTime = TimeUtil.getUnixTime();
        this.userId = userId;
        this.title = title;
    }
    /**
     * 大主题
     */
    private String title;

    /**
     * 接收人id
     */
    private String userId;

    /**
     * 跳转url(可pc端工作台打开)
     * @see "https://developers.dingtalk.com/document/app/message-link-description?spm=ding_open_doc.document.0.0.1e81c943ZYDqaC#section-7w8-4c2-9az"
     */
    private String url = "";

    /**
     * 表单列表
     */
    private List<OapiWorkrecordAddRequest.FormItemVo> formItemVoList = new ArrayList<>();

    /**
     * 创建时间 (默认当前时间)
     */
    private Long createTime;

    //===========================以下不必填==============================

    /**
     * PC端跳转URl
     */
    private String pcUrl;

    /**
     * 发送人员id
     */
    private String originatorUserId;

    /**
     * 来源名称
     */
    private String sourceName;

    /**
     * 待办的PC打开方式
     * 2：在PC端打开
     * 4：在浏览器打开
     */
    private Long pcOpenType;

    /**
     * 外部业务ID
     */
    private String bizId;


    public void setVoList(String title, String content){
        formItemVoList.add((new DingForm(title,content)).getVo());
    }
}
