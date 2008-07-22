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

public class AddAttribute 
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
		
		AddAttribute addAttribute = new AddAttribute(connection);
		addAttribute.addAttribute();
	}
	
	public void addAttribute() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		
		Set<String> keySet = entityNameAttributeNameMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			String entityName = (String) iterator.next();
			List<String> attributes = entityNameAttributeNameMap
					.get(entityName);
			for (String attr : attributes)
			{
		
				stmt = connection.createStatement();
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
			attributes.add("quantity");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.DistributionSpecimenRequirement",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("unsignedConsentDocumentURL");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.CollectionProtocol",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("signedConsentDocumentURL");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.CollectionProtocolRegistration",attributes);
		}
		
		private void populateAttributeColumnNameMap()
		{
			attributeColumnNameMap.put("quantity", "QUANTITY");
			attributeColumnNameMap.put("unsignedConsentDocumentURL", "UNSIGNED_CONSENT_DOC_URL");
			attributeColumnNameMap.put("signedConsentDocumentURL", "CONSENT_DOC_URL");
		}
		
		private void populateAttributeDatatypeMap()
		{
			attributeDatatypeMap.put("quantity", "double");
			attributeDatatypeMap.put("unsignedConsentDocumentURL", "string");
			attributeDatatypeMap.put("signedConsentDocumentURL", "string");
		}
		private void populateAttributePrimaryKeyMap() 
		{
			attributePrimarkeyMap.put("quantity", "0");
			attributePrimarkeyMap.put("unsignedConsentDocumentURL", "0");
			attributePrimarkeyMap.put("signedConsentDocumentURL", "0");
		}
		private void populateEntityList()
		{
			entityList.add("edu.wustl.catissuecore.domain.DistributionSpecimenRequirement");
			entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol");
			entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration");
			
		}

		public AddAttribute(Connection connection) throws SQLException
		{
			super();
			this.connection = connection;
			this.stmt = connection.createStatement();
		}

}
