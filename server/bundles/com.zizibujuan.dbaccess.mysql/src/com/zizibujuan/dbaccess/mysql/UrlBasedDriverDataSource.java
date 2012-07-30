/*******************************************************************************
 * Copyright (c) 2010 Oracle.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution. 
 * The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at 
 *     http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 * Contributors:
 *     mkeith - URL support for Client/Server Derby JDBC  
 ******************************************************************************/

package com.zizibujuan.dbaccess.mysql;

import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;
import java.sql.SQLFeatureNotSupportedException;

import javax.sql.DataSource;

import static org.osgi.service.jdbc.DataSourceFactory.*;

/** 
 * An abbreviated/simplified DataSource impl that takes a URL from the client
 * and just returns a thin data source wrapper around the basic JDBC driver.
 */
class UrlBasedDriverDataSource implements DataSource {

    Driver driver;
    Properties properties = null;
    String url = null;

    /**
     * @param properties The properties to use for operations on the driver
     * @param embedded Whether to wrap an embedded or a client driver
     */
    public UrlBasedDriverDataSource(Properties properties, Driver driver) {
        this.driver = driver;
        this.properties = (Properties) properties.clone();
        this.url = properties.getProperty(JDBC_URL);
    }

    public Connection getConnection() throws java.sql.SQLException {
        return driver.connect(url, properties);
    }

    public Connection getConnection(String user, String password) throws java.sql.SQLException {
        Properties localProps = (Properties) properties.clone();
        localProps.put(JDBC_USER, user);
        localProps.put(JDBC_PASSWORD, password);
        return driver.connect(url, localProps);
    }
 
    public boolean isWrapperFor(Class<?> cls) { 
        return cls.isInstance(driver);
    }
    
    public <T> T unwrap(Class<T> cls) { 
        return (isWrapperFor(cls)) 
            ? (T) driver 
            : null;
    }

    public PrintWriter getLogWriter() throws SQLException { 
        return DriverManager.getLogWriter(); 
    }

    public int getLoginTimeout() throws SQLException { 
        return DriverManager.getLoginTimeout(); 
    }

    // Don't support setting log writer or timeout 

    public void setLogWriter(PrintWriter writer) throws SQLException {
        throw new SQLException("Can't set Log Writer on URL data source");
    }
    
    public void setLoginTimeout(int timeout) throws SQLException {
        throw new SQLException("Can't set Login Timeout on URL data source");
    }

	
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("Can't get Parent Logger from URL data source");
    }
}