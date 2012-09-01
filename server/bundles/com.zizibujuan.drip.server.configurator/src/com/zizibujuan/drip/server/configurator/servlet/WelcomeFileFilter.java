package com.zizibujuan.drip.server.configurator.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 指向欢迎页面的过滤器。
 * 使用org.eclipse.orion.server.configurator中的WelcomeFileFilter.java 谢谢。
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class WelcomeFileFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(WelcomeFileFilter.class);
	
	private static final String PUBLIC_WELCOME_FILE_NAME = "index.html";//"exercises"; //$NON-NLS-1$
	private static final String PRIVATE_WELCOME_FILE_NAME = "myDashboard.html";//"exercises"; //$NON-NLS-1$

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final String requestPath = httpRequest.getServletPath() + (httpRequest.getPathInfo() == null ? "" : httpRequest.getPathInfo()); //$NON-NLS-1$
		logger.info("跳转到WelcomeFileFilter中，请求路径为:"+requestPath);
		System.out.println("跳转到WelcomeFileFilter中，请求路径为:'"+requestPath+"'");
		if (requestPath.equals("/")) { //$NON-NLS-1$
			System.out.println("初步满足跳转要求");
			String fileName = "";
			if(httpRequest.getSession(false)==null){
				fileName = requestPath + PUBLIC_WELCOME_FILE_NAME;
			}else{
				fileName = requestPath + PRIVATE_WELCOME_FILE_NAME;
			}
			httpRequest.getRequestDispatcher(fileName).forward(httpRequest, response);
			
			return;
		}
		HttpServletResponse resp = (HttpServletResponse) response;
		System.out.println("状态码为："+resp.getStatus());
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// do nothing

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
