package com.jeeplus.ding.io;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: lh
 * @date: 2021/3/17
 */

@Getter
@Setter
public class DingNoticeIo {
    /**
     * 人员列表多人以逗号分开
     */
    private String users;

    private DingNotice dingNotice;

    public void setContentList(DingNotice dingNotice){
        this.dingNotice = dingNotice;
    }
}
