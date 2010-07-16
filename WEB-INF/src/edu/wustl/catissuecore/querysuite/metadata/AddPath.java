
package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.util.AssociatesForms;
import edu.wustl.common.util.logger.Logger;

/**
 * AddPath.
 *
 */
public class AddPath
{
	private static final Logger logger = Logger.getCommonLogger(AssociatesForms.class);
	/**
	 * specify superClass And SubClasses Map.
	 */
	static Map<String, List<String>> superClassAndSubClassesMap = new HashMap<String, List<String>>();
	/**
	 * specify superClass And Associations Map.
	 */
	static Map<String, List<String>> superClassAndAssociationsMap = new HashMap<String, List<String>>();
	/**
	 * specify event Parameters SubClasses Map.
	 */
	static Map<String, List<String>> eventParametersSubClassesMap = new HashMap<String, List<String>>();
	/**
	 * specify identifier.
	 */
	int identifier = 0;

	/**
	 * Initialize Data.
	 */
	public static void initData()
	{
		List<String> subClassesList = new ArrayList<String>();
		subClassesList.add("edu.wustl.catissuecore.domain.CellSpecimen");
		subClassesList.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		subClassesList.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		subClassesList.add("edu.wustl.catissuecore.domain.TissueSpecimen");
		superClassAndSubClassesMap.put("edu.wustl.catissuecore.domain.Specimen", subClassesList);

		List<String> associationsList = new ArrayList<String>();
		associationsList.add("edu.wustl.catissuecore.domain.SpecimenRequirement");
		associationsList.add("edu.wustl.catissuecore.domain.SpecimenPosition");
		associationsList.add("edu.wustl.catissuecore.domain.SpecimenArrayContent");
		associationsList.add("edu.wustl.catissuecore.domain.DistributedItem");
		superClassAndAssociationsMap
				.put("edu.wustl.catissuecore.domain.Specimen", associationsList);

		subClassesList = new ArrayList<String>();
		subClassesList.add("edu.wustl.catissuecore.domain.Specimen");
		subClassesList.add("edu.wustl.catissuecore.domain.CellSpecimen");
		subClassesList.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		subClassesList.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		subClassesList.add("edu.wustl.catissuecore.domain.TissueSpecimen");
		superClassAndSubClassesMap.put("edu.wustl.catissuecore.domain.AbstractSpecimen",
				subClassesList);

		associationsList = new ArrayList<String>();
		associationsList.add("edu.wustl.catissuecore.domain.SpecimenCharacteristics");
		associationsList.add("edu.wustl.catissuecore.domain.SpecimenEventParameters");
		associationsList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
		superClassAndAssociationsMap.put("edu.wustl.catissuecore.domain.AbstractSpecimen",
				associationsList);

		subClassesList = new ArrayList<String>();
		subClassesList.add("edu.wustl.catissuecore.domain.CellSpecimenRequirement");
		subClassesList.add("edu.wustl.catissuecore.domain.FluidSpecimenRequirement");
		subClassesList.add("edu.wustl.catissuecore.domain.MolecularSpecimenRequirement");
		subClassesList.add("edu.wustl.catissuecore.domain.TissueSpecimenRequirement");
		superClassAndSubClassesMap.put("edu.wustl.catissuecore.domain.SpecimenRequirement",
				subClassesList);

		associationsList = new ArrayList<String>();
		associationsList.add("edu.wustl.catissuecore.domain.CollectionProtocolEvent");
		associationsList.add("edu.wustl.catissuecore.domain.Specimen");
		superClassAndAssociationsMap.put("edu.wustl.catissuecore.domain.SpecimenRequirement",
				associationsList);

		subClassesList = new ArrayList<String>();
		subClassesList.add("edu.wustl.catissuecore.domain.CollectionEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.FrozenEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.FixedEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter");
		subClassesList.add("edu.wustl.catissuecore.domain.ProcedureEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.SpunEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.TransferEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.ReceivedEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.EmbeddedEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.ThawEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.ReviewEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.CellSpecimenReviewParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters");
		subClassesList.add("edu.wustl.catissuecore.domain.DisposalEventParameters");
		superClassAndSubClassesMap.put("edu.wustl.catissuecore.domain.SpecimenEventParameters",
				subClassesList);
		eventParametersSubClassesMap.put("edu.wustl.catissuecore.domain.SpecimenEventParameters",
				subClassesList);

		associationsList = new ArrayList<String>();
		associationsList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
		superClassAndAssociationsMap.put("edu.wustl.catissuecore.domain.SpecimenEventParameters",
				associationsList);

		subClassesList = new ArrayList<String>();
		subClassesList.add("edu.wustl.catissuecore.domain.SpecimenOrderItem");
		subClassesList.add("edu.wustl.catissuecore.domain.SpecimenArrayOrderItem");
		subClassesList.add("edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem");
		subClassesList.add("edu.wustl.catissuecore.domain.NewSpecimenOrderItem");
		subClassesList.add("edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem");
		subClassesList.add("edu.wustl.catissuecore.domain.PathologicalCaseOrderItem");
		subClassesList.add("edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem");
		subClassesList.add("edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem");
		superClassAndSubClassesMap.put("edu.wustl.catissuecore.domain.OrderItem", subClassesList);

		associationsList = new ArrayList<String>();
		associationsList.add("edu.wustl.catissuecore.domain.DistributedItem");
		superClassAndAssociationsMap.put("edu.wustl.catissuecore.domain.OrderItem",
				associationsList);

		subClassesList = new ArrayList<String>();
		subClassesList.add("edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest");
		subClassesList.add("edu.wustl.catissuecore.domain.shippingtracking.Shipment");
		superClassAndSubClassesMap.put(
				"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", subClassesList);

		associationsList = new ArrayList<String>();
		associationsList.add("edu.wustl.catissuecore.domain.User");
		associationsList.add("edu.wustl.catissuecore.domain.Site");
		associationsList.add("edu.wustl.catissuecore.domain.StorageContainer");
		superClassAndAssociationsMap.put(
				"edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", associationsList);

	}

	/**
	 * This method gets Insert Path Statements.
	 * @param stmt Statement
	 * @param connection Connection
	 * @param flag flag
	 * @return Insert Path Statements.
	 * @throws SQLException SQL Exception
	 */
	public List<String> getInsertPathStatements(Statement stmt, Connection connection, boolean flag)
			throws SQLException
	{
		if (!flag)
		{
			superClassAndSubClassesMap = new HashMap<String, List<String>>(
					eventParametersSubClassesMap);
			superClassAndAssociationsMap = new HashMap<String, List<String>>();

			final List<String> associationsList = new ArrayList<String>();
			associationsList.add("edu.wustl.catissuecore.domain.Specimen");
			associationsList.add("edu.wustl.catissuecore.domain.CellSpecimen");
			associationsList.add("edu.wustl.catissuecore.domain.FluidSpecimen");
			associationsList.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
			associationsList.add("edu.wustl.catissuecore.domain.TissueSpecimen");
			superClassAndAssociationsMap.put(
					"edu.wustl.catissuecore.domain.SpecimenEventParameters", associationsList);
		}
		final List<String> insertPathSQL = new ArrayList<String>();
		ResultSet resultSet;
		stmt = connection.createStatement();

		resultSet = stmt.executeQuery("select max(PATH_ID) from path");
		if (resultSet.next())
		{
			this.identifier = resultSet.getInt(1) + 1;
		}
		String sql;
		String entityId = null;
		String associatedEntityId = null;

		final Set<String> keySet = superClassAndAssociationsMap.keySet();
		final Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			final String key = iterator.next();
			sql = "Select IDENTIFIER from dyextn_abstract_metadata where NAME "
					+ UpdateMetadataUtil.getDBCompareModifier() + "'" + key + "'";
			stmt = connection.createStatement();
			resultSet = stmt.executeQuery(sql);
			if (resultSet.next())
			{
				entityId = String.valueOf(resultSet.getLong(1));

				final List<String> associationsList = superClassAndAssociationsMap.get(key);
				for (final String associatedEntityName : associationsList)
				{
					sql = "Select IDENTIFIER from dyextn_abstract_metadata where NAME "
							+ UpdateMetadataUtil.getDBCompareModifier() + "'"
							+ associatedEntityName + "'";
					final Statement stmt4 = connection.createStatement();
					resultSet = stmt4.executeQuery(sql);
					if (resultSet.next())
					{
						associatedEntityId = String.valueOf(resultSet.getLong(1));

						sql = "Select INTERMEDIATE_PATH from PATH where FIRST_ENTITY_ID = "
								+ entityId + " and LAST_ENTITY_ID = " + associatedEntityId;
						final Statement stmt5 = connection.createStatement();
						resultSet = stmt5.executeQuery(sql);

						while (resultSet.next())
						{
							final String intermediatePathId = resultSet.getString(1);
							final List<String> subClassList = superClassAndSubClassesMap.get(key);
							for (final String subClassEntity : subClassList)
							{
								String subClassEntityId;
								final Statement stmt1 = connection.createStatement();
								sql = "Select IDENTIFIER from"
										+ " dyextn_abstract_metadata where NAME "
										+ UpdateMetadataUtil.getDBCompareModifier() + "'"
										+ subClassEntity + "'";
								final ResultSet rs1 = stmt.executeQuery(sql);
								if (rs1.next())
								{
									subClassEntityId = String.valueOf(rs1.getLong(1));
									if (key.equals(associatedEntityName))
									{
										insertPathSQL
												.add("insert into path values(" + this.identifier++
														+ "," + subClassEntityId + ","
														+ intermediatePathId + ","
														+ subClassEntityId + ")");
									}
									else
									{
										insertPathSQL.add("insert into path values("
												+ this.identifier++ + "," + subClassEntityId + ","
												+ intermediatePathId + "," + associatedEntityId
												+ ")");
									}
								}
								stmt1.close();
							}
						}
						stmt5.close();
						if (!(key.equals(associatedEntityName)))
						{
							sql = "Select INTERMEDIATE_PATH"
									+ " from PATH where FIRST_ENTITY_ID = " + associatedEntityId
									+ " and LAST_ENTITY_ID = " + entityId;
							final Statement stmt2 = connection.createStatement();
							resultSet = stmt2.executeQuery(sql);
							while (resultSet.next())
							{
								final String intermediatePathId = resultSet.getString(1);
								final List<String> subClassList = superClassAndSubClassesMap
										.get(key);
								for (final String subClassEntity : subClassList)
								{
									String subClassEntityId;
									final Statement stmt3 = connection.createStatement();
									sql = "Select IDENTIFIER" + " from dyextn_abstract_metadata"
											+ " where NAME "
											+ UpdateMetadataUtil.getDBCompareModifier() + "'"
											+ subClassEntity + "'";
									final ResultSet rs1 = stmt3.executeQuery(sql);
									if (rs1.next())
									{
										subClassEntityId = String.valueOf(rs1.getLong(1));
										insertPathSQL.add("insert into path values("
												+ this.identifier++ + "," + associatedEntityId
												+ "," + intermediatePathId + "," + subClassEntityId
												+ ")");
									}
									stmt3.close();
								}
							}
							stmt2.close();
						}
					}
					stmt4.close();
				}
			}
			else
			{
				logger.info("Entity with name : " + key + " not found");
			}
		}
		stmt.close();
		insertPathSQL.addAll(this.getInsertPathStatementsSpecimen(connection));//bug 11336
		return insertPathSQL;
	}

	//bug 11336
	/**
	 * This method will return associationsMap
	 *  which contains associations between specimen and its
	 * subclasses.
	 * @return associationsMap
	 */
	private Map<String, List<String>> getAssociationsMapForSpecimens()
	{
		final Map<String, List<String>> associationsMap = new HashMap<String, List<String>>();

		final List<String> associationsListSpecimen = new ArrayList<String>();
		associationsListSpecimen.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		associationsListSpecimen.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		associationsListSpecimen.add("edu.wustl.catissuecore.domain.TissueSpecimen");
		associationsListSpecimen.add("edu.wustl.catissuecore.domain.CellSpecimen");
		associationsMap.put("edu.wustl.catissuecore.domain.Specimen", associationsListSpecimen);

		final List<String> associationsListCell = new ArrayList<String>();
		associationsListCell.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		associationsListCell.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		associationsListCell.add("edu.wustl.catissuecore.domain.TissueSpecimen");
		associationsMap.put("edu.wustl.catissuecore.domain.CellSpecimen", associationsListCell);

		final List<String> associationsListFluid = new ArrayList<String>();
		associationsListFluid.add("edu.wustl.catissuecore.domain.CellSpecimen");
		associationsListFluid.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		associationsListFluid.add("edu.wustl.catissuecore.domain.TissueSpecimen");
		associationsMap.put("edu.wustl.catissuecore.domain.FluidSpecimen", associationsListFluid);

		final List<String> associationsListMolecular = new ArrayList<String>();
		associationsListMolecular.add("edu.wustl.catissuecore.domain.CellSpecimen");
		associationsListMolecular.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		associationsListMolecular.add("edu.wustl.catissuecore.domain.TissueSpecimen");
		associationsMap.put("edu.wustl.catissuecore.domain.MolecularSpecimen",
				associationsListMolecular);

		final List<String> associationsListTissue = new ArrayList<String>();
		associationsListTissue.add("edu.wustl.catissuecore.domain.CellSpecimen");
		associationsListTissue.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		associationsListTissue.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		associationsMap.put("edu.wustl.catissuecore.domain.TissueSpecimen", associationsListTissue);

		return associationsMap;
	}

	/**
	 * Added paths in specimen and its subclasses(eg Specimen to Tissue)
	 * and between subclasses also(eg Tissue to Cell).
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 * @return InsertPathStatementsSpecimen
	 */
	public List<String> getInsertPathStatementsSpecimen(Connection connection) throws SQLException
	{
		final List<String> insertPathSQL = new ArrayList<String>();
		final List<String> intermediatePathIds = new ArrayList<String>();
		String entityId = null;
		Statement stmt = null;
		String sql = "Select IDENTIFIER from dyextn_abstract_metadata where NAME "
				+ UpdateMetadataUtil.getDBCompareModifier()
				+ "'edu.wustl.catissuecore.domain.Specimen'";
		try
		{
			stmt = connection.createStatement();
			final ResultSet specimenIdRS = stmt.executeQuery(sql);
			if (specimenIdRS.next())
			{
				entityId = String.valueOf(specimenIdRS.getLong(1));
			}
			sql = "Select INTERMEDIATE_PATH from PATH where FIRST_ENTITY_ID = " + entityId
					+ " and LAST_ENTITY_ID = " + entityId;
			final ResultSet intermediatePathRS = stmt.executeQuery(sql);
			while (intermediatePathRS.next())
			{
				final String intermediatePathId = intermediatePathRS.getString(1);
				intermediatePathIds.add(intermediatePathId);
			}
			final Map<String, List<String>> associationsMap = this.getAssociationsMapForSpecimens();
			for (final String intermediatePathId : intermediatePathIds)
			{
				final Set<String> keySet = associationsMap.keySet();
				final Iterator<String> iterator = keySet.iterator();
				while (iterator.hasNext())
				{
					final String key = iterator.next();
					String keyId = null;
					sql = "Select IDENTIFIER from dyextn_abstract_metadata where NAME "
							+ UpdateMetadataUtil.getDBCompareModifier() + "'" + key + "'";
					final ResultSet resultSet = stmt.executeQuery(sql);
					if (resultSet.next())
					{
						keyId = String.valueOf(resultSet.getLong(1));
					}

					final List<String> classList = associationsMap.get(key);
					for (final String entity : classList)
					{
						String associateEntityId = null;
						sql = "Select IDENTIFIER from dyextn_abstract_metadata where NAME "
								+ UpdateMetadataUtil.getDBCompareModifier() + "'" + entity + "'";
						final ResultSet entityIdRS = stmt.executeQuery(sql);
						if (entityIdRS.next())
						{
							associateEntityId = String.valueOf(entityIdRS.getLong(1));
							final String sqlStmt = "insert into path values(" + this.identifier++
									+ "," + keyId + "," + intermediatePathId + ","
									+ associateEntityId + ")";
							insertPathSQL.add(sqlStmt);
						}

					}

				}
			}
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
		return insertPathSQL;
	}

}
