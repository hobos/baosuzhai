package com.zizibujuan.dbaccess.mysql.service;

import javax.sql.DataSource;

/**
 * 获取数据库服务
 * @author 金正伟
 * @since 0.0.1
 */
public interface DataSourceService {

	DataSource getDataSource();
}
