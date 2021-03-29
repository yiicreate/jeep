package com.jeeplus.modules.test.moments.vo;

import com.jeeplus.ding.config.DingConf;
import com.jeeplus.ding.io.DingNotice;
import com.jeeplus.ding.utils.TimeUtil;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.moments.entity.Share;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lh
 * @date: 2021/3/18
 */

@Component
@Getter
public class ShareVo {
    @Autowired
    private DingConf dingConf;
    public ShareVo(){};
    /**
     * 委员圈分享模板
     * @return
     */
    public DingNotice shareNotice(Share share,List<ComFiles> comFiles){
        String text = "委员圈分享  \n\n  "
                +">"+share.getContent()+"  \n\n  ";
        if(comFiles.size()>0){
            for (ComFiles s: comFiles) {
                if(s.getType().equals("video")){
                    continue;
                }else{
                    text += "![screenshot]("+dingConf.getPATH()+s.getUrl()+")";
                }

            }
            text += "  \n\n  ";
        }
        text += ">>来源@"+share.getCreateBy().getName()+"  "+ TimeUtil.getCurrent();
        return new DingNotice("markdown",text,"委员圈分享");
    }

}
