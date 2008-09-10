package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddAbstractPositionMetadata extends BaseMetadata
{
	private Connection connection = null;
	private Statement stmt = null;
	
	public void addContainerMetadata() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		stmt = connection.createStatement();
		AddEntity addEntity = new AddEntity(connection);
		addEntity.addEntity(entityList, "CATISSUE_ABSTRACT_POSITION", "NULL", 3,0);
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
			this.connection = connection;
			this.stmt = connection.createStatement();
		}
	}
