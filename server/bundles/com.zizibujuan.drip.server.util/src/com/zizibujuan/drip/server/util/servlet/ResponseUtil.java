package com.zizibujuan.drip.server.util.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.zizibujuan.drip.server.exception.json.JSONAccessException;
import com.zizibujuan.drip.server.util.HttpConstants;

/**
 * 往response中输出内容的帮助类,支持以html或者json的格式输出到客户端
 * @author 金正伟
 * @since 0.0.1
 */
public abstract class ResponseUtil {
	/**
	 * 往response中输出JSON格式内容
	 * @param req HttpServletRequest
	 * @param resp  HttpServletResponse
	 * @param result 转换的对象
	 * @param status {@link HttpServletResponse}
	 * @throws IOException
	 */
	public static void toJSON(HttpServletRequest req, HttpServletResponse resp, Object result, int status) throws IOException{
		resp.setStatus(status);
		resp.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
		resp.setHeader("Cache-Control", "no-store"); //$NON-NLS-1$ //$NON-NLS-2$
		resp.setContentType(HttpConstants.CONTENT_TYPE_JSON);
		String response ="";
		if(result instanceof String){
			response = result.toString();
		}else{
			try {
				response = JSONUtil.serialize(result);
			} catch (JSONException e) {
				throw new JSONAccessException("不是有效的json对象或数组",e);
			}
		}
		resp.getWriter().print(response);
	}
	
	/**
	 * 往response中输出JSON格式内容
	 * @param req HttpServletRequest
	 * @param resp  HttpServletResponse
	 * @param result 转换的对象
	 * @throws IOException
	 */
	public static void toJSON(HttpServletRequest req, HttpServletResponse resp, Object result) throws IOException{
		toJSON(req, resp, result,HttpServletResponse.SC_OK);
	}
	
	/**
	 * 往response中输出html格式内容
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @param html 输出的html文本
	 * @throws IOException
	 */
	public static void toHTML(HttpServletRequest req, HttpServletResponse resp, String html) throws IOException{
		toHTML(req, resp, html, HttpServletResponse.SC_OK);
	}
	
	/**
	 * 往response中输出html格式内容
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @param html 输出的html文本
	 * @param status {@link HttpServletResponse}
	 * @throws IOException
	 */
	public static void toHTML(HttpServletRequest req, HttpServletResponse resp, String html, int status) throws IOException{
		resp.setStatus(status);
		resp.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
		resp.setHeader("Cache-Control", "no-store"); //$NON-NLS-1$ //$NON-NLS-2$
		resp.setContentType(HttpConstants.CONTENT_TYPE_HTML);
		resp.getWriter().print(html);
	}

	public static void toHTMLFile(HttpServletRequest req,
			HttpServletResponse resp, String fileName) throws IOException {
		
		InputStream io = req.getServletContext().getResourceAsStream(fileName);
		if(io == null){
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}else{
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
			resp.setHeader("Cache-Control", "no-store"); //$NON-NLS-1$ //$NON-NLS-2$
			resp.setContentType(HttpConstants.CONTENT_TYPE_HTML);
			IOUtils.copy(io, resp.getOutputStream());
		}
		
	}
}
