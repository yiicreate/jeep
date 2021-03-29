package com.jeeplus.ding.io;

import com.dingtalk.api.request.OapiCalendarV2EventCreateRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lh
 * @date: 2021/3/19
 */

@Getter
public class DingEvent {
    public DingEvent(){};

    public DingEvent(String summary,String description){
        this.description = description;
        this.summary = summary;
    }

    public DingEvent(String summary,String description,Long start,Long end){
        this.description = description;
        this.summary = summary;
        setStart(start);
        setEnd(end);
    }

    /**
     * 日程参与人
     */
    private List<OapiCalendarV2EventCreateRequest.Attendee> attendee = new ArrayList<>();
    public void setAttendee(String userId){
        OapiCalendarV2EventCreateRequest.Attendee obj = new OapiCalendarV2EventCreateRequest.Attendee();
        obj.setUserid(userId);
        attendee.add(obj);
    }

    /**
     * 日程发起人
     */
    private OapiCalendarV2EventCreateRequest.Attendee organizer = new OapiCalendarV2EventCreateRequest.Attendee();
    public void setOrganizer(String userId){
        organizer.setUserid(userId);
    }

    /**
     * 日程描述
     */
    private  String description;

    public void setDescription(String description){
        this.description = description;
    }

    /**
     * 日程主题
     */
    private  String summary;

    public void setSummary(String summary){
        this.summary = summary;
    }


    /**
     * 开始时间
     */
    private  OapiCalendarV2EventCreateRequest.DateTime start = new OapiCalendarV2EventCreateRequest.DateTime();

    public void setStart(Long time){
        start.setTimestamp(time);
        start.setTimezone("Asia/Shanghai");
    }

    /**
     * 结束时间
     */
    private  OapiCalendarV2EventCreateRequest.DateTime end = new OapiCalendarV2EventCreateRequest.DateTime();

    public void setEnd(Long time){
        end.setTimestamp(time);
        end.setTimezone("Asia/Shanghai");
    }
}
