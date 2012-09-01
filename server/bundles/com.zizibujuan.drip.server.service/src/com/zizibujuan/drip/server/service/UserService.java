package com.zizibujuan.drip.server.service;

import java.util.Map;

/**
 * 用户 服务接口
 * @author jinzw
 * @since 0.0.1
 */
public interface UserService {

	/**
	 * 新增用户。
	 * <pre>
	 * 用户信息的格式为:
	 * 		email: 注册邮箱
	 * 		password: 登录密码
	 * 		repassword: 确认密码
	 * 		realName: 真实姓名
	 * </pre>
	 * @param userInfo 用户信息
	 * @return 新增用户的标识
	 */
	int add(Map<String,Object> userInfo);

	/**
	 * 用户登录
	 * @param email 邮箱地址
	 * @param password 密码
	 * @return 如果登录失败则返回null，否则返回用户信息
	 */
	Map<String,Object> login(String email, String password);
}
