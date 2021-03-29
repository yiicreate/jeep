package com.jeeplus.modules.sys.security;


import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.modules.sys.entity.SysConfig;
import com.jeeplus.modules.sys.security.util.JWTUtil;
import com.jeeplus.modules.sys.service.SysConfigService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @description: shiro 自定义filter 实现 并发登录控制
 */
public class KickoutSessionControlFilter  extends AccessControlFilter{

    /** 踢出后到的地址 */
    private String kickoutUrl;

    /**  踢出之前登录的/之后登录的用户 默认踢出之前登录的用户 */
    private boolean kickoutAfter = false;

    /**  同一个帐号最大会话数 默认1 */
    private int maxSession = 1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;
    @Autowired
    private SysConfigService sysConfigService;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }
    /**
     * 是否允许访问，返回true表示允许
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }



    /**
     * 表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，返回false表示自己已经处理了（比如重定向到另一个页面）。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        Session session = subject.getSession();
        String token  = (String) subject.getPrincipal();
        String loginName = JWTUtil.getLoginName(token);
        Serializable sessionId = session.getId();

        // 初始化用户的队列放到缓存里
        Deque<Serializable> deque = (Deque<Serializable>) CacheUtils.get(loginName);
        if(deque == null) {
            deque = new LinkedList<Serializable>();
            CacheUtils.put(loginName, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
            CacheUtils.put(loginName, deque);
        }
        SysConfig config = sysConfigService.get("1");
        //如果队列里的sessionId数超出最大会话数，开始踢人
        if ("1".equals(config.getMultiAccountLogin())) {
            while(deque.size() > maxSession) {
                Serializable kickoutSessionId = null;
                if("2".equals(config.getSingleLoginType())) { //如果踢出后者
                    kickoutSessionId = deque.removeFirst();
                    try {
                        Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                        if(kickoutSession != null) {
                            //设置会话的kickout属性表示踢出了
                            kickoutSession.setAttribute("forbid", true);
                        }
                    } catch (Exception e) {//ignore exception
                        e.printStackTrace();
                    }
                } else { //否则踢出前者
                    kickoutSessionId = deque.removeLast();
                    try {
                        Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                        if(kickoutSession != null) {
                            //设置会话的kickout属性表示踢出了
                            kickoutSession.setAttribute("kickout", true);
                        }
                    } catch (Exception e) {//ignore exception
                        e.printStackTrace();
                    }
                }

            }
        }


        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) {
            }
            request.getRequestDispatcher("/403/2").forward(request,response);
            return false;
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("forbid") != null) {
            //会话被踢出了
            try {
                subject.logout();

            } catch (Exception e) {
            }
            request.getRequestDispatcher("/403/1").forward(request,response);
            return false;
        }
        return true;
    }
}
