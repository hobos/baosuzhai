package com.zizibujuan.drip.server.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.drip.server.dao.ActivityDao;
import com.zizibujuan.drip.server.dao.AnswerDao;
import com.zizibujuan.drip.server.dao.ExerciseDao;
import com.zizibujuan.drip.server.service.ActivityService;
import com.zizibujuan.drip.server.util.ActionType;
import com.zizibujuan.drip.server.util.PageInfo;

/**
 * 活动列表实现类
 * @author jinzw
 * @since 0.0.1
 */
public class ActivityServiceImpl implements ActivityService {

	private static final Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);
	private ActivityDao activityDao;
	private ExerciseDao exerciseDao;
	private AnswerDao answerDao;
	
	@Override
	public List<Map<String, Object>> get(Long userId, PageInfo pageInfo) {
		// 获取用户的活动列表
		List<Map<String,Object>> list = activityDao.get(userId, pageInfo);
		// 然后循环着获取每个活动的详情,如果缓存中已存在，则从缓存中获取。
		// 注意，新增的习题和答案，都要缓存起来。
		System.out.println(list);
		for(Map<String,Object> each : list){
			Long contentId = Long.valueOf(each.get("contentId").toString());
			String actionType = each.get("actionType").toString();
			if(actionType.equals(ActionType.SAVE_EXERCISE)){
				Map<String,Object> exercise = getExercise(contentId);
				each.put("exercise", exercise);
			}else if(actionType.equals(ActionType.ANSWER_EXERCISE)){
				Map<String,Object> answer = getAnswer(contentId);
				Long exerciseId = Long.valueOf(answer.get("exerId").toString());
				Map<String,Object> exercise = getExercise(exerciseId);
				each.put("exercise", exercise);
				each.put("answer", answer);
			}
		}
		return list;
	}
	
	private Map<String,Object> getExercise(Long dbid){
		// TODO: 改为从缓存中获取。
		Map<String,Object> result = exerciseDao.get(dbid);
		return result;
	}
	
	private Map<String,Object> getAnswer(Long answerId){
		// TODO: 改为从缓存中获取
		Map<String,Object> result = answerDao.get(answerId);
		return result;
	}

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
	
	public void setExerciseDao(ExerciseDao exerciseDao) {
		logger.info("注入ExerciseDao");
		this.exerciseDao = exerciseDao;
	}

	public void unsetExerciseDao(ExerciseDao exerciseDao) {
		if (this.exerciseDao == exerciseDao) {
			logger.info("注销exerciseDao");
			this.exerciseDao = null;
		}
	}
	
	public void setAnswerDao(AnswerDao answerDao) {
		logger.info("注入AnswerDao");
		this.answerDao = answerDao;
	}

	public void unsetAnswerDao(AnswerDao answerDao) {
		if (this.answerDao == answerDao) {
			logger.info("注销AnswerDao");
			this.answerDao = null;
		}
	}
}
