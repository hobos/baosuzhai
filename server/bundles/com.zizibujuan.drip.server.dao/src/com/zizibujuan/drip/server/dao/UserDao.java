package com.zizibujuan.drip.server.dao;

import java.util.Map;

/**
 * 用户数据访问接口
 * @author jinzw
 * @since 0.0.1
 */
public interface UserDao {

	/**
	 * 新增用户。
	 * <pre>
	 * 用户信息的格式为:
	 * 		login: 注册邮箱
	 * 		password: 登录密码(已加过密)
	 * 		repassword: 确认密码
	 * 		realName: 真实姓名
	 * </pre>
	 * @param userInfo 用户信息
	 * @return 新增用户的标识
	 */
	int add(Map<String,Object> userInfo);

	/**
	 * 获取用户信息
	 * @param email 电子邮箱
	 * @param md5Password 加密后的密码
	 * @return 如果系统中存在该用户信息则返回，否则返回空的map对象
	 */
	Map<String, Object> get(String email, String md5Password);

	/**
	 * 更新用户的最近登录时间
	 * @param userId 用户标识
	 */
	void updateLastLoginTime(String userId);

	/**
	 * 获取用户登录信息，返回到客户端的，所以不能包含用户隐私信息。
	 * @param userId 用户标识
	 * @return <pre>用户登录信息
	 * 		realName : 用户真实姓名
	 * 		userId : 用户标识
	 * </pre> 
	 */
	Map<String, Object> getLoginInfo(String userId);
	
}
