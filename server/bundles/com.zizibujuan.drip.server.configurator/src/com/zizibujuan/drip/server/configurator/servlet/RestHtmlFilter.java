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

import com.zizibujuan.drip.server.util.servlet.RequestUtil;

/**
 * 为了使用rest风格的资源连接，所有请求html页面，都不使用html后缀，而是后台自动解析。 只要请求非ajax请求，没有后缀的就加上html。
 * 暂时搁置，因为这种解决方案无法满足有子资源的情况，即显示一个访问某班某个学生的详情页面时，没有办法定位。
 * @author jinzw
 * @since 0.0.1
 */
public class RestHtmlFilter implements Filter {

	private static final String ROOT_WEB = "/drip";
	private static final String LIST_HTML = "/list.html";
	private static final String NEW_PATHINFO = "/new";
	private static final String HTML = ".html";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) resp;
		System.out.println("ServletPath:'"+httpRequest.getServletPath()+"'");
		System.out.println("PathInfo:'"+httpRequest.getPathInfo()+"'");
		System.out.println("ContextPath:'"+httpRequest.getContextPath()+"'");
		
		// 因为约定rest名与文件夹名相同，所以servletPath中存储的通常是servlet的别名。
		String servletPath = httpRequest.getServletPath();
		if(!servletPath.equals("")){
			if (!RequestUtil.isAjax(httpRequest)) {
				String pathInfo = httpRequest.getPathInfo();
				if(pathInfo == null || pathInfo.equals("/")){			
					//FIXME:暂时支持一级路径。即把所有的list的html容器都放在根目录下
					String fileName = ROOT_WEB + servletPath + LIST_HTML;
					httpRequest.getRequestDispatcher(fileName).forward(httpRequest, httpResponse);
					return;
				}else if(pathInfo.equals(NEW_PATHINFO)){
					String fileName = ROOT_WEB + servletPath + pathInfo + HTML;
					httpRequest.getRequestDispatcher(fileName).forward(httpRequest, httpResponse);
					return;
				}
			}
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
