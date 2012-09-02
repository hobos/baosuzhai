package com.zizibujuan.drip.server.dao.mysql;

import java.util.List;
import java.util.Map;

import com.zizibujuan.drip.server.dao.ExerciseDao;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 维护习题 数据访问实现类
 * @author jinzw
 * @since 0.0.1
 */
public class ExerciseDaoImpl extends AbstractDao implements ExerciseDao {

	private static final String SQL_LIST_EXERCISE = 
			"SELECT DBID, CONTENT, CRT_TM, CRT_USER_ID FROM DRIP_EXERCISE ORDER BY CRT_TM DESC";
	@Override
	public List<Map<String, Object>> get() {
		return DatabaseUtil.queryForList(getDataSource(), SQL_LIST_EXERCISE);
	}
	
	private static final String SQL_INSERT_EXERCISE = 
			"INSERT INTO DRIP_EXERCISE (CONTENT, CRT_TM, CRT_USER_ID) VALUES (?,now(),?)";
	@Override
	public int add(Map<String, Object> exerciseInfo) {
		Object oContent = exerciseInfo.get("content");
		Object oUserId = exerciseInfo.get("userId");
		return DatabaseUtil.insert(getDataSource(), SQL_INSERT_EXERCISE, oContent,oUserId);
	}

}
