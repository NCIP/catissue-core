
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
public class AddRaceMetadata extends BaseMetadata
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
	public void addRaceMetadata() throws SQLException, IOException
	{
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();

		this.stmt = this.connection.createStatement();
		final AddEntity addEntity = new AddEntity(this.connection);
		addEntity.addEntity(this.entityList, "CATISSUE_RACE", "NULL", 3, 0);

		final AddAttribute addAttribute = new AddAttribute(this.connection,
				this.entityNameAttributeNameMap, this.attributeColumnNameMap,
				this.attributeDatatypeMap, this.attributePrimarkeyMap, this.entityList);
		addAttribute.addAttribute();

		final String entityName = "edu.wustl.catissuecore.domain.Participant";
		final String targetEntityName = "edu.wustl.catissuecore.domain.Race";

		final AddAssociations addAssociations = new AddAssociations(this.connection);
		addAssociations.addAssociation(entityName, targetEntityName, "participant_race",
				"CONTAINTMENT", "raceCollection", true, "participant", "PARTICIPANT_ID", null, 2,
				1, "BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName, entityName, "race_participant",
				"ASSOCIATION", "participant", false, "", "PARTICIPANT_ID", null, 2, 1,
				"BI_DIRECTIONAL");
	}

	/**
	 * This method populates Entity Attribute Map.
	 */
	private void populateEntityAttributeMap()
	{
		final List<String> attributes = new ArrayList<String>();
		attributes.add("id");
		attributes.add("raceName");
		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.Race", attributes);
	}

	/**
	 * This method populates Attribute ColumnName Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put("id", "IDENTIFIER");
		this.attributeColumnNameMap.put("raceName", "RACE_NAME");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("id", "long");
		this.attributeDatatypeMap.put("raceName", "string");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put("id", "1");
		this.attributePrimarkeyMap.put("raceName", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.Race");
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public AddRaceMetadata(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
