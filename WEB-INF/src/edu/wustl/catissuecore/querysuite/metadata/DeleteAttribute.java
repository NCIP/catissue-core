package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * 
 * @author deepti_shelar
 *
 */
public class DeleteAttribute {
	private Connection connection = null;

	private Statement stmt = null;

	private HashMap<Long, List<AttributeInterface>> entityIDAttributeListMap = new HashMap<Long, List<AttributeInterface>>();
	private HashMap<String, List<String>> entityAttributesToDelete = new HashMap<String, List<String>>();
	private HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private List<String> entityNameList = new ArrayList<String>();
	private Map<String, Long> entityIDMap = new HashMap<String, Long>();
	
	/*public static void main(String[] args) throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		Statement stmt = connection.createStatement();
		
		DeleteAttribute deleteAttribute =new DeleteAttribute(connection);
		
		List<String> deleteSQL = deleteAttribute.deleteAttribute();
		UpdateMetadataUtil.executeSQLs(deleteSQL,stmt, true);

		connection.close();
	}*/

	public List<String> deleteAttribute() throws SQLException
	{
		if(entityNameList==null || entityNameList.size()==0)
		{
			populateEntityList();
			populateAttributesToDeleteMap();
			populateAttributeDatatypeMap();
		}
		
		List<String> deleteSQL = new ArrayList<String>();
		

		populateEntityIDList();
		entityIDAttributeListMap = UpdateMetadataUtil.populateEntityAttributeMap(connection, entityIDMap);
		Set<String> keySet = entityIDMap.keySet();
		Long identifier;
		for(String  key : keySet)
		{
			identifier = entityIDMap.get(key);
			List<AttributeInterface> attibuteList = entityIDAttributeListMap.get(identifier);
			for(AttributeInterface attribute : attibuteList)
			{
				if(isAttributeToDelete(key, attribute.getName()))
				{
					
					deleteSQL.addAll(deleteAttribute(identifier, attribute));
				}
			}
		}
		return deleteSQL;
	}


	private List<String> deleteAttribute(Long identifier, AttributeInterface attribute) throws SQLException
	{
		//System.out.println("/*----- SQL Statements to delete Attribute: "+attribute.getName()+" -------*/");
		List<String> deleteSQL = new ArrayList<String>();
		String sql;
		sql = "delete from dyextn_column_properties where identifier = "+attribute.getColumnProperties().getId();
		deleteSQL.add(sql);
		
		sql = "delete from dyextn_database_properties where identifier = "+attribute.getColumnProperties().getId();
		deleteSQL.add(sql);
		
		String dataType = getDataTypeOfAttribute(attribute.getName());
		if(dataType.equals("long"))
		{
			sql = "delete from dyextn_long_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
			
			sql = "delete from dyextn_numeric_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if(dataType.equals("string"))
		{
			sql = "delete from  dyextn_string_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if(dataType.equals("object"))
		{
			sql = "delete from  dyextn_object_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if(dataType.equals("file"))
		{
			sql = "delete from  dyextn_file_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if(dataType.equals("integer"))
		{
			sql = "delete from  dyextn_integer_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
			
			sql = "delete from dyextn_numeric_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if(dataType.equals("double"))
		{
			sql = "delete from  dyextn_double_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
			
			sql = "delete from dyextn_numeric_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if(dataType.equals("boolean"))
		{
			sql = "delete from  dyextn_boolean_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
			
		}
		UpdateMetadataUtil.commonDeleteStatements(attribute, deleteSQL);
		
		return deleteSQL;	
	}

	private boolean isAttributeToDelete(String entityName, String name)
	{
		List<String> attributesTodeleteList = entityAttributesToDelete.get(entityName);
		for(String attributeName  : attributesTodeleteList)
		{
			if(attributeName.equals(name))
			{
				return true;
			}
		}
		return false;
	}

	private String getDataTypeOfAttribute(String attr) 
	{
		return attributeDatatypeMap.get(attr);
	}	
	
	private void populateAttributesToDeleteMap()
	{
		List<String> attributeToDelete =  new ArrayList<String>();
		attributeToDelete.add("positionDimensionOne");
		attributeToDelete.add("positionDimensionTwo");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.Container",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.StorageContainer",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.SpecimenArray",attributeToDelete);
		
		attributeToDelete =  new ArrayList<String>();
		attributeToDelete.add("positionDimensionOne");
		attributeToDelete.add("positionDimensionTwo");
		attributeToDelete.add("isCollectionProtocolRequirement");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.Specimen",attributeToDelete);

		attributeToDelete=new ArrayList<String>();
		attributeToDelete.add("positionDimensionOne");
		attributeToDelete.add("positionDimensionTwo");
		attributeToDelete.add("isCollectionProtocolRequirement");
		attributeToDelete.add("initialQuantity");
		attributeToDelete.add("lineage");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.CellSpecimen",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.FluidSpecimen",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.MolecularSpecimen",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.TissueSpecimen",attributeToDelete);
		
	}

	
	private void populateAttributeDatatypeMap() 
	{
		attributeDatatypeMap.put("positionDimensionOne", "integer");
		attributeDatatypeMap.put("positionDimensionTwo", "integer");
		attributeDatatypeMap.put("initialQuantity", "double");
		attributeDatatypeMap.put("isCollectionProtocolRequirement","boolean");
		attributeDatatypeMap.put("type", "string");
		attributeDatatypeMap.put("raceName", "string");
		attributeDatatypeMap.put("lineage", "string");
		
	}

	private void populateEntityList() 
	{
		entityNameList.add("edu.wustl.catissuecore.domain.Container");
		entityNameList.add("edu.wustl.catissuecore.domain.StorageContainer");
		entityNameList.add("edu.wustl.catissuecore.domain.Specimen");
		entityNameList.add("edu.wustl.catissuecore.domain.SpecimenArray");
		entityNameList.add("edu.wustl.catissuecore.domain.CellSpecimen");
		entityNameList.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		entityNameList.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityNameList.add("edu.wustl.catissuecore.domain.TissueSpecimen");	
	}

	private void populateEntityIDList() throws SQLException 
	{
		String sql;
		for(String entityName : entityNameList)
		{
			sql = "select identifier from dyextn_abstract_metadata where name='"+entityName+"'";
			ResultSet rs = executeQuery(sql);
			if (rs.next()) 
			{
				Long identifier = rs.getLong(1);
				entityIDMap.put(entityName,identifier);				
			}
		}
	}

	private ResultSet executeQuery(String sql) throws SQLException
	{
		stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

	public DeleteAttribute(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}

	
	public void setEntityAttributesToDelete(HashMap<String, List<String>> entityAttributesToDelete)
	{
		this.entityAttributesToDelete = entityAttributesToDelete;
	}

	
	public void setAttributeDatatypeMap(HashMap<String, String> attributeDatatypeMap)
	{
		this.attributeDatatypeMap = attributeDatatypeMap;
	}

	
	public void setEntityNameList(List<String> entityNameList)
	{
		this.entityNameList = entityNameList;
	}
}
