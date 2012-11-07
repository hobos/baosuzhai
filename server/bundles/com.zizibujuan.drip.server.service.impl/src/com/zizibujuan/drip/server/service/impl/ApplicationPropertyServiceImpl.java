package com.zizibujuan.drip.server.service.impl;

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

}
