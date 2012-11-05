package com.zizibujuan.cache.ehcache.service;

import java.util.Map;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 缓存服务实现类
 * @author jinzw
 * @since 0.0.1
 */
public class CacheServiceImpl implements CacheService {

	private CacheManager cacheManager;
	private static final String CACHE_CONFIGURATION_FILE = "ehcache.xml";
	
	private static final String CACHE_USER_INFO = "userInfo";
	private static final String CACHE_EXERCISE = "exercise";
	private static final String CACHE_ANSWER = "answer";
	
	private void put(String cacheName, Object key, Object value){
		ensureInstallCacheManager();
		
		Element element = new Element(key, value);
		cacheManager.getCache(cacheName).put(element);
	}
	
	private Object get(String cacheName, Object key){
		ensureInstallCacheManager();
		return cacheManager.getCache(cacheName).get(key);
	}

	private void ensureInstallCacheManager() {
		if(cacheManager == null){
			cacheManager = new CacheManager(CACHE_CONFIGURATION_FILE);
		}
	}
	
	@Override
	public void putUser(Long userId, Map<String, Object> userInfo) {
		put(CACHE_USER_INFO, userId, userInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getUser(Long userId) {
		return (Map<String, Object>) get(CACHE_USER_INFO, userId);
	}

	@Override
	public String getUserDisplayName(Long userId) {
		return (String) getUser(userId).get("displayName");
	}

	@Override
	public void putExercise(Long exerId, Map<String, Object> exerInfo) {
		put(CACHE_EXERCISE, exerId, exerInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getExercise(Long exerId) {
		return (Map<String, Object>) get(CACHE_EXERCISE, exerId);
	}

	@Override
	public void putAnswer(Long answerId, Map<String, Object> answerInfo) {
		put(CACHE_ANSWER, answerId, answerInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAnswer(Long answerId) {
		return (Map<String, Object>) get(CACHE_ANSWER, answerId);
	}

}
