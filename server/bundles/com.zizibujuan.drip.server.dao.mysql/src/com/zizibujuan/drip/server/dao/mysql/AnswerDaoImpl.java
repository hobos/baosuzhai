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
			"EXER_ID \"exerId\"," +
			"CRT_TM \"createTime\"," +
			"CRT_USER_ID \"createUserId\"," +
			"UPT_TM \"updateTime\"," +
			"UPT_USER_ID \"updateUserId\" " +
			"FROM DRIP_ANSWER " +
			"WHERE DBID=? ";
	private static final String SQL_LIST_ANSWER_DETAIL = "SELECT " +
			"DBID \"id\"," +
			"ANSWER_ID \"answerId\"," +
			"OPT_ID \"optionId\"," +
			"CONTENT \"content\" " +
			"FROM DRIP_ANSWER_DETAIL " +
			"WHERE ANSWER_ID=?";
	@Override
	public Map<String, Object> get(Long answerId) {
		Map<String,Object> result = DatabaseUtil.queryForMap(getDataSource(), SQL_GET_ANSWER, answerId);
		if(!result.isEmpty()){
			List<Map<String,Object>> detail = DatabaseUtil.queryForList(getDataSource(), SQL_LIST_ANSWER_DETAIL, answerId);
			result.put("detail", detail);
		}
		return result;
	}

}
