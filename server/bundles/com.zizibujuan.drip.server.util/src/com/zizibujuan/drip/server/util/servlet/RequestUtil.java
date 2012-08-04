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
 * 请求帮助类
 * @author jinzw
 * @since 0.0.1
 */
public abstract class RequestUtil {

	@SuppressWarnings("unchecked")
	protected Map<String,Object> fromJsonObject(HttpServletRequest req) throws IOException{
		Object o = deserializeJson(req);
		Map<String,Object> result = (Map<String,Object>)o;
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Map<String,Object>> fromJsonArray(HttpServletRequest req) throws IOException{
		Object o = deserializeJson(req);
		return (List<Map<String,Object>>)o;
	}

	private Object deserializeJson(HttpServletRequest req) throws IOException {
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
}
