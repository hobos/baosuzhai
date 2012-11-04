package com.zizibujuan.drip.server.dao;

import java.util.Map;

/**
 * 答案 数据访问接口
 * @author jinzw
 * @since 0.0.1
 */
public interface AnswerDao {

	/**
	 * 根据答案标识获取答案详情
	 * @param answerId 答案标识
	 * @return 答案
	 * <pre>
	 * map结构为：
	 * 		id: 答案标识
	 * 		exerId: 答案所属习题标识
	 * 		createTime: 创建时间
	 * 		updateTime: 更新时间
	 * 		createUserId: 创建用户标识
	 * 		updateUserId: 更新用户标识
	 * 		detail：支持1到多个可选答案
	 * 			id：答案详情标识
	 * 			answerId: 所属答案标识
	 * 			optionId: 所选选项标识（习题为选择题时使用）
	 * 			content: 所填答案内容
	 * </pre>
	 */
	Map<String, Object> get(Long answerId);

}
