
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author pooja_deshpande
 * Class to add metadata for subclasses of all newly added entities
 */

public class AddSubClassesMetaData extends BaseMetadata
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
	 * specify entity Map.
	 */
	private static HashMap<String, List<String>> entityMap = new HashMap<String, List<String>>();
	/**
	 * specify entity Tables Map.
	 */
	private static HashMap<String, List<String>> entityTablesMap = new HashMap<String, List<String>>();

	/**
	 * This method adds SubClasses Meta data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addSubClassesMetadata() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();
		this.populateEntityTablesMap();

		final Set<String> keySet = entityMap.keySet();
		final Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			final String key = iterator.next();
			final List<String> entityList = entityMap.get(key);
			final List<String> tableList = entityTablesMap.get(key);
			for (final String entityName : entityList)
			{
				this.stmt = this.connection.createStatement();
				//insert statements
				String sql = "select max(identifier) from dyextn_abstract_metadata";
				ResultSet rs = this.stmt.executeQuery(sql);
				int nextIdOfAbstractMetadata = 0;
				if (rs.next())
				{
					final int maxId = rs.getInt(1);
					nextIdOfAbstractMetadata = maxId + 1;
				}
				int nextIdDatabaseproperties = 0;
				sql = "select max(identifier) from dyextn_database_properties";
				rs = this.stmt.executeQuery(sql);
				if (rs.next())
				{
					final int maxId = rs.getInt(1);
					nextIdDatabaseproperties = maxId + 1;
				}

				sql = "INSERT INTO dyextn_abstract_metadata"
						+ "(IDENTIFIER,CREATED_DATE,DESCRIPTION,LAST_UPDATED,NAME,PUBLIC_ID) values("
						+ nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + entityName + "',null)";
				if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
				{
					sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
							"dyextn_abstract_metadata");
				}
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
				sql = "INSERT INTO dyextn_abstract_entity values(" + nextIdOfAbstractMetadata + ")";
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

				final int parEId = UpdateMetadataUtil.getEntityIdByName(key, this.connection
						.createStatement());
				sql = "INSERT INTO dyextn_entity values(" + nextIdOfAbstractMetadata + ",3,0,"
						+ parEId + ",3,NULL,NULL,1)";
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

				sql = "INSERT INTO dyextn_database_properties(IDENTIFIER,NAME) values("
						+ nextIdDatabaseproperties + ",'"
						+ tableList.get(entityList.indexOf(entityName)) + "')";
				if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
				{
					sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
							"dyextn_database_properties");
				}
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

				sql = "INSERT INTO dyextn_table_properties (IDENTIFIER,ABSTRACT_ENTITY_ID) values("
						+ nextIdDatabaseproperties + "," + nextIdOfAbstractMetadata + ")";
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
			}
		}
		final AddAttribute addAttribute = new AddAttribute(this.connection,
				this.entityNameAttributeNameMap, this.attributeColumnNameMap,
				this.attributeDatatypeMap, this.attributePrimarkeyMap, this.entityList);
		addAttribute.addAttribute();
	}

	/**
	 * This method populates Entity Attribute Map.
	 */
	private void populateEntityAttributeMap()
	{
		List<String> attributes = new ArrayList<String>();
		attributes.add("id");
		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.CellSpecimenRequirement", attributes);

		attributes = new ArrayList<String>();
		attributes.add("id");
		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.FluidSpecimenRequirement", attributes);

		attributes = new ArrayList<String>();
		attributes.add("id");
		attributes.add("concentrationInMicrogramPerMicroliter");
		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.MolecularSpecimenRequirement", attributes);

		attributes = new ArrayList<String>();
		attributes.add("id");
		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.TissueSpecimenRequirement", attributes);

		attributes = new ArrayList<String>();
		attributes.add("id");
		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.ContainerPosition",
				attributes);

		attributes = new ArrayList<String>();
		attributes.add("id");
		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.SpecimenPosition",
				attributes);

		attributes = new ArrayList<String>();
		attributes.add("id");
		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest", attributes);

		attributes = new ArrayList<String>();
		attributes.add("id");
		attributes.add("barcode");
		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.shippingtracking.Shipment", attributes);
	}

	/**
	 * This method populates Attribute Column Name Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
		this.attributeColumnNameMap.put("concentrationInMicrogramPerMicroliter", "CONCENTRATION");

		this.attributeColumnNameMap.put("barcode", "BARCODE");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
		this.attributeDatatypeMap.put("concentrationInMicrogramPerMicroliter", "double");

		this.attributeDatatypeMap.put("barcode", "string");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
		this.attributePrimarkeyMap.put("concentrationInMicrogramPerMicroliter", "0");

		this.attributePrimarkeyMap.put("barcode", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.CellSpecimenRequirement");
		this.entityList.add("edu.wustl.catissuecore.domain.FluidSpecimenRequirement");
		this.entityList.add("edu.wustl.catissuecore.domain.MolecularSpecimenRequirement");
		this.entityList.add("edu.wustl.catissuecore.domain.TissueSpecimenRequirement");
		entityMap.put("edu.wustl.catissuecore.domain.SpecimenRequirement", this.entityList);

		this.entityList = new ArrayList<String>();
		this.entityList.add("edu.wustl.catissuecore.domain.ContainerPosition");
		this.entityList.add("edu.wustl.catissuecore.domain.SpecimenPosition");
		entityMap.put("edu.wustl.catissuecore.domain.AbstractPosition", this.entityList);

		this.entityList = new ArrayList<String>();
		this.entityList.add("edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest");
		this.entityList.add("edu.wustl.catissuecore.domain.shippingtracking.Shipment");
		entityMap.put("edu.wustl.catissuecore.domain.shippingtracking.BaseShipment",
				this.entityList);
	}

	/**
	 * This method populates Entity Tables Map.
	 */
	private void populateEntityTablesMap()
	{
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("CATISSUE_CELL_REQ_SPECIMEN");
		tableNames.add("CATISSUE_FLUID_REQ_SPECIMEN");
		tableNames.add("CATISSUE_MOL_REQ_SPECIMEN");
		tableNames.add("CATISSUE_TISSUE_REQ_SPECIMEN");
		entityTablesMap.put("edu.wustl.catissuecore.domain.SpecimenRequirement", tableNames);

		tableNames = new ArrayList<String>();
		tableNames.add("CATISSUE_CONTAINER_POSITION");
		tableNames.add("CATISSUE_SPECIMEN_POSITION");
		entityTablesMap.put("edu.wustl.catissuecore.domain.AbstractPosition", tableNames);

		tableNames = new ArrayList<String>();
		tableNames.add("CATISSUE_SHIPMENT_REQUEST");
		tableNames.add("CATISSUE_SHIPMENT");
		entityTablesMap.put("edu.wustl.catissuecore.domain.shippingtracking.BaseShipment",
				tableNames);

	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddSubClassesMetaData(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
