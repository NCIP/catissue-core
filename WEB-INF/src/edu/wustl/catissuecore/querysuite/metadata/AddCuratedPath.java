
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author vijay_pande
 * Class to add curated path between two entities
 *  if an intermediate entity exist common to source as well as target entity.
 */
public class AddCuratedPath
{

	/**
	 * Connection instance.
	 */
	private static Connection connection;
	/**
	 * Specify entity List.
	 */
	private List<String> entityList;

	/*public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException
	{
		Connection connection=DBUtil.getConnection();

	//		Class.forName("com.mysql.jdbc.Driver");
	//		String url = "jdbc:mysql://localhost:3307/upgrade";
	//		Connection connection = DriverManager.getConnection(url, "root", "pspl");

		AddCuratedPath addCurratedPath = new AddCuratedPath(connection);
		addCurratedPath.addCurratedPath();
	}*/

	/**
	 * This method adds Currated Path.
	 * @throws IOException IO Exception
	 * @throws SQLException SQL Exception
	 */
	public void addCurratedPath() throws IOException, SQLException
	{
		this.populateMapForPath();
		StringTokenizer st;
		String sourceEntity;
		String targetEntity;
		String intermediatePath;
		int sourceEntityId = 0;
		int targetEntityId = 0;
		int mainEntityId = 0;
		int nextIdPath = 0;
		String sql = "select max(PATH_ID) from path";
		final Statement stmt = connection.createStatement();
		final ResultSet rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			final int maxId = rs.getInt(1);
			nextIdPath = maxId + 1;
		}
		stmt.close();
		for (final String key : this.entityList)
		{
			intermediatePath = "";
			st = new StringTokenizer(key, ",");
			sourceEntity = st.nextToken();
			sourceEntityId = UpdateMetadataUtil.getEntityIdByName(sourceEntity, connection
					.createStatement());
			mainEntityId = sourceEntityId;
			while (st.hasMoreTokens())
			{
				targetEntity = st.nextToken();
				targetEntityId = UpdateMetadataUtil.getEntityIdByName(targetEntity, connection
						.createStatement());
				final String tempPath = this.getIntermediatePath(sourceEntityId, targetEntityId);
				if (intermediatePath.equals(""))
				{
					intermediatePath = tempPath;
				}
				else
				{
					intermediatePath = intermediatePath + "_" + tempPath;
				}
				sourceEntity = targetEntity;
				sourceEntityId = targetEntityId;
			}
			sql = "insert into path values(" + nextIdPath + "," + mainEntityId + ",'"
					+ intermediatePath + "'," + targetEntityId + ")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			nextIdPath++;
		}
	}

	/**
	 * This method gets Intermediate Path.
	 * @param sourceEntityId source Entity Id
	 * @param intermediateEntityId intermediate Entity Id
	 * @return Intermediate Path
	 * @throws SQLException SQL Exception
	 */
	private String getIntermediatePath(int sourceEntityId, int intermediateEntityId)
			throws SQLException
	{
		String inetrmediatePath = null;
		ResultSet rs;
		final Statement stmt = connection.createStatement();
		final String sql = "select INTERMEDIATE_PATH from path where FIRST_ENTITY_ID ='"
				+ sourceEntityId + "'  and LAST_ENTITY_ID = '" + intermediateEntityId + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			inetrmediatePath = rs.getString(1);
		}
		stmt.close();
		return inetrmediatePath;
	}

	/**
	 * This method populates Map For Path.
	 */
	private void populateMapForPath()
	{
		this.entityList = new ArrayList<String>();
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.Specimen");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.TissueSpecimen");

		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport");

		this.entityList.add("edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.Specimen");
		this.entityList.add("edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.TissueSpecimen");

		this.entityList.add("edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.CollectionProtocolRegistration,"
				+ "edu.wustl.catissuecore.domain.Participant");

		this.entityList.add("edu.wustl.catissuecore.domain.StorageContainer,"
				+ "edu.wustl.catissuecore.domain.SpecimenPosition,"
				+ "edu.wustl.catissuecore.domain.Specimen");
		this.entityList.add("edu.wustl.catissuecore.domain.StorageContainer,"
				+ "edu.wustl.catissuecore.domain.SpecimenPosition,"
				+ "edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.StorageContainer,"
				+ "edu.wustl.catissuecore.domain.SpecimenPosition,"
				+ "edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.StorageContainer,"
				+ "edu.wustl.catissuecore.domain.SpecimenPosition,"
				+ "edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.StorageContainer,"
				+ "edu.wustl.catissuecore.domain.SpecimenPosition,"
				+ "edu.wustl.catissuecore.domain.TissueSpecimen");

		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.Specimen");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.TissueSpecimen");

		this.entityList.add("edu.wustl.catissuecore.domain.Site,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.Specimen");
		this.entityList.add("edu.wustl.catissuecore.domain.Site,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.Site,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.Site,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.Site,"
				+ "edu.wustl.catissuecore.domain.SpecimenCollectionGroup,"
				+ "edu.wustl.catissuecore.domain.TissueSpecimen");

		this.entityList.add("edu.wustl.catissuecore.domain.OrderDetails,"
				+ "edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.Specimen");
		this.entityList.add("edu.wustl.catissuecore.domain.OrderDetails,"
				+ "edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.OrderDetails,"
				+ "edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.OrderDetails,"
				+ "edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.OrderDetails,"
				+ "edu.wustl.catissuecore.domain.Distribution,"
				+ "edu.wustl.catissuecore.domain.TissueSpecimen");

		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.CollectionProtocolRegistration,"
				+ "edu.wustl.catissuecore.domain.Participant");

		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.CollectionProtocolEvent,"
				+ "edu.wustl.catissuecore.domain.SpecimenRequirement");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.CollectionProtocolEvent,"
				+ "edu.wustl.catissuecore.domain.CellSpecimenRequirement");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.CollectionProtocolEvent,"
				+ "edu.wustl.catissuecore.domain.FluidSpecimenRequirement");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.CollectionProtocolEvent,"
				+ "edu.wustl.catissuecore.domain.MolecularSpecimenRequirement");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,"
				+ "edu.wustl.catissuecore.domain.CollectionProtocolEvent,"
				+ "edu.wustl.catissuecore.domain.TissueSpecimenRequirement");

		this.entityList.add("edu.wustl.catissuecore.domain.OrderItem,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.Specimen");
		this.entityList.add("edu.wustl.catissuecore.domain.OrderItem,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.OrderItem,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.OrderItem,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityList.add("edu.wustl.catissuecore.domain.OrderItem,"
				+ "edu.wustl.catissuecore.domain.DistributedItem,"
				+ "edu.wustl.catissuecore.domain.TissueSpecimen");
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 */
	public AddCuratedPath(Connection connection)
	{
		super();
		AddCuratedPath.connection = connection;
	}
}
