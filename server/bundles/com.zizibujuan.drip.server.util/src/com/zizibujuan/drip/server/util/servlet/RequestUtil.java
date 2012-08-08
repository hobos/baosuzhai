package com.zizibujuan.drip.server.util.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.zizibujuan.drip.server.exception.json.JSONAccessException;

/**
 * http请求帮助类
 * @author jinzw
 * @since 0.0.1
 */
public abstract class RequestUtil {

	@SuppressWarnings("unchecked")
	public static Map<String,Object> fromJsonObject(HttpServletRequest req) throws IOException{
		Object o = deserializeJson(req);
		Map<String,Object> result = (Map<String,Object>)o;
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> fromJsonArray(HttpServletRequest req) throws IOException{
		Object o = deserializeJson(req);
		return (List<Map<String,Object>>)o;
	}

	private static Object deserializeJson(HttpServletRequest req) throws IOException {
		StringWriter sw = new StringWriter();
		IOUtils.copy(req.getInputStream(), sw,"UTF-8");
		Object o = null;
		try {
			o = JSONUtil.deserialize(sw.toString());
		} catch (JSONException e) {
			throw new JSONAccessException(e);
		}
		return o;
	}

	private static final String HEADER_REQUESTED_WITH = "X-Requested-With";//$NON-NLS-1$
	private static final String VALUE_REQUESTED_WITH = "XMLHttpRequest";//$NON-NLS-1$
	
	/**
	 * 判断请求是否是ajax请求。如果返回false，则是页面跳转。
	 * @param req
	 * @return 是则返回true;否则返回false
	 */
	public static boolean isAjax(HttpServletRequest req) {
		String xRequestedWith = req.getHeader(HEADER_REQUESTED_WITH);
		if(VALUE_REQUESTED_WITH.equals(xRequestedWith)){
			return true;
		}
		return false;
	}
}
