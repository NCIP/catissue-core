package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddRequirementSpecimenSubClassesMetaData 
{
	private Connection connection = null;
	private Statement stmt = null;
	private static HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
	private static HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();
	private static HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private static HashMap<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	private static List<String> entityList = new ArrayList<String>();
	
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
			
			sql = "INSERT INTO dyextn_abstract_metadata values("
			+ nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + entityName
			+ "',null)";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			sql = "INSERT INTO dyextn_abstract_entity values("+ nextIdOfAbstractMetadata + ")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			int parEId = UpdateMetadataUtil.getEntityIdByName("edu.wustl.catissuecore.domain.SpecimenRequirement", connection.createStatement());
			sql = "INSERT INTO dyextn_entity values("
			+ nextIdOfAbstractMetadata + ",3,0,"+parEId+",3,NULL,NULL,1)";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			sql = "INSERT INTO dyextn_database_properties values("
			+ nextIdDatabaseproperties + ",'"+tableNames[cnt++]+"')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			sql = "INSERT INTO dyextn_table_properties (IDENTIFIER,ABSTRACT_ENTITY_ID) values("
			+ nextIdDatabaseproperties + "," + nextIdOfAbstractMetadata+")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}
		AddAttribute addAttribute = new AddAttribute(connection,entityNameAttributeNameMap,attributeColumnNameMap,attributeDatatypeMap,attributePrimarkeyMap,entityList);
		addAttribute.addAttribute();	
	}				
		private void populateEntityAttributeMap() 
		{
			List<String> attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.CellSpecimenRequirement",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.FluidSpecimenRequirement",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			attributes.add("concentrationInMicrogramPerMicroliter");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.MolecularSpecimenRequirement",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.TissueSpecimenRequirement",attributes);
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
			entityList.add("edu.wustl.catissuecore.domain.CellSpecimenRequirement");
			entityList.add("edu.wustl.catissuecore.domain.FluidSpecimenRequirement");
			entityList.add("edu.wustl.catissuecore.domain.MolecularSpecimenRequirement");
			entityList.add("edu.wustl.catissuecore.domain.TissueSpecimenRequirement");
		}

		public AddRequirementSpecimenSubClassesMetaData(Connection connection) throws SQLException
		{
			super();
			this.connection = connection;
			this.stmt = connection.createStatement();
		}
}
