package com.zizibujuan.drip.server.dao.mysql;

import java.util.Map;

import com.zizibujuan.drip.server.dao.ExerciseGuideDao;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 习题解析 数据访问实现类
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class ExerciseGuideDaoImpl extends AbstractDao implements
		ExerciseGuideDao {

	private static final String SQL_GET_EXERCISE_GUIDE_BY_USER_ID = "SELECT " +
			"DBID \"id\"," +
			"EXER_ID \"exerciseId\"," +
			"CONTENT \"content\"," +
			"CRT_TM \"createTime\"," +
			"CRT_USER_ID \"createUserId\"," +
			"UPT_TM \"updateTime\"," +
			"UPT_USER_ID \"updateUserId\" " +
			"FROM DRIP_EXER_GUIDE " +
			"WHERE CRT_USER_ID=? AND EXER_ID=?";;

	@Override
	public Map<String, Object> get(Long userId, Long exerciseId) {
		return DatabaseUtil.queryForMap(getDataSource(),
				SQL_GET_EXERCISE_GUIDE_BY_USER_ID, userId, exerciseId);
	}

}
