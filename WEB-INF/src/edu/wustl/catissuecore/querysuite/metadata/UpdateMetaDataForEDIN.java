
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * class to Update Meta Data For EDIN.
 *
 */
public class UpdateMetaDataForEDIN
{

	/**
	 * Connection instance.
	 */
	private static Connection connection = null;
	/**
	 * Statement instance.
	 */
	private static Statement stmt = null;

	/**
	 * Database specific compare operator.
	 */
	static String DB_SPECIFIC_COMPARE_OPERATOR;

	/**
	 * main method.
	 * @param args arguments
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 * @throws ClassNotFoundException Class Not Found Exception
	 */
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException
	{
		try
		{
			connection = DBConnectionUtil.getDBConnection(args);
			connection.setAutoCommit(true);
			stmt = connection.createStatement();
			UpdateMetadataUtil.isExecuteStatement = true;

			DB_SPECIFIC_COMPARE_OPERATOR = UpdateMetadataUtil.getDBCompareModifier();

			deleteMeatadata();

			updateMetadata();

		}
		finally
		{
			if (UpdateMetadataUtil.metadataSQLFile != null)
			{
				UpdateMetadataUtil.metadataSQLFile.close();
			}
			if (UpdateMetadataUtil.failureWriter != null)
			{
				UpdateMetadataUtil.failureWriter.close();
			}
			if (connection != null)
			{
				connection.close();
			}
		}
	}

	/**
	 * This method updates Meta data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	private static void updateMetadata() throws SQLException, IOException
	{
		/**  update statements  start **/
		final List<String> updateSQL = getUpdateSQL();
		UpdateMetadataUtil.executeSQLs(updateSQL, connection.createStatement(), false);

		/**  update statements  end **/
	}

	/**
	 * This method deletes Meta data.
	 * @throws IOException IO Exception
	 * @throws SQLException SQL Exception
	 */
	private static void deleteMeatadata() throws IOException, SQLException
	{

		/**  delete attribute statements  start **/
		final DeleteAttributeForEDIN deleteAttribute = new DeleteAttributeForEDIN(connection);
		final List<String> deleteSQL = deleteAttribute.deleteAttribute();
		UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);
		/**  delete attribute statements  end **/

	}

	/**
	 * This method gets Update SQL list.
	 * @return dbUpdateSQL list
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	private static List<String> getUpdateSQL() throws SQLException, IOException
	{
		final List<String> dbUpdateSQL = new ArrayList<String>();
		ResultSet rs;

		stmt = connection.createStatement();

		//		 Added by Geeta for edinburgh : Renaming zipcode to postCode
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata"
				+ " where IDENTIFIER in (Select IDENTIFIER"
				+ " from dyextn_attribute where ENTIY_ID"
				+ " in (Select IDENTIFIER from dyextn_abstract_metadata where name "
				+ DB_SPECIFIC_COMPARE_OPERATOR
				+ "'edu.wustl.catissuecore.domain.Address')) and NAME "
				+ DB_SPECIFIC_COMPARE_OPERATOR + "'zipCode'");
		if (rs.next())
		{
			final Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME ="
					+ " 'postCode' where IDENTIFIER =" + identifier);
		}
		stmt.close();

		//Added by Geeta for edinburgh : Renaming irb id to Ethics number
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata"
				+ " where IDENTIFIER in (Select IDENTIFIER"
				+ " from dyextn_attribute where ENTIY_ID in"
				+ " (Select IDENTIFIER from dyextn_abstract_metadata where name "
				+ DB_SPECIFIC_COMPARE_OPERATOR
				+ "'edu.wustl.catissuecore.domain.SpecimenProtocol')) and NAME "
				+ DB_SPECIFIC_COMPARE_OPERATOR + "'irbIdentifier'");
		if (rs.next())
		{
			final Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME ="
					+ " 'ethicsNumber' where IDENTIFIER =" + identifier);
		}
		stmt.close();

		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata"
				+ " where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute"
				+ " where ENTIY_ID in (Select IDENTIFIER "
				+ "from dyextn_abstract_metadata where name " + DB_SPECIFIC_COMPARE_OPERATOR
				+ "'edu.wustl.catissuecore.domain.CollectionProtocol')) and NAME "
				+ DB_SPECIFIC_COMPARE_OPERATOR + "'irbIdentifier'");
		if (rs.next())
		{
			final Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME ="
					+ " 'ethicsNumber' where IDENTIFIER =" + identifier);
		}
		stmt.close();
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata"
				+ " where IDENTIFIER in (Select IDENTIFIER"
				+ " from dyextn_attribute where ENTIY_ID"
				+ " in (Select IDENTIFIER from dyextn_abstract_metadata where name "
				+ DB_SPECIFIC_COMPARE_OPERATOR
				+ "'edu.wustl.catissuecore.domain.DistributionProtocol'))" + " and NAME "
				+ DB_SPECIFIC_COMPARE_OPERATOR + "'irbIdentifier'");
		if (rs.next())
		{
			final Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME ="
					+ " 'ethicsNumber' where IDENTIFIER =" + identifier);
		}
		stmt.close();

		// End By Geeta

		return dbUpdateSQL;
	}

}
