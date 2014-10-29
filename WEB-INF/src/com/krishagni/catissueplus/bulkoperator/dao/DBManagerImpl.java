/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationUtility;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * Database manager for Bulk Operation from UI.
 * @author sagar_baldwa
 *
 */
public class DBManagerImpl
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(DBManagerImpl.class);

	/**
	 * Get Connection Object.
	 * @return Connection.
	 * @throws BulkOperationException BulkOperationException.
	 */
	public static Connection getConnection() throws BulkOperationException
	{
		Properties bulkOprProp = BulkOperationUtility.getBulkOperationPropertiesInstance();
		String dbCredentialsFileName = bulkOprProp
				.getProperty(BulkOperationConstants.DATABASE_CREDENTIALS_FILE);
		Properties properties = BulkOperationUtility
				.getPropertiesFile("./" + dbCredentialsFileName);
		return getConnection(properties);
	}

	/**
	 * Get Connection Object.
	 * @param properties Properties.
	 * @return Connection.
	 * @throws BulkOperationException BulkOperationException.
	 */
	private static Connection getConnection(Properties properties) throws BulkOperationException
	{
		Connection connection = null;
		try
		{
			String databaseDriver = null;
			String databaseType = properties.getProperty("database.type");
			String databaseHost = properties.getProperty("database.host");
			String databasePort = properties.getProperty("database.port");
			String databaseName = properties.getProperty("database.name");
			String databaseUsername = properties.getProperty("database.username");
			String databasePassword = properties.getProperty("database.password");
			String databaseURL = null;
			if (BulkOperationConstants.ORACLE_DATABASE.equalsIgnoreCase(databaseType))
			{
				databaseDriver = "oracle.jdbc.driver.OracleDriver";
				databaseURL = "jdbc:oracle:thin:@" + databaseHost + ":" + databasePort + ":"
						+ databaseName;
			}
			else if (BulkOperationConstants.MYSQL_DATABASE.equalsIgnoreCase(databaseType))
			{
				databaseDriver = "com.mysql.jdbc.Driver";
				databaseURL = "jdbc:mysql://" + databaseHost + ":" + databasePort + "/"
						+ databaseName;
			}
			Class.forName(databaseDriver);
			connection = DriverManager.getConnection(databaseURL, databaseUsername,
					databasePassword);
		}
		catch (ClassNotFoundException cnfExp)
		{
			logger.debug("Error in creating database connection."
					+ " Please check the database driver.", cnfExp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.database.error.driver");
			throw new BulkOperationException(errorkey, cnfExp, "");
		}
		catch (SQLException sqlExp)
		{
			logger.debug("Error in creating database connection.", sqlExp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.database.error");
			throw new BulkOperationException(errorkey, sqlExp, "");
		}
		catch (Exception exp)
		{
			logger.debug("Error in creating database connection."
					+ " Please check the database driver or the host applications install "
					+ "properties have some missing database properties.", exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.database.error.driver.msg");
			throw new BulkOperationException(errorkey, exp, "");
		}
		return connection;
	}
}