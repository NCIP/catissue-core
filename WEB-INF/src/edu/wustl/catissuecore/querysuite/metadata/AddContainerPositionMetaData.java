package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wustl.common.util.dbManager.DBUtil;

public class AddContainerPositionMetaData
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
		Statement stmt = connection.createStatement();
		
		AddContainerPositionMetaData addContainerPositionMetaData = new AddContainerPositionMetaData(connection);
		addContainerPositionMetaData.addContainerPositionMetaData();
	}
	
	public void addContainerPositionMetaData() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		stmt = connection.createStatement();
		AddEntity addEntity = new AddEntity(connection);
		addEntity.addEntity(entityList, "CATISSUE_CONTAINER_POSITION", "edu.wustl.catissuecore.domain.AbstractPosition", 3);
		
		AddAttribute addAttribute = new AddAttribute(connection,entityNameAttributeNameMap,attributeColumnNameMap,attributeDatatypeMap,attributePrimarkeyMap,entityList);
		addAttribute.addAttribute();
		
		String entityName = "edu.wustl.catissuecore.domain.Container";
		String targetEntityName = "edu.wustl.catissuecore.domain.ContainerPosition";
		
		AddAssociations addAssociations = new AddAssociations(connection);
		addAssociations.addAssociation(entityName,targetEntityName,"container_containerPosition","CONTAINTMENT","occupiedPositions",true,"occupiedContainer","PARENT_CONTAINER_ID",2,1);
		addAssociations.addAssociation(targetEntityName,entityName,"containerPosition_container","ASSOCIATION","occupiedContainer",false,"","PARENT_CONTAINER_ID",2,1);
	}

	/**
	 * @throws SQLException
	 * @throws IOException
	 */
		private void populateEntityAttributeMap() 
		{
			List<String> attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.ContainerPosition",attributes);		
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
			entityList.add("edu.wustl.catissuecore.domain.ContainerPosition");
		}
		
		public AddContainerPositionMetaData(Connection connection) throws SQLException
		{
			super();
			this.connection = connection;
			this.stmt = connection.createStatement();
		}
}
