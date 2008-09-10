package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * 
 * @author deepti_shelar
 *
 */
public class DeletePermissibleValue extends DeleteBaseMetadata
{
	private Connection connection = null;
	private HashMap<String, Integer> numberOfOccurenceToDeleteMap = new HashMap<String, Integer>();
	private List<String> permissibleValueToDeleteList = new ArrayList<String>();

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
		if(permissibleValueToDeleteList.contains(name) && numberOfOccurenceToDeleteMap.get(name+"_"+string)!=null && numberOfOccurenceToDeleteMap.get(name+"_"+string) > 0)
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
		numberOfOccurenceToDeleteMap.put("tissueSite_ACCESSORY SINUSES", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_ADRENAL GLAND", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_ANUS AND ANAL CANAL", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_BASE OF TONGUE", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_BLADDER", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_BONES, JOINTS AND ARTICULAR CARTILAGE", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_BONES, JOINTS AND ARTICULAR CARTILAGE OF LIMBS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_BONES, JOINTS AND ARTICULAR CARTILAGE OF OTHER AND UNSPECIFIED SITES", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_BRAIN", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_BREAST", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_BRONCHUS AND LUNG", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_CERVIX UTERI", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_COLON", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_CONNECTIVE, SUBCUTANEOUS AND OTHER SOFT TISSUES", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_CORPUS UTERI", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_DIGESTIVE ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_ESOPHAGUS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_EYE AND ADNEXA", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_EYE, BRAIN AND OTHER PARTS OF CENTRAL NERVOUS SYSTEM", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_FEMALE GENITAL ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_FLOOR OF MOUTH", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_GALLBLADDER", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_GUM", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_HEART, MEDIASTINUM, AND PLEURA", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_HEMATOPOIETIC AND RETICULOENDOTHELIAL SYSTEMS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_KIDNEY", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_LARYNX", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_LIP", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_LIP, ORAL CAVITY AND PHARYNX", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_LIVER AND INTRAHEPATIC BILE DUCTS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_LYMPH NODES", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_MALE GENITAL ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_MENINGES", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_NASAL CAVITY AND MIDDLE EAR", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_NASOPHARYNX", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OROPHARYNX", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND ILL-DEFINED DIGESTIVE ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND ILL-DEFINED SITES", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND ILL-DEFINED SITES IN LIP, ORAL CAVITY AND PHARYNX", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND ILL-DEFINED SITES WITHIN RESPIRATORY SYSTEM AND INTRATHORACIC ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED FEMALE GENITAL ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED MAJOR SALIVARY GLANDS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED MALE GENITAL ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED PARTS OF BILIARY TRACT", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED PARTS OF MOUTH", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED PARTS OF TONGUE", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED URINARY ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OTHER ENDOCRINE GLANDS AND RELATED STRUCTURES", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_OVARY", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_PALATE", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_PANCREAS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_PAROTID GLAND", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_PENIS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_PERIPHERAL NERVES AND AUTONOMIC NERVOUS SYSTEM", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_PLACENTA", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_PROSTATE GLAND", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_PYRIFORM SINUS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_RECTOSIGMOID JUNCTION", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_RECTUM", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_RENAL PELVIS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_RESPIRATORY SYSTEM AND INTRATHORACIC ORGANS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_RETROPERITONEUM AND PERITONEUM", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_SKIN", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_SMALL INTESTINE", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_SPINAL CORD, CRANIAL NERVES, AND OTHER PARTS OF CENTRAL NERVOUS SYSTEM", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_STOMACH", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_TESTIS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_THYROID AND OTHER ENDOCRINE GLANDS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_THYROID GLAND", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_THYMUS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_TONSIL", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_TRACHEA", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_UNKNOWN PRIMARY SITE", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_URETER", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_URINARY TRACT", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_UTERUS, NOS", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_VAGINA", 1);
		numberOfOccurenceToDeleteMap.put("tissueSite_VULVA", 1);
		
		numberOfOccurenceToDeleteMap.put("specimenType_Not Specified", 3);
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException
	{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3307/upgrade";
		Connection connection = DriverManager.getConnection(url, "root", "pspl");
		UpdateMetadataUtil.isExecuteStatement = true;
		
		DeletePermissibleValue deletePermissibleValue = new DeletePermissibleValue(connection);
		List<String> arraList = deletePermissibleValue.deletePermissibleValue();
		for(String sql : arraList)
		{
			System.out.println(sql);
		}
		
		UpdateMetadataUtil.metadataSQLFile.close();
		UpdateMetadataUtil.failureWriter.close();
	}
	
	private void populatePermissibleValueToDeleteMap()
	{
		permissibleValueToDeleteList.add("tissueSite");
		permissibleValueToDeleteList.add("specimenType");
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
		this.connection = connection;
	}
}
