import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Description: This method with Automate date function and update the REPORTING_PERIOD of MDS DB. 
 * @author virender_mehta
 *
 */
public class ExtractLoadMinimalData
{
	/**
	 * The Name of the server for the database. For example : localhost
	 */
	private static String DATABASE_SERVER_NAME;
	/**
	 *  The Port number of the server for the database.
	 */
	private static String DATABASE_SERVER_PORT_NUMBER;
	/**
	 * The Type of Database. Use one of the two values 'MySQL', 'Oracle'.
	 */
	private static String DATABASE_TYPE;
	/**
	 * Name of the Database.
	 */
	private static String DATABASE_NAME;
	/**
	 *  Database User name
	 */
	private static String DATABASE_USERNAME;
	/**
	 *  Database Password
	 */
	private static String DATABASE_PASSWORD;
	/**
	 *  The database Driver 
	 */
	private static String DATABASE_DRIVER;
	/**
	 * Constant for Single Quotes
	 */
	private static String SINGLE_QUOTES = "\'";
	/**
	 * Constant for date format
	 */
	private static String DATE_FORMAT = "YYYY-MM-DD";
	
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Configuring Database Connection Parameters");
		configureDBConnection(args);
		try
		{
			// Create an Connection object
			Connection connection = getConnection();
			try
			{
				createSQL(connection);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will create a database connection using configuration info.
	 * @return Connection : Database connection object
	 * @throws ClassNotFoundException Class NotFound Exception Exception
	 * @throws SQLException SQLException Exception
	 */
	private static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Connection connection = null;
		// Load the JDBC driver
		Class.forName(DATABASE_DRIVER);
		String url = "";
		// Create a connection to the database
		if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:mysql://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER + "/"
					+ DATABASE_NAME; 
			
		}
		if ("Oracle".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:oracle:thin:@" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER
					+ ":" + DATABASE_NAME;
		}
		System.out.println("URL : " + url);
		connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
	}

	/**
	 * This method is for configuring database connection.
	 * @param args String[] of configuration info 
	 */
	private static void configureDBConnection(String[] args)
	{
		if (args.length == 7)
		{
			DATABASE_SERVER_NAME = args[0];
			DATABASE_SERVER_PORT_NUMBER = args[1];
			DATABASE_TYPE = args[2];
			DATABASE_NAME = args[3];
			DATABASE_USERNAME = args[4];
			DATABASE_PASSWORD = args[5];
			DATABASE_DRIVER = args[6];
			printDBInfo();
		}
		else
		{
			System.out.println("Incorrect number of parameters!");
			throw new RuntimeException("Incorrect number of parameters!!!!");
		}
	}
	
	/**
	 * Printing the Configuration info for database.
	 */
	private static void printDBInfo()
	{
		System.out.println("DATABASE_SERVER_NAME        : " + DATABASE_SERVER_NAME);
		System.out.println("DATABASE_SERVER_PORT_NUMBER : " + DATABASE_SERVER_PORT_NUMBER);
		System.out.println("DATABASE_TYPE               : " + DATABASE_TYPE);
		System.out.println("DATABASE_NAME               : " + DATABASE_NAME);
		System.out.println("DATABASE_DRIVER             : " + DATABASE_DRIVER);
	}
	/**
	 *  
	 * @param connection Connection object
	 * @throws SQLException SQLException exception
	 * @throws IOException IOException exception
	 */
	public static void createSQL(Connection connection) throws SQLException, IOException
	{
		String query = "select id from reporting_period";
		getResultSet(connection,query);
	}
	
	/**
	 * This method will update reporting_period of MDS database with new dates
	 * @param connection Database connection object
	 * @param sql Query String
	 * @throws SQLException SQLException exception
	 * @throws IOException IOException exception
	 */
	private static void getResultSet(Connection connection, String sql) throws SQLException, IOException
	{
		String query = null;
		String year =null;
		String startDate=null;
		String endDate = null;
		String quarter=null;
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		ResultSet resultSet = stmt.executeQuery(sql);
		Statement updateStatement = connection.createStatement();
		System.out.println(resultSet);
		if(resultSet!= null)
		{
		  while(resultSet.next())
	      { 
	    	  quarter  = resultSet.getString(1).substring(4, 5);
	    	  System.out.println("Quater Number:"+quarter);
	    	  year =resultSet.getString(1).substring(0, 4);
	    	  System.out.println("Year:"+year);
	    	  if("1".equals(quarter))
	    	  {
	    		 startDate = year+"-01-01"; 
	    		 endDate = year+"-03-31";
	    		 if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
	    		 {
	    			 query = "UPDATE REPORTING_PERIOD SET START_DATE="+SINGLE_QUOTES+startDate+SINGLE_QUOTES+",END_DATE="+ SINGLE_QUOTES+endDate+SINGLE_QUOTES +" WHERE ID="+resultSet.getString(1);
	    		 }
	    		 else
	    		 {
	    			 query = "UPDATE REPORTING_PERIOD SET START_DATE = TO_DATE("+SINGLE_QUOTES+startDate+SINGLE_QUOTES+","+SINGLE_QUOTES+DATE_FORMAT+SINGLE_QUOTES+"),"
	    			 +" END_DATE = TO_DATE("+SINGLE_QUOTES+endDate+SINGLE_QUOTES+","+SINGLE_QUOTES+DATE_FORMAT+SINGLE_QUOTES+") WHERE ID="+resultSet.getString(1);
	    		 }
	    	  }
	    	  else if("2".equals(quarter))
	    	  {
	    		  startDate = year+"-04-01"; 
	    		  endDate = year+"-06-30";
    			 if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
	    		 {
	    			 query = "UPDATE REPORTING_PERIOD SET START_DATE="+SINGLE_QUOTES+startDate+SINGLE_QUOTES+",END_DATE="+ SINGLE_QUOTES+endDate+SINGLE_QUOTES +" WHERE ID="+resultSet.getString(1);
	    		 }
    			 else
    			 {
    				 query = "UPDATE REPORTING_PERIOD SET START_DATE= TO_DATE("+SINGLE_QUOTES+startDate+SINGLE_QUOTES+","+SINGLE_QUOTES+DATE_FORMAT+SINGLE_QUOTES+"),"
	    			 +" END_DATE = TO_DATE("+SINGLE_QUOTES+endDate+SINGLE_QUOTES+","+SINGLE_QUOTES+DATE_FORMAT+SINGLE_QUOTES+") WHERE ID="+resultSet.getString(1); 
    			 }

	    	  }
	    	  else if("3".equals(quarter))
	    	  {
	    		  startDate = year+"-07-01"; 
	    		  endDate = year+"-09-30";
	    			 if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		    		 {
		    			 query = "UPDATE REPORTING_PERIOD SET START_DATE="+SINGLE_QUOTES+startDate+SINGLE_QUOTES+",END_DATE="+ SINGLE_QUOTES+endDate+SINGLE_QUOTES +" WHERE ID="+resultSet.getString(1);
		    		 }
	    			 else
	    			 {
	    				 query = "UPDATE REPORTING_PERIOD SET START_DATE= TO_DATE("+SINGLE_QUOTES+startDate+SINGLE_QUOTES+","+SINGLE_QUOTES+DATE_FORMAT+SINGLE_QUOTES+"),"
		    			 +" END_DATE = TO_DATE("+SINGLE_QUOTES+endDate+SINGLE_QUOTES+","+SINGLE_QUOTES+DATE_FORMAT+SINGLE_QUOTES+") WHERE ID="+resultSet.getString(1); 
	    			 }
	    	  }
	    	  else if("4".equals(quarter))
	    	  {
	    		  startDate = year+"-10-01"; 
	    		  endDate = year+"-12-31";
	    			 if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		    		 {
		    			 query = "UPDATE REPORTING_PERIOD SET START_DATE="+SINGLE_QUOTES+startDate+SINGLE_QUOTES+",END_DATE="+ SINGLE_QUOTES+endDate+SINGLE_QUOTES +" WHERE ID="+resultSet.getString(1);
		    		 }
	    			 else
	    			 {
	    				 query = "UPDATE REPORTING_PERIOD SET START_DATE= TO_DATE("+SINGLE_QUOTES+startDate+SINGLE_QUOTES+","+SINGLE_QUOTES+DATE_FORMAT+SINGLE_QUOTES+"),"
		    			 +" END_DATE = TO_DATE("+SINGLE_QUOTES+endDate+SINGLE_QUOTES+","+SINGLE_QUOTES+DATE_FORMAT+SINGLE_QUOTES+") WHERE ID="+resultSet.getString(1);
	    			 }
	    	  }
	    	  System.out.println(query);
	    	  updateStatement.executeUpdate(query);
	      }
		}
		resultSet.close();
		stmt.close();
		updateStatement.close();
	}
	
}
