package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.wustl.common.util.dbManager.DBUtil;

public class AddAbstractSpecimenMetaData 
{
	private Connection connection = null;

	private Statement stmt = null;

	private static HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();

	private static HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();

	private static HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private static HashMap<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	private static List<String> entityList = new ArrayList<String>();
	
	public static void main(String[] args)
	throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		
		AddAbstractSpecimenMetaData addAbstractPositionMetaData = new AddAbstractSpecimenMetaData(connection);
		addAbstractPositionMetaData.addAbstractPositionMetaData();
	}
	public void addAbstractPositionMetaData() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		stmt = connection.createStatement();
		AddEntity addEntity = new AddEntity(connection);
		addEntity.addEntity(entityList, "CATISSUE_ABSTRACT_SPECIMEN", "NULL", 3);
		AddAttribute addAttribute = new AddAttribute(connection,entityNameAttributeNameMap,attributeColumnNameMap,attributeDatatypeMap,attributePrimarkeyMap,entityList);
		addAttribute.addAttribute();
		
		String entityName = "edu.wustl.catissuecore.domain.AbstractSpecimen";
		String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenEventParameters";
		
		AddAssociations addAssociations = new AddAssociations(connection);
		addAssociations.addAssociation(entityName,targetEntityName,"abstractSpecimen_SpecimenEventParameters","CONTAINTMENT","specimenEventCollection",true,"abstractSpecimen","SPECIMEN_ID",2,1);
		addAssociations.addAssociation(targetEntityName,entityName,"SpecimenEventParameters_abstractSpecimen","ASSOCIATION","abstractSpecimen",false,"","SPECIMEN_ID",2,1);
	}		
		private void populateEntityAttributeMap() 
		{
			List<String> attributes = new ArrayList<String>();
			
			attributes.add("id");
			//attributes.add("specimenClass");
		
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.AbstractSpecimen",attributes);
		}
		
		private void populateAttributeColumnNameMap()
		{
			attributeColumnNameMap.put("id", "IDENTIFIER");
			//attributeColumnNameMap.put("specimenClass", "SPECIMEN_CLASS");
		}
		
		private void populateAttributeDatatypeMap()
		{
			attributeDatatypeMap.put("id", "long");
			//attributeDatatypeMap.put("specimenClass", "string");
		}
		private void populateAttributePrimaryKeyMap() 
		{
			attributePrimarkeyMap.put("id", "1");
			//attributePrimarkeyMap.put("specimenClass", "0");
		}
		private void populateEntityList()
		{
			entityList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
		}
		public AddAbstractSpecimenMetaData(Connection connection) throws SQLException
		{
			super();
			this.connection = connection;
			this.stmt = connection.createStatement();
		}
		
	}
