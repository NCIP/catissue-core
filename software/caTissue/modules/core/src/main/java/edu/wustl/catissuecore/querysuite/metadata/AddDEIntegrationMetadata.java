
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.dao.exception.DAOException;

/**
 *
 * @author deepali_ahirrao
 *
 */
public class AddDEIntegrationMetadata
{

	private Connection connection = null;

	// For AbstractRecordEntry
	private final List<String> entityListRE = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapRE = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapRE = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapRE = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapRE = new HashMap<String, String>();
	private static final String ENT_ABST_REC_ENTRY =
		"edu.common.dynamicextensions.domain.integration.AbstractRecordEntry";

	private final List<String> entityListPartRE = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapPartRE = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapPartRE = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapPartRE = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapPartRE = new HashMap<String, String>();
	private static final String ENT_PART_REC_ENTRY =
		"edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry";

	private final List<String> entityListSpRE = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapSpRE = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapSpRE = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapSpRE = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapSpRE = new HashMap<String, String>();
	private static final String ENT_SPECIMEN_REC_ENTRY =
		"edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry";

	private final List<String> entityListSCGRE = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapSCGRE = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapSCGRE = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapSCGRE = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapSCGRE = new HashMap<String, String>();
	private static final String ENT_SCG_REC_ENTRY =
		"edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry";

	private static final String ID = "id";
	private static final String ACTIVITY_STATUS = "activityStatus";
	private static final String CONTAINER_ID = "containerId";
	private static final String FORM_LABEL = "formLabel";
	private static final String HIDE_FORM = "hideForm";
	private static final String MODIFIED_DATE = "modifiedDate";
	private static final String MODIFIED_BY = "modifiedBy";

	// For AbstractFormContext
	private final List<String> entityListFC = new ArrayList<String>();
	private final Map<String, List<String>> entityAttrMapFC = new HashMap<String, List<String>>();
	private final Map<String, String> attrColumnMapFC = new HashMap<String, String>();
	private final Map<String, String> attributeTypeMapFC = new HashMap<String, String>();
	private final Map<String, String> attrPrimarkeyMapFC = new HashMap<String, String>();
	private static final String entityAbstractFormContext =
		"edu.common.dynamicextensions.domain.integration.AbstractFormContext";

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
		final AddDEIntegrationMetadata addMetadata = new AddDEIntegrationMetadata();
		addMetadata.connection = DBConnectionUtil.getDBConnection(args);

		addMetadata.addIntegrationMetadata();
		System.out.println("---ADDED DE INTEGRATION METADATA---------------------------");
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
		final AddEntity addEntity = new AddEntity(this.connection);

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
				ENT_ABST_REC_ENTRY, 3, 0);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapPartRE,
				attrColumnMapPartRE, attributeTypeMapPartRE, attrPrimarkeyMapPartRE,
				entityListPartRE);
		addEntityAttribute.addAttribute();

		addEntity.addEntity(entityListSpRE, "CATISSUE_SPECIMEN_REC_NTRY",
				ENT_ABST_REC_ENTRY, 3, 0);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapSpRE,
				attrColumnMapSpRE, attributeTypeMapSpRE, attrPrimarkeyMapSpRE, entityListSpRE);
		addEntityAttribute.addAttribute();

		addEntity.addEntity(entityListSCGRE, "CATISSUE_SCG_REC_NTRY", ENT_ABST_REC_ENTRY, 3,
				0);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapSCGRE,
				attrColumnMapSCGRE, attributeTypeMapSCGRE, attrPrimarkeyMapSCGRE, entityListSCGRE);
		addEntityAttribute.addAttribute();

		addEntity.addEntity(entityListSFC, "CATISSUE_STUDY_FORM_CONTEXT",
				entityAbstractFormContext, 3, 0);
		addEntityAttribute = new AddAttribute(this.connection, entityAttrMapSFC, attrColumnMapSFC,
				attributeTypeMapSFC, attrPrimarkeyMapSFC, entityListSFC);
		addEntityAttribute.addAttribute();

		final String entityName = entityAbstractFormContext;
		final String targetEntityName = ENT_ABST_REC_ENTRY;

		final AddAssociations addAssociations = new AddAssociations(this.connection);
		// add association from AbstractFormContext to AbstractRecordEntry
		addAssociations.addAssociation(entityName, targetEntityName,
				"abstractFormContext_abstractRecordEntry", "ASSOCIATION", "recordEntryCollection",
				true, "", "ABSTRACT_FORM_CONTEXT_ID", null, 100, 1, "BI_DIRECTIONAL", false);
		// add association from AbstractRecordEntry to AbstractFormContext
		addAssociations.addAssociation(targetEntityName, entityName,
				"abstractRecordEntry_abstractFormContext", "ASSOCIATION", "formContext", false, "",
				"ABSTRACT_FORM_CONTEXT_ID", null, 1, 0, "BI_DIRECTIONAL", false);

		// add association from Participant to ParticipantRecordEntry
		addAssociations.addAssociation(entityParticipant, ENT_PART_REC_ENTRY,
				"participant_participantRecordEntry", "CONTAINTMENT", "recordEntryCollection",
				true, "participant", "PARTICIPANT_ID", null, 100, 1, "BI_DIRECTIONAL", false);

		// add association from ParticipantRecordEntry to Participant
		addAssociations.addAssociation(ENT_PART_REC_ENTRY, entityParticipant,
				"participantRecordEntry_participant", "ASSOCIATION", "participant", false, "",
				"PARTICIPANT_ID", null, 1, 0, "BI_DIRECTIONAL", false);

		// add association from Specimen to SpecimenRecordEntry
		addAssociations.addAssociation(entitySpecimen, ENT_SPECIMEN_REC_ENTRY,
				"specimen_specimenRecordEntry", "CONTAINTMENT", "recordEntryCollection", true,
				"specimen", "SPECIMEN_ID", null, 100, 1, "BI_DIRECTIONAL", false);

		// add association from SpecimenRecordEntry to Specimen
		addAssociations.addAssociation(ENT_SPECIMEN_REC_ENTRY, entitySpecimen,
				"specimenRecordEntry_specimen", "ASSOCIATION", "specimen", false, "",
				"SPECIMEN_ID", null, 1, 0, "BI_DIRECTIONAL", false);

		// add association from SCG to SCGRecordEntry
		addAssociations.addAssociation(entitySCG, ENT_SCG_REC_ENTRY, "scg_scgRecordEntry", "CONTAINTMENT",
				"recordEntryCollection", true, "specimenCollectionGroup",
				"SPECIMEN_COLLECTION_GROUP_ID", null, 100, 1, "BI_DIRECTIONAL", false);

		// add association from SCGRecordEntry to SCG
		addAssociations.addAssociation(ENT_SCG_REC_ENTRY, entitySCG, "scgRecordEntry_scg", "ASSOCIATION",
				"specimenCollectionGroup", false, "", "SPECIMEN_COLLECTION_GROUP_ID", null, 1, 0,
				"BI_DIRECTIONAL", false);

		// add association between CollectionProtocol and StudyFormContext
		addAssociations.addAssociation(entityCollectionProtocol, entityStudyFormContext,
				"CATISSUE_CP_STUDYFORMCONTEXT", "ASSOCIATION", "studyFormContextCollection", true,
				"collectionProtocolCollection", "STUDY_FORM_CONTEXT_ID", "COLLECTION_PROTOCOL_ID",
				2, 0, "BI_DIRECTIONAL", true);

		// add association between StudyFormContext and CollectionProtocol
		addAssociations.addAssociation(entityStudyFormContext, entityCollectionProtocol,
				"CATISSUE_CP_STUDYFORMCONTEXT", "ASSOCIATION", "collectionProtocolCollection",
				false, "studyFormContextCollection", "STUDY_FORM_CONTEXT_ID",
				"COLLECTION_PROTOCOL_ID", 2, 0, "BI_DIRECTIONAL", true);

		System.out.println(" ============== DONE =============");
	}


	/**
	 *
	 */
	private void populateEntityList()
	{
		entityListFC.add(entityAbstractFormContext);
		entityListRE.add(ENT_ABST_REC_ENTRY);
		entityListPartRE.add(ENT_PART_REC_ENTRY);
		entityListSpRE.add(ENT_SPECIMEN_REC_ENTRY);
		entityListSCGRE.add(ENT_SCG_REC_ENTRY);
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
		attributes.add(HIDE_FORM);
		entityAttrMapFC.put(entityAbstractFormContext, attributes);

		// For AbstractRecordEntry
		attributes = new ArrayList<String>();
		attributes.add(ID);
		attributes.add(ACTIVITY_STATUS);
		attributes.add(MODIFIED_DATE);
		attributes.add(MODIFIED_BY);
		entityAttrMapRE.put(ENT_ABST_REC_ENTRY, attributes);

		attributes = new ArrayList<String>();
		attributes.add(ID);
		entityAttrMapPartRE.put(ENT_PART_REC_ENTRY, attributes);

		attributes = new ArrayList<String>();
		attributes.add(ID);
		entityAttrMapSpRE.put(ENT_SPECIMEN_REC_ENTRY, attributes);

		attributes = new ArrayList<String>();
		attributes.add(ID);
		entityAttrMapSCGRE.put(ENT_SCG_REC_ENTRY, attributes);

		attributes = new ArrayList<String>();
		attributes.add(ID);
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
		attrColumnMapFC.put(HIDE_FORM, "HIDE_FORM");

		// For AbstractRecordEntry
		attrColumnMapRE.put(ID, "IDENTIFIER");
		attrColumnMapRE.put(ACTIVITY_STATUS, "ACTIVITY_STATUS");
		attrColumnMapRE.put(MODIFIED_DATE, "MODIFIED_DATE");
		attrColumnMapRE.put(MODIFIED_BY, "MODIFIED_BY");

		attrColumnMapPartRE.put(ID, "IDENTIFIER");
		attrColumnMapSpRE.put(ID, "IDENTIFIER");
		attrColumnMapSCGRE.put(ID, "IDENTIFIER");
		attrColumnMapSFC.put(ID, "IDENTIFIER");
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
		attributeTypeMapFC.put(HIDE_FORM, "boolean");

		// Attribute data type for AbstractRecordEntry
		attributeTypeMapRE.put(ID, "long");
		attributeTypeMapRE.put(ACTIVITY_STATUS, "string");
		attributeTypeMapRE.put(MODIFIED_DATE, "date");
		attributeTypeMapRE.put(MODIFIED_BY, "string");

		attributeTypeMapPartRE.put(ID, "long");
		attributeTypeMapSpRE.put(ID, "long");
		attributeTypeMapSCGRE.put(ID, "long");
		attributeTypeMapSFC.put(ID, "long");
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
		attrPrimarkeyMapFC.put(HIDE_FORM, "0");

		// For AbstractRecordEntry
		attrPrimarkeyMapRE.put(ID, "1");
		attrPrimarkeyMapRE.put(ACTIVITY_STATUS, "0");
		attrPrimarkeyMapRE.put(MODIFIED_DATE, "0");
		attrPrimarkeyMapRE.put(MODIFIED_BY, "0");

		attrPrimarkeyMapPartRE.put(ID, "1");
		attrPrimarkeyMapSpRE.put(ID, "1");
		attrPrimarkeyMapSCGRE.put(ID, "1");
		attrPrimarkeyMapSFC.put(ID, "1");
	}

}
