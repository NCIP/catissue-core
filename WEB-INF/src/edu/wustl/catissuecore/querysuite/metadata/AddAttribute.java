
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * AddAttribute.
 * @author pooja
 * @version
 *
 */
public class AddAttribute extends BaseMetadata
{
	public final static String QUANTITY = "quantity";
	public final static String UNSIGNED_CONSENT = "unsignedConsentDocumentURL";
	public final static String SIGNED_CONSENT = "signedConsentDocumentURL";
	public final static String CONSENT_DATE = "consentSignatureDate";

	/**
	 * Connection instance.
	 */
	private Connection connection = null;

	/**
	 * add Attribute.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addAttribute() throws SQLException, IOException
	{
		final Set<String> keySet = this.entityNameAttributeNameMap.keySet();
		final Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			final String entityName = iterator.next();
			final List<String> attributes = this.entityNameAttributeNameMap.get(entityName);
			for (final String attr : attributes)
			{
				Long nextIdOfAbstractMetadata = getNextId("dyextn_abstract_metadata", "identifier");
				Long nextIdAttrTypeInfo = getNextId("dyextn_attribute_type_info", "identifier");
				Long nextIdDatabaseproperties = getNextId("dyextn_database_properties",
						"identifier");

				String sql = "INSERT INTO dyextn_abstract_metadata "
						+ "(IDENTIFIER,CREATED_DATE,DESCRIPTION,LAST_UPDATED," + "NAME,PUBLIC_ID)"
						+ " values(" + nextIdOfAbstractMetadata + ",NULL,NULL,NULL,'" + attr
						+ "',null)";
				if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
				{
					sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
							"dyextn_abstract_metadata");
				}
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
				sql = "INSERT INTO DYEXTN_BASE_ABSTRACT_ATTRIBUTE (IDENTIFIER) values("
						+ nextIdOfAbstractMetadata + ")";
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

				final int entityId = UpdateMetadataUtil.getEntityIdByName(entityName,
						this.connection.createStatement());
				sql = "INSERT INTO dyextn_attribute values (" + nextIdOfAbstractMetadata + ","
						+ entityId + ")";
				UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

				insertAttributeData(attr, nextIdOfAbstractMetadata, nextIdAttrTypeInfo, entityId);
				insertAttributeType(attr, nextIdAttrTypeInfo);
				insertDbProp(attr, nextIdOfAbstractMetadata, nextIdDatabaseproperties);
			}
		}
	}

	private void insertAttributeData(String attr, Long nextIdMetadata, Long nextIdAttrType,
			int entityId) throws SQLException, IOException
	{
		String sql;
		String primaryKey = attributePrimarkeyMap.get(attr);
		sql = "insert into dyextn_primitive_attribute (IDENTIFIER,IS_IDENTIFIED,IS_PRIMARY_KEY,IS_NULLABLE)"
				+ " values (" + nextIdMetadata + ",NULL," + primaryKey + ",1)";

		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		if ("1".equals(primaryKey))
		{
			sql = "insert into DYEXTN_ENTIY_COMPOSITE_KEY_REL "
					+ "(ENTITY_ID,ATTRIBUTE_ID,INSERTION_ORDER) " + " values (" + entityId + ","
					+ nextIdMetadata + ",0)";
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		}

		sql = "insert into dyextn_attribute_type_info (IDENTIFIER,PRIMITIVE_ATTRIBUTE_ID) values ("
				+ nextIdAttrType + "," + nextIdMetadata + ")";

		if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
		{
			sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
					"dyextn_attribute_type_info");
		}

		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
	}

	private void insertAttributeType(String attr, Long nextIdAttrType) throws SQLException,
			IOException
	{
		String dataType = getDataTypeOfAttribute(attr, attributeDatatypeMap);
		dataType = dataType.trim();
		String sql = null;
		//In case of long,integer & double , the entry should be made in two tables
		if (dataType.equalsIgnoreCase("long") || dataType.equalsIgnoreCase("int")
				|| dataType.equalsIgnoreCase("double"))
		{
			sql = "insert into dyextn_numeric_type_info (IDENTIFIER,MEASUREMENT_UNITS,DECIMAL_PLACES,NO_DIGITS) values ("
					+ nextIdAttrType + ",NULL,0,NULL)";
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		}
		if (dataType.equalsIgnoreCase("string"))
		{
			sql = "insert into dyextn_string_type_info (IDENTIFIER) values (" + nextIdAttrType
					+ ")";
		}
		else if (dataType.equalsIgnoreCase("date"))
		{
			sql = "insert into dyextn_date_type_info (IDENTIFIER,FORMAT) values (" + nextIdAttrType
					+ ",'MM-dd-yyyy')";
		}
		else if (dataType.equalsIgnoreCase("boolean"))
		{
			sql = "insert into dyextn_boolean_type_info (IDENTIFIER) values (" + nextIdAttrType
					+ ")";
		}
		else
		{
			sql = insertNumericType(nextIdAttrType, dataType);
		}
		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
	}

	private String insertNumericType(Long nextIdAttrType, String dataType)
	{
		String sql = null;
		if (dataType.equalsIgnoreCase("double"))
		{
			sql = "insert into dyextn_double_type_info (IDENTIFIER) values (" + nextIdAttrType
					+ ")";
		}
		else if (dataType.equalsIgnoreCase("int"))
		{
			sql = "insert into dyextn_integer_type_info (IDENTIFIER) values (" + nextIdAttrType
					+ ")";
		}
		else if (dataType.equalsIgnoreCase("long"))
		{
			sql = "insert into dyextn_long_type_info (IDENTIFIER) values (" + nextIdAttrType + ")";
		}
		return sql;
	}

	private void insertDbProp(String attr, Long nextIdMetadata, Long nextIdDbProp)
			throws SQLException, IOException
	{
		String sql;
		String columnName = getColumnNameOfAttribute(attr, this.attributeColumnNameMap);
		sql = "insert into dyextn_database_properties (IDENTIFIER,NAME) values (" + nextIdDbProp
				+ ",'" + columnName + "')";
		if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
		{
			sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
					"dyextn_database_properties");
		}

		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		sql = "insert into dyextn_column_properties (IDENTIFIER,PRIMITIVE_ATTRIBUTE_ID) values ("
				+ nextIdDbProp + "," + nextIdMetadata + ")";
		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
	}

	private Long getNextId(String tablename, String column) throws SQLException
	{
		final Statement stmt = this.connection.createStatement();
		String sql = "select max(" + column + ") from " + tablename;
		ResultSet resultSet = stmt.executeQuery(sql);
		long nextId = 0;
		if (resultSet.next())
		{
			long maxId = resultSet.getLong(1);
			nextId = maxId + 1;
		}
		resultSet.close();
		stmt.close();
		return nextId;
	}

	/**
	 * This method gets Column Name Of Attribute.
	 * @param attr Attribute.
	 * @param attributeColumnNameMap attribute Column Name Map
	 * @return Column Name
	 */
	private String getColumnNameOfAttribute(String attr, Map<String, String> attributeColumnNameMap)
	{
		return attributeColumnNameMap.get(attr);
	}

	/**
	 * This method gets Data Type Of Attribute.
	 * @param attr Attribute
	 * @param attributeDatatypeMap attribute Data type Map
	 * @return Data Type
	 */
	private String getDataTypeOfAttribute(String attr, Map<String, String> attributeDatatypeMap)
	{
		return attributeDatatypeMap.get(attr);
	}

	/**
	 * This method populates Entity Attribute Map.
	 */
	private void populateEntityAttributeMap()
	{
		List<String> attributes = new ArrayList<String>();
		attributes.add(QUANTITY);
		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.DistributionSpecimenRequirement", attributes);
		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.DistributedItem",
				attributes);

		attributes = new ArrayList<String>();
		attributes.add(UNSIGNED_CONSENT);
		this.entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.CollectionProtocol",
				attributes);

		attributes = new ArrayList<String>();
		attributes.add(SIGNED_CONSENT);
		attributes.add(CONSENT_DATE);
		//attributes.add("barcode");
		this.entityNameAttributeNameMap.put(
				"edu.wustl.catissuecore.domain.CollectionProtocolRegistration", attributes);

		/*attributes = new ArrayList<String>();
		attributes.add("barcode");
		entityNameAttributeNameMap.put("edu.wustl.catissuecore.domain.SpecimenCollectionGroup",attributes);
		*/}

	/**
	 * This method populates Attribute Column Name Map.
	 */
	private void populateAttributeColumnNameMap()
	{
		this.attributeColumnNameMap.put(QUANTITY, "QUANTITY");
		this.attributeColumnNameMap.put(UNSIGNED_CONSENT, "UNSIGNED_CONSENT_DOC_URL");
		this.attributeColumnNameMap.put(SIGNED_CONSENT, "CONSENT_DOC_URL");
		this.attributeColumnNameMap.put(CONSENT_DATE, "CONSENT_SIGN_DATE");
		//		attributeColumnNameMap.put("barcode", "BARCODE");
	}

	/**
	 * This method populates Attribute Data type Map.
	 */
	private void populateAttributeDatatypeMap()
	{
		this.attributeDatatypeMap.put(QUANTITY, "double");
		this.attributeDatatypeMap.put(UNSIGNED_CONSENT, "string");
		this.attributeDatatypeMap.put(SIGNED_CONSENT, "string");
		this.attributeDatatypeMap.put(CONSENT_DATE, "date");
		//	attributeDatatypeMap.put("barcode", "string");
	}

	/**
	 * This method populates Attribute Primary Key Map.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		this.attributePrimarkeyMap.put(QUANTITY, "0");
		this.attributePrimarkeyMap.put(UNSIGNED_CONSENT, "0");
		this.attributePrimarkeyMap.put(SIGNED_CONSENT, "0");
		this.attributePrimarkeyMap.put(CONSENT_DATE, "0");
		//	attributePrimarkeyMap.put("barcode", "0");
	}

	/**
	 * This method populates Entity List.
	 */
	private void populateEntityList()
	{
		this.entityList.add("edu.wustl.catissuecore.domain.DistributionSpecimenRequirement");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol");
		this.entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration");
		this.entityList.add("edu.wustl.catissuecore.domain.DistributedItem");
		this.entityList.add("edu.wustl.catissuecore.domain.SpecimenCollectionGroup");
	}

	/**
	 * Constructor.
	 * @param connection Connection object
	 * @throws SQLException SQL Exception
	 */
	public AddAttribute(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.populateEntityList();
		this.populateEntityAttributeMap();
		this.populateAttributeColumnNameMap();
		this.populateAttributeDatatypeMap();
		this.populateAttributePrimaryKeyMap();
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @param entityNameAttributeNameMap entity Name Attribute Name Map
	 * @param attributeColumnNameMap  attribute Column Name Map
	 * @param attributeDatatypeMap attribute Data type Map
	 * @param attributePrimarkeyMap attribute Primary key Map
	 * @param entityList entity List
	 */
	public AddAttribute(Connection connection,
			Map<String, List<String>> entityNameAttributeNameMap,
			Map<String, String> attributeColumnNameMap, Map<String, String> attributeDatatypeMap,
			Map<String, String> attributePrimarkeyMap, List<String> entityList)
	{
		super();
		this.connection = connection;
		this.entityNameAttributeNameMap = entityNameAttributeNameMap;
		this.attributeColumnNameMap = attributeColumnNameMap;
		this.attributeDatatypeMap = attributeDatatypeMap;
		this.attributePrimarkeyMap = attributePrimarkeyMap;
		this.entityList = entityList;
	}
}
