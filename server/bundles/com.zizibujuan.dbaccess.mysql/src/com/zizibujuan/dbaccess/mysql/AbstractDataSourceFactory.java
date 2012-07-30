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
 *     JJ Snyder - Embedded Derby JDBC support 
 *     mkeith - Abstracted out the logic and made reusable for all databases
 ******************************************************************************/

package com.zizibujuan.dbaccess.mysql;

import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.osgi.service.jdbc.DataSourceFactory;

/**
 * Abstract factory for creating JDBC data sources and drivers. The properties 
 * specified in the create methods determine how the created object is configured.
 * 
 * Sample code for obtaining a network data source (e.g. using Derby):
 * 
 *     ServiceTracker tracker = 
 *         new ServiceTracker(context, DataSourceFactory.class.getName(), null); 
 *     tracker.open();
 *     DataSourceFactory dsf = (DataSourceFactory) tracker.getService(); 
 *     Properties props = new Properties();
 *     props.put(DataSourceFactory.JDBC_SERVER_NAME, "localhost");
 *     props.put(DataSourceFactory.JDBC_PORT_NUMBER, "1527");
 *     props.put(DataSourceFactory.JDBC_DATABASE_NAME, "myDB");
 *     props.put(DataSourceFactory.JDBC_USER, "mike");
 *     props.put(DataSourceFactory.JDBC_PASSWORD, "password");
 *     DataSource ds = dsf.createDataSource(props);
 *
 * This service also supports a URL-based data source. The following 3 properties
 * can be provided instead of the 5 properties above if the driver supports URLs.
 * 
 *     props.put(DataSourceFactory.JDBC_URL, "jdbc:derby://localhost:1527/myDB");
 *     props.put(DataSourceFactory.JDBC_USER, "mike");
 *     props.put(DataSourceFactory.JDBC_PASSWORD, "password");
 */
public abstract class AbstractDataSourceFactory implements DataSourceFactory {

    /*****************************************************/
    /* Abstract methods must be implemented by subclass. */
    /*                                                   */
    /* Subclass drivers must return an instance of the   */
    /* appropriate driver class implementing the correct */
    /* interface return type.                            */
    /*****************************************************/

    public abstract Driver newJdbcDriver() throws SQLException;

    public abstract DataSource newDataSource() throws SQLException;

    public abstract ConnectionPoolDataSource newConnectionPoolDataSource() throws SQLException;

    public abstract XADataSource newXADataSource() throws SQLException;


    /*************************/
    /* DataSourceFactory API */
    /*************************/

    /**
     * Create a DataSource object.
     * 
     * @param props The properties that define the DataSource implementation to
     *              create and how the DataSource is configured
     * @return The configured DataSource
     * @throws SQLException
     * @see org.osgi.service.jdbc.DataSourceFactory#createDataSource(java.util.Properties)
     */
    public DataSource createDataSource(Properties props) throws SQLException {
        if (props == null) props = new Properties();
        if (props.get(DataSourceFactory.JDBC_URL) != null) {
            return new UrlBasedDriverDataSource(props, newJdbcDriver());
        } else {
            DataSource dataSource = newDataSource();
            setDataSourceProperties(dataSource, props);
            return dataSource;
        }
    }

    /**
     * Create a ConnectionPoolDataSource object.
     * 
     * @param props The properties that define the ConnectionPoolDataSource
     *              implementation to create and how the ConnectionPoolDataSource is
     *              configured
     * @return The configured ConnectionPoolDataSource
     * @throws SQLException
     * @see org.osgi.service.jdbc.DataSourceFactory#createConnectionPoolDataSource(java.util.Properties)
     */
    public ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws SQLException {
        if (props == null) props = new Properties();
        ConnectionPoolDataSource dataSource = newConnectionPoolDataSource();
        setDataSourceProperties(dataSource, props);
        return dataSource;
    }

    /**
     * Create an XADataSource object.
     * 
     * @param props The properties that define the XADataSource implementation
     *              to create and how the XADataSource is configured
     * @return The configured XADataSource
     * @throws SQLException
     * @see org.osgi.service.jdbc.DataSourceFactory#createXADataSource(java.util.Properties)
     */
    public XADataSource createXADataSource(Properties props) throws SQLException {
        if (props == null) props = new Properties();
        XADataSource dataSource = newXADataSource();
        setDataSourceProperties(dataSource, props);
        return dataSource;
    }

    /**
     * Create a new JDBC Driver.
     * 
     * @param props The properties used to configure the Driver.  Null 
     *              indicates no properties.
     *              If the property cannot be set on the Driver being 
     *              created then an SQLException must be thrown.
     * @return A configured java.sql.Driver.
     * @throws SQLException If the driver cannot be created
     */
    public Driver createDriver(Properties props) throws SQLException {
        Driver driver = newJdbcDriver();
        setDataSourceProperties(driver, props);
        return driver;
    }

    /**********************/
    /* Supporting methods */
    /**********************/

	protected void setDataSourceProperties(Object object, Properties props)
			throws SQLException {

	    if (props == null) return;
	    
		Enumeration<?> enumeration = props.keys();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			setProperty(object, name, props.getProperty(name));
		}
	}

	protected void throwSQLException(Exception cause, String theType, String value)
			throws SQLException {
		SQLException sqlException = 
		    new SQLException("Invalid " + theType + " value: " + value);
		sqlException.initCause(cause);
		throw sqlException;
	}

	protected Object toBasicType(String value, String type) throws SQLException {

        // Early return from first "if" condition that evaluates to true

        if (value == null)
			return null;

        if (type == null || type.equals(String.class.getName()))
			return value;

        if (type.equals(Integer.class.getName()) || type.equals(int.class.getName())) {
			try { return Integer.valueOf(value); } 
			catch (NumberFormatException e) {
				throwSQLException(e, "Integer", value);
			}
		}

		if (type.equals(Float.class.getName()) || type.equals(float.class.getName())) {
			try { return Float.valueOf(value); }
			catch (NumberFormatException e) {
				throwSQLException(e, "Float", value);
			}
		}

		if (type.equals(Long.class.getName()) || type.equals(long.class.getName())) {
			try { return Long.valueOf(value); }
			catch (NumberFormatException e) {
				throwSQLException(e, "Long", value);
			}
		}

		if (type.equals(Double.class.getName()) || type.equals(double.class.getName())) {
			try { return Double.valueOf(value); }
			catch (NumberFormatException e) {
				throwSQLException(e, "Double", value);
			}
		}

		if (type.equals(Character.class.getName()) || type.equals(char.class.getName())) {
			if (value.length() != 1) {
				throw new SQLException("Invalid Character value: " + value);
			}
			return new Character(value.charAt(0));
		}

		if (type.equals(Byte.class.getName()) || type.equals(byte.class.getName())) {
			try { return Byte.valueOf(value); }
			catch (NumberFormatException e) {
				throwSQLException(e, "Byte", value);
			}
		}

		if (type.equals(Short.class.getName()) || type.equals(short.class.getName())) {
			try { return Short.valueOf(value); }
			catch (NumberFormatException e) {
				throwSQLException(e, "Short", value);
			}
		}

        // Will be "false" if not in correct format...
        if (type.equals(Boolean.class.getName()) || type.equals(boolean.class.getName())) {
			return Boolean.valueOf(value);
		}

        throw new SQLException("Unrecognized property type: " + type);
	}

	protected void setProperty(Object object, String name, String value)
			throws SQLException {
		Class<?> type = object.getClass();

		PropertyDescriptor[] descriptors;
		try {
			descriptors = Introspector.getBeanInfo(type)
					                  .getPropertyDescriptors();
		} catch (Exception ex) {
			SQLException sqlException = new SQLException();
			sqlException.initCause(ex);
			throw sqlException;
		}
		List<String> names = new ArrayList<String>();

		for (int i = 0; i < descriptors.length; i++) {
			if (descriptors[i].getWriteMethod() == null) {
				continue;
			}

			if (descriptors[i].getName().equals(name)) {
				Method method = descriptors[i].getWriteMethod();
				Class<?> paramType = method.getParameterTypes()[0];
				Object param = toBasicType(value, paramType.getName());

				try {
					method.invoke(object, new Object[] {param});
				}
				catch (Exception ex) {
					SQLException sqlException = new SQLException();
					sqlException.initCause(ex);
					throw sqlException;
				}
				return;
			}

			names.add(descriptors[i].getName());
		}
		throw new SQLException("No such property: " + name +
							   ", exists.  Writable properties are: " + names);
	}
}
