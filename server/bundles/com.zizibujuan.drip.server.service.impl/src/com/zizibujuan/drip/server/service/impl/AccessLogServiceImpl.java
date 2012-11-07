package com.zizibujuan.drip.server.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.drip.server.dao.AccessLogDao;
import com.zizibujuan.drip.server.service.AccessLogService;

/**
 * 用户访问日志 实现类
 * @author jinzw
 * @since 0.0.1
 */
public class AccessLogServiceImpl implements AccessLogService {
	private static final Logger logger = LoggerFactory.getLogger(AccessLogServiceImpl.class);
	private AccessLogDao accessLogDao;
	
	@Override
	public void log(String ip, boolean anonymous, Long userId, String urlFrom,
			String urlAccess) {
		accessLogDao.log(ip, anonymous, userId, urlFrom, urlAccess);
	}

	
	public void setAccessLogDao(AccessLogDao accessLogDao) {
		logger.info("注入AccessLogDao");
		this.accessLogDao = accessLogDao;
	}

	public void unsetAccessLogDao(AccessLogDao accessLogDao) {
		if (this.accessLogDao == accessLogDao) {
			logger.info("注销AccessLogDao");
			this.accessLogDao = null;
		}
	}
}
