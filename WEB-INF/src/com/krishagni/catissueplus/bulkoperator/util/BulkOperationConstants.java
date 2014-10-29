/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.util;


public class BulkOperationConstants
{
	public static final String unmigratedObjectflag ="migrate.unmigrated.objects";
	public static final String MIGRATION_INSTALL_PROPERTIES_FILE = "./migrationInstall.properties";
	public static final String CLIENT_SESSION_USER_NAME = "clientSession.username";
	public static final String CLIENT_SESSION_PASSWORD = "clientSession.password";
	public static final String PRODUCTION_DATABASE_TYPE = "production.database.type";
	public static final String PRODUCTION_DATABASE_NAME = "production.database.name";
	public static final String PRODUCTION_DATABASE_USERNAME = "production.database.username";
	public static final String PRODUCTION_DATABASE_PASSWORD = "production.database.password";
	public static final String PRODUCTION_DATABASE_PORT = "production.database.port";
	public static final String PRODUCTION_DATABASE_HOST = "production.database.host";
	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";
	public static final String STAGING_HIBERNATE_CFG_XML_FILE = "hibernateForStagingDb.cfg.xml";
	public static final String CSM_DATABASE_TYPE = "csm.database.type";
	public static final String CSM_DATABASE_HOST = "csm.database.host";
	public static final String CSM_DATABASE_PORT = "csm.database.port";
	public static final String CSM_DATABASE_NAME = "csm.database.name";
	public static final String CSM_DATABASE_USERNAME = "csm.database.username";
	public static final String CSM_DATABASE_PASSWORD = "csm.database.password";
	public static final String MIGRATION_METADATA_XML_FILE_NAME = "mapping.metadata.file.name";
	public static final String MIGRATION_SERVICE_TYPE = "migration.service.type";
	public static final String JBOSS_HOME = "jboss.home.dir";
	public static final String CATISSUE_THICK_CLIENT_SERVICE = "CaTissueThickClientService";
	public static final String CA_CORE_MIGRATION_APP_SERVICE = "com.krishagni.catissueplus.bulkoperator.appservice.CaCoreMigrationAppServiceImpl";
	public static final String BULK_OPEARTION_META_DATA_XML_FILE_NAME = "bulkOperationMetaData.xml";
	public static final String BULK_OPERATION_INSTALL_PROPERTIES_FILE = "bulkOperation.properties";
	public static final String BULK_OPERATION_APPSERVICE_CLASSNAME = "bulkoperator.appservice.class";
	public static final String BULK_OPERATION_LIST = "bulkOperationList";
	public static final String ORACLE_DATABASE = "ORACLE";
	public static final String MYSQL_DATABASE = "MYSQL";
	public static final String PAGE_OF_BULK_OPERATION = "pageOfBulkOperation";
	public static final String PAGE_OF = "pageOf";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String PAGE_OF_PARTICIPANT_CP_QUERY = "pageOfParticipantCPQuery";
	public static final String CP_AND_PARTICIPANT_VIEW = "cpAndParticipantView";
	public static final String CP_TREE_VIEW = "cpTreeView";
	public static final String SINGLE_COMMA = ",";
	public static final String NEW_LINE = "\n";
	public static final String ERROR_CONSOLE_FORMAT = "------------------------ERROR:--------------------------------";
	public static final String COMMON_ISSUES_ERROR_KEY = "bulk.operation.issues";
	public static final String JAVA_LANG_STRING_DATATYPE = "java.lang.String";
	public static final String BULKOPERATION_INSTALL_PROPERTIES = "bulkOperation.properties";
	public static final String DATABASE_CREDENTIALS_FILE = "database.credentials.file";
	public static final String STATUS = "Status";
	public static final String MESSAGE = "Message";
	public static final String MAIN_OBJECT_ID = "Main Object Id";
	public static final String CONFIG_DIR = "config.dir";
	public static final String ENCOUNTER_DATE = "encounterDate";
	public static final String FORM_LABEL="formLabel";
	public static final String CLINICAL_STUDY_EVENT_LABEL="clinicalStudyEventLabel";
	public static final String CLINICAL_STUDY_TITLE="clinicalStudyTitle";
	public static final String PARTICIPANT_ID="participantId";
	public static final String PPI="ppi";
	public static final String COLLECTION_PROTOCOL_LABEL="collectionProtocol";
	public static final String SPECIMEN_LABEL="specimenLabel";
	public static final String SPECIMEN_BARCODE="specimenBarcode";
	public static final String SCG_NAME="scgName";
	public static final String SCG_BARCODE="scgBarcode";
	public static final String SCG_ID="scgId";
	public static final String SPECIMEN_ID="specimenId";
	public static final String CATEGORY_TYPE="Category";
	public static final String ENTITY_TYPE="Entity";
	public static final String[] DEFAULT_COLUMNS = { BulkOperationConstants.STATUS, BulkOperationConstants.MESSAGE,
			BulkOperationConstants.MAIN_OBJECT_ID };
}