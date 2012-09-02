package com.zizibujuan.drip.server.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zizibujuan.drip.server.service.UserService;
import com.zizibujuan.drip.server.util.servlet.DripServlet;
import com.zizibujuan.drip.server.util.servlet.RequestUtil;
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;
import com.zizibujuan.drip.server.util.servlet.UserSession;

/**
 * 用户
 * @author jinzw
 * @since 0.0.1
 */
public class UserServlet extends DripServlet {

	private static final long serialVersionUID = 5222934801942017724L;
	private UserService userService = null;
	
	public UserServlet() {
		this.userService = ServiceHolder.getDefault().getUserService();
	}

	/**
	 * <pre>
	 * 传入的用户信息的格式为:
	 * 		email: 注册邮箱
	 * 		password: 登录密码
	 * 		repassword: 确认密码
	 * 		realName: 真实姓名
	 * </pre>
	 * 密码加密：MD5+salt
	 * 注册用户，需要从邮箱中激活（还差一步功能）
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		traceRequest(req);
		String pathInfo = req.getPathInfo();
		if(isNullOrSeparator(pathInfo)){
			Map<String,Object> userInfo = RequestUtil.fromJsonObject(req);
			// 先做服务器端检验
			if(isValid(userInfo)){
				// 通过校验之后再保存
				userService.add(userInfo);
				// 登录
				String email = userInfo.get("email").toString();
				String password = userInfo.get("password").toString();
				
				userInfo = userService.login(email, password);
				if(userInfo != null){
					// 如果登录成功，则跳转到用户专有首页
					Object oUserId = userInfo.get("DBID");
					UserSession.setUserId(req, oUserId);
					// 返回到客户端，然后客户端跳转到首页
					Map<String,Object> result = new HashMap<String, Object>();
					result.put("status", "1");//1表示注册并且登录成功。
					ResponseUtil.toJSON(req, resp, result);
				}else{
					// 登录失败
					Map<String,Object> result = new HashMap<String, Object>();
					result.put("status", "2");//2表示注册成功但是登录失败。
					ResponseUtil.toJSON(req, resp, result);
				}
			}else{
				// 什么也不做
			}
			return;
		}
		super.doPost(req, resp);
	}

	private boolean isValid(Map<String, Object> userInfo) {
		// 在该方法中，逐个校验，记录下所有的错误，然后一次性的输入到客户端
		// trim后都不为空,trim之后往map中再存一份
		
		// 返回一个表示错误的状态码。
		return true;
	}
}
