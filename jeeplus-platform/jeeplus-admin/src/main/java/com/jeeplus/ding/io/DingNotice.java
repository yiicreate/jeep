package com.jeeplus.ding.io;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: lh
 * @date: 2021/3/17
 */

@Setter
@Getter
public class DingNotice {
    public DingNotice(){}
    public DingNotice(String type,String content){
        this.type = type;
        switch (type){
            case "text":
                this.content = content;
                break;
            case "image":
            case "file":
                this.mediaId = content;
                break;
        }
    }

    /**
     * 类型 link /action_card
     * @param type
     * @param title
     * @param text
     * @param content
     * @param url
     */
    public DingNotice(String type,String title,String text,String content,String url){
        this.type = type;
        this.title = title;
        this.text = text;
        this.content = content;
        this.url = url;
    }

    /**
     * 类型 markdown  / oa
     * @param type
     * @param text
     * @param content
     */
    public DingNotice(String type,String text,String content){
        this.type = type;
        this.text = text;
        switch (type){
            case "markdown":
                this.title = content;
                break;
            case "file":
                this.content = content;
                break;
        }
    }

    /**
     * 类型
     */
    private String type;
    /**
     * 内容
     */
    private String content;

    private String mediaId;

    private String title;

    private String text;

    private String url;
}
