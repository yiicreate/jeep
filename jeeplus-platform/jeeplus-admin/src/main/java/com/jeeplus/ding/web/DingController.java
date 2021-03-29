package com.jeeplus.ding.web;

import cn.hutool.core.util.RandomUtil;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.config.properties.JeePlusProperites;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.ding.io.DingEvent;
import com.jeeplus.ding.io.DingNotice;
import com.jeeplus.ding.io.DingNoticeIo;
import com.jeeplus.ding.io.DingWorkIo;
import com.jeeplus.ding.utils.DingUtil;
import com.jeeplus.ding.utils.TimeUtil;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.security.util.JWTUtil;
import com.jeeplus.modules.sys.service.RoleService;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lh
 * @date: 2021/3/8
 */

@RestController
@RequestMapping("/ding")
@Api(tags = "钉钉管理")
public class DingController  extends BaseController {
    @Autowired
    private DingUtil dingUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/sign")
    @ApiOperation(value = "钉钉签名")
    public AjaxJson getSign(@RequestParam("url") String url
    ) throws Exception {
        String accessToken = dingUtil.getToken();

        String ticket = dingUtil.getJsapiTicket(accessToken);
        String ranStr = RandomUtil.randomString(10);
        Long timeStamp = TimeUtil.getUnixTime();
        String sign = dingUtil.sign(ticket,ranStr,timeStamp, url);

        Map<String,Object> map = new HashMap<>();
        map.put("sign", sign);
        map.put("ranStr", ranStr);
        map.put("timeStamp", timeStamp);
        return AjaxJson.success().put("data",map);
    }

    @PostMapping("/register")
    @ApiOperation(value = "钉钉注册 暂时返回用户标识，用于绑定用户")
    public AjaxJson register(@RequestParam("code") String code){
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = dingUtil.getToken();
        //获取用户信息
        Map<String,Object> userInfo = dingUtil.getUserInfo(accessToken,code);

        AjaxJson j = new AjaxJson();
        User user = UserUtils.getByUserId((String) userInfo.get("user_id"));
        do {
            if (user != null) {
                j.setSuccess(false);
                j.setMsg("该用户已注册!");
                break;
            }
            j.setSuccess(true);
            j.put("name",userInfo.get("name"));
            j.put("userId",userInfo.get("user_id"));
        }while (false);

        return j;
    }

    @PostMapping("/login")
    @ApiOperation(value = "钉钉登录")
    public AjaxJson login(@RequestParam("code") String code){
        //获取accessToken
        String accessToken = dingUtil.getToken();

        //获取用户信息
        Map<String,Object> userInfo = dingUtil.getUserInfo(accessToken,code);

        AjaxJson j = new AjaxJson();
        User user = UserUtils.getByUserId((String) userInfo.get("user_id"));
        if (user != null) {
            if (JeePlusProperites.NO.equals(user.getLoginFlag())){
                j.setSuccess(false);
                j.setMsg("该用户已经被禁止登陆!");
            }else {
                j.setSuccess(true);
                j.put(JWTUtil.TOKEN, JWTUtil.createAccessToken(user.getLoginName(), user.getPassword()));
                j.put(JWTUtil.REFRESH_TOKEN, JWTUtil.createRefreshToken(user.getLoginName(), user.getPassword()));
            }
        } else {
            j.setSuccess(false);
            j.setMsg("用户不存在,请注册");
        }
        return  j;
    }
}
