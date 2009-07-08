
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * AddEntity.
 *
 */
public class AddEntity
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;

	/**
	 * Constructor.
	 * @param connection Connection object
	 */
	AddEntity(Connection connection)
	{
		this.connection = connection;
	}

	/*public static void main(String args[])throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
	}*/

	/**
	 * This method adds entity.
	 * @param entityList entity List
	 * @param tableName table Name
	 * @param parentEntity parent Entity
	 * @param inheritanceStrategy inheritance Strategy
	 * @param isAbstract is Abstract
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addEntity(List<String> entityList, String tableName, String parentEntity,
			int inheritanceStrategy, int isAbstract) throws SQLException, IOException
	{
		final Statement stmt = this.connection.createStatement();
		for (final String entityName : entityList)
		{
			//insert statements
			String sql = "select max(identifier) from dyextn_abstract_metadata";
			ResultSet rs = stmt.executeQuery(sql);
			int nextIdOfAbstractMetadata = 0;
			if (rs.next())
			{
				final int maxId = rs.getInt(1);
				nextIdOfAbstractMetadata = maxId + 1;
			}
			rs.close();
			int nextIdDatabaseproperties = 0;
			sql = "select max(identifier) from dyextn_database_properties";
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				final int maxId = rs.getInt(1);
				nextIdDatabaseproperties = maxId + 1;
			}
			rs.close();

			sql = "INSERT INTO dyextn_abstract_metadata" +
				" (IDENTIFIER,CREATED_DATE,DESCRIPTION,LAST_UPDATED,NAME,PUBLIC_ID) values("
					+ nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + entityName + "',null)";
			if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
						"dyextn_abstract_metadata");
			}
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
			sql = "INSERT INTO dyextn_abstract_entity values(" + nextIdOfAbstractMetadata + ")";
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

			if (parentEntity.equals("NULL"))
			{
				sql = "INSERT INTO dyextn_entity values(" + nextIdOfAbstractMetadata + ",3,"
						+ isAbstract + ",NULL," + inheritanceStrategy + ",NULL,NULL,1)";
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
			}
			else
			{
				final int parEId = UpdateMetadataUtil.getEntityIdByName(parentEntity,
						this.connection.createStatement());
				sql = "INSERT INTO dyextn_entity values(" + nextIdOfAbstractMetadata + ",3,"
						+ isAbstract + ","
						+ parEId + "," + inheritanceStrategy + ",NULL,NULL,1)";
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
			}

			sql = "INSERT INTO dyextn_database_properties(IDENTIFIER,NAME) values("
					+ nextIdDatabaseproperties + ",'" + tableName + "')";
			if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
						"dyextn_database_properties");
			}
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

			if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
						"dyextn_database_properties");
			}

			sql = "INSERT INTO dyextn_table_properties (IDENTIFIER,ABSTRACT_ENTITY_ID) values("
					+ nextIdDatabaseproperties + "," + nextIdOfAbstractMetadata + ")";
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		}
	}
}
