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

/**
 * 指向欢迎页面的过滤器。
 * 使用org.eclipse.orion.server.configurator中的WelcomeFileFilter.java 谢谢。
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class WelcomeFileFilter implements Filter {

	private static final String WELCOME_FILE_NAME = "exercises.html"; //"index.html";//$NON-NLS-1$
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
		if (requestPath.endsWith("/") && isIncluded(requestPath) && !isExcluded(requestPath)) { //$NON-NLS-1$
			response = new HttpServletResponseWrapper((HttpServletResponse) response) {

				private boolean handleWelcomeFile(int sc) {
					if (sc == SC_NOT_FOUND || sc == SC_FORBIDDEN) {
						try {
							httpRequest.getRequestDispatcher(requestPath + WELCOME_FILE_NAME).forward(httpRequest, getResponse());
							return true;
						} catch (Exception e) {
							// fall through
						}
					}
					return false;
				}

				public void sendError(int sc) throws IOException {
					if (!handleWelcomeFile(sc)) {
						super.sendError(sc);
					}
				}

				public void sendError(int sc, String msg) throws IOException {
					if (!handleWelcomeFile(sc)) {
						super.sendError(sc, msg);
					}
				}

				public void setStatus(int sc) {
					if (!handleWelcomeFile(sc)) {
						super.setStatus(sc);
					}
				}
			};
		}
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
		// TODO Auto-generated method stub

	}

}
