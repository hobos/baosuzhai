package com.zizibujuan.drip.server.util;


/**
 * http请求/返回头信息的key值
 * @author jinzw
 * @since 0.0.1
 */
public abstract class HttpConstants {

	public static final String KEY_HEADER_REQUEST_RANGE = "Range";//$NON-NLS-1$
	public static final String KEY_HEADER_RESPONSE_RANGE = "Content-Range";//$NON-NLS-1$
	public static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";//$NON-NLS-1$
	
	public static final String HEADER_BANG_PROFILE = "Drip-Profile";//$NON-NLS-1$
	public static final String PROFILE_TEST = "0";//$NON-NLS-1$
	public static final String PROFILE_PRODUCTION = "1";//$NON-NLS-1$
	public static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";//$NON-NLS-1$
	
	public static final String SESSION_USER = "DRIP_USER";
}
