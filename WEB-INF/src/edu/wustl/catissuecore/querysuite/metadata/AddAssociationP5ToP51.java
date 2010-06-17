
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * AddAssociation class.
 */
public class AddAssociationP5ToP51
{

	/**
	 * Specify Connection instance.
	 */
	private static Connection connection;

	/**
	 * adds Association.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addAssociation() throws SQLException, IOException
	{
		final AddAssociations addAssociations = new AddAssociations(connection);
		final String entityName = "edu.wustl.catissuecore.domain.SpecimenArray";
		final String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenArrayContent";
		UpdateMetadataUtil.isExecuteStatement = true;
		addAssociations.addAssociation(entityName, targetEntityName,
				"specimenArray_specimenArrayContent", "ASSOCIATION", "specimenArrayContentCollection",
				false, "specimenArray", null, "SPECIMEN_ARRAY_ID", 2, 1, "BI_DIRECTIONAL", false);
	}

	/**
	 * Constructor.
	 * @param connection connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddAssociationP5ToP51(final Connection conn) throws SQLException
	{
		super();
		connection = conn;
	}

	public static void main(String [] args) throws SQLException, IOException, ClassNotFoundException
	{
		try
		{
			connection = DBConnectionUtil.getDBConnection(args);
			connection.setAutoCommit(true);
			AddAssociationP5ToP51 addAssociationNew = new AddAssociationP5ToP51(connection);
			addAssociationNew.addAssociation();
		}
		finally
		{
			if (connection != null)
			{
				connection.close();
			}
		}
	}
}
