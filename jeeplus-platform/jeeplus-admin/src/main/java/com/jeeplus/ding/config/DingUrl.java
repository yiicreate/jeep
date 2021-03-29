package com.jeeplus.ding.config;

/**
 * @author: lh
 * @date: 2021/3/8
 */


public class DingUrl {
    /**
     * 钉钉网关gettoken地址
     */
    public static final String URL_GET_TOKKEN = "https://oapi.dingtalk.com/gettoken";

    /**
     *获取用户在企业内userId的接口URL
     */
    public static final String URL_GET_USER_INFO = "https://oapi.dingtalk.com/topapi/v2/user/getuserinfo";

    /**
     *获取用户姓名的接口url
     */
    public static final String URL_USER_GET = "https://oapi.dingtalk.com/user/get";

    /**
     * 获取ticket
     */
    public static final String URL_JSAPI_TICKET = "https://oapi.dingtalk.com/get_jsapi_ticket";

    /**
     * 发起工作待办通知
     */
    public static final String URL_WORK_CORD = "https://oapi.dingtalk.com/topapi/workrecord/add";

    /**
     * 发起工作通知
     */
    public static final String URL_WORK_NOTICE = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    /**
     * 创建日程
     */
    public static final String URL_DATE_EVENT = "https://oapi.dingtalk.com/topapi/calendar/v2/event/create";

    /**
     * 取消日程
     */
    public static final String URL_DATE_EVENT_CANCEL = "https://oapi.dingtalk.com/topapi/calendar/v2/event/cancel";
}
