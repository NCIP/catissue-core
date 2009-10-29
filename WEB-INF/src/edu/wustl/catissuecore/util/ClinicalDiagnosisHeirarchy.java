package edu.wustl.catissuecore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.common.exceptionformatter.ConstraintViolationFormatter;
import edu.wustl.common.util.logger.Logger;


public class ClinicalDiagnosisHeirarchy
{
	/**
	 * Logger. 
	 */
	private Logger logger = Logger.getCommonLogger(ConstraintViolationFormatter.class);
	
	static String DATABASE_SERVER_NAME;
	static String DATABASE_SERVER_PORT_NUMBER;
	static String DATABASE_TYPE;
	static String DATABASE_NAME;
	static String DATABASE_USERNAME;
	static String DATABASE_PASSWORD;
	static String DATABASE_DRIVER;

	static Object [] connectionArgs = null;
	static final String mysqlUrl="jdbc:mysql://{0}:{1}/{2}";
	static final String oracleUrl="jdbc:oracle:thin:@{0}:{1}:{2}";
	static final String mssqlserverUrl="jdbc:sqlserver://{0}:{1};databaseName={2};";

	private void initDataBase(String[] args) throws CatissueException
	{
		if(args.length<6)
		{
			 throw new CatissueException("In sufficient number of arguments");
		}
		DATABASE_TYPE = args[2];
		DATABASE_USERNAME = args[4];
		DATABASE_PASSWORD = args[5];
		connectionArgs=new Object[] {args[0],args[1],args[3]};
		//DATABASE_DRIVER = args[6];
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{
		Connection connection = null;
		// Load the JDBC driver

		// Create a connection to the database
		String url = "";
		if ("MySQL".equalsIgnoreCase(DATABASE_TYPE)) {
			DATABASE_DRIVER="com.mysql.jdbc.Driver";
			url=mysqlUrl;
		}
		if ("Oracle".equalsIgnoreCase(DATABASE_TYPE)) {
			DATABASE_DRIVER="oracle.jdbc.driver.OracleDriver";
			url=oracleUrl;
		}
		if ("MSSQLSERVER".equalsIgnoreCase(DATABASE_TYPE)) {
			DATABASE_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
			url=mssqlserverUrl;
		}
		url = MessageFormat.format(url, connectionArgs);
		Class.forName(DATABASE_DRIVER).newInstance();
		logger.info("URL:" + url);
		connection= DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
	}

	private List <String>populateClinicalDiagnosisHeirarchy(Connection connection) throws SQLException {
		List< Long > identifiers = new ArrayList< Long >();
		String reteriveRootNode = "Select IDENTIFIER from catissue_permissible_value where"
			+
			" PUBLIC_ID ='Clinical_Diagnosis_PID'";
		ResultSet resultSet = null;
		Statement statement = null;
			statement = connection.createStatement();
			List<String>  idList = new ArrayList< String >();
			resultSet=statement.executeQuery(reteriveRootNode);
			while(resultSet.next())
			{
				String id = resultSet.getString("IDENTIFIER");
				idList.add(id);

				retrieveChilds(id, connection ,idList);
			}
			resultSet.close();
			statement.close();

		return idList;
	}
	private void retrieveChilds(String parentId,Connection connection, List<String> idList ) throws SQLException
	{
		String retreiveChild = "Select IDENTIFIER from catissue_permissible_value where PARENT_IDENTIFIER="+parentId;
		ResultSet resultSet = null;
		Statement statement = null;
		statement = connection.createStatement();
		resultSet = statement.executeQuery(retreiveChild);

		while(resultSet.next())
		{
			String id = resultSet.getString("IDENTIFIER");
			idList.add(id);
			retrieveChilds(id, connection ,idList);
		}
		resultSet.close();
		statement.close();
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, CatissueException
	{
		ClinicalDiagnosisHeirarchy clinicalDiagnosisHeirarchy = new ClinicalDiagnosisHeirarchy();

		clinicalDiagnosisHeirarchy.initDataBase(args);
		Connection connection = null;
		try
		{
		connection = clinicalDiagnosisHeirarchy.getConnection();
		List<String> idList = clinicalDiagnosisHeirarchy.populateClinicalDiagnosisHeirarchy(connection);
		clinicalDiagnosisHeirarchy.deleteClinicalDiagnosisHeirarchy(idList,connection);

		}
		catch (SQLException e)
		{
			clinicalDiagnosisHeirarchy.logger.
			error("SQLException"+e.getMessage(),e);
			e.printStackTrace();
			try
			{
				connection.rollback();
			}
			catch (SQLException e1)
			{
				clinicalDiagnosisHeirarchy.logger.
				error("SQLException"+e1.getMessage(),e1);
				e1.printStackTrace();
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				clinicalDiagnosisHeirarchy.logger.
				error("SQLException"+e.getMessage(),e);
				e.printStackTrace();
			}
		}
	}

	private  void deleteClinicalDiagnosisHeirarchy(List<String> idList,Connection connection) throws SQLException
	{
		Statement statement2 = connection.createStatement();
		Iterator<String> iter = idList.iterator();
		StringBuffer ids = new StringBuffer();
		ids.append("(");
		logger.info(idList);
		while(iter.hasNext())
		{
			ids.append(iter.next());
			ids.append(",");
		}

		ids.deleteCharAt(ids.lastIndexOf(","));
		ids.append(")");

		String deleteOldClinicalDiagnosis = "Delete from catissue_permissible_value where  " +
		"IDENTIFIER in " + ids;
		//logger.info(deleteOldClinicalDiagnosis);
		if ("MYSQL".equalsIgnoreCase(DATABASE_TYPE)) {
			statement2.executeUpdate(deleteOldClinicalDiagnosis);
		}

		if ("ORACLE".equalsIgnoreCase(DATABASE_TYPE)) {
			statement2.executeUpdate("ALTER TABLE catissue_permissible_value disable CONSTRAINT FK57DDCE153B5435E");
			statement2.executeUpdate(deleteOldClinicalDiagnosis);
			statement2.executeUpdate("ALTER TABLE catissue_permissible_value  ENABLE  CONSTRAINT FK57DDCE153B5435E");
		}
		if ("MSSQLSERVER".equalsIgnoreCase(DATABASE_TYPE)) {
			statement2.executeUpdate("ALTER TABLE catissue_permissible_value NOCHECK CONSTRAINT FK_CAT_PERMI_VALUE_PARENT_ID");
			statement2.executeUpdate(deleteOldClinicalDiagnosis);
			statement2.executeUpdate("ALTER TABLE catissue_permissible_value CHECK CONSTRAINT FK_CAT_PERMI_VALUE_PARENT_ID");
		}
		statement2.close();
		logger.info("All the old Clinical Diagnosis values deleted successfully");
		
	}
}