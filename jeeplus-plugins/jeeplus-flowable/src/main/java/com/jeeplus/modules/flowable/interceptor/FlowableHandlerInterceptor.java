package com.jeeplus.modules.flowable.interceptor;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.ui.common.security.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求拦截器，Flowable动态设置用户信息
 * @author liugaofeng
 * @version 2018-8-19
 * 需要增加拦截器，动态设置Flowable用户信息
 *
 */
public class FlowableHandlerInterceptor implements HandlerInterceptor {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String servletPath = request.getServletPath();
		com.jeeplus.modules.sys.entity.User u = UserUtils.getUser();
		if (servletPath.startsWith("/app") || servletPath.startsWith("/idm")) {
			User currentUserObject = SecurityUtils.getCurrentUserObject();
			if (currentUserObject == null || StringUtils.isBlank (currentUserObject.getId ())) {
				User user = new UserEntityImpl();
				user.setId(u.getId());
				user.setFirstName(u.getName());
				user.setLastName("");
				user.setEmail(u.getEmail());
				SecurityUtils.assumeUser(user);
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
