package com.zizibujuan.drip.server.util.dao;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.zizibujuan.drip.server.exception.dao.DataAccessException;
import com.zizibujuan.drip.server.util.PageInfo;

/**
 * 与数据库进行交互的工具集
 * @author jinzw
 * @since 0.0.1
 */
public abstract class DatabaseUtil {
	private static String DEFAULT_ID = "DBID";
	private static String SQL_DELETE_BY_IDENTITY = "DELETE FROM %s WHERE %s=?";
	private static String SQL_GET_BY_IDENTITY = "SELECT * FROM %s WHERE %s=?";
	
	public static List<Map<String, Object>> queryForList(DataSource ds, String sql, Object... params) {
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		PreparedStatement stmt = null;
		ResultSet rst = null;
		Connection con= null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			addParams(stmt, params);
			
			rst = stmt.executeQuery();
			ResultSetMetaData meta = rst.getMetaData();
			int columnCount = meta.getColumnCount();
			while(rst.next())
			{
				Map<String,Object> map = new HashMap<String, Object>();
				for(int i = 0; i < columnCount; i++){
					int index = i + 1;
					String key = meta.getColumnLabel(index);
					Object value = getResultSetValue(rst, index);
					map.put(key, value);
				}
				result.add(map);
			}
		} catch (SQLException e) {
			throw new DataAccessException("执行失败：sql语句为:"+sql,e);
		}
		finally
		{
			DatabaseUtil.safeClose(con, rst, stmt);
		}
		return result;
	}

	public static List<Map<String, Object>> queryForList(DataSource ds, String sql,PageInfo pageInfo, Object... params){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		PreparedStatement stmt = null;
		ResultSet rst = null;
		Connection con= null;
		try {
			String sqlPage =  "SELECT * FROM ( SELECT ROWNUM AS NUMROW, table_data.* from ( " 
		+ sql + " ) table_data) WHERE NUMROW >= "  + (pageInfo.getStart()+1) + "  AND NUMROW <= " + (pageInfo.getEnd()+1) ; 
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sqlPage);
			addParams(stmt, params);
			
			rst = stmt.executeQuery();
			ResultSetMetaData meta = rst.getMetaData();
			int columnCount = meta.getColumnCount();
			while(rst.next())
			{
				Map<String,Object> map = new HashMap<String, Object>();
				for(int i = 0; i < columnCount; i++){
					int index = i + 1;
					String key = meta.getColumnLabel(index);
					Object value = getResultSetValue(rst, index);
					map.put(key, value);
				}
				result.add(map);
			}
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		finally
		{
			DatabaseUtil.safeClose(con, rst, stmt);
		}
		
		int count = getCount(ds,sql,params);
		pageInfo.setCount(count);
		return result;
	}
	
	private static int getCount(DataSource ds, String sql, Object... params){
		String sqlCount = "select count(*) from ( " + sql + " ) table_count";
		return queryForInt(ds, sqlCount, params);
	}
	
	private static Object getResultSetValue(ResultSet rst, int index)throws SQLException{
		Object obj = rst.getObject(index);
		
		if(obj instanceof Blob){
			obj = rst.getBytes(index);
		}
		else if(obj instanceof Clob){
			obj = rst.getString(index);
		}else if(obj !=null && obj instanceof java.sql.Date){
			if(rst.getMetaData().getColumnClassName(index).equals("java.sql.Timestamp")){
				obj = rst.getTimestamp(index);
			}
		}
		return obj;
	}
	
	/**
	 * FIXME:目前只支持传递字符串类型的参数
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public static String queryForString(DataSource ds,String sql, Object... params){
		String result=null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			addParams(stmt, params);
			
			rst = stmt.executeQuery();

			if(rst.next())
			{
				result = rst.getString(1);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		finally
		{
			DatabaseUtil.safeClose(con, rst, stmt);
		}
		return result;
	}
	
	public static int queryForInt(DataSource ds, String sql, Object... params) {
		int result=0;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			addParams(stmt, params);
			
			rst = stmt.executeQuery();

			if(rst.next())
			{
				result = rst.getInt(1);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		finally
		{
			DatabaseUtil.safeClose(con, rst, stmt);
		}
		return result;
	}

	public static void addParams(PreparedStatement stmt, Object... params)
			throws SQLException {
		if(params != null){
			int len = params.length;
			for(int i = 0; i < len; i++){
				stmt.setObject((i+1), params[i]);
			}
		}
	}
	
	/**
	 * 约定：sql的select语句中的第一列为key值，第二列为value值
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String,Object>> queryForKeyValuePair(DataSource ds,String sql,Object... params){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		PreparedStatement stmt = null;
		ResultSet rst = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.prepareStatement(sql);
			addParams(stmt, params);
			
			rst = stmt.executeQuery();
			
			while (rst.next())
			{
				Map<String,Object> map = new HashMap<String, Object>();
				String key = rst.getString(1);
				String value = rst.getString(2);
				map.put(key, value);
				result.add(map);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		finally
		{
			DatabaseUtil.safeClose(con, rst, stmt);
		}
		return result;	
	}
	
	
	public static void safeClose(Connection con, ResultSet rst, Statement stmt){
		try {
			if (rst != null){
				rst.close();
			}
			if (stmt != null){
				stmt.close();
			}
			if (con != null){
				if (con.getAutoCommit() == false) {
					con.setAutoCommit(true);
				}
				con.close();
			}
		} catch (SQLException e) {
			//TODO:把这个异常类进一步具体话，以便获取更详细的信息
			throw new DataAccessException(e);
		}
	}
	
	public static void safeClose(Connection con, Statement stmt){
		try {
			if (stmt != null){
				stmt.close();
			}
			if (con != null){
				if (con.getAutoCommit() == false) {
					con.setAutoCommit(true);
				}
				con.close();
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static void closeResultSet(ResultSet... params){
		if(params != null){
			for(ResultSet each : params){
				if (each != null){
					try {
						each.close();
					} catch (SQLException e) {
						throw new DataAccessException(e);
					}
				}
			}
		}
	}

	public static void closeStatement(PreparedStatement... params){
		if(params != null){
			for(PreparedStatement each : params){
				if (each != null){
					try {
						each.close();
					} catch (SQLException e) {
						throw new DataAccessException(e);
					}
				}
			}
		}
		
	}

	public static void closeConnection(Connection con) {
		if (con != null){
			try {
				if (con.getAutoCommit() == false) {
					con.setAutoCommit(true);
				}
				con.close();
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
		}
	}

	public static Map<String, Object> queryForMap(DataSource ds,String sql, Object... params){
		List<Map<String,Object>> list = queryForList(ds,sql, params);
		int count = list.size();
		if(count==1){
			return list.get(0);
		}else if(count==0){
			return new HashMap<String, Object>();
		}else{
			throw new IllegalStateException("查询出来的记录数不允许大于1，结果却为"+count);
		}
	}
	
	/**
	 * 新增数据
	 * @param ds 数据源
	 * @param map 数据
	 * @param insertIdentity 是否在sql语句的最前面插入标识，即由程序生成主键标识
	 * @return 如果insertIdentity为<code>true</code>,则返回系统生成的主键;如果<code>false</code>,则返回null
	 */
	public static String insert(DataSource ds, Map<String,Object> map, boolean insertIdentity){
		String sql = SqlParser.getInsertSql(map.keySet(),insertIdentity);
		Object[] inParams = null;
		if(insertIdentity){
			String dbid = IdGenerator.uuid();
			inParams = SqlParser.wrapWithUUID(dbid, SqlParser.getInParams(map));
			update(ds, sql, inParams);
			return dbid;
		}else{
			inParams = SqlParser.getInParams(map);
			update(ds, sql, inParams);
			return null;
		}
	}
	
	public static String insert(DataSource ds, Map<String,Object> map){
		return insert(ds, map,true);
	}
	
	public static int update(DataSource ds, String sql, Object...inParams){
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		int result = 0;
		try {
			con = ds.getConnection();
			pst = con.prepareStatement(sql);
			con.setAutoCommit(false);
			int len = inParams.length;
			for(int i = 0; i < len; i++){
				pst.setObject(i+1, inParams[i]);
			}
			result = pst.executeUpdate();
			con.commit();
			return result;
			
		}catch(SQLException e){
			safeRollback(con);
			throw new DataAccessException(e);
		}finally{
			safeClose(con, rst, pst);
		}
	}
	
	public static int deleteByIdentity(DataSource ds, String tableName, String IdFieldName,
			String id) {
		String sql = String.format(SQL_DELETE_BY_IDENTITY, tableName,IdFieldName);
		return update(ds, sql, id);
	}
	
	public static int deleteByIdentity(DataSource ds, String tableName,
			String id) {
		return deleteByIdentity(ds, tableName, DEFAULT_ID,id);
	}
	
	public static Map<String,Object> getByIdentity(DataSource ds, String tableName, String IdFieldName,
			String id){
		String sql = String.format(SQL_GET_BY_IDENTITY, tableName, IdFieldName);
		return queryForMap(ds, sql, id);
	}
	
	public static Map<String, Object> getByIdentity(DataSource ds,
			String tableName, String id) {
		return getByIdentity(ds, tableName, DEFAULT_ID, id);
	}
	
	public static void safeRollback(Connection con) {
		if(con !=null){
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new DataAccessException(e1);
			}
		}
	}
}
