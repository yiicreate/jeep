/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tools.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.tools.utils.HttpToolUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具测试类
 *
 * @author lgf
 * @version 2016-01-07
 */
@RestController
@RequestMapping("/http")
public class HttpToolController extends BaseController {


    /**
     * 接口内部请求
     *
     * @param
     * @throws Exception
     */
    @GetMapping("/get")
    public Object getTest(String serverUrl, String requestBody ) {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success", str = "", rTime = "";
        try {
            long startTime = System.currentTimeMillis();
                Map<String, String> params = new HashMap<String, String>();

                if (requestBody != null && !requestBody.equals("")) {
                    String[] paramList = requestBody.split("&");

                    for (String param : paramList) {
                        if (param.split("=").length == 2) {
                            params.put(param.split("=")[0], param.split("=")[1]);
                        } else {
                            params.put(param.split("=")[0], "");
                        }
                    }
                }
                HttpToolUtils test = new HttpToolUtils(serverUrl, params);

                str = test.post();

            long endTime = System.currentTimeMillis();
            rTime = String.valueOf(endTime - startTime);
        } catch (Exception e) {
            errInfo = "error";
            str = e.getMessage();
        }
        return AjaxJson.success().put("errInfo", errInfo).put("result", str).put("rTime", rTime);
    }


    /**
     * 接口内部请求
     *
     * @param
     * @throws Exception
     */
    @GetMapping("/post")
    public Object postTest(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success", str = "", rTime = "";
        try {
            long startTime = System.currentTimeMillis();
            String s_url = request.getParameter("serverUrl");//请求起始时间_毫秒
            String type = request.getParameter("requestMethod");
            String requestBody = request.getParameter("requestBody");
            URL url;
            if (type.equals("POST")) {//请求类型  POST or GET
                Map<String, String> params = new HashMap<String, String>();

                if (requestBody != null && !requestBody.equals("")) {
                    String[] paramList = requestBody.split("&");

                    for (String param : paramList) {
                        if (param.split("=").length == 2) {
                            params.put(param.split("=")[0], param.split("=")[1]);
                        } else {
                            params.put(param.split("=")[0], "");
                        }
                    }
                }
                HttpToolUtils test = new HttpToolUtils(s_url, params);

                str = test.post();
            } else {
                url = new URL(s_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                //请求结束时间_毫秒
                String temp = "";
                while ((temp = in.readLine()) != null) {
                    str = str + temp;
                }

            }

            long endTime = System.currentTimeMillis();
            rTime = String.valueOf(endTime - startTime);
        } catch (Exception e) {
            errInfo = "error";
            str = e.getMessage();
        }
        map.put("errInfo", errInfo);    //状态信息
        map.put("result", str);            //返回结果
        map.put("rTime", rTime);        //服务器请求时间 毫秒
        return map;
    }



}
