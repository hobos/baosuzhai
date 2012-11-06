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
	 * 		login: 注册邮箱 之所以叫login，是因为也可能是手机号或用户名
	 * 		password: 登录密码
	 * 		repassword: 确认密码
	 * 		realName: 真实姓名
	 * </pre>
	 * @param userInfo 用户信息
	 * @return 新增用户的标识
	 */
	Long add(Map<String,Object> userInfo);

	/**
	 * 用户登录
	 * @param email 邮箱地址 (未加密)
	 * @param password 密码
	 * @return 如果登录失败则返回null，否则返回用户信息
	 */
	Map<String,Object> login(String email, String password);

	/**
	 * 获取用户登录信息，返回到客户端的，所以不能包含用户隐私信息。
	 * @param userId 用户标识
	 * @return <pre>用户登录信息
	 * 		realName : 用户真实姓名
	 * 		userId : 用户标识
	 * </pre> 
	 */
	Map<String, Object> getLoginInfo(Long userId);

	/**
	 * 判断邮箱是否已被使用
	 * @param email 有效的邮箱地址
	 * @return 已存在则返回<code>true</code>；否则返回<code>false</code>
	 */
	boolean emailIsExist(String email);
}
