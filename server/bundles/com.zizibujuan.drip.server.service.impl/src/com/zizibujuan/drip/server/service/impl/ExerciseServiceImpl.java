package com.zizibujuan.drip.server.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.drip.server.dao.ExerciseDao;
import com.zizibujuan.drip.server.service.ExerciseService;

/**
 * 维护习题 服务实现类
 * @author jinzw
 * @since 0.0.1
 */
public class ExerciseServiceImpl implements ExerciseService {
	private static final Logger logger = LoggerFactory.getLogger(ExerciseServiceImpl.class);
	private ExerciseDao exerciseDao;
	
	public void setExerciseDao(ExerciseDao exerciseDao) {
		logger.info("注入exerciseDao");
		this.exerciseDao = exerciseDao;
	}

	public void unsetExerciseDao(ExerciseDao exerciseDao) {
		if (this.exerciseDao == exerciseDao) {
			logger.info("注销exerciseDao");
			this.exerciseDao = null;
		}
	}
	
	@Override
	public List<Map<String, Object>> get() {
		return exerciseDao.get();
	}

	@Override
	public Long add(Map<String, Object> exerciseInfo) {
		return exerciseDao.add(exerciseInfo);
	}

}
