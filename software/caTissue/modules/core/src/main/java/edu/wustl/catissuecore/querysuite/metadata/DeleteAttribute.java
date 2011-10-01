
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
 *
 * @author deepti_shelar
 *
 */
public class DeleteAttribute extends DeleteBaseMetadata
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
	 * This method deletes Attribute.
	 * @return deleteSQL
	 * @throws SQLException SQL Exception
	 */
	public List<String> deleteAttribute() throws SQLException
	{
		if (this.entityNameList == null || this.entityNameList.size() == 0)
		{
			this.populateEntityList();
			this.populateAttributesToDeleteMap();
			this.populateAttributeDatatypeMap();
		}

		final List<String> deleteSQL = new ArrayList<String>();

		this.populateEntityIDList();
		this.entityIDAttributeListMap = UpdateMetadataUtil.populateEntityAttributeMap(
				this.connection, this.entityIDMap);
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

					deleteSQL.addAll(this.deleteAttribute(identifier, attribute));
				}
			}
		}
		return deleteSQL;
	}

	/**
	 * This method deletes Attribute.
	 * @param identifier identifier
	 * @param attribute AttributeInterface object
	 * @return deleteSQL
	 * @throws SQLException SQL Exception
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
			sql = "delete from dyextn_long_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);

			sql = "delete from dyextn_numeric_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if (dataType.equals("string"))
		{
			sql = "delete from  dyextn_string_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if (dataType.equals("object"))
		{
			sql = "delete from  dyextn_object_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if (dataType.equals("file"))
		{
			sql = "delete from  dyextn_file_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if (dataType.equals("integer"))
		{
			sql = "delete from  dyextn_integer_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);

			sql = "delete from dyextn_numeric_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if (dataType.equals("double"))
		{
			sql = "delete from  dyextn_double_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);

			sql = "delete from dyextn_numeric_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);
		}
		else if (dataType.equals("boolean"))
		{
			sql = "delete from  dyextn_boolean_type_info where identifier = "
					+ attribute.getAttributeTypeInformation().getDataElement().getId();
			deleteSQL.add(sql);

		}
		UpdateMetadataUtil.commonDeleteStatements(attribute, deleteSQL);

		return deleteSQL;
	}

	/**
	 * This method checks is Attribute To Delete.
	 * @param entityName entity Name
	 * @param name name
	 * @return true or false.
	 */
	public boolean isAttributeToDelete(String entityName, String name)
	{
		boolean isDelete = false;
		final List<String> attributesTodeleteList = this.entityAttributesToDelete.get(entityName);
		for (final String attributeName : attributesTodeleteList)
		{
			if (attributeName.equals(name))
			{
				isDelete = true;
			}
		}
		return isDelete;
	}

	/**
	 * This method gets Data Type Of Attribute.
	 * @param attr attribute
	 * @return Data type
	 */
	public String getDataTypeOfAttribute(String attr)
	{
		return this.attributeDatatypeMap.get(attr);
	}

	/**
	 * This method populates Attributes To Delete Map.
	 */
	private void populateAttributesToDeleteMap()
	{
		List<String> attributeToDelete = new ArrayList<String>();
		attributeToDelete.add("positionDimensionOne");
		attributeToDelete.add("positionDimensionTwo");
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.Container",
				attributeToDelete);
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.StorageContainer",
				attributeToDelete);
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.SpecimenArray",
				attributeToDelete);

		attributeToDelete = new ArrayList<String>();
		attributeToDelete.add("positionDimensionOne");
		attributeToDelete.add("positionDimensionTwo");
		attributeToDelete.add("isCollectionProtocolRequirement");
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.Specimen",
				attributeToDelete);

		attributeToDelete = new ArrayList<String>();
		attributeToDelete.add("positionDimensionOne");
		attributeToDelete.add("positionDimensionTwo");
		attributeToDelete.add("isCollectionProtocolRequirement");
		attributeToDelete.add("initialQuantity");
		attributeToDelete.add("lineage");
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.CellSpecimen",
				attributeToDelete);
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.FluidSpecimen",
				attributeToDelete);
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.MolecularSpecimen",
				attributeToDelete);
		this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.TissueSpecimen",
				attributeToDelete);

	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("positionDimensionOne", "integer");
		this.attributeDatatypeMap.put("positionDimensionTwo", "integer");
		this.attributeDatatypeMap.put("initialQuantity", "double");
		this.attributeDatatypeMap.put("isCollectionProtocolRequirement", "boolean");
		this.attributeDatatypeMap.put("type", "string");
		this.attributeDatatypeMap.put("raceName", "string");
		this.attributeDatatypeMap.put("lineage", "string");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityNameList.add("edu.wustl.catissuecore.domain.Container");
		this.entityNameList.add("edu.wustl.catissuecore.domain.StorageContainer");
		this.entityNameList.add("edu.wustl.catissuecore.domain.Specimen");
		this.entityNameList.add("edu.wustl.catissuecore.domain.SpecimenArray");
		this.entityNameList.add("edu.wustl.catissuecore.domain.CellSpecimen");
		this.entityNameList.add("edu.wustl.catissuecore.domain.FluidSpecimen");
		this.entityNameList.add("edu.wustl.catissuecore.domain.MolecularSpecimen");
		this.entityNameList.add("edu.wustl.catissuecore.domain.TissueSpecimen");
	}

	/**
	 * This method populates Entity ID List.
	 * @throws SQLException SQL Exception
	 */
	public void populateEntityIDList() throws SQLException
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
	 * @param sql query
	 * @return ResultSet
	 * @throws SQLException SQL Exception
	 */
	public ResultSet executeQuery(String sql) throws SQLException
	{
		this.stmt = this.connection.createStatement();
		final ResultSet rs = this.stmt.executeQuery(sql);
		return rs;
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public DeleteAttribute(Connection connection) throws SQLException
	{
		this.connection = connection;
		this.stmt = connection.createStatement();
	}

	/**
	 * This method sets Entity Attributes To Delete.
	 * @param entityAttributesToDelete entity Attributes To Delete.
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
	 * This method sets Entity Name List.
	 * @param entityNameList entity Name List.
	 */
	public void setEntityNameList(List<String> entityNameList)
	{
		this.entityNameList = entityNameList;
	}
}
