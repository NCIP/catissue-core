
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

/**
 * @author deepti_shelar
 *
 */
public class DeletePermissibleValueAttribute extends DeleteBaseMetadata
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Statement instance.
	 */
	private Statement stmt = null;

	/**
	 * This method gets delete Permissible Value list.
	 * @return deleteAttributeSQL.
	 * @throws SQLException SQL Exception
	 */
	public List<String> deletePermissibleValue() throws SQLException
	{
		final List<String> deleteAttributeSQL = new ArrayList<String>();
		List<String> deleteSQL;
		this.populateEntityList();
		this.populateAttributesToDeleteMap();
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
					deleteSQL = this.deleteAttribute(identifier, attribute);
					deleteAttributeSQL.addAll(deleteSQL);
				}
			}
		}
		return deleteAttributeSQL;
	}

	/**
	 * This method gets delete Attribute list.
	 * @param identifier identifier
	 * @param attribute attribute
	 * @return deleteSQL
	 * @throws SQLException SQLException
	 */
	private List<String> deleteAttribute(Long identifier, AttributeInterface attribute)
			throws SQLException
	{
		final List<String> deleteSQL = new ArrayList<String>();
		String sql;
		sql = "delete from dyextn_column_properties where identifier = "
				+ attribute.getColumnProperties().getId();
		deleteSQL.add(sql);

		sql = "delete from dyextn_database_properties where identifier = "
				+ attribute.getColumnProperties().getId();
		deleteSQL.add(sql);

		final String dataType = this.getDataTypeOfAttribute(attribute.getName());
		if (dataType.equals("long"))
		{
			sql = "delete from dyextn_long_concept_value"
					+ " where IDENTIFIER in (select PERMISSIBLE_VALUE_ID"
					+ " from dyextn_userdef_de_value_rel where USER_DEF_DE_ID"
					+ " in (select identifier from dyextn_userdefined_de"
					+ " where  identifier in (select identifier"
					+ " from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="
					+ attribute.getAttributeTypeInformation().getDataElement().getId() + ")))";
			deleteSQL.add(sql);

			sql = "delete from  dyextn_long_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if (dataType.equals("string"))
		{
			sql = "delete from  dyextn_string_concept_value"
					+ " where IDENTIFIER in (select PERMISSIBLE_VALUE_ID"
					+ " from dyextn_userdef_de_value_rel where USER_DEF_DE_ID"
					+ " in (select identifier from dyextn_userdefined_de where"
					+ "  identifier in (select identifier from dyextn_data_element"
					+ " where ATTRIBUTE_TYPE_INFO_ID="
					+ attribute.getAttributeTypeInformation().getDataElement().getId() + ")))";
			deleteSQL.add(sql);

			sql = "delete from  dyextn_string_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if (dataType.equals("integer"))
		{
			sql = "delete from  dyextn_integer_concept_value where IDENTIFIER"
					+ " in (select PERMISSIBLE_VALUE_ID" + " from dyextn_userdef_de_value_rel"
					+ " where USER_DEF_DE_ID in (select identifier"
					+ " from dyextn_userdefined_de where  identifier"
					+ " in (select identifier from dyextn_data_element"
					+ " where ATTRIBUTE_TYPE_INFO_ID="
					+ attribute.getAttributeTypeInformation().getDataElement().getId() + ")))";
			deleteSQL.add(sql);

			sql = "delete from  dyextn_integer_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);

		}
		else if (dataType.equals("double"))
		{
			sql = "delete from  dyextn_double_concept_value where IDENTIFIER"
					+ " in (select PERMISSIBLE_VALUE_ID" + " from dyextn_userdef_de_value_rel"
					+ " where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de"
					+ " where  identifier in (select identifier from dyextn_data_element"
					+ " where ATTRIBUTE_TYPE_INFO_ID="
					+ attribute.getAttributeTypeInformation().getDataElement().getId() + ")))";
			deleteSQL.add(sql);

			sql = "delete from  dyextn_double_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);

		}
		else if (dataType.equals("boolean"))
		{
			sql = "delete from  dyextn_boolean_concept_value where IDENTIFIER"
					+ " in (select PERMISSIBLE_VALUE_ID from dyextn_userdef_de_value_rel"
					+ " where USER_DEF_DE_ID in (select identifier from dyextn_userdefined_de"
					+ " where  identifier in (select identifier from dyextn_data_element"
					+ " where ATTRIBUTE_TYPE_INFO_ID="
					+ attribute.getAttributeTypeInformation().getDataElement().getId() + ")))";
			deleteSQL.add(sql);

			sql = "delete from  dyextn_boolean_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}

		sql = "delete from dyextn_userdef_de_value_rel where USER_DEF_DE_ID"
				+ " in (select identifier from dyextn_userdefined_de where"
				+ "  identifier in (select identifier from dyextn_data_element"
				+ " where ATTRIBUTE_TYPE_INFO_ID="
				+ attribute.getAttributeTypeInformation().getDataElement().getId() + "))";
		deleteSQL.add(sql);

		sql = "delete from dyextn_userdefined_de where  identifier"
				+ " in (select identifier from dyextn_data_element"
				+ " where ATTRIBUTE_TYPE_INFO_ID="
				+ attribute.getAttributeTypeInformation().getDataElement().getId() + ")";
		deleteSQL.add(sql);

		sql = "delete from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID="
				+ attribute.getAttributeTypeInformation().getDataElement().getId();
		deleteSQL.add(sql);

		UpdateMetadataUtil.commonDeleteStatements(attribute, deleteSQL);

		return deleteSQL;
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
	 * This method populates Attributes To Delete Map.
	 */
	private void populateAttributesToDeleteMap()
	{
		List<String> attributeToDelete = new ArrayList<String>();
		attributeToDelete.add("type");
		attributeToDelete.add("pathologicalStatus");
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.CellSpecimen",
				attributeToDelete);
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.FluidSpecimen",
				attributeToDelete);
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.MolecularSpecimen",
				attributeToDelete);
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.TissueSpecimen",
				attributeToDelete);

		attributeToDelete = new ArrayList<String>();
		attributeToDelete.add("clinicalDiagnosis");
		attributeToDelete.add("clinicalStatus");
		attributeToDelete.add("activityStatus");
		this.entityAttributesToDelete.put(
				"edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup",
				attributeToDelete);

		attributeToDelete = new ArrayList<String>();
		attributeToDelete.add("clinicalDiagnosis");
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.SpecimenCollectionGroup",
				attributeToDelete);
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("type", "string");
		this.attributeDatatypeMap.put("pathologicalStatus", "string");

		this.attributeDatatypeMap.put("clinicalDiagnosis", "string");
		this.attributeDatatypeMap.put("clinicalStatus", "string");
		this.attributeDatatypeMap.put("activityStatus", "string");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityNameList.add("edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityNameList.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityNameList.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityNameList.add("edu.wustl.catissuecore.domain.TissueSpecimen");

		this.entityNameList.add("edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup");

		this.entityNameList.add("edu.wustl.catissuecore.domain.SpecimenCollectionGroup");
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
	 * @throws SQLException SQL Exception
	 * @return ResultSet.
	 */
	private ResultSet executeQuery(String sql) throws SQLException
	{
		this.stmt = this.connection.createStatement();
		final ResultSet rs = this.stmt.executeQuery(sql);
		return rs;
	}

	/**
	 * This method sets Entity Name List.
	 * @param entityNameList entity Name List
	 */
	public void setEntityNameList(List<String> entityNameList)
	{
		this.entityNameList = entityNameList;
	}

	/**
	 * This method sets Entity Attributes To Delete.
	 * @param entityAttributesToDelete entity Attributes To Delete
	 */
	public void setEntityAttributesToDelete(HashMap<String, List<String>> entityAttributesToDelete)
	{
		this.entityAttributesToDelete = entityAttributesToDelete;
	}

	/**
	 * This method sets Attribute Data type Map.
	 * @param attributeDatatypeMap attribute Data type Map
	 */
	public void setAttributeDatatypeMap(HashMap<String, String> attributeDatatypeMap)
	{
		this.attributeDatatypeMap = attributeDatatypeMap;
	}

	/**
	 * Constructor.
	 * @param connection Connection object
	 * @throws SQLException SQL Exception
	 */
	public DeletePermissibleValueAttribute(Connection connection) throws SQLException
	{
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
