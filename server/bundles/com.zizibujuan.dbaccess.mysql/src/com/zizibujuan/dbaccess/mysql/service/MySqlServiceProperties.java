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
 *     tware - Added MySQL constants
 *     mkeith - Refactorization
 ******************************************************************************/

package com.zizibujuan.dbaccess.mysql.service;

/**
 * Service property values for MySQL data source factory services.
 */
public class MySqlServiceProperties {

    // Register data source factory service under the driver class name
    public static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    // All MySQL DataSourceFactory services will have their
    // DataSourceFactory.OSGI_JDBC_DRIVER_NAME service property set to this driver name
    public static final String MYSQL_DRIVER_NAME = "MySQL";

}
