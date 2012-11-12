package com.zizibujuan.drip.server.dao;

import java.util.Map;

/**
 * 习题解析 数据访问接口。注意，只有如果习题解析内容为空，则不保存。
 * 
 * @author jinzw
 * @since 0.0.1
 */
public interface ExerciseGuideDao {

	Map<String, Object> get(Long userId, Long exerciseId);

}
