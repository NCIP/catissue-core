package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public class DeleteAttributeForEDIN extends DeleteAttribute {
	
	private Connection connection = null;
	private Statement stmt = null;
	
	
	public DeleteAttributeForEDIN(Connection connection) throws SQLException
	{
	    super(connection);
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
	
	public List<String> deleteAttribute() throws SQLException
	{
		if(entityNameList==null || entityNameList.size()==0)
		{
			entityNameList.add("edu.wustl.catissuecore.domain.Participant");	
			
			//Added by geeta : Attribute delete for Edinburgh
			List<String>  attributeToDelete=new ArrayList<String>();
			attributeToDelete.add("socialSecurityNumber");
			attributeToDelete.add("sexGenotype");
			attributeToDelete.add("ethnicity");
			
			entityAttributesToDelete.put("edu.wustl.catissuecore.domain.Participant",attributeToDelete);
			
			//Added by geeta for edin burgh
			attributeDatatypeMap.put("socialSecurityNumber", "string");
		    attributeDatatypeMap.put("sexGenotype", "string");
		    attributeDatatypeMap.put("ethnicity", "string");
									 
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
		
		stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("select identifier from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="+attribute.getAttributeTypeInformation().getDataElement().getId());
        if(rs!=null){
        	
        	if(rs.next())
        	{
        		identifier = rs.getLong(1);
        		System.out.println(" id found  " + identifier );
        		
        		sql = "delete from dyextn_userdef_de_value_rel where USER_DEF_DE_ID="+identifier;
        		deleteSQL.add(sql);
		
        		sql = "delete from dyextn_userdefined_de where IDENTIFIER="+identifier;
        		deleteSQL.add(sql);
		
        		sql = "delete from  dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID = "+attribute.getAttributeTypeInformation().getDataElement().getId();
        		deleteSQL.add(sql);	
        	}
        }
		
		sql="delete from dyextn_column_properties where PRIMITIVE_ATTRIBUTE_ID="+attribute.getId();
		deleteSQL.add(sql);
		
		UpdateMetadataUtil.commonDeleteStatements(attribute, deleteSQL);
		
		return deleteSQL;	
	}
}
