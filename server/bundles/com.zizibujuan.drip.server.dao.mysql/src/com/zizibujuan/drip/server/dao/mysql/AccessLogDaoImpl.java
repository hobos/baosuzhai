package com.zizibujuan.drip.server.dao.mysql;

import com.zizibujuan.drip.server.dao.AccessLogDao;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 用户访问日志 数据访问实现类
 * @author jinzw
 * @since 0.0.1
 */
public class AccessLogDaoImpl extends AbstractDao implements AccessLogDao {

	private static final String SQL_INSERT_ACCESS_LOG = "INSERT INTO DRIP_ACCESS_LOG " +
			"(IP, USER_ID, ANONYMOUS, URL_FROM, URL_ACCESS, ACCESS_TIME, LEAVE_TIME) " +
			"VALUES " +
			"(?,?,?,?,?,now(),null)";
	@Override
	public void log(String ip, boolean anonymous, Long userId, String urlFrom,
			String urlAccess) {
		int anony = anonymous?1:0;
		DatabaseUtil.insert(getDataSource(),SQL_INSERT_ACCESS_LOG,ip,userId, anony, urlFrom, urlAccess);
	}

}
