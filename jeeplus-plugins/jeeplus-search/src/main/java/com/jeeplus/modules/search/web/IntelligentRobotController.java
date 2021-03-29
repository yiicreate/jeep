package com.jeeplus.modules.search.web;


import com.baidu.aip.speech.AipSpeech;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.form.entity.Form;
import com.jeeplus.modules.form.service.FormService;
import com.jeeplus.modules.search.entity.SearchVo;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.identitylink.api.IdentityLink;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 智能机器人Controller
 * @author lc
 * @version 2021-03-19
 */
@RestController
@RequestMapping("/sys/robot")
public class IntelligentRobotController {

    public static final String APP_ID = "23767244";
    public static final String API_KEY = "8wBQtOVcdYOt0YdHUGO72hGz";
    public static final String SECRET_KEY = "R5LqHOP4Ku1TeKGm2mH4G76Bxkbzan85";

    @Autowired
    private FormService formService;

    @Autowired
    private RepositoryService repositoryService;

    public static void main(String[] args) {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res = client.asr("D:\\1.m4a", "m4a", 16000, null);
        System.out.println(res.toString(2));

//        // 对本地语音文件进行识别
//        String path = "D:\\code\\java-sdk\\speech_sdk\\src\\test\\resources\\16k_test.pcm";
//        JSONObject asrRes = client.asr(path, "pcm", 16000, null);
//        System.out.println(asrRes);
//
//        // 对语音二进制数据进行识别
//        byte[] data = Util.readFileByBytes(path);     //readFileByBytes仅为获取二进制数据示例
//        JSONObject asrRes2 = client.asr(data, "pcm", 16000, null);
//        System.out.println(asrRes2);
    }

    @GetMapping("like")
    public AjaxJson like(String name) {

        List<SearchVo> searchVos = new ArrayList<>();
        /**
         * 模糊查询流程设计
         */
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().active ()
                .latestVersion().orderByProcessDefinitionKey().asc();

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            if(this.isAuth(UserUtils.getUser(), processDefinition.getId())){
                Matcher matcher = pattern.matcher(processDefinition.getName());
                if(matcher.find()){
                    SearchVo searchVo = new SearchVo();
                    searchVo.setName(processDefinition.getName());
                    searchVo.setPathId(processDefinition.getId());
                    searchVo.setType(0);
                    searchVos.add(searchVo);
                }
            }
        }

        /**
         * 模糊查询动态表单
         */
        Form entity = new Form();
        entity.setName(name);
        List<Form> list = formService.findList(entity);

        for (Form form : list) {
            SearchVo searchVo = new SearchVo();
            searchVo.setName(form.getName());
            searchVo.setPathId(form.getId());
            searchVo.setType(1);
            searchVos.add(searchVo);
        }

        return AjaxJson.success().put("like", searchVos);
    }

    public boolean isAuth(User user, String processDefId) {
        List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefId);
        if(identityLinks.size() == 0){
            return true;
        }
        for (IdentityLink identityLink : identityLinks ) {
            if(user.getId().equals(identityLink.getUserId())){
                return true;
            }
            if((","+user.getRoleIds()+",").contains(","+identityLink.getGroupId()+",")) {
                return true;
            }
        }
        return false;
    }
}
