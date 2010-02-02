
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;

/**
 *
 * @author deepti_shelar
 *
 */
public class DeleteAssociation
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Statement instance.
	 */
	private Statement stmt = null;

	/**
	 * Specify entity Name List Delete.
	 */
	private static List<String> entityNameListDelete = new ArrayList<String>();
	/**
	 * Specify entity To Delete.
	 */
	private static List<Entity> entityToDelete = new ArrayList<Entity>();
	/**
	 * Specify two Entity Map.
	 */
	static Map<String, String> twoEntityMap = new HashMap<String, String>();

	/*public static void main(String[] args) throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		DeleteAssociation deleteAssociation = new DeleteAssociation(connection);
		List<String> deleteSQL = new ArrayList<String>();
		deleteAssociation.populateEntityToDeletetList();
		deleteAssociation.updateEntityToDeleteList();
		deleteAssociation.populateAssociationToDelete();
		Set<String> keySet = twoEntityMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext())
		{
			String srcName = iterator.next();
			deleteSQL.addAll(deleteAssociation.
			deleteAssociation(srcName,twoEntityMap.get(srcName)));
		}
		connection.close();
	}*/

	/**
	 * This method populates Association To Delete.
	 */
	private void populateAssociationToDelete()
	{

		twoEntityMap.put("edu.wustl.catissuecore.domain.SpecimenArray",
				"edu.wustl.catissuecore.domain.StorageContainer");

	}

	/**
	 * This method deletes Association.
	 * @param srcName srcName
	 * @param targetName target Name
	 * @return list
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public List<String> deleteAssociation(String srcName, String targetName) throws SQLException,
			IOException
	{
		final int sourceEntityId = UpdateMetadataUtil.getEntityIdByName(srcName, this.connection
				.createStatement());
		final int targetEntityId = UpdateMetadataUtil.getEntityIdByName(targetName, this.connection
				.createStatement());

		final List<String> deleteSQL = this.deleteAssociation(sourceEntityId, targetEntityId);

		return deleteSQL;
	}

	/**
	 * This method deletes Association.
	 * @param sourceEntityId source Entity Id
	 * @param targetEntityId target Entity Id
	 * @return delete SQL
	 * @throws SQLException SQL Exception
	 */
	public List<String> deleteAssociation(int sourceEntityId, int targetEntityId)
			throws SQLException
	{
		final List<String> deleteSQL = new ArrayList<String>();
		String sql;

		final List<Long> roleIdMap = this.getSourceSQL(deleteSQL, sourceEntityId, targetEntityId);
		roleIdMap.addAll(this.getSourceSQL(deleteSQL, targetEntityId, sourceEntityId));

		for (final Long srcRoleId : roleIdMap)
		{
			if (srcRoleId != null)
			{
				sql = "delete from dyextn_role where IDENTIFIER=" + srcRoleId;
				deleteSQL.add(sql);
			}
		}

		return deleteSQL;
	}

	/**
	 * This method gets Source SQL.
	 * @param deleteSQL delete SQL list
	 * @param sourceEntityId source Entity Id
	 * @param targetEntityId target Entity Id
	 * @return list
	 * @throws SQLException SQL Exception
	 */
	public List<Long> getSourceSQL(List<String> deleteSQL, int sourceEntityId, int targetEntityId)
			throws SQLException
	{
		String sql;
		Long srcRoleId = null;
		Long targetRoleId = null;
		Long deAssociationId = null;

		final List<Long> roleIdMap = new ArrayList<Long>();

		sql = "select INTERMEDIATE_PATH from  path where FIRST_ENTITY_ID =" + sourceEntityId
				+ " AND LAST_ENTITY_ID = " + targetEntityId;
		this.stmt = this.connection.createStatement();
		final ResultSet rs = this.stmt.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				final String intermediatePathId = rs.getString(1);

				sql = "delete from path where INTERMEDIATE_PATH ='" + intermediatePathId + "'";
				deleteSQL.add(sql);

				final StringTokenizer st = new StringTokenizer(intermediatePathId, "_");
				while (st.hasMoreTokens())
				{
					final String pathId = st.nextToken();
					sql = "delete from path where INTERMEDIATE_PATH ='" + pathId + "'";
					deleteSQL.add(sql);
					sql = "select DE_ASSOCIATION_ID"
							+ " from intra_model_association where ASSOCIATION_ID=" + pathId;
					final Statement stmt2 = this.connection.createStatement();
					final ResultSet rs2 = stmt2.executeQuery(sql);
					if (rs2.next())
					{
						deAssociationId = rs2.getLong(1);
						sql = "select DIRECTION,SOURCE_ROLE_ID,TARGET_ROLE_ID"
								+ " from dyextn_association where identifier = " + deAssociationId;
						final Statement stmt3 = this.connection.createStatement();
						final ResultSet rs3 = stmt3.executeQuery(sql);
						if (rs3.next())
						{
							if (rs3.getString(1).equals("BI_DIRECTIONAL"))
							{
								srcRoleId = rs3.getLong(2);
								targetRoleId = rs3.getLong(3);
								roleIdMap.add(srcRoleId);
								roleIdMap.add(targetRoleId);
							}
						}
						rs3.close();
						stmt3.close();
					}
					rs2.close();
					stmt2.close();

					sql = "select identifier" + " from dyextn_constraint_properties"
							+ " where ASSOCIATION_ID = " + deAssociationId;
					final Statement stmt1 = this.connection.createStatement();
					final ResultSet rs1 = stmt1.executeQuery(sql);
					if (rs1.next())
					{
						final Long constraintId = rs1.getLong(1);

						sql = "delete from dyextn_column_properties where cnstr_key_prop_id in "
								+ "(select identifier from dyextn_constraintkey_prop where "
								+ "src_constraint_key_id=" + constraintId
								+ " or tgt_constraint_key_id=" + constraintId + ")";
						deleteSQL.add(sql);

						sql = "delete from dyextn_constraintkey_prop where src_constraint_key_id="
								+ constraintId + " or tgt_constraint_key_id=" + constraintId;
						deleteSQL.add(sql);

						sql = "delete from dyextn_constraint_properties" + " where identifier = "
								+ constraintId;
						deleteSQL.add(sql);

						sql = "delete from dyextn_database_properties where identifier = "
								+ constraintId;
						deleteSQL.add(sql);

					}
					rs1.close();
					stmt1.close();

					sql = "delete from intra_model_association" + " where ASSOCIATION_ID ="
							+ pathId;
					deleteSQL.add(sql);

					sql = "delete from association where ASSOCIATION_ID =" + pathId;
					deleteSQL.add(sql);

					sql = "delete from dyextn_association where IDENTIFIER=" + deAssociationId;
					deleteSQL.add(sql);

					sql = "delete from dyextn_attribute where identifier =" + deAssociationId;
					deleteSQL.add(sql);

					sql = "delete from DYEXTN_BASE_ABSTRACT_ATTRIBUTE where identifier="
							+ deAssociationId;
					deleteSQL.add(sql);

					sql = "delete from dyextn_tagged_value where ABSTRACT_METADATA_ID="
							+ deAssociationId;
					deleteSQL.add(sql);

					sql = "delete from dyextn_semantic_property where ABSTRACT_METADATA_ID="
							+ deAssociationId;
					deleteSQL.add(sql);

					sql = "delete from dyextn_primitive_attribute where identifier ="
							+ deAssociationId;
					deleteSQL.add(sql);

					sql = "delete from dyextn_rule_parameter"
							+ " where  RULE_ID =(select IDENTIFIER"
							+ " from dyextn_rule where ATTRIBUTE_ID =" + deAssociationId + ")";
					deleteSQL.add(sql);

					sql = "delete from dyextn_rule where ATTRIBUTE_ID =" + deAssociationId;
					deleteSQL.add(sql);

					sql = "delete from dyextn_abstract_metadata where identifier="
							+ deAssociationId;
					deleteSQL.add(sql);
				}

			}
			if (srcRoleId != null)
			{
				sql = "delete from dyextn_role where IDENTIFIER=" + srcRoleId;
				deleteSQL.add(sql);
			}

			if (targetRoleId != null)
			{
				sql = "delete from dyextn_role where IDENTIFIER=" + targetRoleId;
				deleteSQL.add(sql);
			}
		}
		finally
		{
			rs.close();
			this.stmt.close();
		}
		return roleIdMap;
	}

	/**
	 * This method populates Entity To Deletet List.
	 */
	private void populateEntityToDeletetList()
	{
		entityNameListDelete = new ArrayList<String>();
		entityNameListDelete.add("edu.wustl.catissuecore.domain.Quantity");
		entityNameListDelete
				.add("edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup");
	}

	/**
	 * This method updates Entity To Delete List.
	 * @throws SQLException SQL Exception
	 */
	private void updateEntityToDeleteList() throws SQLException
	{
		String sql;
		this.stmt = this.connection.createStatement();
		ResultSet rs;
		for (final String entityName : entityNameListDelete)
		{
			final Entity entity = new Entity();
			sql = "select identifier,name from dyextn_abstract_metadata where NAME "
					+ UpdateMetadataUtil.getDBCompareModifier() + "'" + entityName + "'";
			rs = this.stmt.executeQuery(sql);
			if (rs.next())
			{
				entity.setId(rs.getLong(1));
				entity.setName(rs.getString(2));
			}
			final TableProperties tableProperties = new TableProperties();
			sql = "select identifier from dyextn_table_properties where ABSTRACT_ENTITY_ID="
					+ entity.getId();
			rs = this.stmt.executeQuery(sql);

			if (rs.next())
			{
				tableProperties.setId(rs.getLong(1));
			}
			rs.close();
			entity.setTableProperties(tableProperties);

			entityToDelete.add(entity);
		}
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public DeleteAssociation(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}

	/**
	 * This method gets SQL To Delete Association.
	 * @return list.
	 * @throws IOException IO Exception
	 * @throws SQLException SQL Exception
	 */
	public List<String> getSQLToDeleteAssociation() throws IOException, SQLException
	{
		final List<String> deleteSQL = new ArrayList<String>();

		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.StorageContainer"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.Quantity"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Container",
				"edu.wustl.catissuecore.domain.StorageContainer"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.StorageContainer",
				"edu.wustl.catissuecore.domain.Container"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.StorageContainer",
				"edu.wustl.catissuecore.domain.SpecimenArray"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArray",
				"edu.wustl.catissuecore.domain.StorageContainer"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Container",
				"edu.wustl.catissuecore.domain.SpecimenArray"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArray",
				"edu.wustl.catissuecore.domain.Container"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArray",
				"edu.wustl.catissuecore.domain.SpecimenArray"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Container",
				"edu.wustl.catissuecore.domain.Container"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.StorageContainer",
				"edu.wustl.catissuecore.domain.StorageContainer"));

		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.SpecimenArrayContent",
				"edu.wustl.catissuecore.domain.CellSpecimen"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.SpecimenArrayContent",
				"edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.SpecimenArrayContent",
				"edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.SpecimenArrayContent",
				"edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.DistributedItem",
				"edu.wustl.catissuecore.domain.CellSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.DistributedItem",
				"edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.DistributedItem",
				"edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.DistributedItem",
				"edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.CellSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.CellSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.SpecimenCharacteristics"));

		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.TissueSpecimen"));

		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.FixedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.FixedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.FixedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.FixedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.FixedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.ProcedureEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.ProcedureEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.ProcedureEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.ProcedureEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.ProcedureEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.SpunEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.SpunEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.SpunEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.SpunEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.SpunEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.TransferEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.TransferEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.TransferEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.TransferEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.TransferEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.ReceivedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.ReceivedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.ReceivedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.ReceivedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.ReceivedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.EmbeddedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.EmbeddedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.EmbeddedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.EmbeddedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.EmbeddedEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.ThawEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.ThawEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.ThawEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.ThawEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.ThawEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.ReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.ReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.ReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.ReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.ReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.CellSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.CellSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.CellSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.CellSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.CellSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.Specimen",
				"edu.wustl.catissuecore.domain.DisposalEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen",
				"edu.wustl.catissuecore.domain.DisposalEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen",
				"edu.wustl.catissuecore.domain.DisposalEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen",
				"edu.wustl.catissuecore.domain.DisposalEventParameters"));
		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen",
				"edu.wustl.catissuecore.domain.DisposalEventParameters"));

		deleteSQL.addAll(this.deleteAssociation("edu.wustl.catissuecore.domain.SpecimenOrderItem",
				"edu.wustl.catissuecore.domain.DistributedItem"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.SpecimenArrayOrderItem",
				"edu.wustl.catissuecore.domain.DistributedItem"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem",
				"edu.wustl.catissuecore.domain.DistributedItem"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.NewSpecimenOrderItem",
				"edu.wustl.catissuecore.domain.DistributedItem"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem",
				"edu.wustl.catissuecore.domain.DistributedItem"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.PathologicalCaseOrderItem",
				"edu.wustl.catissuecore.domain.DistributedItem"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem",
				"edu.wustl.catissuecore.domain.DistributedItem"));
		deleteSQL.addAll(this.deleteAssociation(
				"edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem",
				"edu.wustl.catissuecore.domain.DistributedItem"));

		return deleteSQL;
	}
}
