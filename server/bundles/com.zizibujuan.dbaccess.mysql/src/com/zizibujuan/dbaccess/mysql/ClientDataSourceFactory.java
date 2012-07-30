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
 *     mkeith - Template for JDBC driver support
 *     tware - MySQL driver class additions
 ******************************************************************************/

package com.zizibujuan.dbaccess.mysql;

import java.sql.Driver;
import java.sql.SQLException;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

/**
 * A factory for creating MySQL data sources. The properties specified
 * in the create methods determine how the created object is configured.
 *
 * This service also supports a URL-based data source. 
 */
public class ClientDataSourceFactory extends AbstractDataSourceFactory {
    
    public ClientDataSourceFactory() {}

    @Override
    public Driver newJdbcDriver() throws SQLException {
        return new com.mysql.jdbc.Driver();
    }

    @Override
    public DataSource newDataSource() throws SQLException {
        return new MysqlDataSource();
    }

    @Override
    public ConnectionPoolDataSource newConnectionPoolDataSource() 
            throws SQLException {
        return new MysqlConnectionPoolDataSource();
    }

    @Override
    public XADataSource newXADataSource() throws SQLException {
        return new MysqlXADataSource();
    }
}
