package com.zizibujuan.drip.server.exception.json;
/**
 * json处理的异常基类
 * @author jinzw
 * @since 0.0.1
 */
public class JSONAccessException extends RuntimeException {
	private static final long serialVersionUID = 6751739892881112518L;

	public JSONAccessException(String msg) {
		super(msg);
	}
	
	public JSONAccessException(Throwable cause){
		super(cause);
	}

	public JSONAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
