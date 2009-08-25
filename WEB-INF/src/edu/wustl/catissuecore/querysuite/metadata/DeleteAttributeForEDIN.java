
package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * Class to Delete Attribute For EDIN.
 *
 */
public class DeleteAttributeForEDIN extends DeleteAttribute
{

	/**
	 * Specify Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Specify Statement instance.
	 */
	private Statement stmt = null;

	/**
	 * Constructor.
	 * @param connection Connection object
	 * @throws SQLException SQL Exception
	 */
	public DeleteAttributeForEDIN(Connection connection) throws SQLException
	{
		super(connection);
		this.connection = connection;
		this.stmt = connection.createStatement();
	}

	/**
	 * This method deletes Attribute.
	 * @throws SQLException SQL Exception.
	 * @return deleteSQL
	 */
	@Override
	public List<String> deleteAttribute() throws SQLException
	{
		if (this.entityNameList == null || this.entityNameList.size() == 0)
		{
			this.entityNameList.add("edu.wustl.catissuecore.domain.Participant");

			//Added by geeta : Attribute delete for Edinburgh
			final List<String> attributeToDelete = new ArrayList<String>();
			attributeToDelete.add("socialSecurityNumber");
			attributeToDelete.add("sexGenotype");
			attributeToDelete.add("ethnicity");

			this.entityAttributesToDelete.put("edu.wustl.catissuecore.domain.Participant",
					attributeToDelete);

			//Added by geeta for edin burgh
			this.attributeDatatypeMap.put("socialSecurityNumber", "string");
			this.attributeDatatypeMap.put("sexGenotype", "string");
			this.attributeDatatypeMap.put("ethnicity", "string");

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
	 * @param attribute attribute
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

		this.stmt = this.connection.createStatement();
		final ResultSet rs = this.stmt.executeQuery("select identifier from dyextn_data_element"
				+ " where ATTRIBUTE_TYPE_INFO_ID="
				+ attribute.getAttributeTypeInformation().getDataElement().getId());
		if (rs != null)
		{

			if (rs.next())
			{
				identifier = rs.getLong(1);
				System.out.println(" id found  " + identifier);

				sql = "delete from dyextn_userdef_de_value_rel where USER_DEF_DE_ID=" + identifier;
				deleteSQL.add(sql);

				sql = "delete from dyextn_userdefined_de where IDENTIFIER=" + identifier;
				deleteSQL.add(sql);

				sql = "delete from  dyextn_data_element where ATTRIBUTE_TYPE_INFO_ID = "
						+ attribute.getAttributeTypeInformation().getDataElement().getId();
				deleteSQL.add(sql);
			}
		}

		sql = "delete from dyextn_column_properties where PRIMITIVE_ATTRIBUTE_ID="
				+ attribute.getId();
		deleteSQL.add(sql);

		UpdateMetadataUtil.commonDeleteStatements(attribute, deleteSQL);

		return deleteSQL;
	}
}
