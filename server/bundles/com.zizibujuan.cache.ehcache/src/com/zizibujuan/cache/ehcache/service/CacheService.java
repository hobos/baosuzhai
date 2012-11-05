package com.zizibujuan.cache.ehcache.service;

import java.util.Map;

/**
 * 缓存服务接口
 * 
 * @author jinzw
 * @since 0.0.1
 */
public interface CacheService {
	// 缓存用户基本信息
	/**
	 * 缓存用户信息
	 * @param userId 用户标识
	 * @param userInfo 用户信息
	 * <pre>
	 * map结构为：
	 * 		id： 用户标识
	 * 		displayName: 供显示的用户名
	 * </pre>
	 */
	void putUser(Long userId, Map<String,Object> userInfo);
	
	Map<String,Object> getUser(Long userId);
	
	String getUserDisplayName(Long userId);

	// TODO:缓存系统属性

	// TODO:缓存系统编码

	// 缓存习题
	void putExercise(Long exerId, Map<String,Object> exerInfo);
	Map<String,Object> getExercise(Long exerId);

	// 缓存答案
	void putAnswer(Long answerId, Map<String,Object> answerInfo);
	Map<String, Object> getAnswer(Long answerId);
}
