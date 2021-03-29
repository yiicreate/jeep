package com.jeeplus.modules.search.web;


import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.flowable.service.FlowProcessService;
import com.jeeplus.modules.form.entity.Form;
import com.jeeplus.modules.form.service.FormService;
import com.jeeplus.modules.search.entity.SearchVo;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.identitylink.api.IdentityLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 系统智能搜索Controller
 * @author lc
 * @version 2021-03-15
 */
@RestController
@RequestMapping("/sys/search")
public class SearchController extends BaseController {


	@Autowired
	private FormService formService;

	@Autowired
	private RepositoryService repositoryService;

	/**
	 * 智能问答请求接口，前端语音转文字后接收字符参数
	 * @param name
	 * @return
	 */
	@GetMapping("robot")
	public AjaxJson robot(String name) {
		return this.search(name.replaceAll("[\\pP\\pS\\pZ]", ""), "robot");
	}

	/**
	 * 智能检索请求接口
	 * @param name
	 * @return
	 */
	@GetMapping("like")
	public AjaxJson like(String name) {
		return this.search(name, "like");
	}

	public AjaxJson search(String name, String alis) {

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

		return AjaxJson.success().put(alis, searchVos);
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
