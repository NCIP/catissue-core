
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
	private static final Logger LOGGER = Logger.getCommonLogger(AssociatesForms.class);
	/**
	 * specify superClass And SubClasses Map.
	 */
	public static Map<String, List<String>> superClassAndSubClassesMap = new HashMap<String, List<String>>();
	/**
	 * specify superClass And Associations Map.
	 */
	public static Map<String, List<String>> superClassAndAssociationsMap = new HashMap<String, List<String>>();
	/**
	 * specify event Parameters SubClasses Map.
	 */
	public static Map<String, List<String>> eventParametersSubClassesMap = new HashMap<String, List<String>>();
	/**
	 * specify superClass name and Description Map.
	 */
	public static Map<String, String> superClassDescMap = new HashMap<String, String>();
	/**
	 * specify identifier.
	 */
	private int identifier = 0;
	/**
	 * Cell Specimen.
	 */
	private static final String CELL_SPECIMEN = "edu.wustl.catissuecore.domain.CellSpecimen";
	/**
	 * Fluid Specimen.
	 */
	private static final String FLUID_SPECIMEN = "edu.wustl.catissuecore.domain.FluidSpecimen";
	/**
	 * Molecular Specimen.
	 */
	private static final String MOLECULAR_SPECIMEN = "edu.wustl.catissuecore.domain.MolecularSpecimen";
	/**
	 * Tissue Specimen.
	 */
	private static final String TISSUE_SPECIMEN = "edu.wustl.catissuecore.domain.TissueSpecimen";
	/**
	 * Specimen.
	 */
	private static final String SPECIMEN = "edu.wustl.catissuecore.domain.Specimen";
	/**
	 * Initialize Data.
	 */
	public static void initData()
	{
		List<String> subClassesList = new ArrayList<String>();
		subClassesList.add(CELL_SPECIMEN);
		subClassesList.add(FLUID_SPECIMEN);
		subClassesList.add(MOLECULAR_SPECIMEN);
		subClassesList.add(TISSUE_SPECIMEN);
		superClassAndSubClassesMap.put(SPECIMEN, subClassesList);

		List<String> associationsList = new ArrayList<String>();
		associationsList.add("edu.wustl.catissuecore.domain.SpecimenRequirement");
		associationsList.add("edu.wustl.catissuecore.domain.SpecimenPosition");
		associationsList.add("edu.wustl.catissuecore.domain.SpecimenArrayContent");
		associationsList.add("edu.wustl.catissuecore.domain.DistributedItem");
		superClassAndAssociationsMap
				.put(SPECIMEN, associationsList);

		subClassesList = new ArrayList<String>();
		subClassesList.add(SPECIMEN);
		subClassesList.add(CELL_SPECIMEN);
		subClassesList.add(FLUID_SPECIMEN);
		subClassesList.add(MOLECULAR_SPECIMEN);
		subClassesList.add(TISSUE_SPECIMEN);
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
		associationsList.add(SPECIMEN);
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
			associationsList.add(SPECIMEN);
			associationsList.add(CELL_SPECIMEN);
			associationsList.add(FLUID_SPECIMEN);
			associationsList.add(MOLECULAR_SPECIMEN);
			associationsList.add(TISSUE_SPECIMEN);
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
			sql = getSqlForDesc(key);
			stmt = connection.createStatement();
			resultSet = stmt.executeQuery(sql);
			if (resultSet.next())
			{
				entityId = String.valueOf(resultSet.getLong(1));

				final List<String> associationsList = superClassAndAssociationsMap.get(key);
				for (final String associatedEntityName : associationsList)
				{
					sql = getSqlForDesc(associatedEntityName);
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
								sql = getSqlForDesc(subClassEntity);
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
				LOGGER.info("Entity with name : " + key + " not found");
			}
		}
		stmt.close();
		insertPathSQL.addAll(this.getInsertPathStatementsSpecimen(connection));//bug 11336
		return insertPathSQL;
	}

	/**
	 * Get appropriate SQL to get the identifier of the DE entity.
	 * @param key key
	 * @return sql
	 */
	private String getSqlForDesc(String key)
	{
		String sql;
		final String description = superClassDescMap.get(key);
		if (description == null)
		{
			sql = "Select IDENTIFIER from dyextn_abstract_metadata where NAME "
				+ UpdateMetadataUtil.getDBCompareModifier() + "'" + key + "'";
		}
		else
		{
			sql = "Select IDENTIFIER from dyextn_abstract_metadata where NAME "
				+ UpdateMetadataUtil.getDBCompareModifier() + "'" + key +
				"' and CREATED_DATE is not null";
		}
		return sql;
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
		associationsListSpecimen.add(FLUID_SPECIMEN);
		associationsListSpecimen.add(MOLECULAR_SPECIMEN);
		associationsListSpecimen.add(TISSUE_SPECIMEN);
		associationsListSpecimen.add(CELL_SPECIMEN);
		associationsMap.put(SPECIMEN, associationsListSpecimen);

		final List<String> associationsListCell = new ArrayList<String>();
		associationsListCell.add(FLUID_SPECIMEN);
		associationsListCell.add(MOLECULAR_SPECIMEN);
		associationsListCell.add(TISSUE_SPECIMEN);
		associationsMap.put(CELL_SPECIMEN, associationsListCell);

		final List<String> associationsListFluid = new ArrayList<String>();
		associationsListFluid.add(CELL_SPECIMEN);
		associationsListFluid.add(MOLECULAR_SPECIMEN);
		associationsListFluid.add(TISSUE_SPECIMEN);
		associationsMap.put(FLUID_SPECIMEN, associationsListFluid);

		final List<String> associationsListMolecular = new ArrayList<String>();
		associationsListMolecular.add(CELL_SPECIMEN);
		associationsListMolecular.add(FLUID_SPECIMEN);
		associationsListMolecular.add(TISSUE_SPECIMEN);
		associationsMap.put(MOLECULAR_SPECIMEN,
				associationsListMolecular);

		final List<String> associationsListTissue = new ArrayList<String>();
		associationsListTissue.add(CELL_SPECIMEN);
		associationsListTissue.add(FLUID_SPECIMEN);
		associationsListTissue.add(MOLECULAR_SPECIMEN);
		associationsMap.put(TISSUE_SPECIMEN, associationsListTissue);

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
