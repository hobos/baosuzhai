package com.zizibujuan.drip.server.util.dao;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * sql脚本解析器
 * @author jinzw
 * @since 0.0.1
 */
public class SqlParser {

	private static final String TMPL_SQL_INSERT = "INSERT INTO %s (%s) VALUES(%s)";
	private static final String TMPL_SQL_DELETE = "DELETE FROM %s WHERE %s";
	private static final String TMPL_SQL_UPDATE = "UPDATE %s SET %s";
	private static final String TMPL_SQL_WITH_CONDITION_UPDATE = "UPDATE %s SET %s WHERE 1=1 %s";
	
	private static final String DEFAULT_PRIMARY_KEY = "DBID";
	
	/**
	 * 解析插入数据库表的sql语句
	 * @param keySet key值必须遵循TBL.COL的格式
	 * @return insert sql
	 */
	public static String getInsertSql(Set<String> keySet){
		return getInsertSql(keySet, true);
	}
	
	public static String getInsertSql(Set<String> keySet, boolean insertIdentity) {
		if(keySet.isEmpty())return null;
		Iterator<String> iterator = keySet.iterator();
		
		String tableName = null;
		StringBuilder sbColumns = new StringBuilder();
		StringBuilder sbValues = new StringBuilder();
		String key = "";
		String[] split = null;
		if(insertIdentity){
			sbColumns.append(DEFAULT_PRIMARY_KEY).append(",");
			sbValues.append("?,");
		}
		
		while(iterator.hasNext()){
			key = iterator.next();
			split = key.split("\\.");
			
			if(key.indexOf(".")==-1){
				throw new IllegalArgumentException("key值必须包含一个'.'");
			}
			
			if(split.length >2){
				throw new IllegalArgumentException("key值只能包含一个'.'");
			}
			
			tableName = split[0];
			
			sbColumns.append(split[1]).append(",");
			sbValues.append("?,");
		}
		
		sbColumns.deleteCharAt(sbColumns.length()-1);
		sbValues.deleteCharAt(sbValues.length()-1);
		
		return String.format(TMPL_SQL_INSERT, tableName, sbColumns, sbValues);
	}

	/**
	 * 解析删除数据库表数据的sql语句
	 * @param keySet key值必须遵循TBL.COL的格式
	 * @return deleteSql
	 */
	public static String getDeleteSql(Set<String> keySet){
		if(keySet.isEmpty())return null;
		Iterator<String> iterator = keySet.iterator();
		String tableName = null;
		StringBuilder sbDelConditions = new StringBuilder();
		String key = "";
		String[] split = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			split = key.split("\\.");
			if(key.indexOf(".")==-1){
				throw new IllegalArgumentException("key值必须包含一个'.'");
			}
			if(split.length>2){
				throw new IllegalArgumentException("key值只能包含一个'.'");
			}
			tableName = split[0];
			sbDelConditions.append(split[1]).append("=?,");
		}
		sbDelConditions.deleteCharAt(sbDelConditions.length()-1);
		return String.format(TMPL_SQL_DELETE, tableName,sbDelConditions);
	}
	
	/**
	 * 解析更新数据库表数据的sql语句
	 * @param keyModifys
	 * @param keyConditions
	 * @return
	 */
	public static String getUpdateSql(Set<String> keyModifys,Set<String> keyConditions){
		if(keyModifys.isEmpty())return null;
		Iterator<String> iModify = keyModifys.iterator();
		String tableName = null;
		StringBuilder sbModifyParams = new StringBuilder();
		String keyModify = "";
		String[] splitModify = null;
		while (iModify.hasNext()) {
			keyModify = iModify.next();
			splitModify = keyModify.split("\\.");
			if(keyModify.indexOf(".")==-1){
				throw new IllegalArgumentException("更新列中key值必须包含一个'.'");
			}
			if(splitModify.length>2){
				throw new IllegalArgumentException("更新列中key值只能包含一个'.'");
			}
			tableName = splitModify[0];
			sbModifyParams.append(splitModify[1]).append("=?,");
		}
		sbModifyParams.deleteCharAt(sbModifyParams.length()-1);
		if(!keyConditions.isEmpty()){
			Iterator<String> iCondition = keyConditions.iterator();
			String keyCondition = "";
			StringBuilder sbConditions  =new StringBuilder();
			String[] splitCondition = null;
			while (iCondition.hasNext()) {
				keyCondition = iCondition.next();
				splitCondition = keyCondition.split("\\.");
				if(keyCondition.indexOf(".")==-1){
					throw new IllegalArgumentException("更新条件中key值必须包含一个'.'");
				}
				if(splitCondition.length>2){
					throw new IllegalArgumentException("更新条件中key值值能包含一个'.'");
				}
				sbConditions.append("AND ").append(splitCondition[1]).append("=? ");
			}
			sbConditions.deleteCharAt(sbConditions.length()-1);
			return String.format(TMPL_SQL_WITH_CONDITION_UPDATE, tableName,
					sbModifyParams, sbConditions);
		}
		
		return String.format(TMPL_SQL_UPDATE, tableName, sbModifyParams);
	}
	
	/**
	 * 新增(删除)时的对象必须用linkedHashMap来封装
	 * @param map
	 * @return 参数数组
	 */
	public static Object[] getInParams(Map<String,Object> map) {
		if(map == null || map.isEmpty())return null;
		int len = map.size();
		Object[] result = new Object[len];
		int i = 0;
		for(Map.Entry<String, Object> entry : map.entrySet()){
			result[i] = entry.getValue();
			i++;
		}
		return result;
	}
	
	/**
	 * 更新时的参数和条件必须用linkedHashMap来封装
	 * @param map_modify 更新参数信息
	 * @param map_condition 更新条件信息
	 * @return
	 */
	public static Object[] getUpdateInParams(Map<String,Object> map_modify,Map<String,Object> map_condition) {
		if ((map_modify == null || map_modify.isEmpty()))
			return null;
		int len = map_modify.size();
		if(!map_condition.isEmpty() || map_condition != null ){
			len += map_condition.size();
		}
		Object[] result = new Object[len];
		int i = 0;
		for (Map.Entry<String, Object> entry : map_modify.entrySet()) {
			result[i] = entry.getValue();
			i++;
		}
		for (Map.Entry<String, Object> entry : map_condition.entrySet()) {
			result[i] = entry.getValue();
			i++;
		}
		return result;
	}

	public static Object[] wrapWithUUID(String uuid,Object[] inParams) {
		int sourceLen = inParams.length;
		Object[] inParamsWithId = new Object[sourceLen+1];
		inParamsWithId[0] = uuid;
		System.arraycopy(inParams, 0, inParamsWithId, 1, sourceLen);
		return inParamsWithId;
	}
}
