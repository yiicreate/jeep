package com.jeeplus.ding.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.ding.config.DingConf;
import com.jeeplus.ding.io.DingEvent;
import com.jeeplus.ding.io.DingNotice;
import com.jeeplus.ding.io.DingNoticeIo;
import com.jeeplus.ding.io.DingWorkIo;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.*;

import static com.jeeplus.ding.config.DingUrl.*;
import static com.jeeplus.ding.config.NoticeUrl.BASE_URl;

/**
 * @author: lh
 * @date: 2021/3/8
 */

@Component
public class DingUtil {
    @Autowired
    private DingConf dingConf;

    public static final String DING_CACHE = "dingCache";
    public static final String DING_ACCESS_TOKEN = "ding_token";

    public String getToken(){
        try {
            String accessToken = (String) CacheUtils.get(DING_CACHE, DING_ACCESS_TOKEN);
            if (accessToken ==  null){
                DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
                OapiGettokenRequest request = new OapiGettokenRequest();

                request.setAppkey(dingConf.getAPP_KEY());
                request.setAppsecret(dingConf.getAPP_SECRET());
                request.setHttpMethod("GET");
                OapiGettokenResponse response = client.execute(request);
                if(!(response.getErrcode()==0)){
                    throw new RuntimeException("获取acess_token错误"+response.getErrcode()+response.getErrmsg());
                }else {
                    accessToken = response.getAccessToken();
                    CacheUtils.put(DING_CACHE, DING_ACCESS_TOKEN, accessToken);
                }
            }
            return accessToken;
        } catch (ApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取用户信息包括（设备id，用户id，用户名等）
     * @param accessToken
     * @param code
     * @return
     */
    public Map<String,Object> getUserInfo(String accessToken, String code){
        //获取用户信息
        DingTalkClient client = new DefaultDingTalkClient(URL_GET_USER_INFO);
        OapiV2UserGetuserinfoRequest request = new OapiV2UserGetuserinfoRequest();
        request.setCode(code);

        OapiV2UserGetuserinfoResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            throw new RuntimeException(e.getMessage());
        }
        Map<String,Object> resMap= new HashMap<>();
        if(!(response.getErrcode()==0)){
            throw new RuntimeException("获取用户信息错误"+response.getErrorCode());
        }else {
            resMap.put("name",response.getResult().getName());
            resMap.put("unionid",response.getResult().getUnionid());
            resMap.put("userid",response.getResult().getUserid());
            resMap.put("sys_level",response.getResult().getSysLevel());
            resMap.put("sys",response.getResult().getSys());
            resMap.put("device_id",response.getResult().getDeviceId());
            return resMap;
        }
    }


    /**
     * 获取用户姓名
     *
     * @param accessToken
     * @param userId
     * @return
     */
    public  String getUserName(String accessToken, String userId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URL_USER_GET);
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = client.execute(request, accessToken);
            return response.getName();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取ticket
     * @param accessToken
     * @return
     */
    public String getJsapiTicket(String accessToken){
        try {
            DingTalkClient client = new DefaultDingTalkClient(URL_JSAPI_TICKET);
            OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
            req.setHttpMethod("GET");
            OapiGetJsapiTicketResponse response = client.execute(req, accessToken);
            if(!(response.getErrcode()==0)){
                throw new RuntimeException("获取ticket错误"+response.getErrorCode());
            }else {
                return response.getTicket();
            }
        } catch (ApiException e) {
            throw  new RuntimeException("获取ticket错误"+e.getErrCode()+e.getErrMsg());
        }
    }


    /**
     * 计算dd.config的签名参数
     *
     * @param jsticket  通过微应用appKey获取的jsticket
     * @param nonceStr  随机字符串
     * @param timeStamp 当前时间戳
     * @param url       调用dd.config的当前页面URL
     * @return
     * @throws Exception
     */
    public  String sign(String jsticket, String nonceStr, long timeStamp, String url) throws Exception {
        String plain = "jsapi_ticket=" + jsticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
                + "&url=" + decodeUrl(url);
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-256");
            sha1.reset();
            sha1.update(plain.getBytes("UTF-8"));
            return byteToHex(sha1.digest());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 字节数组转化成十六进制字符串
     * @param hash
     * @return
     */
    private  String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 因为ios端上传递的url是encode过的，android是原始的url。开发者使用的也是原始url,
     * 所以需要把参数进行一般urlDecode
     *
     * @param url
     * @return
     * @throws Exception
     */
    private  String decodeUrl(String url) throws Exception {
        URL urler = new URL(url);
        StringBuilder urlBuffer = new StringBuilder();
        urlBuffer.append(urler.getProtocol());
        urlBuffer.append(":");
        if (urler.getAuthority() != null && urler.getAuthority().length() > 0) {
            urlBuffer.append("//");
            urlBuffer.append(urler.getAuthority());
        }
        if (urler.getPath() != null) {
            urlBuffer.append(urler.getPath());
        }
        if (urler.getQuery() != null) {
            urlBuffer.append('?');
            urlBuffer.append(URLDecoder.decode(urler.getQuery(), "utf-8"));
        }
        return urlBuffer.toString();
    }

    /**
     * 发起待办任务
     *  每人每天最多收到一条表单内容相同的待办。触发这个限制，会返回错误码400001。
     * 每人每天最多收到100条待办。触发这个限制，会返回错误码400002。
     * @see "https://developers.dingtalk.com/document/app/new-to-do-items"
     * @return
     */
    public Map<String,Object> sendWorkCord(DingWorkIo dingWorkIo){
        if(StringUtils.isBlank(dingWorkIo.getUserId())){
            return  null;
        }
        DingTalkClient client = new DefaultDingTalkClient(URL_WORK_CORD);
        OapiWorkrecordAddRequest req = new OapiWorkrecordAddRequest();
        req.setUserid(dingWorkIo.getUserId());
        req.setCreateTime(dingWorkIo.getCreateTime());
        req.setTitle(dingWorkIo.getTitle());
        req.setUrl(getUrl(dingWorkIo.getUrl()));
        req.setPcUrl(dingWorkIo.getPcUrl());
        req.setFormItemList(dingWorkIo.getFormItemVoList());
        req.setOriginatorUserId(dingWorkIo.getOriginatorUserId());
        req.setSourceName(dingWorkIo.getSourceName());
        req.setPcOpenType(dingWorkIo.getPcOpenType());
        req.setBizId(dingWorkIo.getBizId());
        try {
            OapiWorkrecordAddResponse rsp = client.execute(req, getToken());
            Map<String,Object> res = new HashMap<>();
            res.put("code",rsp.getErrorCode());
            if(rsp.getErrcode() == 0){
                res.put("record_id",rsp.getRecordId());
                res.put("request_id",rsp.getRequestId());
            }
            return  res;
        }catch (ApiException e){
            throw new RuntimeException("待办任务发起失败");
        }
    }

    private String getUrl(String url){
        String base = BASE_URl;
        String a= base.replace("#{corpid}",dingConf.getCORP_ID());
        String b= a.replace("#{app_id}",dingConf.getAGENT_ID()+"");
        String c= b.replace("#{url}",dingConf.getPATH()+url);
        return  c;
    }

    /**
     * 发送工作通知
     * @param dingNoticeIo
     * @return
     */
    public Map<String,Object> sendWorkNotice(DingNoticeIo dingNoticeIo){
        if(StringUtils.isBlank(dingNoticeIo.getUsers())){
            return  null;
        }
        DingTalkClient client = new DefaultDingTalkClient(URL_WORK_NOTICE);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(dingConf.getAGENT_ID());
        request.setUseridList(dingNoticeIo.getUsers());
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        DingNotice n = dingNoticeIo.getDingNotice();
        if(StringUtils.isBlank(n.getType())){
                return  null;
        }
        switch (n.getType()){
                case "text":
                    msg.setMsgtype("text");
                    msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
                    msg.getText().setContent(n.getContent());
                    break;
                case "image":
                    msg.setMsgtype("image");
                    msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
                    msg.getImage().setMediaId(n.getMediaId());
                    break;
                case "file":
                    msg.setMsgtype("file");
                    msg.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
                    msg.getFile().setMediaId(n.getMediaId());
                    break;
                case "link":
                    msg.setMsgtype("link");
                    msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
                    msg.getLink().setTitle(n.getTitle());
                    msg.getLink().setText(n.getText());
                    msg.getLink().setMessageUrl(n.getContent());
                    msg.getLink().setPicUrl(n.getUrl());
                    break;
                case "markdown":
                    msg.setMsgtype("markdown");
                    msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
                    msg.getMarkdown().setTitle(n.getTitle());
                    msg.getMarkdown().setText(n.getText());
                    break;
                case "oa":
                    msg.setMsgtype("oa");
                    msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
                    msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
                    msg.getOa().getHead().setText(n.getText());
                    msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
                    msg.getOa().getBody().setContent(n.getContent());
                    msg.getOa().getBody().setTitle(n.getTitle());
                case "action_card":
                    msg.setMsgtype("action_card");
                    msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
                    msg.getActionCard().setTitle(n.getTitle());
                    msg.getActionCard().setSingleTitle(n.getText());
                    msg.getActionCard().setMarkdown(n.getContent());
                    msg.getActionCard().setSingleUrl(n.getUrl());
                    break;
        }
        request.setMsg(msg);
        try {
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, getToken());
            Map<String,Object> res = new HashMap<>();
            res.put("code",rsp.getErrorCode());
            if(rsp.getErrcode() == 0){
                res.put("task_id",rsp.getTaskId());
                res.put("request_id",rsp.getRequestId());
            }
            return  res;
        }catch (ApiException e){
            throw new RuntimeException("工作通知发起失败");
        }
    }

    /**
     * 发送日程
     * @return
     */
    public Map<String,Object> sendEvent(DingEvent dingEvent){
        DingTalkClient client = new DefaultDingTalkClient(URL_DATE_EVENT);
        OapiCalendarV2EventCreateRequest req = new OapiCalendarV2EventCreateRequest();
        OapiCalendarV2EventCreateRequest.Event obj1 = new OapiCalendarV2EventCreateRequest.Event();
        obj1.setAttendees(dingEvent.getAttendee());
        obj1.setCalendarId("primary");
        obj1.setDescription(dingEvent.getDescription());
        obj1.setEnd(dingEvent.getEnd());
        obj1.setOrganizer(dingEvent.getOrganizer());;
        obj1.setStart(dingEvent.getStart());
        obj1.setSummary(dingEvent.getSummary());
        //设置为默认在app提醒，时间提前15分钟
        OapiCalendarV2EventCreateRequest.OpenCalendarReminderVo obj8 = new OapiCalendarV2EventCreateRequest.OpenCalendarReminderVo();
        obj8.setMethod("app");
        obj8.setMinutes(15L);
        obj1.setReminder(obj8);
//        OapiCalendarV2EventCreateRequest.LocationVo obj9 = new OapiCalendarV2EventCreateRequest.LocationVo();
//        obj9.setLatitude("30.285228");
//        obj9.setLongitude("120.017022");
//        obj9.setPlace("未来park");
//        obj1.setLocation(obj9);
        obj1.setNotificationType("NONE");
        req.setEvent(obj1);
        req.setAgentid(dingConf.getAGENT_ID());

        try {
            OapiCalendarV2EventCreateResponse rsp = client.execute(req, getToken());
            Map<String,Object> res = new HashMap<>();
            res.put("code",rsp.getErrorCode());
            if(rsp.getErrcode() == 0){
                res.put("event_id",rsp.getResult().getEventId());
                res.put("request_id",rsp.getRequestId());
            }
            return  res;
        }catch (ApiException e){
            throw new RuntimeException("日程发起失败");
        }
    }

    /**
     * 取消日程
     * @return
     */
    public String cancelEvent(String EventId){
        DingTalkClient client = new DefaultDingTalkClient(URL_DATE_EVENT_CANCEL);
        OapiCalendarV2EventCancelRequest req = new OapiCalendarV2EventCancelRequest();
        req.setCalendarId("primary");
        req.setEventId(EventId);
        req.setAgentid(dingConf.getAGENT_ID());
        try {
            OapiCalendarV2EventCancelResponse rsp = client.execute(req, getToken());
            return String.valueOf(rsp.getErrcode());
        }catch (ApiException e){
            throw new RuntimeException("日程取消失败");
        }
    }

}
