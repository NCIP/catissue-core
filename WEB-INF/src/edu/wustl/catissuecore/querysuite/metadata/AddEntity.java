package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.wustl.common.util.dbManager.DBUtil;

public class AddEntity 
{
	private Connection connection = null;
	
	AddEntity(Connection connection)
	{
		this.connection = connection;
	}
	
	public static void main(String args[])throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
	}
	
	public void addEntity(List<String> entityList,String tableName,String parentEntity,int inheritanceStrategy) throws SQLException, IOException
	{
		//populateEntityList();
		
		Statement stmt = connection.createStatement();
		for(String entityName : entityList)
		{
			//insert statements
			String sql = "select max(identifier) from dyextn_abstract_metadata";
			ResultSet rs = stmt.executeQuery(sql);
			int nextIdOfAbstractMetadata = 0;
			if (rs.next())
			{
				int maxId = rs.getInt(1);
				nextIdOfAbstractMetadata = maxId + 1;
			}
			rs.close();
			int nextIdDatabaseproperties = 0;
			sql = "select max(identifier) from dyextn_database_properties";
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				int maxId = rs.getInt(1);
				nextIdDatabaseproperties = maxId + 1;
			}
			rs.close();
			//			dyextn_abstract_metadata
			sql = "INSERT INTO dyextn_abstract_metadata values("
			+ nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + entityName
			+ "',null)";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			sql = "INSERT INTO dyextn_abstract_entity values("+ nextIdOfAbstractMetadata + ")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			if(parentEntity.equals("NULL"))
			{
				sql = "INSERT INTO dyextn_entity values("
					+ nextIdOfAbstractMetadata + ",3,0,NULL,"+inheritanceStrategy+",NULL,NULL,1)";
					UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			}
			else
			{
				int parEId = UpdateMetadataUtil.getEntityIdByName(parentEntity, connection.createStatement());
				sql = "INSERT INTO dyextn_entity values("
				+ nextIdOfAbstractMetadata + ",3,0,"+parEId+","+inheritanceStrategy+",NULL,NULL,1)";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			}
		
			sql = "INSERT INTO dyextn_database_properties values("
			+ nextIdDatabaseproperties + ",'"+tableName+"')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			sql = "INSERT INTO dyextn_table_properties (IDENTIFIER,ABSTRACT_ENTITY_ID) values("
			+ nextIdDatabaseproperties + "," + nextIdOfAbstractMetadata+")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}
	}
}
