package com.zizibujuan.drip.server.util.dao;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.dbaccess.mysql.service.DataSourceService;

/**
 * 所有dao实现类的基类，提供注入和注销DataSourceService的功能，
 * 可以通过DataSourceService获取数据库链接
 * @author jinzw
 * @since 0.0.1
 */
public abstract class AbstractDao {
	private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);
	private DataSourceService dataSourceService;
	public void unsetDataSourceService(DataSourceService dataSourceService) {
		logger.info("注销datasourceService");
		if(this.dataSourceService == dataSourceService){
			this.dataSourceService = null;
		}
	}
	public void setDataSourceService(DataSourceService dataSourceService) {
		logger.info("注入datasourceService");
		logger.info(dataSourceService.toString());
		this.dataSourceService = dataSourceService;
	}
	protected DataSource getDataSource(){
		return this.dataSourceService.getDataSource();
	}
}
