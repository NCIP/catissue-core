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

public class AddRequirementSpecimenSubClassesMetaData 
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
		
		AddRequirementSpecimenSubClassesMetaData addSpecimenMetaData = new AddRequirementSpecimenSubClassesMetaData(connection);
		addSpecimenMetaData.addSpecimenMetadata();
	}
	
	public void addSpecimenMetadata() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		
		String[] tableNames = {"CATISSUE_CELL_REQ_SPECIMEN","CATISSUE_FLUID_REQ_SPECIMEN","CATISSUE_MOL_REQ_SPECIMEN","CATISSUE_TISSUE_REQ_SPECIMEN"};
		int cnt = 0;
		for(String entityName : entityList)
		{
			stmt = connection.createStatement();
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
		
			//			change it for PA :dyextn_entity
			sql = "INSERT INTO dyextn_entity values("
			+ nextIdOfAbstractMetadata + ",3,0,1868,3,NULL,NULL,1)";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			//			dyextn_entity_group_rel
		
		//	sql = "INSERT INTO dyextn_entity_group_rel values(1," + nextIdOfAbstractMetadata + ")";
			//UpdateMetadataUtil.executeInsertSQL(sql, stmt);
		
			sql = "INSERT INTO dyextn_database_properties values("
			+ nextIdDatabaseproperties + ",'"+tableNames[cnt++]+"')";
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
			System.out.println("Entity Name : "+entityName);
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
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.CellRequirementSpecimen",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.FluidRequirementSpecimen",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			attributes.add("concentrationInMicrogramPerMicroliter");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.MolecularRequirementSpecimen",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.TissueRequirementSpecimen",attributes);
		}
		
		private void populateAttributeColumnNameMap()
		{
			attributeColumnNameMap.put("id", "IDENTIFIER");
			attributeColumnNameMap.put("concentrationInMicrogramPerMicroliter", "CONCENTRATION");
		}
		
		private void populateAttributeDatatypeMap()
		{
			attributeDatatypeMap.put("id", "long");
			attributeDatatypeMap.put("concentrationInMicrogramPerMicroliter", "double");
		}
		private void populateAttributePrimaryKeyMap() 
		{
			attributePrimarkeyMap.put("id", "1");
			attributePrimarkeyMap.put("concentrationInMicrogramPerMicroliter","0");
		}
		private void populateEntityList()
		{
			entityList.add("edu.wustl.catissuecore.domain.CellRequirementSpecimen");
			entityList.add("edu.wustl.catissuecore.domain.FluidRequirementSpecimen");
			entityList.add("edu.wustl.catissuecore.domain.MolecularRequirementSpecimen");
			entityList.add("edu.wustl.catissuecore.domain.TissueRequirementSpecimen");
		}

		public AddRequirementSpecimenSubClassesMetaData(Connection connection) throws SQLException
		{
			super();
			this.connection = connection;
			this.stmt = connection.createStatement();
		}
}
