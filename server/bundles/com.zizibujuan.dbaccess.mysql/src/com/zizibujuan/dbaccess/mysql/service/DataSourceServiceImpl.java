package com.zizibujuan.dbaccess.mysql.service;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.osgi.service.jdbc.DataSourceFactory;

import com.zizibujuan.dbaccess.mysql.ClientDataSourceFactory;

/**
 * 获取数据库服务
 * @author 金正伟
 * @since 0.0.1
 */
public class DataSourceServiceImpl implements DataSourceService {

	private DataSource dataSource;
	public DataSource getDataSource(){
		if(dataSource==null){
			ClientDataSourceFactory factory = new ClientDataSourceFactory();
			Properties props = new Properties();
			// TODO：把这些数据存入dbConfig.xml文件中，注意这些数据不能存入数据库中，因为这些信息是为了链接数据库用的。
			//注意编码
			props.put(DataSourceFactory.JDBC_URL, "jdbc:mysql://localhost:3306/drip?useUnicode=true&characterEncoding=utf8");
			props.put(DataSourceFactory.JDBC_USER, "root");
			props.put(DataSourceFactory.JDBC_PASSWORD, "abc123");

			try {
				dataSource = factory.createDataSource(props);
			} catch (SQLException e) {
				// TODO:throw new DataAccessException(e);
			}
		}
		return dataSource;
	}

}
