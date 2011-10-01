
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to Add Specimen Class Attribute Meta data.
 *
 */
public class AddSpecimenClassAttributeMetadata extends BaseMetadata
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;

	/**
	 * This method adds Specimen Class.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addSpecimenClass() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();

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
		final List<String> attributes = new ArrayList<String>();
		attributes.add("specimenClass");

		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.AbstractSpecimen",
				attributes);
	}

	/**
	 * This method populates Attribute Column Name Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("specimenClass", "SPECIMEN_CLASS");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("specimenClass", "string");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("specimenClass", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddSpecimenClassAttributeMetadata(Connection connection) throws SQLException
	{
		this.connection = connection;
	}
}
