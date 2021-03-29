/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web.app;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.config.properties.FileProperties;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.FileKit;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * 文件管理Controller
 * @author liugf
 * @version 2018-01-21
 */
@RestController
@RequestMapping("/app/sys/file")
public class AppFileController extends BaseController {

	@Autowired
	private FileProperties fileProperties;

	/**
	 * 移动端上传
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping("webupload/upload")
	public AjaxJson webupload( HttpServletRequest request, HttpServletResponse response,MultipartFile file) throws IllegalStateException, IOException {
        AjaxJson j = new AjaxJson();
		String uploadPath = request.getParameter("uploadPath");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
		String token = UserUtils.getToken();
		String fileUrl = FileKit.getAttachmentUrl()+uploadPath+"/"+year+"/"+month+"/";
		String fileDir = FileKit.getAttachmentDir()+uploadPath+"/"+year+"/"+month+"/";
		// 判断文件是否为空
		if (!file.isEmpty()) {
			String name = file.getOriginalFilename ();
			if(fileProperties.isAvailable (name)) {
				// 文件保存路径
				// 转存文件
				FileUtils.createDirectory (fileDir);
				String filePath = fileDir + name;
				File newFile = FileUtils.getAvailableFile (filePath, 0);
				file.transferTo (newFile);
				j.put ("id", FileKit.transDirToUrl (newFile.getAbsolutePath ()));
				j.put ("url", fileUrl + newFile.getName ());
				return j;
			}else{
				return AjaxJson.error ("请勿上传非法文件!");
			}
		}else {
			return AjaxJson.error ("文件不存在!");
		}
	}


}
