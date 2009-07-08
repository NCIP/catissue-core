
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
 * @author deepti_shelar
 *
 */
public class DeletePermissibleValue extends DeleteBaseMetadata
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Specify number Of Occurence To Delete Map.
	 */
	private final HashMap<String, Integer> numberOfOccurenceToDeleteMap = new HashMap<String, Integer>();
	/**
	 * Specify permissible Value To Delete List.
	 */
	private final List<String> permissibleValueToDeleteList = new ArrayList<String>();

	/**
	 * This method gets deletePermissibleValue list.
	 * @return deleteAttributeSQL
	 * @throws SQLException SQL Exception
	 */
	public List<String> deletePermissibleValue() throws SQLException
	{
		final List<String> deleteAttributeSQL = new ArrayList<String>();
		List<String> deleteSQL;
		this.populateEntityList();
		this.populateAttributesToDeleteMap();
		this.populatePermissibleValueToDeleteMap();
		this.populateNumberOfOccurenceToDeleteMap();
		this.populateEntityIDList();
		this.entityIDAttributeListMap = UpdateMetadataUtil.populateEntityAttributeMap(
				this.connection, this.entityIDMap);
		this.populateAttributeDatatypeMap();
		final Set<String> keySet = this.entityIDMap.keySet();
		Long identifier;
		for (final String key : keySet)
		{
			identifier = this.entityIDMap.get(key);
			final List<AttributeInterface> attibuteList = this.entityIDAttributeListMap
					.get(identifier);
			for (final AttributeInterface attribute : attibuteList)
			{
				if (this.isAttributeToDelete(key, attribute.getName()))
				{
					deleteSQL = this.deleteAttributeValue(identifier, attribute);
					deleteAttributeSQL.addAll(deleteSQL);
				}
			}
		}
		return deleteAttributeSQL;
	}

	/**
	 * This method gets delete Attribute Value list.
	 * @param identifier identifier
	 * @param attribute attribute
	 * @return deleteSQL.
	 * @throws SQLException SQL Exception
	 */
	private List<String> deleteAttributeValue(Long identifier, AttributeInterface attribute)
			throws SQLException
	{
		final List<String> deleteSQL = new ArrayList<String>();
		String sql;

		final String dataType = this.getDataTypeOfAttribute(attribute.getName());
		if (dataType.equals("string"))
		{
			sql = "select * from dyextn_string_concept_value" +
					" where IDENTIFIER in (select PERMISSIBLE_VALUE_ID" +
					" from dyextn_userdef_de_value_rel where USER_DEF_DE_ID" +
					" in (select identifier from dyextn_userdefined_de" +
					" where  identifier in (select identifier from dyextn_data_element" +
					" where ATTRIBUTE_TYPE_INFO_ID="
					+ attribute.getAttributeTypeInformation().getDataElement().getId() + ")))";
			final Statement stmt = this.connection.createStatement();
			final ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				if (this.isValueToDelete(rs.getString(2), attribute.getName()))
				{
					sql = "delete from dyextn_string_concept_value where IDENTIFIER ="
							+ rs.getLong(1);
					deleteSQL.add(sql);

					sql = "delete from dyextn_userdef_de_value_rel where PERMISSIBLE_VALUE_ID="
							+ rs.getLong(1);
					deleteSQL.add(sql);
					sql = "delete from dyextn_semantic_property where ABSTRACT_VALUE_ID="
							+ rs.getLong(1);
					deleteSQL.add(sql);
					sql = "delete from dyextn_permissible_value where IDENTIFIER="
						+ rs.getLong(1);
					deleteSQL.add(sql);

				}
			}
		}
		return deleteSQL;
	}

	/**
	 * This method checks is Value To Delete.
	 * @param string string
	 * @param name name
	 * @return true or false.
	 */
	private boolean isValueToDelete(String string, String name)
	{
		if (this.permissibleValueToDeleteList.contains(name)
				&& this.numberOfOccurenceToDeleteMap.get(name + "_" + string) != null
				&& this.numberOfOccurenceToDeleteMap.get(name + "_" + string) > 0)
		{
			int temp = this.numberOfOccurenceToDeleteMap.get(name + "_" + string);
			temp--;
			this.numberOfOccurenceToDeleteMap.put(name + "_" + string, temp--);
			return true;
		}
		return false;
	}

	/**
	 * This method checks is Attribute To Delete.
	 * @param entityName entity Name
	 * @param name name
	 * @return true or false.
	 */
	private boolean isAttributeToDelete(String entityName, String name)
	{
		final List<String> attributesTodeleteList = this.entityAttributesToDelete.get(entityName);
		for (final String attributeName : attributesTodeleteList)
		{
			if (attributeName.equals(name))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * This method gets Data Type Of Attribute.
	 * @param attr Attribute
	 * @return Data Type
	 */
	private String getDataTypeOfAttribute(String attr)
	{
		return this.attributeDatatypeMap.get(attr);
	}

	/**
	 * This method populates Number Of Occurrence To Delete Map.
	 */
	private void populateNumberOfOccurenceToDeleteMap()
	{
		this.numberOfOccurenceToDeleteMap.put("tissueSite_ACCESSORY SINUSES", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_ADRENAL GLAND", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_ANUS AND ANAL CANAL", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_BASE OF TONGUE", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_BLADDER", 1);
		this.numberOfOccurenceToDeleteMap
				.put("tissueSite_BONES, JOINTS AND ARTICULAR CARTILAGE", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_BONES, JOINTS AND ARTICULAR CARTILAGE OF LIMBS", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_BONES, JOINTS AND ARTICULAR CARTILAGE OF OTHER AND UNSPECIFIED SITES",
				1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_BRAIN", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_BREAST", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_BRONCHUS AND LUNG", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_CERVIX UTERI", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_COLON", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_CONNECTIVE, SUBCUTANEOUS AND OTHER SOFT TISSUES", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_CORPUS UTERI", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_DIGESTIVE ORGANS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_ESOPHAGUS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_EYE AND ADNEXA", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_EYE, BRAIN AND OTHER PARTS OF CENTRAL NERVOUS SYSTEM", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_FEMALE GENITAL ORGANS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_FLOOR OF MOUTH", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_GALLBLADDER", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_GUM", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_HEART, MEDIASTINUM, AND PLEURA", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_HEMATOPOIETIC AND RETICULOENDOTHELIAL SYSTEMS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_KIDNEY", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_LARYNX", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_LIP", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_LIP, ORAL CAVITY AND PHARYNX", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_LIVER AND INTRAHEPATIC BILE DUCTS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_LYMPH NODES", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_MALE GENITAL ORGANS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_MENINGES", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_NASAL CAVITY AND MIDDLE EAR", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_NASOPHARYNX", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_OROPHARYNX", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND ILL-DEFINED DIGESTIVE ORGANS",
				1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND ILL-DEFINED SITES", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_OTHER AND ILL-DEFINED SITES IN LIP, ORAL CAVITY AND PHARYNX", 1);
		this.numberOfOccurenceToDeleteMap
				.put(
					"tissueSite_OTHER AND ILL-DEFINED SITES WITHIN RESPIRATORY SYSTEM" +
						" AND INTRATHORACIC ORGANS",
						1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_OTHER AND UNSPECIFIED FEMALE GENITAL ORGANS", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_OTHER AND UNSPECIFIED MAJOR SALIVARY GLANDS", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_OTHER AND UNSPECIFIED MALE GENITAL ORGANS", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_OTHER AND UNSPECIFIED PARTS OF BILIARY TRACT", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED PARTS OF MOUTH", 1);
		this.numberOfOccurenceToDeleteMap
				.put("tissueSite_OTHER AND UNSPECIFIED PARTS OF TONGUE", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_OTHER AND UNSPECIFIED URINARY ORGANS", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_OTHER ENDOCRINE GLANDS AND RELATED STRUCTURES", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_OVARY", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_PALATE", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_PANCREAS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_PAROTID GLAND", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_PENIS", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_PERIPHERAL NERVES AND AUTONOMIC NERVOUS SYSTEM", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_PLACENTA", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_PROSTATE GLAND", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_PYRIFORM SINUS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_RECTOSIGMOID JUNCTION", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_RECTUM", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_RENAL PELVIS", 1);
		this.numberOfOccurenceToDeleteMap.put(
				"tissueSite_RESPIRATORY SYSTEM AND INTRATHORACIC ORGANS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_RETROPERITONEUM AND PERITONEUM", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_SKIN", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_SMALL INTESTINE", 1);
		this.numberOfOccurenceToDeleteMap
				.put(
				"tissueSite_SPINAL CORD, CRANIAL NERVES," +
				" AND OTHER PARTS OF CENTRAL NERVOUS SYSTEM",
						1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_STOMACH", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_TESTIS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_THYROID AND OTHER ENDOCRINE GLANDS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_THYROID GLAND", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_THYMUS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_TONSIL", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_TRACHEA", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_UNKNOWN PRIMARY SITE", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_URETER", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_URINARY TRACT", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_UTERUS, NOS", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_VAGINA", 1);
		this.numberOfOccurenceToDeleteMap.put("tissueSite_VULVA", 1);

		this.numberOfOccurenceToDeleteMap.put("specimenType_Not Specified", 3);
	}

	/**
	 * Main method.
	 * @param args arguments.
	 * @throws SQLException SQL Exception
	 * @throws ClassNotFoundException Class Not Found Exception
	 * @throws IOException IO Exception
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException
	{
		Class.forName("com.mysql.jdbc.Driver");
		final String url = "jdbc:mysql://localhost:3307/upgrade";
		final Connection connection = DriverManager.getConnection(url, "root", "pspl");
		UpdateMetadataUtil.isExecuteStatement = true;

		final DeletePermissibleValue deletePermissibleValue = new DeletePermissibleValue(connection);
		final List<String> arraList = deletePermissibleValue.deletePermissibleValue();
		for (final String sql : arraList)
		{
			System.out.println(sql);
		}

		UpdateMetadataUtil.metadataSQLFile.close();
		UpdateMetadataUtil.failureWriter.close();
	}

	/**
	 * This method populates Permissible Value To Delete Map.
	 */
	private void populatePermissibleValueToDeleteMap()
	{
		this.permissibleValueToDeleteList.add("tissueSite");
		this.permissibleValueToDeleteList.add("specimenType");
	}

	/**
	 * This method populates Attributes To Delete Map.
	 */
	private void populateAttributesToDeleteMap()
	{
		List<String> attributeToDelete = new ArrayList<String>();

		attributeToDelete.add("tissueSite");
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.SpecimenCharacteristics",
				attributeToDelete);

		attributeToDelete = new ArrayList<String>();
		attributeToDelete.add("specimenType");
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.AbstractSpecimen",
				attributeToDelete);
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("tissueSite", "string");
		this.attributeDatatypeMap.put("specimenType", "string");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityNameList.add("edu.wustl.catissuecore.domain.SpecimenCharacteristics");
		this.entityNameList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
	}

	/**
	 * This method populates Entity ID List.
	 * @throws SQLException SQL Exception
	 */
	private void populateEntityIDList() throws SQLException
	{
		String sql;
		for (final String entityName : this.entityNameList)
		{
			sql = "select identifier from dyextn_abstract_metadata where name "
					+ UpdateMetadataUtil.getDBCompareModifier() + "'" + entityName + "'";
			final ResultSet rs = this.executeQuery(sql);
			if (rs.next())
			{
				final Long identifier = rs.getLong(1);
				this.entityIDMap.put(entityName, identifier);
			}
		}
	}

	/**
	 * This method executes Query.
	 * @param sql Query
	 * @return ResultSet
	 * @throws SQLException SQL Exception
	 */
	private ResultSet executeQuery(String sql) throws SQLException
	{
		final Statement stmt = this.connection.createStatement();
		final ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public DeletePermissibleValue(Connection connection) throws SQLException
	{
		this.connection = connection;
	}
}
