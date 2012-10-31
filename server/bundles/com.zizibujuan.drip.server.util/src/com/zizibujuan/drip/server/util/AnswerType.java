package com.zizibujuan.drip.server.util;

/**
 * 答案类型，目前支持text（文本）类型和从option中选择的int类型（是option的代理主键）
 * @author jinzw
 * @since 0.0.1
 */
public abstract class AnswerType {

	/**
	 * 输入的文本类型
	 */
	public static final int TEXT = 1;
	/**
	 * 对应习题选项中的选项标识
	 */
	public static final int FROM_OPTION = 2;
}
