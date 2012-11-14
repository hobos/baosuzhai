package com.zizibujuan.drip.server.dao.mysql;

import java.util.List;
import java.util.Map;

import com.zizibujuan.drip.server.dao.AnswerDao;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 答案 数据访问实现类
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class AnswerDaoImpl extends AbstractDao implements AnswerDao {
	
	private static final String SQL_GET_ANSWER = "SELECT " +
			"DBID \"id\"," +
			"EXER_ID \"exerciseId\"," +
			"GUIDE \"guide\"," +
			"CRT_TM \"createTime\"," +
			"CRT_USER_ID \"createUserId\"," +
			"UPT_TM \"updateTime\"," +
			"UPT_USER_ID \"updateUserId\" " +
			"FROM DRIP_ANSWER ";

	// 暂定，将习题解析看作答案的一部分。
	private static final String SQL_GET_ANSWER_BY_ID = SQL_GET_ANSWER + "WHERE DBID=? ";
	
	private static final String SQL_LIST_ANSWER_DETAIL = "SELECT " +
			"DBID \"id\"," +
			"ANSWER_ID \"answerId\"," +
			"OPT_ID \"optionId\"," +
			"CONTENT \"content\" " +
			"FROM DRIP_ANSWER_DETAIL " +
			"WHERE ANSWER_ID=?";
	@Override
	public Map<String, Object> get(Long answerId) {
		Map<String,Object> result = DatabaseUtil.queryForMap(getDataSource(), SQL_GET_ANSWER_BY_ID, answerId);
		
		if(!result.isEmpty()){
			List<Map<String,Object>> detail = DatabaseUtil.queryForList(getDataSource(), SQL_LIST_ANSWER_DETAIL, answerId);
			result.put("detail", detail);
		}
		return result;
	}
	
	
	@Override
	public void saveOpUpdate(Long userId, Map<String, Object> answer) {
		// 查询用户是否已经回答过该习题
		// 如果已经回答过，则删除之前的答案和习题解析
		// 注意，习题解析只有存在时才删除，而不是判断是不是答案已存在过。
		// 新增答案和习题解析
		
		// 将新的答案，在答题历史表中存储一份
		// 将习题解析在历史表中存储一份。如果存在的话
		
		//Connection con = null;
		
		
	}


	private static final String SQL_GET_EXERCISE_ANSWER_BY_USER_ID = SQL_GET_ANSWER + "WHERE CRT_USER_ID=? AND EXER_ID=?";
	@Override
	public Map<String, Object> get(Long userId, Long exerciseId) {
		Map<String,Object> result = DatabaseUtil.queryForMap(getDataSource(), SQL_GET_EXERCISE_ANSWER_BY_USER_ID, userId, exerciseId);
		if(!result.isEmpty()){
			Long answerId = Long.valueOf(result.get("id").toString());
			List<Map<String,Object>> detail = DatabaseUtil.queryForList(getDataSource(), SQL_LIST_ANSWER_DETAIL, answerId);
			result.put("detail", detail);
		}
		return result;
	}

}
