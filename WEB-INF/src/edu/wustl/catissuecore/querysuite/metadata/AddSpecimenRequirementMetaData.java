
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * class to Add Specimen Requirement Meta Data.
 *
 */
public class AddSpecimenRequirementMetaData extends BaseMetadata
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Statement instance.
	 */
	private final Statement stmt = null;

	/**
	 * This method adds Specimen Requirement Meta Data.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addSpecimenRequirementMetaData() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();

		this.connection.createStatement();
		final AddEntity addEntity = new AddEntity(this.connection);
		addEntity.addEntity(this.entityList, "CATISSUE_CP_REQ_SPECIMEN",
				"edu.wustl.catissuecore.domain.AbstractSpecimen", 3, 0);

		final AddAttribute addAttribute = new AddAttribute(this.connection,
				this.entityNameAttributeNameMap, this.attributeColumnNameMap,
				this.attributeDatatypeMap, this.attributePrimarkeyMap, this.entityList);
		addAttribute.addAttribute();

		final AddAssociations addAssociations = new AddAssociations(this.connection);
		String entityName = "edu.wustl.catissuecore.domain.CollectionProtocolEvent";
		String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenRequirement";
		addAssociations.addAssociation(entityName, targetEntityName,
				"collectionProtocolEvent_specimenRequirement", "CONTAINTMENT",
				"specimenRequirementCollection", true, "CollectionProtocolEvent",
				"COLLECTION_PROTOCOL_EVENT_ID", null, 2, 1, "BI_DIRECTIONAL", false);
		addAssociations.addAssociation(targetEntityName, entityName,
				"specimenRequirement_collectionProtocolEvent", "ASSOCIATION",
				"collectionProtocolEvent", false, "", "COLLECTION_PROTOCOL_EVENT_ID", null, 2, 1,
				"BI_DIRECTIONAL", false);

		entityName = "edu.wustl.catissuecore.domain.SpecimenRequirement";
		targetEntityName = "edu.wustl.catissuecore.domain.Specimen";

		addAssociations.addAssociation(entityName, targetEntityName,
				"specimenRequirement_specimen", "ASSOCIATION", "specimenCollection", true,
				"specimenRequirement", "REQ_SPECIMEN_ID", null, 2, 1, "BI_DIRECTIONAL", false);
	addAssociations.addAssociation(targetEntityName, entityName,
				"specimen_specimenRequirement", "ASSOCIATION", "specimenRequirement", false, "",
				"REQ_SPECIMEN_ID", null, 2, 1, "BI_DIRECTIONAL", false);
	}

	/**
	 * This method populates Entity Attribute Map.
	 */
	private void populateEntityAttributeMap()
	{
		final List<String> attributes = new ArrayList<String>();
		attributes.add("id");
		attributes.add("storageType");
		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.SpecimenRequirement",
				attributes);
	}

	/**
	 * THis method populates Attribute Column Name Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
		this.attributeColumnNameMap.put("storageType", "STORAGE_TYPE");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
		this.attributeDatatypeMap.put("storageType", "string");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
		this.attributePrimarkeyMap.put("storageType", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.SpecimenRequirement");
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddSpecimenRequirementMetaData(Connection connection) throws SQLException
	{
		this.connection = connection;
	}
}
