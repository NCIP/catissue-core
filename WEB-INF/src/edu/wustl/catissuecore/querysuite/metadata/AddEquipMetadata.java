
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author deepti_shelar
 * Class to add metadata for Race and participant
 */
public class AddEquipMetadata extends BaseMetadata
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
	 * This method adds Race Meta data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addEquipMetadata() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();

		this.stmt = this.connection.createStatement();
		final AddEntity addEntity = new AddEntity(this.connection);
		addEntity.addEntity(this.entityList, "catissue_equipment", "NULL", 3, 0);

		final AddAttribute addAttribute = new AddAttribute(this.connection,
				this.entityNameAttributeNameMap, this.attributeColumnNameMap,
				this.attributeDatatypeMap, this.attributePrimarkeyMap, this.entityList);
		addAttribute.addAttribute();

		final String entityName = "krishagni.catissueplus.imaging.domain.Image";
		final String targetEntityName = "krishagni.catissueplus.imaging.domain.Equipment";

		final AddAssociations addAssociations = new AddAssociations(this.connection);
		addAssociations.addAssociation(entityName,targetEntityName, "equip_image",
				"ASSOCIATION", "equipment", false, "", "EQUIPMENT_ID", null, 2, 1,
				"BI_DIRECTIONAL", false);
	}

	/**
	 * This method populates Entity Attribute Map.
	 */
	private void populateEntityAttributeMap()
	{
		final List<String> equipAttributes = new ArrayList<String>();
		equipAttributes.add("deviceName");
		equipAttributes.add("deviceSerialNumber");
		equipAttributes.add("manufacturerName");
		equipAttributes.add("id");
		equipAttributes.add("displayName");
		equipAttributes.add("equipmentId");
		
		this.entityNameAttributeNameMap.put("krishagni.catissueplus.imaging.domain.Equipment", equipAttributes);
	}

	/**
	 * This method populates Attribute ColumnName Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
		this.attributeColumnNameMap.put("deviceName", "DEVICE_NAME");
		this.attributeColumnNameMap.put("deviceSerialNumber", "DEVICE_SER_NUMBER");
		this.attributeColumnNameMap.put("manufacturerName", "MANUFACTURER_NAME");
		this.attributeColumnNameMap.put("displayName", "DISPLAY_NAME");
		this.attributeColumnNameMap.put("equipmentId", "EQUIPMENT_ID");
		this.attributeColumnNameMap.put("softwareVersion", "SOFTWARE_VERSION");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
		this.attributeDatatypeMap.put("deviceName", "string");
		this.attributeDatatypeMap.put("deviceSerialNumber", "string");
		this.attributeDatatypeMap.put("manufacturerName", "string");
		this.attributeDatatypeMap.put("displayName", "string");
		this.attributeDatatypeMap.put("equipmentId", "string");
		this.attributeDatatypeMap.put("softwareVersion", "string");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
		this.attributePrimarkeyMap.put("deviceName", "0");
		this.attributePrimarkeyMap.put("deviceSerialNumber", "0");
		this.attributePrimarkeyMap.put("manufacturerName", "0");
		this.attributePrimarkeyMap.put("displayName", "0");
		this.attributePrimarkeyMap.put("equipmentId", "0");
		this.attributePrimarkeyMap.put("softwareVersion", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("krishagni.catissueplus.imaging.domain.Equipment");
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddEquipMetadata(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
