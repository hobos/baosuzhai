package com.zizibujuan.drip.server.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.EmailValidator;

import com.zizibujuan.drip.server.service.UserService;
import com.zizibujuan.drip.server.util.servlet.DripServlet;
import com.zizibujuan.drip.server.util.servlet.RequestUtil;
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;

/**
 * 用户注册时，单个校验每个输入框的值
 * @author jinzw
 * @since 0.0.1
 */
public class SignUpCheckServlet extends DripServlet {

	private static final long serialVersionUID = 3796956621336183000L;
	private UserService userService;
	public SignUpCheckServlet(){
		userService = ServiceHolder.getDefault().getUserService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		traceRequest(req);
		String pathInfo = req.getPathInfo();
		if(pathInfo != null && !pathInfo.equals(REST_SEPARATOR)){
			if(pathInfo.equals("/email")){
				// 只有当是有效的邮件的时候，才执行该方法
				Map<String,Object> data = RequestUtil.fromJsonObject(req);
				String email = data.get("value").toString().trim();
				boolean isValidEmail = EmailValidator.getInstance().isValid(email);
				if(!isValidEmail){
					//invalid email
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("msg", "无效的邮箱地址");
					ResponseUtil.toJSON(req, resp, map, HttpServletResponse.SC_FORBIDDEN);
					return;
				}
				
				if(userService.emailIsExist(email)){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("msg", "该邮箱已被使用");
					ResponseUtil.toJSON(req, resp, map, HttpServletResponse.SC_FORBIDDEN);
					return;
				}else{
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("msg", "该邮箱可用");
					ResponseUtil.toJSON(req, resp, map);
					return;
				}
			}
		}
		super.doPost(req, resp);
	}
	
	

}
