package com.zizibujuan.drip.server.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizibujuan.drip.server.service.ActivityService;
import com.zizibujuan.drip.server.util.PageInfo;
import com.zizibujuan.drip.server.util.servlet.DripServlet;
import com.zizibujuan.drip.server.util.servlet.ResponseUtil;
import com.zizibujuan.drip.server.util.servlet.UserSession;

/**
 * 个人首页
 * @author jinzw
 * @since 0.0.1
 */
public class DashboardServlet extends DripServlet {

	private static final long serialVersionUID = -6808419824643346046L;

	private ActivityService activityService;
	
	public DashboardServlet(){
		activityService = ServiceHolder.getDefault().getActivityService();
	}
	/**
	 * 获取登录用户和登录用户关注的人的最新活动列表。
	 * <pre>
	 * 目前支持的关注对象为：
	 * 		人
	 * 		资源（习题，讲义和视频等）
	 * 		关注分组（如班级，也许取名为"加入班级"更好，可关注多个分组）
	 * </pre>
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if(pathInfo == null || pathInfo.equals("/")){
			Long userId = UserSession.getUserId(req);
			PageInfo pageInfo = null;
			List<Map<String,Object>> result = activityService.get(userId,pageInfo);
			ResponseUtil.toJSON(req, resp, result);
			return;
		}
		super.doGet(req, resp);
	}
}
