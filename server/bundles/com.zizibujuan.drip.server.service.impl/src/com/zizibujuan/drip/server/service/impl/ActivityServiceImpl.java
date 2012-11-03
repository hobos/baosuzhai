package com.zizibujuan.drip.server.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.drip.server.dao.ActivityDao;
import com.zizibujuan.drip.server.service.ActivityService;
import com.zizibujuan.drip.server.util.PageInfo;

/**
 * 活动列表实现类
 * @author jinzw
 * @since 0.0.1
 */
public class ActivityServiceImpl implements ActivityService {

	private static final Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);
	private ActivityDao activityDao;
	
	public void setActivityDao(ActivityDao activityDao) {
		logger.info("注入ActivityDao");
		this.activityDao = activityDao;
	}

	public void unsetExerciseDao(ActivityDao activityDao) {
		if (this.activityDao == activityDao) {
			logger.info("注销ActivityDao");
			this.activityDao = null;
		}
	}
	
	@Override
	public List<Map<String, Object>> get(Long userId, PageInfo pageInfo) {
		return null;
	}

}
