package com.zizibujuan.drip.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizibujuan.drip.server.util.servlet.DripServlet;

/**
 * 用户注销，删除会话
 * @author jinzw
 * @since 0.0.1
 */
public class LogoutServlet extends DripServlet {

	private static final long serialVersionUID = -4415570541175504883L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		traceRequest(req);
		String pathInfo = req.getPathInfo();
		if(isNullOrSeparator(pathInfo)){
			req.getSession().invalidate();
			// 注销之后，跳转到首页。
			// FIXME：要是可以停留在当时的页面，但是显示适合该页面的不同内容，会不会更好些呢？
			resp.setStatus(HttpServletResponse.SC_FOUND);
			return;
		}
		super.doPost(req, resp);
	}

}
