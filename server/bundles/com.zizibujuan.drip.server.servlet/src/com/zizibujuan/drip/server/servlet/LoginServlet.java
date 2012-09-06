package com.zizibujuan.drip.server.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizibujuan.drip.server.service.UserService;
import com.zizibujuan.drip.server.servlet.command.LoginCommand;
import com.zizibujuan.drip.server.util.servlet.DripServlet;
import com.zizibujuan.drip.server.util.servlet.RequestUtil;
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;
import com.zizibujuan.drip.server.util.servlet.UserSession;

/**
 * 用户登录，建立会话
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class LoginServlet extends DripServlet {

	private static final long serialVersionUID = 3186980773671995338L;
	private UserService userService = null;

	public LoginServlet() {
		this.userService = ServiceHolder.getDefault().getUserService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		traceRequest(req);
		String pathInfo = req.getPathInfo();
		if (isNullOrSeparator(pathInfo)) {
			// 获取用户登录信息
			Long userId = UserSession.getUserId(req);
			Map<String,Object> loginInfo = userService.getLoginInfo(userId);
			ResponseUtil.toJSON(req, resp, loginInfo);
			return;
		}else{
			if(pathInfo.equals("/form")){
				Map<String, Object> userInfo = RequestUtil.fromJsonObject(req);
				LoginCommand.handleCommand(req, resp, userService, userInfo);
				return;
			}
		}
		super.doPost(req, resp);
	}
}
