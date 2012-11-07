package com.zizibujuan.drip.server.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.drip.server.dao.ApplicationPropertyDao;
import com.zizibujuan.drip.server.service.ApplicationPropertyService;
import com.zizibujuan.drip.server.util.ApplicationPropertyKey;

/**
 * 系统属性服务实现类
 * @author jinzw
 * @since 0.0.1
 */
public class ApplicationPropertyServiceImpl implements
		ApplicationPropertyService {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationPropertyServiceImpl.class);
	private ApplicationPropertyDao applicationPropertyDao;
	
	@Override
	public Long getNextAnonymouseId() {
		// TODO:缓存?
		String propertyName = ApplicationPropertyKey.DRIP_COOKIE_MAX_USER_ID;
		Long id = applicationPropertyDao.getLong(propertyName);
		id++;
		applicationPropertyDao.putLong(propertyName, id);
		return id;
	}
	
	public void setApplicationPropertyDao(ApplicationPropertyDao applicationPropertyDao) {
		logger.info("注入ApplicationPropertyDao");
		this.applicationPropertyDao = applicationPropertyDao;
	}

	public void unsetApplicationPropertyDao(ApplicationPropertyDao applicationPropertyDao) {
		if (this.applicationPropertyDao == applicationPropertyDao) {
			logger.info("注销ApplicationPropertyDao");
			this.applicationPropertyDao = null;
		}
	}

}
