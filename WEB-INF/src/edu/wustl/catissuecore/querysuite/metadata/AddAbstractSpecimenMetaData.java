
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * AddAbstractSpecimenMetaData class.
 *
 */
public class AddAbstractSpecimenMetaData extends BaseMetadata
{

	/**
	 * Specify Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Specify Statement instance.
	 */
	private Statement stmt = null;

	/**
	 * adds Abstract Position Meta Data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addAbstractPositionMetaData() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();

		this.stmt = this.connection.createStatement();
		final AddEntity addEntity = new AddEntity(this.connection);
		addEntity.addEntity(this.entityList, "CATISSUE_ABSTRACT_SPECIMEN", "NULL", 3, 1);
		final AddAttribute addAttribute = new AddAttribute(this.connection,
				this.entityNameAttributeNameMap, this.attributeColumnNameMap,
				this.attributeDatatypeMap, this.attributePrimarkeyMap, this.entityList);
		addAttribute.addAttribute();

		final String entityName = "edu.wustl.catissuecore.domain.AbstractSpecimen";
		final String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenEventParameters";

		final AddAssociations addAssociations = new AddAssociations(this.connection);
		addAssociations.addAssociation(entityName, targetEntityName,
				"abstractSpecimen_SpecimenEventParameters", "CONTAINTMENT",
				"specimenEventCollection", true, "abstractSpecimen", "SPECIMEN_ID", null, 2, 1,
				"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName, entityName,
				"SpecimenEventParameters_abstractSpecimen", "ASSOCIATION", "abstractSpecimen",
				false, "", "SPECIMEN_ID", null, 2, 1, "BI_DIRECTIONAL");
	}

	/**
	 * This method populates Entity Attribute Map.
	 */
	private void populateEntityAttributeMap()
	{
		final List<String> attributes = new ArrayList<String>();

		attributes.add("id");

		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.AbstractSpecimen",
				attributes);
	}

	/**
	 * This method populates Attribute ColumnName Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
	}

	/**
	 * AddAbstractSpecimenMetaData.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddAbstractSpecimenMetaData(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
