/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tools.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.FileKit;
import com.jeeplus.modules.tools.utils.TwoDimensionCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 二维码Controller
 *
 * @author jeeplus
 * @version 2015-11-30
 */
@RestController
@RequestMapping("/tools/TwoDimensionCodeController")
public class TwoDimensionCodeController extends BaseController {

    /**
     * 生成二维码
     *
     * @throws Exception
     */
    @PostMapping("createTwoDimensionCode")
    public AjaxJson createTwoDimensionCode(String encoderContent) {
        String realPath = FileKit.getAttachmentDir() + "qrcode/";
        FileUtils.createDirectory(realPath);
        String name = "test.png"; //encoderImgId此处二维码的图片名
        String filePath = realPath + name;  //存放路径
        TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");//执行生成二维码
        return AjaxJson.success("二维码生成成功").put("filePath", FileKit.getAttachmentUrl() + "qrcode/" + name);
    }

}
