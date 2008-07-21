package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
		for(String entityName : entityList)
		{
			//insert statements
			String sql = "select max(identifier) from dyextn_abstract_metadata";
			ResultSet rs = stmt.executeQuery(sql);
			int nextIdOfAbstractMetadata = 0;
			if (rs.next())
			{
				int maxId = rs.getInt(1);
				nextIdOfAbstractMetadata = maxId + 1;
			}
			int nextIdDatabaseproperties = 0;
			sql = "select max(identifier) from dyextn_database_properties";
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				int maxId = rs.getInt(1);
				nextIdDatabaseproperties = maxId + 1;
			}
			//			dyextn_abstract_metadata
			sql = "INSERT INTO dyextn_abstract_metadata values("
			+ nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + entityName
			+ "',null)";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			sql = "INSERT INTO dyextn_abstract_entity values("+ nextIdOfAbstractMetadata + ")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			int parEId = UpdateMetadataUtil.getEntityIdByName("edu.wustl.catissuecore.domain.AbstractPosition", connection.createStatement());
			//			change it for PA :dyextn_entity
			sql = "INSERT INTO dyextn_entity values("
			+ nextIdOfAbstractMetadata + ",3,0,"+parEId+",3,NULL,NULL,1)";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			//			dyextn_entity_group_rel
		
		//	sql = "INSERT INTO dyextn_entity_group_rel values(1," + nextIdOfAbstractMetadata + ")";
			//UpdateMetadataUtil.executeInsertSQL(sql);
		
			sql = "INSERT INTO dyextn_database_properties values("
			+ nextIdDatabaseproperties + ",'CATISSUE_CONTAINER_POSITION')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			sql = "INSERT INTO dyextn_table_properties (IDENTIFIER,ABSTRACT_ENTITY_ID) values("
			+ nextIdDatabaseproperties + "," + nextIdOfAbstractMetadata+")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}
		Set<String> keySet = entityNameAttributeNameMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			String entityName = (String) iterator.next();
			List<String> attributes = entityNameAttributeNameMap
					.get(entityName);
			for (String attr : attributes)
			{
		
				String sql = "select max(identifier) from dyextn_abstract_metadata";
				ResultSet rs = stmt.executeQuery(sql);
				int nextIdOfAbstractMetadata = 0;
				if (rs.next())
				{
					int maxId = rs.getInt(1);
					nextIdOfAbstractMetadata = maxId + 1;
				}
		
				int nextIdAttrTypeInfo = 0;
				sql = "select max(identifier) from dyextn_attribute_type_info";
				rs = stmt.executeQuery(sql);
				if (rs.next())
				{
					int maxId = rs.getInt(1);
					nextIdAttrTypeInfo = maxId + 1;
				}
		
				int nextIdDatabaseproperties = 0;
				sql = "select max(identifier) from dyextn_database_properties";
				rs = stmt.executeQuery(sql);
				if (rs.next())
				{
					int maxId = rs.getInt(1);
					nextIdDatabaseproperties = maxId + 1;
				}
				
			
				sql = "INSERT INTO dyextn_abstract_metadata values("
				+ nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + attr
				+ "',null)";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
				sql = "INSERT INTO DYEXTN_BASE_ABSTRACT_ATTRIBUTE values("+ nextIdOfAbstractMetadata + ")";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
				int entityId = UpdateMetadataUtil.getEntityIdByName(entityName, connection.createStatement());
				sql = "INSERT INTO dyextn_attribute values ("
				+ nextIdOfAbstractMetadata + "," + entityId + ")";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
				String primaryKey = attributePrimarkeyMap.get(attr); 
				sql = "insert into dyextn_primitive_attribute (IDENTIFIER,IS_COLLECTION,IS_IDENTIFIED,IS_PRIMARY_KEY,IS_NULLABLE)"
						+ " values ("
						+ nextIdOfAbstractMetadata
						+ ",0,NULL,"+primaryKey+",1)";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
				sql = "insert into dyextn_attribute_type_info (IDENTIFIER,PRIMITIVE_ATTRIBUTE_ID) values ("
						+ nextIdAttrTypeInfo
						+ ","
						+ nextIdOfAbstractMetadata
						+ ")";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
				String dataType = getDataTypeOfAttribute(attr);
				if (!dataType.equalsIgnoreCase("String"))
				{
					sql = "insert into dyextn_numeric_type_info (IDENTIFIER,MEASUREMENT_UNITS,DECIMAL_PLACES,NO_DIGITS) values ("
							+ nextIdAttrTypeInfo + ",NULL,0,NULL)";
					UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
				}
		
				if (dataType.equalsIgnoreCase("string"))
				{
					sql = "insert into dyextn_string_type_info (IDENTIFIER) values ("
					+ nextIdAttrTypeInfo + ")";
				}
				else if (dataType.equalsIgnoreCase("double"))
				{
					sql = "insert into dyextn_double_type_info (IDENTIFIER) values ("
					+ nextIdAttrTypeInfo + ")";
				}
				else if (dataType.equalsIgnoreCase("int"))
				{
					sql = "insert into dyextn_integer_type_info (IDENTIFIER) values ("
					+ nextIdAttrTypeInfo + ")";
				}
				else if (dataType.equalsIgnoreCase("long"))
				{
					sql = "insert into dyextn_long_type_info (IDENTIFIER) values ("
					+ nextIdAttrTypeInfo + ")";
				}
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
				
				String columnName = getColumnNameOfAttribue(attr);
				sql = "insert into dyextn_database_properties (IDENTIFIER,NAME) values ("
				+ nextIdDatabaseproperties + ",'" + columnName + "')";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
				sql = "insert into dyextn_column_properties (IDENTIFIER,PRIMITIVE_ATTRIBUTE_ID) values ("
						+ nextIdDatabaseproperties
						+ ","
						+ nextIdOfAbstractMetadata + ")";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			}
		}
		String entityName = "edu.wustl.catissuecore.domain.Container";
		String targetEntityName = "edu.wustl.catissuecore.domain.ContainerPosition";
		addAssociation(entityName,targetEntityName,"container_containerPosition","CONTAINTMENT","occupiedPositions",true);
		addAssociation(targetEntityName,entityName,"containerPosition_container","ASSOCIATION","occupiedContainer",false);
		
		/*	addAssociation("participant_CPR","ASSOCIATION","collectionProtocolRegistrationCollection",true);
		addAssociation("participant_CPR","ASSOCIATION","collectionProtocolRegistrationCollection",false);
		addAssociation("participant_PMI","CONTAINTMENT","participantMedicalIdentifierCollection",true);
		addAssociation("participant_PMI","CONTAINTMENT","participantMedicalIdentifierCollection",false);*/
		
	}
		
	private void addAssociation(String entityName,String associatedEntityName,String associationName,String associationType, String roleName,boolean isSwap) throws SQLException, IOException
	{
		String sql = "select max(identifier) from dyextn_abstract_metadata";
		stmt=connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		int nextIdOfAbstractMetadata = 0;
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdOfAbstractMetadata = maxId + 1;
		}
		int nextIdOfDERole = 0;
		sql = "select max(identifier) from dyextn_role";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdOfDERole = maxId + 1;
		}
		int nextIdOfDBProperties = 0;
		
		sql = "select max(identifier) from dyextn_database_properties";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdOfDBProperties = maxId + 1;
		}
		int nextIDintraModelAssociation = 0;
		sql = "select max(ASSOCIATION_ID) from intra_model_association";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIDintraModelAssociation = maxId + 1;
		}
		
		int nextIdPath = 0;
		sql = "select max(PATH_ID) from path";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdPath = maxId + 1;
		}
		
		int entityId = 0;
		
		sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ entityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			entityId = rs.getInt(1);
		}
		if (entityId == 0)
		{
			System.out.println("Entity not found of name ");
		}
		
		int associatedEntityId =0;
		
		sql = "select identifier from dyextn_abstract_metadata where name like '"
			+ associatedEntityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			associatedEntityId = rs.getInt(1);
		}
		if (associatedEntityId == 0)
		{
			System.out.println("Entity not found of name ");
		}
		
		sql = "insert into dyextn_abstract_metadata values ("
				+ nextIdOfAbstractMetadata
				+ ",null,null,null,'"+associationName+"',null)";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
		sql = "INSERT INTO DYEXTN_BASE_ABSTRACT_ATTRIBUTE values("+ nextIdOfAbstractMetadata + ")";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
		sql = "insert into dyextn_attribute values ("
				+ nextIdOfAbstractMetadata + "," + entityId + ")";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		int roleId = nextIdOfDERole;
		if(isSwap)
		{
			roleId = nextIdOfDERole +1;
			sql = "insert into dyextn_role values (" + nextIdOfDERole
					+ ",'"+associationType+"',2,0,'"+roleName+"')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			sql = "insert into dyextn_role values (" + roleId
					+ ",'ASSOCIATION',1,0,'occupiedContainer')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}		
		if (!isSwap)
		{
			int lastIdOfDERole =nextIdOfDERole -2;
			int idOfDERole =nextIdOfDERole -1;
			sql = "insert into dyextn_association values ("
					+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
					+ associatedEntityId + "," + lastIdOfDERole + "," + idOfDERole + ",1)";
		} 
		else 
		{
			sql = "insert into dyextn_association values ("
				+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
				+ associatedEntityId + "," + roleId + "," + nextIdOfDERole + ",1)";
		}
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		sql = "insert into dyextn_database_properties values ("
				+ nextIdOfDBProperties
				+ ",'"+associationName+"')";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		if(isSwap)
		{
		sql = "insert into dyextn_constraint_properties (IDENTIFIER,SOURCE_ENTITY_KEY,TARGET_ENTITY_KEY,ASSOCIATION_ID) values("
				+ nextIdOfDBProperties + ",null,'PARENT_CONTAINER_ID',"
				+ nextIdOfAbstractMetadata + ")";
		}else
		{
			sql = "insert into dyextn_constraint_properties (IDENTIFIER,SOURCE_ENTITY_KEY,TARGET_ENTITY_KEY,ASSOCIATION_ID) values("
				+ nextIdOfDBProperties + ",'PARENT_CONTAINER_ID',null,"
				+ nextIdOfAbstractMetadata + ")";
			
		}
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
						
		sql = "insert into association values("
			+ nextIDintraModelAssociation + ",2)";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
		
		sql = "insert into intra_model_association values("
				+ nextIDintraModelAssociation + "," + nextIdOfAbstractMetadata + ")";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		sql = "insert into path values (" + nextIdPath + "," + entityId + ","
		+ nextIDintraModelAssociation + "," + associatedEntityId + ")";
		
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}//End of addAssociation()
		
		private String getColumnNameOfAttribue(String attr)
		{
			return attributeColumnNameMap.get(attr);
		}
		
		private String getDataTypeOfAttribute(String attr)
		{
			return attributeDatatypeMap.get(attr);
		}
		
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
