package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UpdateMetaDataForEDIN extends UpdateMetadata{
	
    private static Connection connection = null;
	private static Statement stmt = null;
	
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException
	{
		try
		{
			configureDBConnection(args);
			connection = getConnection();
			connection.setAutoCommit(true);
			stmt = connection.createStatement();
			UpdateMetadataUtil.isExecuteStatement = true;
			
			DB_SPECIFIC_COMPARE_OPERATOR = UpdateMetadataUtil.getDBCompareModifier();
					
			deleteMeatadata();
		
			updateMetadata();
			
		}
		finally
		{
			if(UpdateMetadataUtil.metadataSQLFile!=null)
			{
				UpdateMetadataUtil.metadataSQLFile.close();
			}
			if(UpdateMetadataUtil.failureWriter!=null)
			{
				UpdateMetadataUtil.failureWriter.close();
			}
			if(connection!=null)
			{
				connection.close();
			}
		}
	}
	

	
	private static void updateMetadata() throws SQLException, IOException
	{
		/**  update statements  start **/
		List<String> updateSQL = getUpdateSQL();
		UpdateMetadataUtil.executeSQLs(updateSQL, connection.createStatement(), false);
		
		/**  update statements  end **/	
	}

	private static void deleteMeatadata() throws IOException, SQLException
	{
		
		/**  delete attribute statements  start **/
		DeleteAttributeForEDIN deleteAttribute = new DeleteAttributeForEDIN(connection);
		List<String> deleteSQL = deleteAttribute.deleteAttribute();
		UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);
		/**  delete attribute statements  end **/
	
	}
	
	private static List<String> getUpdateSQL() throws SQLException, IOException
	{
		List<String> dbUpdateSQL = new ArrayList<String>();
		ResultSet rs;
		
		stmt = connection.createStatement();
		
//		 Added by Geeta for edinburgh : Renaming zipcode to postCode
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Address')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'zipCode'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'postCode' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		
		//Added by Geeta for edinburgh : Renaming irb id to Ethics number
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.SpecimenProtocol')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'irbIdentifier'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'ethicsNumber' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.CollectionProtocol')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'irbIdentifier'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'ethicsNumber' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionProtocol')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'irbIdentifier'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'ethicsNumber' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		
		// End By Geeta
		
		return dbUpdateSQL;
	}

}
