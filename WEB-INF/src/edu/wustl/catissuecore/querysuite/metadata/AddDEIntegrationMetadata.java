
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
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

/**
 *
 * @author deepali_ahirrao
 *
 */
public class AddDEIntegrationMetadata extends UpdateMetadata
{

	//Logger
	//private static transient Logger logger = Logger.getCommonLogger(AddDEIntegrationMetadata.class);

	private Connection connection = null;

	// For AbstractRecordEntry
	private final List<String> entityListRE = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapRE = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapRE = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapRE = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapRE = new HashMap<String, String>();
	private static final String entityAbstractRecordEntry = "edu.common.dynamicextensions.domain.integration.AbstractRecordEntry";

	private final List<String> entityListPartRE = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapPartRE = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapPartRE = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapPartRE = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapPartRE = new HashMap<String, String>();
	private static final String entityParticiapntRE = "edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry";

	private final List<String> entityListSpRE = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapSpRE = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapSpRE = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapSpRE = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapSpRE = new HashMap<String, String>();
	private static final String entitySpecimenRE = "edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry";

	private final List<String> entityListSCGRE = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapSCGRE = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapSCGRE = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapSCGRE = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapSCGRE = new HashMap<String, String>();
	private static final String entitySCGRE = "edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry";

	private static final String ID = "id";
	private static final String ACTIVITY_STATUS = "activityStatus";
	private static final String CONTAINER_ID = "containerId";
	private static final String FORM_LABEL = "formLabel";

	// For AbstractFormContext
	private final List<String> entityListFC = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapFC = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapFC = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapFC = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapFC = new HashMap<String, String>();
	private static final String entityAbstractFormContext = "edu.common.dynamicextensions.domain.integration.AbstractFormContext";

	private final List<String> entityListSFC = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapSFC = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapSFC = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapSFC = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapSFC = new HashMap<String, String>();
	private static final String entityStudyFormContext = "edu.wustl.catissuecore.domain.StudyFormContext";
	private static final String entityParticipant = "edu.wustl.catissuecore.domain.Participant";
	private static final String entitySpecimen = "edu.wustl.catissuecore.domain.Specimen";
	private static final String entitySCG = "edu.wustl.catissuecore.domain.SpecimenCollectionGroup";
	private static final String entityCollectionProtocol = "edu.wustl.catissuecore.domain.CollectionProtocol";

	public AddDEIntegrationMetadata(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
	}

	AddDEIntegrationMetadata()
	{

	}

	/**
	 *
	 * @param args
	 * @throws DAOException
	 * @throws DynamicExtensionsSystemException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *
	 */
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException
	{
		configureDBConnection(args);
		Connection conn = getDBConnection();
		AddDEIntegrationMetadata addMetadata = new AddDEIntegrationMetadata();
		addMetadata.connection = conn;

		addMetadata.addIntegrationMetadata();
	//	logger.info("Added metadata for DE integration restructure --->");
		System.out.println("---ADDED DE INTEGRATION METADATA---------------------------");

	}

	public static void configureDBConnection(String[] args)
	{
		System.out.println(" args length ---------" + args.length);
		if (args.length < 7)
		{
			throw new RuntimeException("Insufficient number of arguments");
		}
		DATABASE_SERVER_NAME = args[0];
		DATABASE_SERVER_PORT_NUMBER = args[1];
		DATABASE_TYPE = args[2];
		DATABASE_NAME = args[3];
		DATABASE_USERNAME = args[4];
		DATABASE_PASSWORD = args[5];
		DATABASE_DRIVER = args[6];
	}

	public static Connection getDBConnection() throws SQLException, ClassNotFoundException
	{
		Connection connection = null;
		// Load the JDBC driver
		Class.forName(DATABASE_DRIVER);
		// Create a connection to the database
		String url = "";
		if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:mysql://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER + "/"
					+ DATABASE_NAME; // a JDBC url
		}
		if ("Oracle".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:oracle:thin:@" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER
					+ ":" + DATABASE_NAME;
		}
		if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:sqlserver://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER
					+ ";" + "databaseName=" + DATABASE_NAME + ";";
			System.out.println("UpdateMetadata.getConnection() URL : " + url);
		}
		System.out.println("-----------url : " + url);
		connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
	}

	/**
	 * Method adds association and path between AbstractFormContext and AbstractRecordEntry
	 * @throws SQLException
	 * @throws IOException
	 * @throws DAOException
	 * @throws DynamicExtensionsSystemException
	 */
	public void addIntegrationMetadata() throws SQLException, IOException
	{
		// populate data for AbstractRecordEntry and AbstractFormContext
		populateEntityList();
		populateEntityAttributeMap();
		populateAttributeColumnNameMap();
		populateAttributeDatatypeMap();
		populateAttributePrimaryKeyMap();

		UpdateMetadataUtil.isExecuteStatement = true;
		AddEntity addEntity = new AddEntity(this.connection);

		// add AbstractFormContext entity to DYEXTN_ABSTRACT_METADATA table
		addEntity.addEntity(entityListFC, "DYEXTN_ABSTRACT_FORM_CONTEXT", "NULL", 3, 1);

		AddAttribute addEntityAttribute = new AddAttribute(this.connection, entityAttrMapFC,
				attrColumnMapFC, attributeTypeMapFC, attrPrimarkeyMapFC, entityListFC);

		// add entity attributes to DYEXTN_ABSTRACT_METADATA and DYEXTN_ATTRIBUTE table
		addEntityAttribute.addAttribute();

		// add AbstractRecordEntry entity to DYEXTN_ABSTRACT_METADATA table
		addEntity.addEntity(entityListRE, "DYEXTN_ABSTRACT_RECORD_ENTRY", "NULL", 3, 1);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapRE, attrColumnMapRE,
				attributeTypeMapRE, attrPrimarkeyMapRE, entityListRE);
		addEntityAttribute.addAttribute();

		addEntity.addEntity(entityListPartRE, "CATISSUE_PARTICIPANT_REC_NTRY",
				entityAbstractRecordEntry, 3, 0);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapPartRE,
				attrColumnMapPartRE, attributeTypeMapPartRE, attrPrimarkeyMapPartRE,
				entityListPartRE);
		addEntityAttribute.addAttribute();

		addEntity.addEntity(entityListSpRE, "CATISSUE_SPECIMEN_REC_NTRY",
				entityAbstractRecordEntry, 3, 0);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapSpRE,
				attrColumnMapSpRE, attributeTypeMapSpRE, attrPrimarkeyMapSpRE, entityListSpRE);
		addEntityAttribute.addAttribute();

		addEntity.addEntity(entityListSCGRE, "CATISSUE_SCG_REC_NTRY", entityAbstractRecordEntry, 3,
				0);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapSCGRE,
				attrColumnMapSCGRE, attributeTypeMapSCGRE, attrPrimarkeyMapSCGRE, entityListSCGRE);
		addEntityAttribute.addAttribute();

		addEntity.addEntity(entityListSFC, "CATISSUE_STUDY_FORM_CONTEXT",
				entityAbstractFormContext, 3, 0);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapSFC, attrColumnMapSFC,
				attributeTypeMapSFC, attrPrimarkeyMapSFC, entityListSFC);
		addEntityAttribute.addAttribute();

		String entityName = entityAbstractFormContext;
		String targetEntityName = entityAbstractRecordEntry;

		AddAssociations addAssociations = new AddAssociations(this.connection);
		// add association from AbstractFormContext to AbstractRecordEntry
		addAssociations.addAssociation(entityName, targetEntityName,
				"abstractFormContext_abstractRecordEntry", "ASSOCIATION", "recordEntryCollection",
				true, "", "ABSTRACT_FORM_CONTEXT_ID", null, 100, 1, "BI_DIRECTIONAL");
		// add association from AbstractRecordEntry to AbstractFormContext
		addAssociations.addAssociation(targetEntityName, entityName,
				"abstractRecordEntry_abstractFormContext", "ASSOCIATION", "formContext", false, "",
				"ABSTRACT_FORM_CONTEXT_ID", null, 1, 0, "BI_DIRECTIONAL");

		// add association from Participant to ParticipantRecordEntry
		addAssociations.addAssociation(entityParticipant, entityParticiapntRE,
				"participant_participantRecordEntry", "ASSOCIATION", "recordEntryCollection", true,
				"", "PARTICIPANT_ID", null, 100, 1, "BI_DIRECTIONAL");

		// add association from ParticipantRecordEntry to Participant
		addAssociations.addAssociation(entityParticiapntRE, entityParticipant,
				"participantRecordEntry_participant", "ASSOCIATION", "participant", false, "",
				"PARTICIPANT_ID", null, 1, 0, "BI_DIRECTIONAL");

		// add association from Specimen to SpecimenRecordEntry
		addAssociations.addAssociation(entitySpecimen, entitySpecimenRE,
				"specimen_specimenRecordEntry", "ASSOCIATION", "recordEntryCollection", true, "",
				"SPECIMEN_ID", null, 100, 1, "BI_DIRECTIONAL");

		// add association from SpecimenRecordEntry to Specimen
		addAssociations.addAssociation(entitySpecimenRE, entitySpecimen,
				"specimenRecordEntry_specimen", "ASSOCIATION", "specimen", false, "",
				"SPECIMEN_ID", null, 1, 0, "BI_DIRECTIONAL");

		// add association from SCG to SCGRecordEntry
		addAssociations.addAssociation(entitySCG, entitySCGRE, "scg_scgRecordEntry", "ASSOCIATION",
				"recordEntryCollection", true, "", "SPECIMEN_COLLECTION_GROUP_ID", null, 100, 1,
				"BI_DIRECTIONAL");

		// add association from SCGRecordEntry to SCG
		addAssociations.addAssociation(entitySCGRE, entitySCG, "scgRecordEntry_scg", "ASSOCIATION",
				"specimenCollectionGroup", false, "", "SPECIMEN_COLLECTION_GROUP_ID", null, 1, 0,
				"BI_DIRECTIONAL");

		// add association between CollectionProtocol and StudyFormContext
		addAssociations.addAssociation(entityCollectionProtocol, entityStudyFormContext,
				"CATISSUE_CP_STUDYFORMCONTEXT", "ASSOCIATION", "studyFormContextCollection", true,
				"collectionProtocolCollection", "STUDY_FORM_CONTEXT_ID", "COLLECTION_PROTOCOL_ID",
				2, 0, "BI_DIRECTIONAL");

		// add association between StudyFormContext and CollectionProtocol
		addAssociations.addAssociation(entityStudyFormContext, entityCollectionProtocol,
				"CATISSUE_CP_STUDYFORMCONTEXT", "ASSOCIATION", "collectionProtocolCollection",
				false, "studyFormContextCollection", "STUDY_FORM_CONTEXT_ID",
				"COLLECTION_PROTOCOL_ID", 2, 0, "BI_DIRECTIONAL");

		System.out.println(" ============== DONE =============");
	}

	public void modifyAssociations() throws SQLException, IOException
	{
		DeleteAssociation deleteAssociation = new DeleteAssociation(connection);
		AddAssociations addAssociations = new AddAssociations(connection);

		// get the entity IDs
		String sourceEntitySQL = "select identifier, name from dyextn_abstract_metadata " +
				"where name in ('edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry'," +
				"'edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry'," +
				"'edu.wustl.catissuecore.domain.deintegration.SCgRecordEntry'," +
				"'edu.wustl.catissuecore.domain.Participant'," +
				"'edu.wustl.catissuecore.domain.Specimen'," +
				"'edu.wustl.catissuecore.domain.SpecimenCollectionGroup') ";

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sourceEntitySQL);
		Map<String, Long> entityIdMap = new HashMap<String, Long>();
		while(resultSet.next())
		{
			entityIdMap.put(resultSet.getString(1), resultSet.getLong(0));
		}



		/*String sourceEntitySQL2 = "select identifier from dyextn_abstract_metadata " +
		"where name = 'edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry') ";

		String sourceEntitySQL3 = "select identifier from dyextn_abstract_metadata " +
		"where name = 'edu.wustl.catissuecore.domain.deintegration.SCgRecordEntry') ";*/

		String sql1 = "select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER where IDENTIFIER " +
				"in (select container_id from dyextn_entity_map where static_entity_id = " +
				entityIdMap.get("edu.wustl.catissuecore.domain.Participant") + ")";

		Long oldSourceEntityId = entityIdMap.get("edu.wustl.catissuecore.domain.Participant");
		String sql = "select dm.IDENTIFIER,dm.name from DYEXTN_CONTAINER dc, dyextn_abstract_metadata dm " +
				"where dc.IDENTIFIER in (select container_id from dyextn_entity_map where " +
				"static_entity_id=" + oldSourceEntityId +  ") " +
				"and dm.IDENTIFIER = dc.ABSTRACT_ENTITY_ID";

		resultSet = statement.executeQuery(sql1);
		while (resultSet.next())
		{
			Long targetEntityId = resultSet.getLong(0);
			String targetEntityName = resultSet.getString(1);
			List deleteSQL = deleteAssociation.deleteAssociation(oldSourceEntityId.toString(), String
					.valueOf(targetEntityId));
			UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);

			Long sourceEntityId = entityIdMap.get("edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry");
			String associationName = sourceEntityId + "_" + targetEntityId;
			String roleName = associationName;
			String roleNameTable = "";
			String srcAssociationId = "DYEXTN_AS" + sourceEntityId + targetEntityId; // ??

			// TODO verify : Deepali
			// Add assocition between RecordEntry and DE atble
			addAssociations.addAssociation(entityParticiapntRE, targetEntityName, associationName,
					"CONTAINTMENT", roleName, true, roleNameTable, srcAssociationId,
					"", 1, 0, "SRC_DESTINATION");

			/*addAssociations.addAssociation(targetEntityName, entityParticipant, associationName,
					"CONTAINTMENT", roleName, false, "", srcAssociationId,
					"", 100, 0, "SRC_DESTINATION");*/
		}


		String sql2 = "select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER where IDENTIFIER " +
		"in (select container_id from dyextn_entity_map where static_entity_id = 4)";

		String sql3 = "select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER where IDENTIFIER " +
		"in (select container_id from dyextn_entity_map where static_entity_id = 379)";

		/*int sourceEntityId = 0;
		int targetEntityId = 0;
		List deleteSQL = deleteAssociation.deleteAssociation(sourceEntityId, targetEntityId);
		UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);*/
	}

	public static int getEntityIdByName(String entityName, JDBCDAO jdbcdao) throws IOException,
			SQLException, DAOException
	{
		ResultSet resultSet = null;
		int entityId = 0;
		String sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ entityName + "'";
		try
		{
			resultSet = jdbcdao.getQueryResultSet(sql);
			if (resultSet.next())
			{
				entityId = resultSet.getInt(1);
			}
			if (entityId == 0)
			{
				System.out.println("Entity not found of name " + entityName);
			}
			jdbcdao.closeStatement(resultSet);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return entityId;
	}

	/**
	 *
	 */
	private void populateEntityList()
	{
		entityListFC.add(entityAbstractFormContext);
		entityListRE.add(entityAbstractRecordEntry);
		entityListPartRE.add(entityParticiapntRE);
		entityListSpRE.add(entitySpecimenRE);
		entityListSCGRE.add(entitySCGRE);
		entityListSFC.add(entityStudyFormContext);
	}

	/**
	 * Set the attributes list
	 */
	private void populateEntityAttributeMap()
	{
		// For AbstractFormContext
		List<String> attributes = new ArrayList<String>();
		attributes.add(ID);
		attributes.add(FORM_LABEL);
		attributes.add(CONTAINER_ID);
		attributes.add(ACTIVITY_STATUS);
		entityAttrMapFC.put(entityAbstractFormContext, attributes);

		// For AbstractRecordEntry
		attributes = new ArrayList<String>();
		attributes.add(ID);
		attributes.add(ACTIVITY_STATUS);
		entityAttrMapRE.put(entityAbstractRecordEntry, attributes);

		attributes = new ArrayList<String>();
		attributes.add(ID);
		entityAttrMapPartRE.put(entityParticiapntRE, attributes);

		attributes = new ArrayList<String>();
		attributes.add(ID);
		entityAttrMapSpRE.put(entitySpecimenRE, attributes);

		attributes = new ArrayList<String>();
		attributes.add(ID);
		entityAttrMapSCGRE.put(entitySCGRE, attributes);

		attributes = new ArrayList<String>();
		attributes.add(ID);
		attributes.add("entityMapCondition");
		entityAttrMapSFC.put(entityStudyFormContext, attributes);
	}

	/**
	 * Set the attribute - column name map
	 */
	private void populateAttributeColumnNameMap()
	{
		// For AbstractFormContext
		attrColumnMapFC.put(ID, "IDENTIFIER");
		attrColumnMapFC.put(FORM_LABEL, "FORM_LABEL");
		attrColumnMapFC.put(CONTAINER_ID, "CONTAINER_ID");
		attrColumnMapFC.put(ACTIVITY_STATUS, "ACTIVITY_STATUS");

		// For AbstractRecordEntry
		attrColumnMapRE.put(ID, "IDENTIFIER");
		attrColumnMapRE.put(ACTIVITY_STATUS, "ACTIVITY_STATUS");

		attrColumnMapPartRE.put(ID, "IDENTIFIER");
		attrColumnMapSpRE.put(ID, "IDENTIFIER");
		attrColumnMapSCGRE.put(ID, "IDENTIFIER");
		attrColumnMapSFC.put(ID, "IDENTIFIER");
		attrColumnMapSFC.put("entityMapCondition", "ENTITY_MAP_CONDITION");
	}

	/**
	 * Set data types
	 */
	private void populateAttributeDatatypeMap()
	{
		// Attribute data type for AbstractFormContext
		attributeTypeMapFC.put(ID, "long");
		attributeTypeMapFC.put(FORM_LABEL, "string");
		attributeTypeMapFC.put(CONTAINER_ID, "long");
		attributeTypeMapFC.put(ACTIVITY_STATUS, "string");

		// Attribute data type for AbstractRecordEntry
		attributeTypeMapRE.put(ID, "long");
		attributeTypeMapRE.put(ACTIVITY_STATUS, "string");

		attributeTypeMapPartRE.put(ID, "long");
		attributeTypeMapSpRE.put(ID, "long");
		attributeTypeMapSCGRE.put(ID, "long");
		attributeTypeMapSFC.put(ID, "long");
		attributeTypeMapSFC.put("entityMapCondition", "string");
	}

	/**
	 * Set primary key.
	 */
	private void populateAttributePrimaryKeyMap()
	{
		// For AbstractFormContext
		// id is the primary key
		attrPrimarkeyMapFC.put(ID, "1");
		attrPrimarkeyMapFC.put(FORM_LABEL, "0");
		attrPrimarkeyMapFC.put(CONTAINER_ID, "0");
		attrPrimarkeyMapFC.put(ACTIVITY_STATUS, "0");

		// For AbstractRecordEntry
		attrPrimarkeyMapRE.put(ID, "1");
		attrPrimarkeyMapRE.put(ACTIVITY_STATUS, "0");

		attrPrimarkeyMapPartRE.put(ID, "1");
		attrPrimarkeyMapSpRE.put(ID, "1");
		attrPrimarkeyMapSCGRE.put(ID, "1");
		attrPrimarkeyMapSFC.put(ID, "1");
		attrPrimarkeyMapSFC.put("entityMapCondition", "0");
	}

}
