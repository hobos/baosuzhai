package com.zizibujuan.drip.server.service;

/**
 * 用户访问日志
 * 
 * TODO：改为异步日志的方式，记录到另一台服务器上。
 * 
 * @author jinzw
 * @since 0.0.1
 */
public interface AccessLogService {

	/**
	 * 记录用户访问日志
	 * @param ip 访问者ip
	 * @param anonymous 是否匿名用户, true表示是匿名用户；false表示不是匿名用户
	 * @param userId 访问者用户标识，如果用户已登录，是用户标识；如果用户没有登录，则从cookie中获取用户标识
	 * @param urlFrom 用户访问的前一个链接
	 * @param urlAccess 用户当前访问的链接
	 */
	void log(String ip, boolean anonymous, Long userId, String urlFrom, String urlAccess);
	
	//void ansyLog(String ip, Long userId, String urlFrom, String urlAccess);
	
}
