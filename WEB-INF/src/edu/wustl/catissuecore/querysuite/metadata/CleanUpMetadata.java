
package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author pooja_deshpande
 * Class to delete all the invalid curated paths between 2 entities.
 */
public class CleanUpMetadata
{

	/**
	 * Connection instance.
	 */
	private static Connection connection;

	/*public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException
	{
		Connection connection=DBUtil.getConnection();
		CleanUpMetadata cleanUpMetadata = new CleanUpMetadata(connection);
		cleanUpMetadata.cleanMetadata();
	}
	*/
	/**
	 * This method clean the Meta data.
	 * @return list
	 * @throws SQLException SQL Exception
	 */
	public List<String> cleanMetadata() throws SQLException
	{
		StringTokenizer st;
		String intermediatePath;
		String nextToken;
		final List<String> deletePaths = new ArrayList<String>();
		final List<String> deleteSQLList = new ArrayList<String>();

		final String sql = "Select INTERMEDIATE_PATH from PATH";

		final Statement stmt = connection.createStatement();
		final ResultSet rs = stmt.executeQuery(sql);

		while (rs.next())
		{
			intermediatePath = rs.getString(1);
			st = new StringTokenizer(intermediatePath, "_");

			while (st.hasMoreTokens())
			{
				nextToken = st.nextToken();
				final int associationId = Integer.valueOf(nextToken);
				final String sql1 = "Select ASSOCIATION_ID" +
						" from intra_model_association where ASSOCIATION_ID = "
						+ associationId;
				final Statement stmt1 = connection.createStatement();
				final ResultSet rs1 = stmt1.executeQuery(sql1);

				if (rs1.next())
				{
					stmt1.close();
					continue;
				}
				else
				{
					deletePaths.add(intermediatePath);
					stmt1.close();
					break;
				}
			}
		}
		for (final String deletePath : deletePaths)
		{
			deleteSQLList.add("delete from PATH where INTERMEDIATE_PATH = '" + deletePath + "'");
		}
		return deleteSQLList;
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 */
	public CleanUpMetadata(Connection connection)
	{
		super();
		CleanUpMetadata.connection = connection;
	}
}
