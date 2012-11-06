package com.zizibujuan.drip.server.dao;

import java.sql.Connection;

/**
 * 用户关系 数据访问接口
 * @author jinzw
 * @since 0.0.1
 */
public interface UserRelationDao {
	
	/**
	 * 用户关注另一个用户
	 * @param con 数据库链接
	 * @param userId 执行关注操作的用户标识
	 * @param watchUserId 被关注的用户标识
	 */
	void watch(Connection con, Long userId, Long watchUserId);
	
	/**
	 * 用户关注另一个用户
	 * @param userId 执行关注操作的用户标识
	 * @param watchUserId 被关注的用户标识
	 */
	void watch(Long userId, Long watchUserId);

}
