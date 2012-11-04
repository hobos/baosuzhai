package com.zizibujuan.drip.server.dao.mysql;

import java.util.List;
import java.util.Map;

import com.zizibujuan.drip.server.dao.ActivityDao;
import com.zizibujuan.drip.server.util.PageInfo;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 活动列表 数据访问实现类
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class ActivityDaoImpl extends AbstractDao implements ActivityDao {

	// 获取用户关注者列表

	private static final String SQL_LIST_ACTIVITY_INDEX = "select " +
				"a.RELATION_USER_ID \"userId\"," +
				"c.REAL_NM \"displayUserName\", " +
				"b.CRT_TM \"createTime\"," +
				"b.CONTENT_ID \"contentId\"," +
				"b.ACTION_TYPE \"actionType\"" +
			"FROM " +
			"DRIP_USER_RELATION a, " +
			"DRIP_ACTIVITY b," +
			"DRIP_USER_INFO c" +
			" where " +
			"a.USER_ID = ? AND a.RELATION_USER_ID=b.USER_ID AND a.RELATION_USER_ID=c.DBID ORDER BY b.CRT_TM DESC";

	@Override
	public List<Map<String, Object>> get(Long userId, PageInfo pageInfo) {
		return DatabaseUtil.queryForList(getDataSource(),
				SQL_LIST_ACTIVITY_INDEX, pageInfo, userId);
	}

}
