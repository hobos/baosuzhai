package com.zizibujuan.drip.server.servlet.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizibujuan.drip.server.service.UserService;
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;
import com.zizibujuan.drip.server.util.servlet.UserSession;

/**
 * 用户登录操作
 * @author jinzw
 * @since 0.0.1
 */
public class LoginCommand{
	private static final String KEY_LOGIN = "login";
	private static final String KEY_PASSWORD = "password";

	/**
	 * <pre>
	 * 约定传入的值的key为
	 * 		login:登录名
	 * 		password:登录密码
	 * </pre>
	 * @param req
	 * @param resp
	 * @param userService
	 * @param userInfo
	 * @throws IOException
	 */
	public static void handleCommand(HttpServletRequest req, HttpServletResponse resp, UserService userService, Map<String,Object> userInfo)
			throws IOException {
		// 登录
		String email = userInfo.get(KEY_LOGIN).toString();
		String password = userInfo.get(KEY_PASSWORD).toString();
		Map<String,Object> existUserInfo = userService.login(email, password);
		if(existUserInfo != null){
			// 如果登录成功，则跳转到用户专有首页
			Object oUserId = existUserInfo.get("DBID");
			UserSession.setUserId(req, oUserId);
			// 返回到客户端，然后客户端跳转到首页
			Map<String,Object> result = new HashMap<String, Object>();
			result.put("status", "1");// 1表示登录成功。
			ResponseUtil.toJSON(req, resp, result);
		}else{
			// 登录失败
			Map<String,Object> result = new HashMap<String, Object>();
			result.put("status", "2");// 2表示登录失败。
			ResponseUtil.toJSON(req, resp, result);
		}
	}

}
