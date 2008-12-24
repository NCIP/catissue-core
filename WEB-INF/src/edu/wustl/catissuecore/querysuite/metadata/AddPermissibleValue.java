package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
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
import edu.wustl.common.util.global.Constants;


public class AddPermissibleValue
{
	private Connection connection = null;
	private HashMap<Long, List<AttributeInterface>> entityIDAttributeListMap = new HashMap<Long, List<AttributeInterface>>();
	private HashMap<String, List<String>> entityAttributesToAdd = new HashMap<String, List<String>>();
	private HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private List<String> entityNameList = new ArrayList<String>();
	private Map<String, Long> entityIDMap = new HashMap<String, Long>();
	private HashMap<String, List> permissibleValueToAddMap = new HashMap<String, List>();
	
	public List<String> addPermissibleValue() throws SQLException, IOException
	{
		List<String> addAttributeSQL=new ArrayList<String>();
		List<String> addSQL;
		populateEntityList();
		populateAttributesToAddMap();
		populatePermissibleValueToAddMap();
		populateEntityIDList();
		entityIDAttributeListMap = UpdateMetadataUtil.populateEntityAttributeMap(connection, entityIDMap);
		populateAttributeDatatypeMap();
		Set<String> keySet = entityIDMap.keySet();
		Long identifier;
		for(String  key : keySet)
		{
			identifier = entityIDMap.get(key);
			List<AttributeInterface> attibuteList = entityIDAttributeListMap.get(identifier);
			for(AttributeInterface attribute : attibuteList)
			{
				if(isAttributeToAdd(key, attribute.getName()))
				{
					addSQL = addPermissibleValue(identifier, attribute);
					addAttributeSQL.addAll(addSQL);
				}
			}
		}
		return addAttributeSQL;
	}

	private List<String> addPermissibleValue(Long identifier, AttributeInterface attribute) throws SQLException, IOException
	{
		List<String> deleteSQL = new ArrayList<String>();
				
		String dataType = getDataTypeOfAttribute(attribute.getName());
		String tableName=null;
		if(dataType.equals("long"))
		{
			tableName= "dyextn_long_concept_value";
		}
		else if(dataType.equals("string"))
		{
			tableName = "dyextn_string_concept_value";
		}
		else if(dataType.equals("integer"))
		{
			tableName = "dyextn_integer_concept_value";
		}
		else if(dataType.equals("double"))
		{
			tableName = "dyextn_double_concept_value";
		}
		else if(dataType.equals("boolean"))
		{
			tableName = "dyextn_boolean_concept_value";
		}
		
		insertPermissibleValue(tableName, attribute);
		
		return deleteSQL;
	}

	private void insertPermissibleValue(String tableName, AttributeInterface attribute) throws IOException, SQLException
	{
		String sql;
		long permissibleValueId=0;
		long dataElementId=0;
		sql = "select max(IDENTIFIER) from dyextn_permissible_value";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next())
		{
			permissibleValueId = rs.getLong(1)+1;
		}
		stmt.close();
		
		sql = "select IDENTIFIER from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID = "+attribute.getAttributeTypeInformation().getDataElement().getId();
		stmt = connection.createStatement();
		rs = stmt.executeQuery(sql);
		if(rs.next())
		{
			dataElementId = rs.getLong(1);
		}
		stmt.close();
		
		if(dataElementId==0)
		{
			stmt = connection.createStatement();
			sql = "select max(IDENTIFIER) from dyextn_data_element";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				dataElementId = rs.getLong(1)+1;
			}
			stmt.close();
			
			sql = "insert into dyextn_data_element(IDENTIFIER,ATTRIBUTE_TYPE_INFO_ID,CATEGORY_ATTRIBUTE_ID) values ("+dataElementId+","+attribute.getAttributeTypeInformation().getDataElement().getId()+",null)";
			if(Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,"dyextn_data_element");
			}
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
			sql = "insert into dyextn_userdefined_de values (" +dataElementId +",1)";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}
		
		List permissibleValueList = permissibleValueToAddMap.get(attribute.getName());
		
		for(Object value : permissibleValueList)
		{
			if (value  instanceof String)
			{
				value = "'"+(String) value+"'";
			}
			sql = "insert into dyextn_permissible_value(IDENTIFIER,DESCRIPTION,ATTRIBUTE_TYPE_INFO_ID,CATEGORY_ATTRIBUTE_ID) values ("+permissibleValueId+",null,"+attribute.getAttributeTypeInformation().getDataElement().getId()+",null)";
			if(Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,"dyextn_permissible_value");
			}
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			sql = "insert into "+tableName+" values("+permissibleValueId+","+value+")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			
			sql = "insert into dyextn_userdef_de_value_rel values("+dataElementId+","+permissibleValueId+")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			
			permissibleValueId++;
		}
		
	}

	private boolean isAttributeToAdd(String entityName, String name)
	{
		List<String> attributesToAddList = entityAttributesToAdd.get(entityName);
		for(String attributeName  : attributesToAddList)
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

	private void populatePermissibleValueToAddMap()
	{
		List permissibleValueList = new ArrayList();
		permissibleValueList.add("Cell");
		permissibleValueList.add("Fluid");
		permissibleValueList.add("Molecular");
		permissibleValueList.add("Tissue");	
		permissibleValueToAddMap.put("specimenClass", permissibleValueList);
		
		permissibleValueList = new ArrayList();
		permissibleValueList.add("CPT");
		permissibleValueList.add("Paxgene");
		permissibleValueToAddMap.put("container", permissibleValueList);
	}
	
	private void populateAttributesToAddMap()
	{
		List<String> attributeToAdd =  new ArrayList<String>();
		
		attributeToAdd =  new ArrayList<String>();
		attributeToAdd.add("specimenClass");
		entityAttributesToAdd.put("edu.wustl.catissuecore.domain.AbstractSpecimen",attributeToAdd);
		
		attributeToAdd =  new ArrayList<String>();
		attributeToAdd.add("container");
		entityAttributesToAdd.put("edu.wustl.catissuecore.domain.CollectionEventParameters",attributeToAdd);
	}

	private void populateAttributeDatatypeMap() 
	{
		attributeDatatypeMap.put("specimenClass", "string");
		attributeDatatypeMap.put("container", "string");
	}

	private void populateEntityList() 
	{
		entityNameList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
		entityNameList.add("edu.wustl.catissuecore.domain.CollectionEventParameters");
	}
	
	private void populateEntityIDList() throws SQLException 
	{
		String sql;
		for(String entityName : entityNameList)
		{
			sql = "select identifier from dyextn_abstract_metadata where name "+UpdateMetadataUtil.getDBCompareModifier()+"'"+entityName+"'";
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
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

	
	
	public AddPermissibleValue(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
	}
}
