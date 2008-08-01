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
public class DeletePermissibleValueAttribute 
{
	private Connection connection = null;
	private Statement stmt = null;
	private HashMap<Long, List<AttributeInterface>> entityIDAttributeListMap = new HashMap<Long, List<AttributeInterface>>();
	private HashMap<String, List<String>> entityAttributesToDelete = new HashMap<String, List<String>>();
	private HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	private List<String> entityNameList = new ArrayList<String>();
	private Map<String, Long> entityIDMap = new HashMap<String, Long>();

	public List<String> deletePermissibleValue() throws SQLException
	{
		List<String> deleteAttributeSQL=new ArrayList<String>();
		List<String> deleteSQL;
		populateEntityList();
		populateAttributesToDeleteMap();
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
					deleteSQL = deleteAttribute(identifier, attribute);
					deleteAttributeSQL.addAll(deleteSQL);
				}
			}
		}
		return deleteAttributeSQL;
	}

	private List<String> deleteAttribute(Long identifier, AttributeInterface attribute) throws SQLException
	{
		List<String> deleteSQL = new ArrayList<String>();
		String sql;
		sql = "delete from dyextn_column_properties where identifier = "+attribute.getColumnProperties().getId();
		deleteSQL.add(sql);
		
		sql = "delete from dyextn_database_properties where identifier = "+attribute.getColumnProperties().getId();
		deleteSQL.add(sql);
		
		String dataType = getDataTypeOfAttribute(attribute.getName());
		if(dataType.equals("long"))
		{
			sql = "delete from dyextn_long_concept_value where IDENTIFIER in (select PERMISSIBLE_VALUE_ID from dyextn_userdef_de_value_rel where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de where  identifier in (select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId()+")))";
			deleteSQL.add(sql);
			
			sql = "delete from  dyextn_long_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if(dataType.equals("string"))
		{
			sql = "delete from  dyextn_string_concept_value where IDENTIFIER in (select PERMISSIBLE_VALUE_ID from dyextn_userdef_de_value_rel where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de where  identifier in (select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId()+")))";
			deleteSQL.add(sql);
			
			sql = "delete from  dyextn_string_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if(dataType.equals("integer"))
		{
			sql = "delete from  dyextn_integer_concept_value where IDENTIFIER in (select PERMISSIBLE_VALUE_ID from dyextn_userdef_de_value_rel where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de where  identifier in (select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId()+")))";
			deleteSQL.add(sql);
			
			sql = "delete from  dyextn_integer_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
			
		}
		else if(dataType.equals("double"))
		{
			sql = "delete from  dyextn_double_concept_value where IDENTIFIER in (select PERMISSIBLE_VALUE_ID from dyextn_userdef_de_value_rel where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de where  identifier in (select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId()+")))";
			deleteSQL.add(sql);
			
			sql = "delete from  dyextn_double_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
			
		}
		else if(dataType.equals("boolean"))
		{
			sql = "delete from  dyextn_boolean_concept_value where IDENTIFIER in (select PERMISSIBLE_VALUE_ID from dyextn_userdef_de_value_rel where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de where  identifier in (select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId()+")))";
			deleteSQL.add(sql);
			
			sql = "delete from  dyextn_boolean_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		
		sql = "delete from dyextn_userdef_de_value_rel where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de where  identifier in (select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId()+"))";
		deleteSQL.add(sql);
		
		sql = "delete from dyextn_userdefined_de where  identifier in (select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId()+")";
		deleteSQL.add(sql);
		
		sql = "delete from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId();
		deleteSQL.add(sql);
		
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
		attributeToDelete.add("type");
		attributeToDelete.add("pathologicalStatus");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.CellSpecimen",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.FluidSpecimen",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.MolecularSpecimen",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.TissueSpecimen",attributeToDelete);

		attributeToDelete =  new ArrayList<String>();
		attributeToDelete.add("clinicalDiagnosis");
		attributeToDelete.add("clinicalStatus");
		attributeToDelete.add("activityStatus");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup",attributeToDelete);
	}

	private void populateAttributeDatatypeMap() 
	{
		attributeDatatypeMap.put("type", "string");
		attributeDatatypeMap.put("pathologicalStatus", "string");
		
		attributeDatatypeMap.put("clinicalDiagnosis", "string");
		attributeDatatypeMap.put("clinicalStatus", "string");
		attributeDatatypeMap.put("activityStatus", "string");
	}

	private void populateEntityList() 
	{
		entityNameList.add("edu.wustl.catissuecore.domain.CellSpecimen");
		entityNameList.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		entityNameList.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityNameList.add("edu.wustl.catissuecore.domain.TissueSpecimen");
		
		entityNameList.add("edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup");
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

	public void setEntityNameList(List<String> entityNameList)
	{
		this.entityNameList = entityNameList;
	}

	public void setEntityAttributesToDelete(
			HashMap<String, List<String>> entityAttributesToDelete)
	{
		this.entityAttributesToDelete = entityAttributesToDelete;
	}

	public void setAttributeDatatypeMap(HashMap<String, String> attributeDatatypeMap)
	{
		this.attributeDatatypeMap = attributeDatatypeMap;
	}
	
	public DeletePermissibleValueAttribute(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
