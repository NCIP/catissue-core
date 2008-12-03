package edu.wustl.catissuecore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;


public class ClinicalDiagnosisHeirarchy {

	static String DATABASE_SERVER_NAME;
	static String DATABASE_SERVER_PORT_NUMBER;
	static String DATABASE_TYPE;
	static String DATABASE_NAME;
	static String DATABASE_USERNAME;
	static String DATABASE_PASSWORD;
	static String DATABASE_DRIVER;
	
	private void initDataBase(String[] args)
	{
		if(args.length<6)
		{
			 throw new RuntimeException("In sufficient number of arguments");
		}
		DATABASE_SERVER_NAME = args[0];
		DATABASE_SERVER_PORT_NUMBER = args[1];
		DATABASE_TYPE = args[2];
		DATABASE_NAME = args[3];
		DATABASE_USERNAME = args[4];
		DATABASE_PASSWORD = args[5];
		//DATABASE_DRIVER = args[6];	
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{
		Connection connection = null;
		// Load the JDBC driver
		
		// Create a connection to the database
		String url = "";
		if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		{
			DATABASE_DRIVER="com.mysql.jdbc.Driver";
			url = "jdbc:mysql://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER + "/"+ DATABASE_NAME; 
		}
		if ("Oracle".equalsIgnoreCase(DATABASE_TYPE))
		{
			DATABASE_DRIVER="oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER+ ":" + DATABASE_NAME;
		}
		Class.forName(DATABASE_DRIVER).newInstance();
		System.out.println("URL : " + url);
		connection= DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
		
	}
	private List populateClinicalDiagnosisHeirarchy(Connection connection ) throws SQLException 
	{
		List<Long> identifiers=new ArrayList<Long>();
		String reteriveRootNode="Select IDENTIFIER from catissue_permissible_value where" +
				" PUBLIC_ID ='Clinical_Diagnosis_PID'";
		ResultSet resultSet=null;
		Statement statement = null;
			statement = connection.createStatement();
			List  idList=new ArrayList<String>();
			resultSet=statement.executeQuery(reteriveRootNode);
			while(resultSet.next())
			{
				String id=resultSet.getString("IDENTIFIER");
				idList.add(id);
			
				retrieveChilds(id, connection ,idList);
			}
			resultSet.close();
			statement.close();

		return idList;
	}
	private void retrieveChilds(String parentId,Connection connection, List idList ) throws SQLException
	{
		String retreiveChild="Select IDENTIFIER from catissue_permissible_value where PARENT_IDENTIFIER="+parentId;
		ResultSet resultSet=null;
		Statement statement = null;
		statement = connection.createStatement();	
		resultSet=statement.executeQuery(retreiveChild);
		
		while(resultSet.next())
		{
			String id=resultSet.getString("IDENTIFIER");
			idList.add(id);
			retrieveChilds(id, connection ,idList);
		}
		resultSet.close();
		statement.close();
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{	
		ClinicalDiagnosisHeirarchy clinicalDiagnosisHeirarchy=new ClinicalDiagnosisHeirarchy();
		
		clinicalDiagnosisHeirarchy.initDataBase(args);
		Connection connection=null;
		try
		{
		connection = clinicalDiagnosisHeirarchy.getConnection();
		List idList=clinicalDiagnosisHeirarchy.populateClinicalDiagnosisHeirarchy(connection);
		clinicalDiagnosisHeirarchy.deleteClinicalDiagnosisHeirarchy(idList,connection);
		
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			try 
			{
				connection.rollback();
			}
			catch (SQLException e1)
			{
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
				e.printStackTrace();
			}
		}
	}

	private  void deleteClinicalDiagnosisHeirarchy(List idList,Connection connection) throws SQLException 
	{
		Statement statement2=connection.createStatement();
		Iterator iter=idList.iterator();
		StringBuffer ids=new StringBuffer();
		ids.append("(");
		System.out.println(idList);
		while(iter.hasNext())
		{
			ids.append(iter.next());
			ids.append(",");
		}
		
		ids.deleteCharAt(ids.lastIndexOf(","));
		ids.append(")");
		
		String deleteOldClinicalDiagnosis=new String();
		if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		{
			deleteOldClinicalDiagnosis="Delete from catissue_permissible_value where  " +
				"IDENTIFIER in "+ids+ "order by PARENT_IDENTIFIER desc";
			System.out.println(deleteOldClinicalDiagnosis);
			statement2.executeUpdate(deleteOldClinicalDiagnosis);
		}
		if ("Oracle".equalsIgnoreCase(DATABASE_TYPE))
		{
			statement2.executeUpdate("ALTER TABLE catissue_permissible_value disable CONSTRAINT FK57DDCE153B5435E");
			deleteOldClinicalDiagnosis="Delete from catissue_permissible_value where  " +
			"IDENTIFIER in "+ids;
			System.out.println(deleteOldClinicalDiagnosis);
			statement2.executeUpdate(deleteOldClinicalDiagnosis);
			statement2.executeUpdate("ALTER TABLE catissue_permissible_value  ENABLE  CONSTRAINT FK57DDCE153B5435E");
		}
		statement2.close();		
		System.out.println("All the old Clinical Diagnosis values deleted successfully");
	}
}
