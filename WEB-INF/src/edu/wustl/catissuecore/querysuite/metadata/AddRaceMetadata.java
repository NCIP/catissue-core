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
	
	

	public static void main(String[] args)
			throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		
		AddRaceMetadata addRaceMetadata = new AddRaceMetadata(connection);
		addRaceMetadata.addRaceMetadata();
	}
	
	public void addRaceMetadata() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		for(String entityName : entityList)
		{
			
			//insert statements
			String sql = "select max(identifier) from dyextn_abstract_metadata";
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(sql);
			int nextIdOfAbstractMetadata = 0;
			if (rs.next()) {
				int maxId = rs.getInt(1);
				nextIdOfAbstractMetadata = maxId + 1;
				System.out.println(nextIdOfAbstractMetadata+" "+maxId);
			}
			int nextIdDatabaseproperties = 0;
			sql = "select max(identifier) from dyextn_database_properties";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
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

			//			change it for PA :dyextn_entity
			sql = "INSERT INTO dyextn_entity values("
			+ nextIdOfAbstractMetadata + ",3,0,NULL,3,NULL,NULL,1)";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());

			//			dyextn_entity_group_rel

		//	sql = "INSERT INTO dyextn_entity_group_rel values(1," + nextIdOfAbstractMetadata + ")";
			//UpdateMetadataUtil.executeInsertSQL(sql, stmt);

			sql = "INSERT INTO dyextn_database_properties values("
			+ nextIdDatabaseproperties + ",'CATISSUE_RACE')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());

			sql = "INSERT INTO dyextn_table_properties (IDENTIFIER,ABSTRACT_ENTITY_ID) values("
			+ nextIdDatabaseproperties + "," + nextIdOfAbstractMetadata+")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}
		Set<String> keySet = entityNameAttributeNameMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String entityName = (String) iterator.next();
			List<String> attributes = entityNameAttributeNameMap
					.get(entityName);
			for (String attr : attributes) {

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
		String entityName = "edu.wustl.catissuecore.domain.Participant";
		String targetEntityName = "edu.wustl.catissuecore.domain.Race";
		addAssociation(entityName,targetEntityName,"participant_race","CONTAINTMENT","raceCollection",true);
		addAssociation(targetEntityName,entityName,"race_participant","ASSOCIATION","participant",false);
	}

	private List<String> addAssociation(String entityName,String associatedEntityName,String associationName,String associationType, String roleName,boolean isSwap) throws SQLException, IOException
	{
		List<String> createAssociationSQL = new ArrayList<String>();
		String sql = "select max(identifier) from dyextn_abstract_metadata";
		ResultSet rs = stmt.executeQuery(sql);

		int nextIdOfAbstractMetadata = 0;
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfAbstractMetadata = maxId + 1;
		}
		int nextIdOfDERole = 0;
		sql = "select max(identifier) from dyextn_role";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfDERole = maxId + 1;
		}
		int nextIdOfDBProperties = 0;

		sql = "select max(identifier) from dyextn_database_properties";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfDBProperties = maxId + 1;
		}
		int nextIDintraModelAssociation = 0;
		sql = "select max(ASSOCIATION_ID) from intra_model_association";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIDintraModelAssociation = maxId + 1;
		}
		
		int nextIdPath = 0;
		sql = "select max(PATH_ID) from path";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdPath = maxId + 1;
		}

		int entityId = 0;
		
		sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ entityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			entityId = rs.getInt(1);

		}
		if (entityId == 0) {
			System.out.println("Entity not found of name ");
		}
		
		int associatedEntityId =0;
		
		sql = "select identifier from dyextn_abstract_metadata where name like '"
			+ associatedEntityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			associatedEntityId = rs.getInt(1);

		}
		if (associatedEntityId == 0) {
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
		
		
			
			//participantAnnotation
			sql = "insert into dyextn_role values (" + roleId
					+ ",'ASSOCIATION',1,0,'participant')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}
		//added association for collectionProtocolRegistrationCollection and participantAnnotation
		
		/*sql = "insert into dyextn_association values ("
			+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
			+ associatedEntityId + "," + roleId + "," + nextIdOfDERole + ",1)";*/
		
		if (!isSwap) {
			int lastIdOfDERole =nextIdOfDERole -2;
			int idOfDERole =nextIdOfDERole -1;
			sql = "insert into dyextn_association values ("
					+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
					+ associatedEntityId + "," + lastIdOfDERole + "," + idOfDERole + ",1)";
		} else {
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
				+ nextIdOfDBProperties + ",null,'PARTICIPANT_ID',"
				+ nextIdOfAbstractMetadata + ")";
		}else
		{
			sql = "insert into dyextn_constraint_properties (IDENTIFIER,SOURCE_ENTITY_KEY,TARGET_ENTITY_KEY,ASSOCIATION_ID) values("
				+ nextIdOfDBProperties + ",'PARTICIPANT_ID',null,"
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
		
		return createAssociationSQL;
	}

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
