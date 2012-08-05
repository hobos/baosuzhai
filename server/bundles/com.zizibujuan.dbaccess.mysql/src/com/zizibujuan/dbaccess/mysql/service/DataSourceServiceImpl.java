package com.zizibujuan.dbaccess.mysql.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.osgi.service.jdbc.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.dbaccess.mysql.ClientDataSourceFactory;
import com.zizibujuan.drip.server.exception.dao.DataAccessException;

/**
 * 获取数据库服务
 * @author 金正伟
 * @since 0.0.1
 */
public class DataSourceServiceImpl implements DataSourceService {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);
	private static final String JDBC_CONFIG = "/jdbc.properties";
	private DataSource dataSource;
	public DataSource getDataSource(){
		if(dataSource==null){
			ClientDataSourceFactory factory = new ClientDataSourceFactory();
			Properties props = new Properties();
			// 注意编码
			// 提供两种环境，开发和生产
			try {
				InputStream io = getClass().getResourceAsStream(JDBC_CONFIG);
				if(io==null){
					logger.info("从硬编码的url中解析");
					setDevelopmentConfig(props);
				}else{
					logger.info("从配置properties文件中解析");
					props.load(io);
					if(props.get(DataSourceFactory.JDBC_URL).toString().startsWith("$")){
						setDevelopmentConfig(props);
					}else{
						logger.info(props.toString());
					}
				}
				dataSource = factory.createDataSource(props);
			}catch (SQLException e) {
				logger.info("",e);
				throw new DataAccessException(e);
			} catch (IOException e) {
				logger.info("",e);
				throw new RuntimeException(e);
			}
			

			try {
				dataSource = factory.createDataSource(props);
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
		}
		return dataSource;
	}
	
	private void setDevelopmentConfig(Properties props) {
		props.put(DataSourceFactory.JDBC_URL, "jdbc:mysql://localhost:3306/drip?useUnicode=true&characterEncoding=utf8");
		props.put(DataSourceFactory.JDBC_USER, "root");
		props.put(DataSourceFactory.JDBC_PASSWORD, "abc123");
	}

}
