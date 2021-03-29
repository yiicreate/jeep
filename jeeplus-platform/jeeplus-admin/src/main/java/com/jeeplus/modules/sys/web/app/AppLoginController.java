/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web.app;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.config.properties.JeePlusProperites;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.security.util.JWTUtil;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 登录Controller
 *
 * @author jeeplus
 * @version 2016-5-31
 */
@RestController
@Api(tags = "移动端登录管理")
@RequestMapping("/app")
public class AppLoginController extends BaseController {


    @Autowired
    JeePlusProperites jeePlusProperites;


    @PostMapping("/sys/login")
    @ApiOperation("登录接口")
    public AjaxJson login(@RequestParam("userName") String userName,
                          @RequestParam("password") String password) {
        AjaxJson j = new AjaxJson();
        User user = UserUtils.getByLoginName(userName);
        if (user != null && UserService.validatePassword(password, user.getPassword())) {

            if (JeePlusProperites.NO.equals(user.getLoginFlag())){
                j.setSuccess(false);
                j.setMsg("该用户已经被禁止登陆!");
            }else {
                j.setSuccess(true);
                j.put(JWTUtil.TOKEN, JWTUtil.createAccessToken(userName, user.getPassword()));
                j.put(JWTUtil.REFRESH_TOKEN, JWTUtil.createRefreshToken(userName, user.getPassword()));
            }

        } else {
            j.setSuccess(false);
            j.setMsg("用户名或者密码错误!");
        }
        return j;
    }


    /**
     * 退出登录
     * @throws IOException
     */
    @ApiOperation("用户退出")
    @GetMapping("/sys/logout")
    public AjaxJson logout() {
        AjaxJson j = new AjaxJson();
        String token = UserUtils.getToken();
        if (StringUtils.isNotBlank(token)) {
            CacheUtils.remove(UserUtils.getUser().getLoginName());
            UserUtils.clearCache();
            UserUtils.getSubject().logout();
        }
        j.setMsg("退出成功");
        return j;
    }


}
