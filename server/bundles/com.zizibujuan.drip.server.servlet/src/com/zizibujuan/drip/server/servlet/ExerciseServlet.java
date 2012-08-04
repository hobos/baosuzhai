package com.zizibujuan.drip.server.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizibujuan.drip.server.service.ExerciseService;
import com.zizibujuan.drip.server.util.servlet.DripServlet;
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;

/**
 * 习题
 * @author 金正伟
 * @since 0.0.1
 */
public class ExerciseServlet extends DripServlet{
	private static final long serialVersionUID = 3368960336480220523L;
	
	private ExerciseService exerciseService = ServiceHolder.getDefault().getExerciseService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		
		if(pathInfo == null || pathInfo.equals(REST_SEPARATOR)){
			List<Map<String,Object>> exercises = exerciseService.get();
			ResponseUtil.toJSON(req, resp, exercises);
			return;
		}
		super.doGet(req, resp);
	}

}