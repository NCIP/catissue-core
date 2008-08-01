package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 
 * @author deepti_shelar
 * Class to add metadata for Race and participant
 */
public class AddRaceMetadata 
{
	private Connection connection = null;
	private Statement stmt = null;
	private HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
	private HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();
	private HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private HashMap<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	private List<String> entityList = new ArrayList<String>();
	
	public void addRaceMetadata() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		stmt = connection.createStatement();
		AddEntity addEntity = new AddEntity(connection);
		addEntity.addEntity(entityList, "CATISSUE_RACE", "NULL", 3);
		
		AddAttribute addAttribute = new AddAttribute(connection,entityNameAttributeNameMap,attributeColumnNameMap,attributeDatatypeMap,attributePrimarkeyMap,entityList);
		addAttribute.addAttribute();
		
		String entityName = "edu.wustl.catissuecore.domain.Participant";
		String targetEntityName = "edu.wustl.catissuecore.domain.Race";
		
		AddAssociations addAssociations = new AddAssociations(connection);
		addAssociations.addAssociation(entityName,targetEntityName,"participant_race","CONTAINTMENT","raceCollection",true,"participant","PARTICIPANT_ID",null,2,1,"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName,entityName,"race_participant","ASSOCIATION","participant",false,"","PARTICIPANT_ID",null,2,1,"BI_DIRECTIONAL");
	}

	private void populateEntityAttributeMap() 
	{
		List<String> attributes = new ArrayList<String>();
		attributes.add("id");
		attributes.add("raceName");
		entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.Race",attributes);
	}

	private void populateAttributeColumnNameMap() 
	{
		attributeColumnNameMap.put("id", "IDENTIFIER");
		attributeColumnNameMap.put("raceName", "RACE_NAME");
	}

	private void populateAttributeDatatypeMap() 
	{
		attributeDatatypeMap.put("id", "long");
		attributeDatatypeMap.put("raceName", "string");
	}
	private void populateAttributePrimaryKeyMap() 
	{
		attributePrimarkeyMap.put("id", "1");
		attributePrimarkeyMap.put("raceName", "0");
	}
	private  void populateEntityList() 
	{
		entityList.add("edu.wustl.catissuecore.domain.Race");
	}

	public AddRaceMetadata(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
