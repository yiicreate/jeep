package com.jeeplus.core.servlet;


import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.config.properties.JeePlusProperites;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns = "/txtFile/*")
public class BooksReadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static String characterCoding = "UTF-8";

    public void fileOutputStream(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String filepath = req.getRequestURI();
        int index = filepath.indexOf(JeePlusProperites.USERFILES_BASE_URL);
        if (index >= 0) {
            filepath = filepath.substring(index + JeePlusProperites.USERFILES_BASE_URL.length());
        }

        filepath = UriUtils.decode(filepath, characterCoding);
        JeePlusProperites jeePlusProperites = SpringContextHolder.getBean(JeePlusProperites.class);
        File file = new File(jeePlusProperites.getUserfilesBaseDir() + JeePlusProperites.USERFILES_BASE_URL + filepath);
        //此处修改为断点续传模式，实现视频分段解析，解决视频加载缓慢及微信解析异常问题
        try {
            ReadFile(file, resp, req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        fileOutputStream(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        fileOutputStream(req, resp);
    }

    public void ReadFile(File file, HttpServletResponse response, HttpServletRequest request) throws Exception {

        StringBuilder builder = new StringBuilder();
        String books = "";

        try {
            String encoding = characterCoding;
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    builder.append(lineTxt);
                }
                read.close();
                books = builder.toString();
            }else{
//                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-type", "text/html;charset=UTF-8");

        os.write(books.getBytes(characterCoding));
    }
}
