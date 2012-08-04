package com.zizibujuan.drip.server.util.dao;

import java.util.UUID;

/**
 * 生成UUID的工具类
 * @author jinzw
 * @since 0.0.1
 */
public class IdGenerator {

	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
