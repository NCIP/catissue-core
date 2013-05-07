
package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class takes connection parameters from command line arguments
 * and return cConnection object
 * @author deepali_ahirrao
 *
 */
public class DBConnectionUtil
{

	private static String DATABASE_SERVER_NAME;
	/**
	 *  The Port number of the server for the database.
	 */
	private static String DATABASE_SERVER_PORT_NUMBER;
	/**
	 *  The Type of Database. Use one of these values 'MySQL', 'Oracle', MsSqlServer.
	 */
	public static String DATABASE_TYPE;
	/**
	 * 	Name of the Database.
	 */
	private static String DATABASE_NAME;
	/**
	 *  Database User name.
	 */
	private static String DATABASE_USERNAME;
	/**
	 *  Database Password.
	 */
	private static String DATABASE_PASSWORD;
	/**
	 *  The database Driver.
	 */
	private static String DATABASE_DRIVER;

	/**
	 *
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection getDBConnection(String[] args) throws ClassNotFoundException,
			SQLException
	{
		if (args.length < 7)
		{
			throw new RuntimeException("Insufficient number of arguments");
		}
		DATABASE_SERVER_NAME = args[0];
		DATABASE_SERVER_PORT_NUMBER = args[1];
		DATABASE_TYPE = args[2];
		DATABASE_NAME = args[3];
		DATABASE_USERNAME = args[4];
		DATABASE_PASSWORD = args[5];
		DATABASE_DRIVER = args[6];

		return getConnection();
	}

	/**
	 * This method will create a database connection using configuration info.
	 * @return Connection : Database connection object
	 * @throws ClassNotFoundException Class Not Found Exception
	 * @throws SQLException SQL Exception
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Connection connection = null;
		// Load the JDBC driver
		Class.forName(DATABASE_DRIVER);
		// Create a connection to the database
		String url = "";
		if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:mysql://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER + "/"
					+ DATABASE_NAME; // a JDBC url
		}
		if ("Oracle".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:oracle:thin:@" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER
					+ ":" + DATABASE_NAME;
		}
		if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:sqlserver://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER
					+ ";" + "databaseName=" + DATABASE_NAME + ";";
		}

		System.out.println("Connection URL : " + url);
		connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
	}

}
