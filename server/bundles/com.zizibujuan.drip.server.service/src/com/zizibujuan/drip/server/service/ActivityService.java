package com.zizibujuan.drip.server.service;

import java.util.List;
import java.util.Map;

import com.zizibujuan.drip.server.util.PageInfo;

/**
 * 活动列表服务
 * @author jinzw
 * @since 0.0.1
 */
public interface ActivityService {

	List<Map<String, Object>> get(Long userId, PageInfo pageInfo);

}
