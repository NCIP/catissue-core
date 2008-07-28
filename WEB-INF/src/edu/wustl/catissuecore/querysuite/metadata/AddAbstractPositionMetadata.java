package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddAbstractPositionMetadata
{
	private Connection connection = null;
	private Statement stmt = null;
	private static HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
	private static HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();
	private static HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private static HashMap<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	private static List<String> entityList = new ArrayList<String>();
	
	/*public static void main(String[] args)
	throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		Statement stmt = connection.createStatement();
		
		AddAbstractPositionMetadata addContainerMetadata = new AddAbstractPositionMetadata(connection);
		addContainerMetadata.addContainerMetadata();
	}*/
	public void addContainerMetadata() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		stmt = connection.createStatement();
		AddEntity addEntity = new AddEntity(connection);
		addEntity.addEntity(entityList, "CATISSUE_ABSTRACT_POSITION", "NULL", 3);
		AddAttribute addAttribute = new AddAttribute(connection,entityNameAttributeNameMap,attributeColumnNameMap,attributeDatatypeMap,attributePrimarkeyMap,entityList);
		addAttribute.addAttribute();
	}		
		private void populateEntityAttributeMap() 
		{
			List<String> attributes = new ArrayList<String>();
			attributes.add("id");
			attributes.add("positionDimensionOne");
			attributes.add("positionDimensionTwo");
		
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.AbstractPosition",attributes);
		}
		
		private void populateAttributeColumnNameMap()
		{
			attributeColumnNameMap.put("id", "IDENTIFIER");
			attributeColumnNameMap.put("positionDimensionOne", "POSITION_DIMENSION_ONE");
			attributeColumnNameMap.put("positionDimensionTwo", "POSITION_DIMENSION_TWO");
		}
		
		private void populateAttributeDatatypeMap()
		{
			attributeDatatypeMap.put("id", "long");
			attributeDatatypeMap.put("positionDimensionOne", "int");
			attributeDatatypeMap.put("positionDimensionTwo", "int");
		}
		private void populateAttributePrimaryKeyMap() 
		{
			attributePrimarkeyMap.put("id", "1");
			attributePrimarkeyMap.put("positionDimensionOne", "0");
			attributePrimarkeyMap.put("positionDimensionTwo", "0");
		}
		private void populateEntityList()
		{
			entityList.add("edu.wustl.catissuecore.domain.AbstractPosition");
		}
		
		public AddAbstractPositionMetadata(Connection connection) throws SQLException
		{
			super();
			this.connection = connection;
			this.stmt = connection.createStatement();
		}
	}
