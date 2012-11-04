package com.zizibujuan.drip.server.dao;

import java.util.List;
import java.util.Map;

import com.zizibujuan.drip.server.util.PageInfo;

/**
 * 活动列表 数据访问接口
 * @author jinzw
 * @since 0.0.1
 */
public interface ActivityDao {

	/**
	 * 获取活动列表的索引信息
	 * @param userId 用户标识
	 * @param pageInfo 分页信息
	 * @return 活动列表的索引，并不包含活动内容详情。如果不存在，则返回空列表。
	 * <pre>
	 * map值：
	 * 		userId：用户标识
	 * 		displayUserName：显示用的用户名
	 * 		createTime：活动发生的时间
	 * 		contentId：活动输出的内容标识
	 * 		actionType：活动类型
	 * </pre>
	 */
	List<Map<String, Object>> get(Long userId, PageInfo pageInfo);
}
