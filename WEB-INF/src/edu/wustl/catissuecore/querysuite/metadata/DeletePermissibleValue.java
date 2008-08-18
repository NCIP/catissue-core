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
public class DeletePermissibleValue 
{
	private Connection connection = null;
	private HashMap<Long, List<AttributeInterface>> entityIDAttributeListMap = new HashMap<Long, List<AttributeInterface>>();
	private HashMap<String, List<String>> entityAttributesToDelete = new HashMap<String, List<String>>();
	private HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private List<String> entityNameList = new ArrayList<String>();
	private Map<String, Long> entityIDMap = new HashMap<String, Long>();
	private HashMap<String, String> permissibleValueToDeleteMap = new HashMap<String, String>();
	private HashMap<String, Integer> numberOfOccurenceToDeleteMap = new HashMap<String, Integer>();

	
	public List<String> deletePermissibleValue() throws SQLException
	{
		List<String> deleteAttributeSQL=new ArrayList<String>();
		List<String> deleteSQL;
		populateEntityList();
		populateAttributesToDeleteMap();
		populatePermissibleValueToDeleteMap();
		populateNumberOfOccurenceToDeleteMap();
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
				if(isAttributeToDelete(key, attribute.getName()))
				{
					deleteSQL = deleteAttributeValue(identifier, attribute);
					deleteAttributeSQL.addAll(deleteSQL);
				}
			}
		}
		return deleteAttributeSQL;
	}

	private List<String> deleteAttributeValue(Long identifier, AttributeInterface attribute) throws SQLException
	{
		List<String> deleteSQL = new ArrayList<String>();
		String sql;
		
		String dataType = getDataTypeOfAttribute(attribute.getName());
		if(dataType.equals("string"))
		{
			sql = "select * from dyextn_string_concept_value where IDENTIFIER in (select PERMISSIBLE_VALUE_ID from dyextn_userdef_de_value_rel where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de where  identifier in (select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId()+")))";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				if(isValueToDelete(rs.getString(2), attribute.getName()))
				{
					sql = "delete from dyextn_string_concept_value where IDENTIFIER =" + rs.getLong(1);
					deleteSQL.add(sql);
					
					sql = "delete from dyextn_userdef_de_value_rel where PERMISSIBLE_VALUE_ID="+rs.getLong(1);
					deleteSQL.add(sql);
					sql = "delete from dyextn_semantic_property where ABSTRACT_VALUE_ID="+rs.getLong(1);
					deleteSQL.add(sql);
					sql = "delete from dyextn_permissible_value where IDENTIFIER="+rs.getLong(1);
					deleteSQL.add(sql);
					
				}
			}
		}
		return deleteSQL;
	}

	private boolean isValueToDelete(String string, String name)
	{
		if(permissibleValueToDeleteMap.get(name).equals(string) && numberOfOccurenceToDeleteMap.get(name+"_"+string) > 0)
		{
			int temp = numberOfOccurenceToDeleteMap.get(name+"_"+string);
			temp--;
			numberOfOccurenceToDeleteMap.put(name+"_"+string, temp--);
			return true;
		}
		return false;
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

	private void populateNumberOfOccurenceToDeleteMap()
	{
		numberOfOccurenceToDeleteMap.put("tissueSite_Thyroid gland", 1);
		numberOfOccurenceToDeleteMap.put("specimenType_Not Specified", 3);
	}

	
	private void populatePermissibleValueToDeleteMap()
	{
		permissibleValueToDeleteMap.put("tissueSite", "Thyroid gland");
		permissibleValueToDeleteMap.put("specimenType", "Not Specified");
	}
	
	private void populateAttributesToDeleteMap()
	{
		List<String> attributeToDelete =  new ArrayList<String>();
		
		attributeToDelete.add("tissueSite");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.SpecimenCharacteristics",attributeToDelete);
		
		attributeToDelete =  new ArrayList<String>();
		attributeToDelete.add("specimenType");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.AbstractSpecimen",attributeToDelete);
	}

	private void populateAttributeDatatypeMap() 
	{
		attributeDatatypeMap.put("tissueSite", "string");
		attributeDatatypeMap.put("specimenType", "string");
	}

	private void populateEntityList() 
	{
		entityNameList.add("edu.wustl.catissuecore.domain.SpecimenCharacteristics");
		entityNameList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
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
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}
	
	public DeletePermissibleValue(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
	}
}
