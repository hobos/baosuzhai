package com.zizibujuan.drip.server.dao.mysql;

import java.util.Locale;

import com.zizibujuan.drip.server.dao.ApplicationPropertyDao;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 系统属性 数据访问实现类。
 * 注意property_key值必须唯一。
 * 系统属性值，都是必须有默认值的。
 * @author jinzw
 * @since 0.0.1
 */
public class ApplicationPropertyDaoImpl extends AbstractDao implements
		ApplicationPropertyDao {

	private static final String SQL_GET_PROPERTY_LONG_VALUE = "SELECT b.PROPERTY_VALUE FROM " +
			"DRIP_PROPERTY_KEY a, DRIP_PROPERTY_VALUE_NUMBER b " +
			"WHERE a.DBID = b.KEY_ID AND a.PROPERTY_KEY=?";
	@Override
	public Long getLong(String propertyName) {
		return DatabaseUtil.queryForLong(getDataSource(), SQL_GET_PROPERTY_LONG_VALUE, propertyName);
	}

	private static final String SQL_UPDATE_LONG_VALUE = "UPDATE DRIP_PROPERTY_VALUE_NUMBER set PROPERTY_VALUE=? where KEY_ID=(SELECT DBID FROM DRIP_PROPERTY_KEY WHERE PROPERTY_KEY=?)";
	@Override
	public void putLong(String propertyName, Long value) {
		DatabaseUtil.update(getDataSource(), SQL_UPDATE_LONG_VALUE, value, propertyName);
	}
	
	@Override
	public void addGroup(String groupName, String displayName, Locale locale) {
		// TODO Auto-generated method stub
		
	}

}
