import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.codec.language.Metaphone;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;



public class UpdateParticipantMetaPhoneInfo
{

	private static Connection connection = null;
	private static Statement stmt = null;
	// The Name of the server for the database. For example : localhost
	static String DATABASE_SERVER_NAME;
	// The Port number of the server for the database.
	static String DATABASE_SERVER_PORT_NUMBER;
	// The Type of Database. Use one of the two values 'MySQL', 'Oracle', MsSqlServer.
	static String DATABASE_TYPE;
	//	Name of the Database.
	static String DATABASE_NAME;
	// Database User name
	static String DATABASE_USERNAME;
	// Database Password
	static String DATABASE_PASSWORD;
	// The database Driver 
	static String DATABASE_DRIVER;
	//Oracle Version
	static String ORACLE_TNS_NAME;

	//static String DB_SPECIFIC_COMPARE_OPERATOR;

	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException,ApplicationException
	{

		configureDBConnection(args);
		connection = getConnection();
		connection.setAutoCommit(true);
		stmt = connection.createStatement();
		insertMetaPhoneCodeForLastName();

	}

	/**
	 * This method will insert metaPhonic codes for last name in the participant table
	 * @throws ApplicationException : ApplicationException
	 * @throws SQLException : SQLException
	 */
	public static void insertMetaPhoneCodeForLastName() throws ApplicationException, SQLException
	{
		String lNameMetaPhone=null;
		String sql="select identifier,last_name from catissue_participant";
		Metaphone metaPhoneObj = new Metaphone();
		ResultSet rs=stmt.executeQuery(sql);
		if (rs!= null)
		{	
				while(rs.next()){
					stmt=connection.createStatement();
					String id=rs.getString("identifier");
					String lastName=rs.getString("last_name");
					lNameMetaPhone = metaPhoneObj.metaphone(lastName);
					stmt.executeUpdate("update catissue_participant set lName_metaPhone='"+lNameMetaPhone+"'"+ "  where identifier="+id);
					stmt.close();
			}
		}

	}

	/**
	 * @param args : database configuration values.
	 */
	public static void configureDBConnection(String[] args)
	{
		if (args.length < 7)
		{
			throw new RuntimeException("In sufficient number of arguments");
		}
		DATABASE_SERVER_NAME = args[0];
		DATABASE_SERVER_PORT_NUMBER = args[1];
		DATABASE_TYPE = args[2];
		DATABASE_NAME = args[3];
		DATABASE_USERNAME = args[4];
		DATABASE_PASSWORD = args[5];
		DATABASE_DRIVER = args[6];
	}

	/**
	* This method will create a database connection using configuration info.
	* @return Connection : Database connection object
	* @throws ClassNotFoundException
	* @throws SQLException
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
			System.out.println("UpdateMetadata.getConnection() URL : " + url);
		}
		connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
	}
}
