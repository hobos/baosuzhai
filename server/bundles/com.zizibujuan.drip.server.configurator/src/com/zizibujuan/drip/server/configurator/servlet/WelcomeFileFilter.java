package com.zizibujuan.drip.server.configurator.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

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
	
	private static final String WELCOME_FILE_NAME ="exercises"; // "index.html"//$NON-NLS-1$
	private final List<String> includes = new ArrayList<String>();
	private final List<String> excludes = new ArrayList<String>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String includesParameter = filterConfig.getInitParameter("includes"); //$NON-NLS-1$
		if (includesParameter != null) {
			StringTokenizer tokenizer = new StringTokenizer(includesParameter, ",", false); //$NON-NLS-1$
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().trim();
				includes.add(token);
			}
		}

		String excludesParameter = filterConfig.getInitParameter("excludes"); //$NON-NLS-1$
		if (excludesParameter != null) {
			StringTokenizer tokenizer = new StringTokenizer(excludesParameter, ",", false); //$NON-NLS-1$
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().trim();
				excludes.add(token);
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final String requestPath = httpRequest.getServletPath() + (httpRequest.getPathInfo() == null ? "" : httpRequest.getPathInfo()); //$NON-NLS-1$
		logger.info("跳转到WelcomeFileFilter中，请求路径为:"+requestPath);
		System.out.println("跳转到WelcomeFileFilter中，请求路径为:'"+requestPath+"'");
		if (requestPath.endsWith("/") && isIncluded(requestPath) && !isExcluded(requestPath)) { //$NON-NLS-1$
			System.out.println("初步满足跳转要求");
			response = new HttpServletResponseWrapper((HttpServletResponse) response) {

				private boolean handleWelcomeFile(int sc) {
					System.out.println("handleWelcomeFile中状态码为："+sc);
					// FIXME:不知什么原因，在生产服务器上状态码，直接输入域名时状态码一直为200
					// 分明就没有对应的资源，为什么会返回200呢？
					if (sc == SC_NOT_FOUND || sc == SC_FORBIDDEN) {
						try {
							System.out.println("跳转到欢迎页面:"+requestPath + WELCOME_FILE_NAME);
							httpRequest.getRequestDispatcher(requestPath + WELCOME_FILE_NAME).forward(httpRequest, getResponse());
							return true;
						} catch (Exception e) {
							// fall through
						}
					}
					return false;
				}

				@Override
				public void sendError(int sc) throws IOException {
					if (!handleWelcomeFile(sc)) {
						super.sendError(sc);
					}
				}

				@Override
				public void sendError(int sc, String msg) throws IOException {
					if (!handleWelcomeFile(sc)) {
						super.sendError(sc, msg);
					}
				}
				
				@Override
				public void setStatus(int sc) {
					if (!handleWelcomeFile(sc)) {
						super.setStatus(sc);
					}
				}

				@Override
				@Deprecated
				public void setStatus(int sc, String sm) {
					if (!handleWelcomeFile(sc)) {
						super.setStatus(sc, sm);
					}
				}
			};
		}
		HttpServletResponse resp = (HttpServletResponse) response;
		System.out.println("状态码为："+resp.getStatus());
		chain.doFilter(request, response);

	}
	
	private boolean isIncluded(String requestPath) {
		return includes.isEmpty() || includes.contains(requestPath);
	}

	private boolean isExcluded(String requestPath) {
		return excludes.contains(requestPath);
	}

	@Override
	public void destroy() {
		// do nothing

	}

}
