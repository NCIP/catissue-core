
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
import edu.wustl.catissuecore.util.global.Constants;

/**
 * AddPermissibleValue.
 *
 */
public class AddPermissibleValue
{

	/**
	 * Specify Connection object.
	 */
	private Connection connection = null;
	/**
	 * Specify entity ID Attribute List Map.
	 */
	private HashMap<Long, List<AttributeInterface>> entityIDAttributeListMap =
		new HashMap<Long, List<AttributeInterface>>();
	/**
	 * Specify entity Attributes To Add.
	 */
	private final HashMap<String, List<String>> entityAttributesToAdd = new HashMap<String, List<String>>();
	/**
	 * Specify attribute Data type Map.
	 */
	private final HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	/**
	 * Specify entity Name List.
	 */
	private final List<String> entityNameList = new ArrayList<String>();
	/**
	 * Specify entity ID Map.
	 */
	private final Map<String, Long> entityIDMap = new HashMap<String, Long>();
	/**
	 * Specify permissible Value To Add Map.
	 */
	private final HashMap<String, List> permissibleValueToAddMap = new HashMap<String, List>();

	/**
	 * This method adds Permissible Value.
	 * @return list
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public List<String> addPermissibleValue() throws SQLException, IOException
	{
		final List<String> addAttributeSQL = new ArrayList<String>();
		List<String> addSQL;
		this.populateEntityList();
		this.populateAttributesToAddMap();
		this.populatePermissibleValueToAddMap();
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
				if (this.isAttributeToAdd(key, attribute.getName()))
				{
					addSQL = this.addPermissibleValue(identifier, attribute);
					addAttributeSQL.addAll(addSQL);
				}
			}
		}
		return addAttributeSQL;
	}

	/**
	 * This method adds Permissible Value.
	 * @param identifier identifier
	 * @param attribute attribute
	 * @return list
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	private List<String> addPermissibleValue(Long identifier, AttributeInterface attribute)
			throws SQLException, IOException
	{
		final List<String> deleteSQL = new ArrayList<String>();

		final String dataType = this.getDataTypeOfAttribute(attribute.getName());
		String tableName = null;
		if (dataType.equals("long"))
		{
			tableName = "dyextn_long_concept_value";
		}
		else if (dataType.equals("string"))
		{
			tableName = "dyextn_string_concept_value";
		}
		else if (dataType.equals("integer"))
		{
			tableName = "dyextn_integer_concept_value";
		}
		else if (dataType.equals("double"))
		{
			tableName = "dyextn_double_concept_value";
		}
		else if (dataType.equals("boolean"))
		{
			tableName = "dyextn_boolean_concept_value";
		}

		this.insertPermissibleValue(tableName, attribute);

		return deleteSQL;
	}

	/**
	 * This method inserts Permissible Value.
	 * @param tableName table Name
	 * @param attribute attribute
	 * @throws IOException IO Exception
	 * @throws SQLException SQL Exception
	 */
	private void insertPermissibleValue(String tableName, AttributeInterface attribute)
			throws IOException, SQLException
	{
		String sql;
		long permissibleValueId = 0;
		long dataElementId = 0;
		sql = "select max(IDENTIFIER) from dyextn_permissible_value";
		Statement stmt = this.connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			permissibleValueId = rs.getLong(1) + 1;
		}
		stmt.close();

		sql = "select IDENTIFIER from dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID = "
				+ attribute.getAttributeTypeInformation().getDataElement().getId();
		stmt = this.connection.createStatement();
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			dataElementId = rs.getLong(1);
		}
		stmt.close();

		if (dataElementId == 0)
		{
			stmt = this.connection.createStatement();
			sql = "select max(IDENTIFIER) from dyextn_data_element";
			stmt = this.connection.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				dataElementId = rs.getLong(1) + 1;
			}
			stmt.close();

			sql = "insert into dyextn_data_element" +
					"(IDENTIFIER,ATTRIBUTE_TYPE_INFO_ID,CATEGORY_ATTRIBUTE_ID) values ("
					+ dataElementId
					+ ","
					+ attribute.getAttributeTypeInformation()
					.getDataElement().getId() + ",null)";
			if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
						"dyextn_data_element");
			}
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

			sql = "insert into dyextn_userdefined_de values (" + dataElementId + ",1)";
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		}

		final List permissibleValueList = this.permissibleValueToAddMap.get(attribute.getName());

		for (Object value : permissibleValueList)
		{
			if (value instanceof String)
			{
				value = "'" + (String) value + "'";
			}
			sql = "insert into dyextn_permissible_value" +
					"(IDENTIFIER,DESCRIPTION,ATTRIBUTE_TYPE_INFO_ID,CATEGORY_ATTRIBUTE_ID)" +
					" values ("
					+ permissibleValueId
					+ ",null,"
					+ attribute.getAttributeTypeInformation().
					getDataElement().getId() + ",null)";
			if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
						"dyextn_permissible_value");
			}
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
			sql = "insert into " + tableName + " values(" + permissibleValueId + "," + value + ")";
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

			sql = "insert into dyextn_userdef_de_value_rel values(" + dataElementId + ","
					+ permissibleValueId + ")";
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

			permissibleValueId++;
		}

	}

	/**
	 * This method checks is Attribute To Add.
	 * @param entityName entity Name
	 * @param name name
	 * @return true or false.
	 */
	private boolean isAttributeToAdd(String entityName, String name)
	{
		final List<String> attributesToAddList = this.entityAttributesToAdd.get(entityName);
		for (final String attributeName : attributesToAddList)
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
	 * This method populates Permissible Value To Add Map.
	 */
	private void populatePermissibleValueToAddMap()
	{
		List permissibleValueList = new ArrayList();
		permissibleValueList.add("Cell");
		permissibleValueList.add("Fluid");
		permissibleValueList.add("Molecular");
		permissibleValueList.add("Tissue");
		this.permissibleValueToAddMap.put("specimenClass", permissibleValueList);

		permissibleValueList = new ArrayList();
		permissibleValueList.add("CPT");
		permissibleValueList.add("Paxgene");
		this.permissibleValueToAddMap.put("container", permissibleValueList);
	}

	/**
	 * This method populates Attributes To Add Map.
	 */
	private void populateAttributesToAddMap()
	{
		List<String> attributeToAdd = new ArrayList<String>();

		attributeToAdd = new ArrayList<String>();
		attributeToAdd.add("specimenClass");
		this.entityAttributesToAdd.put("edu.wustl.catissuecore.domain.AbstractSpecimen",
				attributeToAdd);

		attributeToAdd = new ArrayList<String>();
		attributeToAdd.add("container");
		this.entityAttributesToAdd.put("edu.wustl.catissuecore.domain.CollectionEventParameters",
				attributeToAdd);
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put("specimenClass", "string");
		this.attributeDatatypeMap.put("container", "string");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityNameList.add("edu.wustl.catissuecore.domain.AbstractSpecimen");
		this.entityNameList.add("edu.wustl.catissuecore.domain.CollectionEventParameters");
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
	 * @param sql query
	 * @return ResultSet
	 * @throws SQLException SQLException
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
	public AddPermissibleValue(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
	}
}
