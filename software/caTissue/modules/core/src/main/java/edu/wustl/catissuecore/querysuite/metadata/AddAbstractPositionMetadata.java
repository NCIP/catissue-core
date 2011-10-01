
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * AddAbstractPositionMetadata class.
 */
public class AddAbstractPositionMetadata extends BaseMetadata
{

	/**
	 * Connection Instance.
	 */
	private Connection connection = null;
	/**
	 * Statement Instance.
	 */
	private Statement stmt = null;

	/**
	 * This method adds Container Meta data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addContainerMetadata() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();

		this.stmt = this.connection.createStatement();
		final AddEntity addEntity = new AddEntity(this.connection);
		addEntity.addEntity(this.entityList, "CATISSUE_ABSTRACT_POSITION", "NULL", 3, 0);
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
		attributes.add("id");
		attributes.add("positionDimensionOne");
		attributes.add("positionDimensionTwo");

		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.AbstractPosition",
				attributes);
	}

	/**
	 * This method populates Attribute ColumnName Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
		this.attributeColumnNameMap.put("positionDimensionOne", "POSITION_DIMENSION_ONE");
		this.attributeColumnNameMap.put("positionDimensionTwo", "POSITION_DIMENSION_TWO");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
		this.attributeDatatypeMap.put("positionDimensionOne", "int");
		this.attributeDatatypeMap.put("positionDimensionTwo", "int");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
		this.attributePrimarkeyMap.put("positionDimensionOne", "0");
		this.attributePrimarkeyMap.put("positionDimensionTwo", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.AbstractPosition");
	}

	/**
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddAbstractPositionMetadata(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
