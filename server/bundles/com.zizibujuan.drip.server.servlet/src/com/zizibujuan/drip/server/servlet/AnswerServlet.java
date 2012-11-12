package com.zizibujuan.drip.server.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizibujuan.drip.server.service.AnswerService;
import com.zizibujuan.drip.server.util.servlet.DripServlet;
import com.zizibujuan.drip.server.util.servlet.RequestUtil;
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;
import com.zizibujuan.drip.server.util.servlet.UserSession;

/**
 * 习题答案
 * @author jinzw
 * @since 0.0.1
 */
public class AnswerServlet extends DripServlet {

	private static final long serialVersionUID = -1785829869948967937L;
	private AnswerService answerService = null;
	

	public AnswerServlet() {
		answerService = ServiceHolder.getDefault().getAnswerService();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		traceRequest(req);
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")){
			// TODO:从query parameters中获取参数
			String sExerId = req.getParameter("exerId");
			Long exerciseId = Long.valueOf(sExerId);
			Long userId = UserSession.getUserId(req);
			Map<String,Object> answer = answerService.get(userId, exerciseId);
			ResponseUtil.toJSON(req, resp, answer);
			return;
		}
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		traceRequest(req);
		
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")){
			Map<String,Object> data = RequestUtil.fromJsonObject(req);
			Long userId = UserSession.getUserId(req);
			answerService.save(userId, data);
			return;
		}
		super.doPost(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		traceRequest(req);
		String pathInfo = req.getPathInfo();
		if(pathInfo != null && !pathInfo.equals("/")){
			Long answerId = Long.valueOf(pathInfo.split("/")[1]);
			Map<String,Object> data = RequestUtil.fromJsonObject(req);
			Long userId = UserSession.getUserId(req);
			answerService.update(answerId,userId, data);
			return;
		}
		super.doPut(req, resp);
	}
	
	

	
}
