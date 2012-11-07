package com.zizibujuan.drip.server.dao;

/**
 * 用户访问日志 数据访问接口
 * @author jinzw
 * @since 0.0.1
 */
public interface AccessLogDao {

	void log(String ip, boolean anonymous, Long userId, String urlFrom,
			String urlAccess);

}
