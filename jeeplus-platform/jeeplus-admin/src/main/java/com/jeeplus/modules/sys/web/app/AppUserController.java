/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web.app;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.config.properties.FileProperties;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.FileKit;
import com.jeeplus.modules.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author jeeplus
 * @version 2016-8-29
 */
@RestController
@RequestMapping("/app/sys/user")
@Api(tags ="用户管理")
public class AppUserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private FileProperties fileProperties;

    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return userService.get(id);
        } else {
            return new User();
        }
    }


    @GetMapping("list")
    public AjaxJson list(User user, HttpServletRequest request, HttpServletResponse response) {
        Page<User> page = userService.findPage(new Page<User>(request, response), user);
        return AjaxJson.success().put("page", page);
    }


    /**
     * 用户头像显示编辑保存
     *
     * @return
     * @throws IOException
     * @throws IllegalStateException
     */
    @PostMapping("imageUpload")
    @ApiOperation(value = "上传头像")
    public AjaxJson imageUpload(@RequestParam("file")MultipartFile file) throws IllegalStateException, IOException {
        User currentUser = UserUtils.getUser();
        // 判断文件是否为空
        if (!file.isEmpty()) {
            if(fileProperties.isImage (file.getOriginalFilename ())){

                // 文件保存路径
                String realPath = FileKit.getAttachmentDir() + "sys/user/images/";
                // 转存文件
                FileUtils.createDirectory(realPath);
                file.transferTo(new File(realPath + file.getOriginalFilename()));
                currentUser.setPhoto(FileKit.getAttachmentUrl() + "sys/user/images/" + file.getOriginalFilename());
                userService.updateUserInfo(currentUser);
                return AjaxJson.success("上传成功!").put("path", FileKit.getAttachmentUrl() + "sys/user/images/" + file.getOriginalFilename());
            }else {
                return AjaxJson.error ("请上传图片!");
            }
        }else{
            return AjaxJson.error ("文件不存在!");
        }



    }

    /**
     * 返回用户信息
     *
     * @return
     */
    @GetMapping("info")
    @ApiOperation(value = "获取当前用户信息")
    public AjaxJson infoData() {
        return AjaxJson.success("获取个人信息成功!").put("role", UserUtils.getRoleList()).put("user", UserUtils.getUser());
    }


    @PostMapping("savePwd")
    @ApiOperation(value = "修改密码")
    public AjaxJson savePwd(String oldPassword, String newPassword, Model model) {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
            if (jeePlusProperites.isDemoMode()) {
                return AjaxJson.error("演示模式，不允许操作！");
            }
            if (UserService.validatePassword(oldPassword, user.getPassword())) {
                userService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
                return AjaxJson.success("修改密码成功！");
            } else {
                return AjaxJson.error("修改密码失败，旧密码错误！");
            }
        }
        return AjaxJson.error("参数错误！");
    }



    /**
     * 获取机构JSON数据。
     * @return
     */
    @RequiresPermissions("user")
    @GetMapping("treeData")
    public AjaxJson treeData(){
        List<Office> list = UserUtils.getOfficeAllList ();
        List rootTree = getRootTree(list);
        return AjaxJson.success().put("treeData",rootTree);
    }

    private List<Map> getRootTree(List<Office> list) {
        List<Map> offices = Lists.newArrayList();
        List<Office> rootTrees = officeService.getChildren("0");
        for (Office root:rootTrees){
            offices.add(getChildOfTree(root, list));
        }
        return offices;
    }

    private  Map getChildOfTree(Office officeItem,  List<Office> officeList) {
        Map oMap = new HashMap ();
        oMap.put ("id","o_" + officeItem.getId ());
        oMap.put ("type", "office");
        oMap.put ("label", officeItem.getName ());
        List children = Lists.newArrayList ();
        oMap.put ("children",children);
        List<User> list = userService.findUserByOfficeId(officeItem.getId ());
        for (int i = 0; i < list.size(); i++) {
            User e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id",   e.getId());
            map.put ("type", "user");
            map.put("label", e.getName());
            children.add(map);
        }
        for (Office child : officeList) {
            if (child.getParentId().equals(officeItem.getId())) {
                children.add(getChildOfTree (child, officeList));
            }
        }
        return oMap;
    }




}
