package com.zizibujuan.drip.server.service;

import java.util.List;
import java.util.Map;

/**
 * 维护习题 服务接口
 * @author jinzw
 * @since 0.0.1
 */
public interface ExerciseService {

	/**
	 * 获取系统中的所有习题
	 * @return 习题列表，如果没有习题，则返回空列表。习题按照录入的时间倒序排列。
	 * <pre>
	 * 	Map中存储的key值
	 * 		content:习题内容
	 * </pre>
	 */
	List<Map<String,Object>> get();

	/**
	 * 新增习题。<br/>
	 * <pre>
	 * 习题的数据格式为：
	 * 		content ： 习题内容
	 * </pre>
	 * @param exerciseInfo 习题信息
	 * @return 新增习题的标识
	 */
	int add(Map<String, Object> exerciseInfo);
}
