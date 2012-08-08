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
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;

/**
 * 为了使用rest风格的资源连接，所有请求html页面，都不使用html后缀，而是后台自动解析。 只要请求非ajax请求，没有后缀的就加上html。
 * 暂时搁置，因为这种解决方案无法满足有子资源的情况，即显示一个访问某班某个学生的详情页面时，没有办法定位。
 * @author jinzw
 * @since 0.0.1
 */
public class RestHtmlFilter implements Filter {

	private static final String HTML_EXTENSION = ".html";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) resp;
		System.out.println("ServletPath:"+httpRequest.getServletPath());
		System.out.println("PathInfo:"+httpRequest.getPathInfo());
		System.out.println("ContextPath:"+httpRequest.getContextPath());
		
		
		
		if (!RequestUtil.isAjax(httpRequest)) {
			String servletPath = httpRequest.getServletPath();
			String pathInfo = httpRequest.getPathInfo();
			
			if(pathInfo == null){
				pathInfo = "";
			}
			if(pathInfo.endsWith("/")){
				pathInfo = pathInfo.substring(0, pathInfo.length()-1);
			}
			

			String fileName = servletPath + pathInfo + HTML_EXTENSION;
			String fileNamea = httpRequest.getServletContext().getRealPath("");
			ResponseUtil.toHTMLFile(httpRequest, httpResponse, "/exercises.html");
			
			
			if(pathInfo.lastIndexOf(".")>-1){
				chain.doFilter(req, resp);
				return;
			}
			
//			String fileName = servletPath + pathInfo + HTML_EXTENSION;
//			String fileNamea = httpRequest.getServletContext().getRealPath(fileName);
//			ResponseUtil.toHTMLFile(httpRequest, httpResponse, "/exercises.html");
			
			
			return;
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
