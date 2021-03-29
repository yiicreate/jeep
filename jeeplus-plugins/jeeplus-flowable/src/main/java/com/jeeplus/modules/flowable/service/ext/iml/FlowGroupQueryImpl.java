package com.jeeplus.modules.flowable.service.ext.iml;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.flowable.utils.FlowableUtils;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.idm.api.Group;
import org.flowable.idm.engine.impl.GroupQueryImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展Flowable组Service
 * @author liugoafeng
 * @version 2019-11-02
 */
public class FlowGroupQueryImpl extends GroupQueryImpl {

	private static final long serialVersionUID = 1L;

	private UserService userService;

	public UserService getSystemService() {
		if (userService == null){
			userService = SpringContextHolder.getBean(UserService.class);
		}
		return userService;
	}

	@Override
    public long executeCount(CommandContext commandContext) {
        return executeQuery().size();
    }

    @Override
    public List<Group> executeList(CommandContext commandContext) {
        return executeQuery();
    }

    protected List<Group> executeQuery() {
        if (getUserId() != null) {
            return findGroupsByUser(getUserId());
        } else {
            return findAllGroups();
        }
    }


    protected List<Group> findGroupsByUser(String userId) {
    	List<Group> list = Lists.newArrayList();
		User user = getSystemService().getUserByLoginName(userId);
		if (user != null && user.getRoleList() != null){
			for (Role role : user.getRoleList()){
				list.add(FlowableUtils.toFlowableGroup(role));
			}
		}

    	return list;
    }

    protected List<Group> findAllGroups() {
    	return new ArrayList<>();
    }
}
