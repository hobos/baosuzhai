package com.zizibujuan.drip.server.exception.dao;

/**
 * 数据访问异常的根异常，借用Spring的数据访问异常机制
 * 
 * @author jinzw
 * @since 0.0.1
 */
public class DataAccessException extends RuntimeException {
	private static final long serialVersionUID = -252776612700009109L;

	public DataAccessException(String msg) {
		super(msg);
	}
	
	public DataAccessException(Throwable cause){
		super(cause);
	}

	public DataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
