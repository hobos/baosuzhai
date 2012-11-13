package com.zizibujuan.drip.server.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizibujuan.drip.server.service.ExerciseService;
import com.zizibujuan.drip.server.util.servlet.DripServlet;
import com.zizibujuan.drip.server.util.servlet.RequestUtil;
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;
import com.zizibujuan.drip.server.util.servlet.UserSession;

/**
 * 习题,修改完习题后，需要对修改的内容进行审批。
 * 如果要修改习题的选项，则要记录历史的选项列表。并将答案与这些习题的具体版本相关联。
 * @author jinzw
 * @since 0.0.1
 */
public class ExerciseServlet extends DripServlet{
	private static final long serialVersionUID = 3368960336480220523L;
	
	private ExerciseService exerciseService = ServiceHolder.getDefault().getExerciseService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		
		if(isNullOrSeparator(pathInfo)){
			List<Map<String,Object>> exercises = exerciseService.get();
			ResponseUtil.toJSON(req, resp, exercises);
			return;
		}
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if(isNullOrSeparator(pathInfo)){
			Map<String,Object> exerciseInfo = RequestUtil.fromJsonObject(req);
			// 如果保存成功，则返回一个成功的状态码
			exerciseInfo.put("userId", UserSession.getUserId(req));
			exerciseService.add(exerciseInfo);
			// 因为界面上常用的当前用户的统计数是从session中获取的，所以要更新session中的值
			UserSession.increaseExerciseCount(req);
			if(exerciseInfo.get("answers")!=null){
				UserSession.increaseAnswerCount(req);
			}
			return;
		}
		super.doPost(req, resp);
	}
}
