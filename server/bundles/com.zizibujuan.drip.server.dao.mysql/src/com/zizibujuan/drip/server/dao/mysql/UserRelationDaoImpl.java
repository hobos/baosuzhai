package com.zizibujuan.drip.server.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.zizibujuan.drip.server.dao.UserRelationDao;
import com.zizibujuan.drip.server.exception.dao.DataAccessException;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 用户关系 数据访问实现类
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class UserRelationDaoImpl extends AbstractDao implements UserRelationDao {

	private static final String SQL_INSERT_USER_RELATION = "INSERT INTO " +
			"DRIP_USER_RELATION " +
			"(USER_ID, WATCH_USER_ID,CRT_TM) " +
			"VALUES " +
			"(?,?,now())";
	@Override
	public void watch(Connection con, Long userId, Long watchUserId) {
		DatabaseUtil.insert(con, SQL_INSERT_USER_RELATION, userId, watchUserId);
	}

	@Override
	public void watch(Long userId, Long watchUserId) {
		Connection con = null;
		try {
			con = getDataSource().getConnection();
			con.setAutoCommit(false);
			this.watch(con, userId, watchUserId);
			con.commit();
		} catch (SQLException e) {
			DatabaseUtil.safeRollback(con);
			throw new DataAccessException(e);
		} finally {
			DatabaseUtil.closeConnection(con);
		}
	}

}
