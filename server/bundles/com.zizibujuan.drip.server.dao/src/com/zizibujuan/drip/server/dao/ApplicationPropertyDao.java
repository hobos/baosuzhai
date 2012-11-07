package com.zizibujuan.drip.server.dao;

import java.util.Locale;

/**
 * 系统属性 数据访问接口
 * @author jinzw
 * @since 0.0.1
 */
public interface ApplicationPropertyDao {

	/**
	 * 获取值为long类型的属性值
	 * @param propertyName 属性名
	 * @return 属性值
	 */
	Long getLong(String propertyName);

	/**
	 * 新增long类型的属性值
	 * @param propertyName 属性名
	 * @param value 属性值
	 */
	void putLong(String propertyName, Long value);
	
	/**
	 * 添加分组
	 * @param groupName 属性名
	 * @param displayName 显示名
	 * @param locale 本地化
	 */
	void addGroup(String groupName, String displayName, Locale locale);

}
