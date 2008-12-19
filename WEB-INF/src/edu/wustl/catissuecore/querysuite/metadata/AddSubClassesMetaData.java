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

import edu.wustl.common.util.global.Constants;

/**
 * @author pooja_deshpande
 * Class to add metadata for subclasses of all newly added entities
 */

public class AddSubClassesMetaData extends BaseMetadata
{
	private Connection connection = null;
	private Statement stmt = null;
	private static HashMap<String, List<String>> entityMap = new HashMap<String, List<String>>();
	private static HashMap<String, List<String>> entityTablesMap = new HashMap<String, List<String>>();
	
	public void addSubClassesMetadata() throws SQLException, IOException
	{
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();
		populateEntityTablesMap();
		
		Set<String> keySet = entityMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		 while(iterator.hasNext())
		 {
			String key = iterator.next();
			List<String> entityList = entityMap.get(key);
			List<String> tableList = entityTablesMap.get(key);
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
				
				sql = "INSERT INTO dyextn_abstract_metadata(IDENTIFIER,CREATED_DATE,DESCRIPTION,LAST_UPDATED,NAME,PUBLIC_ID) values("
				+ nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + entityName
				+ "',null)";
				if(Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
				{
					sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,"dyextn_abstract_metadata");
				}
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
				sql = "INSERT INTO dyextn_abstract_entity values("+ nextIdOfAbstractMetadata + ")";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			
				int parEId = UpdateMetadataUtil.getEntityIdByName(key, connection.createStatement());
				sql = "INSERT INTO dyextn_entity values("
				+ nextIdOfAbstractMetadata + ",3,0,"+parEId+",3,NULL,NULL,1)";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			
				sql = "INSERT INTO dyextn_database_properties(IDENTIFIER,NAME) values("
				+ nextIdDatabaseproperties + ",'"+tableList.get(entityList.indexOf(entityName))+"')";
				if(Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
				{
					sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,"dyextn_database_properties");
				}
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			
				sql = "INSERT INTO dyextn_table_properties (IDENTIFIER,ABSTRACT_ENTITY_ID) values("
				+ nextIdDatabaseproperties + "," + nextIdOfAbstractMetadata+")";
				UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			}
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
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.ContainerPosition",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.SpecimenPosition",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest",attributes);
			
			attributes = new ArrayList<String>();
			attributes.add("id");
			attributes.add("barcode");
			entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.shippingtracking.Shipment",attributes);
		}
		
		private void populateAttributeColumnNameMap()
		{
			attributeColumnNameMap.put("id", "IDENTIFIER");
			attributeColumnNameMap.put("concentrationInMicrogramPerMicroliter", "CONCENTRATION");

			attributeColumnNameMap.put("barcode", "BARCODE");
		}
		
		private void populateAttributeDatatypeMap()
		{
			attributeDatatypeMap.put("id", "long");
			attributeDatatypeMap.put("concentrationInMicrogramPerMicroliter", "double");
			
			attributeDatatypeMap.put("barcode", "string");
		}
		private void populateAttributePrimaryKeyMap() 
		{
			attributePrimarkeyMap.put("id", "1");
			attributePrimarkeyMap.put("concentrationInMicrogramPerMicroliter","0");
			
			attributePrimarkeyMap.put("barcode","0");
		}
		private void populateEntityList()
		{
			entityList.add("edu.wustl.catissuecore.domain.CellSpecimenRequirement");
			entityList.add("edu.wustl.catissuecore.domain.FluidSpecimenRequirement");
			entityList.add("edu.wustl.catissuecore.domain.MolecularSpecimenRequirement");
			entityList.add("edu.wustl.catissuecore.domain.TissueSpecimenRequirement");
			entityMap.put("edu.wustl.catissuecore.domain.SpecimenRequirement", entityList);
			
			entityList = new ArrayList<String>();
			entityList.add("edu.wustl.catissuecore.domain.ContainerPosition");
			entityList.add("edu.wustl.catissuecore.domain.SpecimenPosition");
			entityMap.put("edu.wustl.catissuecore.domain.AbstractPosition", entityList);
			
			entityList = new ArrayList<String>();
			entityList.add("edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest");
			entityList.add("edu.wustl.catissuecore.domain.shippingtracking.Shipment");
			entityMap.put("edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", entityList);
		}
		
		private void populateEntityTablesMap() 
		{
			List<String> tableNames = new ArrayList<String>();
			tableNames.add("CATISSUE_CELL_REQ_SPECIMEN");
			tableNames.add("CATISSUE_FLUID_REQ_SPECIMEN");
			tableNames.add("CATISSUE_MOL_REQ_SPECIMEN");
			tableNames.add("CATISSUE_TISSUE_REQ_SPECIMEN");
			entityTablesMap.put("edu.wustl.catissuecore.domain.SpecimenRequirement", tableNames);
			
			tableNames = new ArrayList<String>();
			tableNames.add("CATISSUE_CONTAINER_POSITION");
			tableNames.add("CATISSUE_SPECIMEN_POSITION");
			entityTablesMap.put("edu.wustl.catissuecore.domain.AbstractPosition", tableNames);
			
			tableNames = new ArrayList<String>();
			tableNames.add("CATISSUE_SHIPMENT_REQUEST");
			tableNames.add("CATISSUE_SHIPMENT");
			entityTablesMap.put("edu.wustl.catissuecore.domain.shippingtracking.BaseShipment", tableNames);
			
		}

		public AddSubClassesMetaData(Connection connection) throws SQLException
		{
			super();
			this.connection = connection;
			this.stmt = connection.createStatement();
		}
}
