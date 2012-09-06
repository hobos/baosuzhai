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

	public static Long getUserId(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Object oUserId = session.getAttribute(SESSION_KEY);
		return oUserId == null ? null : Long.valueOf(oUserId.toString());
	}
	
	public static void setUserId(HttpServletRequest req, Object oUserId) {
		HttpSession httpSession = req.getSession();
		httpSession.setMaxInactiveInterval(60*30); // 秒
		httpSession.setAttribute(SESSION_KEY, oUserId);
	}

}
