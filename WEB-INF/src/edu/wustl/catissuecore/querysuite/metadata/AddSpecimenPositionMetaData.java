package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSpecimenPositionMetaData 
{
	private Connection connection = null;
	private Statement stmt = null;
	private static HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
	private static HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();
	private static HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private static HashMap<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	private static List<String> entityList = new ArrayList<String>();
	
	public void addSpecimenPositionMetaData() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		stmt = connection.createStatement();
		AddEntity addEntity = new AddEntity(connection);
		addEntity.addEntity(entityList, "CATISSUE_SPECIMEN_POSITION", "edu.wustl.catissuecore.domain.AbstractPosition", 3,0);
		
		AddAttribute addAttribute = new AddAttribute(connection,entityNameAttributeNameMap,attributeColumnNameMap,attributeDatatypeMap,attributePrimarkeyMap,entityList);
		addAttribute.addAttribute();
		
		String entityName = "edu.wustl.catissuecore.domain.StorageContainer";
		String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenPosition";
		
		AddAssociations addAssociations = new AddAssociations(connection);
		addAssociations.addAssociation(entityName,targetEntityName,"storageContainer_specimenPosition","CONTAINTMENT","specimenPositionCollection",true,"storageContainer","CONTAINER_ID",null,2,1,"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName,entityName,"specimenPosition_storageContainer","ASSOCIATION","storageContainer",false,"","CONTAINER_ID",null,2,1,"BI_DIRECTIONAL");	
	}		
		private void populateEntityAttributeMap() 
		{
			List<String> attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.SpecimenPosition",attributes);
		}
		
		private void populateAttributeColumnNameMap()
		{
			attributeColumnNameMap.put("id", "IDENTIFIER");
		}
		
		private void populateAttributeDatatypeMap()
		{
			attributeDatatypeMap.put("id", "long");
		}
		private void populateAttributePrimaryKeyMap() 
		{
			attributePrimarkeyMap.put("id", "1");
		}
		private void populateEntityList()
		{
			entityList.add("edu.wustl.catissuecore.domain.SpecimenPosition");
		}
		public AddSpecimenPositionMetaData(Connection connection) throws SQLException
		{
			super();
			this.connection = connection;
			this.stmt = connection.createStatement();
		}
}
