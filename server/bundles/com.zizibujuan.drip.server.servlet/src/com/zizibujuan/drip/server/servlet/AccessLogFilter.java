package com.zizibujuan.drip.server.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizibujuan.drip.server.service.AccessLogService;
import com.zizibujuan.drip.server.service.ApplicationPropertyService;
import com.zizibujuan.drip.server.util.servlet.UserSession;

/**
 * 用户访问日志过滤器
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class AccessLogFilter implements Filter {

	private AccessLogService accessLogService;
	private ApplicationPropertyService applicationPropertyService;
	
	private static final String COKIE_NAME = "zzbj_user_cookie_id";
	
	@Override
	public void destroy() {
		accessLogService = null;
		applicationPropertyService = null;
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
		
		String pathInfo = httpServletRequest.getPathInfo();
		
		// TODO：提取常量
		if(pathInfo != null && (pathInfo.endsWith("index.html") || pathInfo.endsWith("dashboard.html") || pathInfo.indexOf(".")==-1)){

			
			// 获取访问者ip
			String ip = httpServletRequest.getRemoteAddr();
			// 获取用户标识
			//		如果用户没有登录，则从cookie中获取
			//		如果用户登录，则从session中获取
			Long userId = null;
			boolean anonymous = true;
			if(UserSession.isLogged(httpServletRequest)){
				userId = UserSession.getUserId(httpServletRequest);
				anonymous = false;
			}else{
				Cookie[] cookies = httpServletRequest.getCookies();
				
				// 判断是否存在cookie，如果不存在的话，则新建一个
				if(cookies == null){
					userId = applicationPropertyService.getNextAnonymouseId();
					Cookie cookie = new Cookie(COKIE_NAME, userId.toString());
					httpServletResponse.addCookie(cookie);
				}else{
					for(Cookie c : cookies){
						if(c.getName().equals(COKIE_NAME)){
							userId = Long.valueOf(c.getValue());
							break;
						}
					}
					if(userId == null){
						userId = applicationPropertyService.getNextAnonymouseId();
						Cookie cookie = new Cookie(COKIE_NAME, userId.toString());
						httpServletResponse.addCookie(cookie);
					}
				}
			}
			
			// 获取用户访问的上一个页面
			String urlFrom = httpServletRequest.getHeader("Referer");
			
			// 获取用户当前访问的一个页面
			String urlAccess = httpServletRequest.getRequestURI();
			
			// FIXME: 到底需不需要引入实体类呢？
			// FIXME: 是存入数据库好呢，还是存入文本文件好呢？
			accessLogService.log(ip, anonymous, userId, urlFrom, urlAccess);
		}
				
				
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		accessLogService = ServiceHolder.getDefault().getAccessLogService();
		applicationPropertyService = ServiceHolder.getDefault().getApplicationPropertyService();
	}

}
