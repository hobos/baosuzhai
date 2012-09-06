package com.zizibujuan.drip.server.service.impl;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.drip.server.dao.UserDao;
import com.zizibujuan.drip.server.service.UserService;

/**
 * 用户服务实现类
 * @author jinzw
 * @since 0.0.1
 */
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		logger.info("注入userDao");
		this.userDao = userDao;
	}

	public void unsetUserDao(UserDao userDao) {
		if (this.userDao == userDao) {
			logger.info("注销userDao");
			this.userDao = null;
		}
	}
	// FIXME:学习如何加入salt，明白加入salt有哪些具体好处
	@Override
	public int add(Map<String, Object> userInfo) {
		String salt = "";
		String password = userInfo.get("password").toString();
		String md5Password = DigestUtils.md5Hex(password+salt);
		userInfo.put("md5Password", md5Password);
		userInfo.put("salt", salt);
		
		return userDao.add(userInfo);
	}

	@Override
	public Map<String,Object> login(String email, String password) {
		// 根据邮箱和密码查找用户信息
		// 如果查到了，则保存在session中。
		String md5Password = DigestUtils.md5Hex(password);
		Map<String,Object> userInfo = userDao.get(email, md5Password);
		
		if(userInfo.isEmpty()){
			return null;
		}else{
			String userId = userInfo.get("DBID").toString();
			userDao.updateLastLoginTime(Long.valueOf(userId));
			return userInfo;
		}
	}

	@Override
	public Map<String, Object> getLoginInfo(Long userId) {
		return userDao.getLoginInfo(userId);
	}

}
