package com.zizibujuan.drip.server.dao.mysql;

import java.util.Map;

import com.zizibujuan.drip.server.dao.UserDao;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 用户数据访问实现类
 * @author jinzw
 * @since 0.0.1
 */
public class UserDaoImpl extends AbstractDao implements UserDao {

	private static final String SQL_INSERT_USER = "INSERT INTO DRIP_USER_INFO " +
			"(LOGIN_NM,LOGIN_EMAIL,LOGIN_PWD,MOBILE,REAL_NM,CRT_TM) " +
			"VALUES " +
			"(?,?,?,?,?,now())";
	
	@Override
	public int add(Map<String, Object> userInfo) {
		return DatabaseUtil.insert(getDataSource(), SQL_INSERT_USER, 
				null,
				userInfo.get("login"),
				userInfo.get("md5Password"),
				userInfo.get("mobile"),
				userInfo.get("realName"));
	}

	private static final String SQL_GET_USER = "SELECT " +
			"DBID,LOGIN_NM,LOGIN_EMAIL,LOGIN_PWD,MOBILE,REAL_NM,CRT_TM " +
			"FROM DRIP_USER_INFO WHERE LOGIN_EMAIL = ? AND LOGIN_PWD = ?";
	@Override
	public Map<String, Object> get(String email, String md5Password) {
		return DatabaseUtil.queryForMap(getDataSource(), SQL_GET_USER, email, md5Password);
	}
	
	private static final String SQL_UPDATE_USER = "UPDATE DRIP_USER_INFO SET LAST_LOGIN_TM = now() " +
			"WHERE DBID=?";
	@Override
	public void updateLastLoginTime(Long userId) {
		DatabaseUtil.update(getDataSource(), SQL_UPDATE_USER, userId);
	}
	
	private static final String SQL_GET_LOGIN = "SELECT DBID \"userId\", REAL_NM \"realName\" FROM DRIP_USER_INFO WHERE DBID=?";
	@Override
	public Map<String, Object> getLoginInfo(Long userId) {
		return DatabaseUtil.queryForMap(getDataSource(), SQL_GET_LOGIN, userId);
	}
	
	private static final String SQL_EMAIL_EXIST = "select 1 from DRIP_USER_INFO where LOGIN_EMAIL = ? limit 1";
	@Override
	public boolean emailIsExist(String email) {
		String result = DatabaseUtil.queryForString(getDataSource(), SQL_EMAIL_EXIST, email);
		return result != null;
	}

}
