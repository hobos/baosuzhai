package com.zizibujuan.drip.server.util.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户session帮助类
 * @author jinzw
 * @since 0.0.1
 */
public abstract class UserSession {
	
	private static final String SESSION_KEY = "drip-user";

	public static Object getUserId(HttpServletRequest req) {
		return req.getSession(false).getAttribute(SESSION_KEY);
	}
	
	public static void setUserId(HttpServletRequest req, Object oUserId) {
		HttpSession httpSession = req.getSession();
		httpSession.setMaxInactiveInterval(60*30); // 秒
		httpSession.setAttribute(SESSION_KEY, oUserId);
	}

}
