
package edu.wustl.catissuecore.caties.util;

/**
 * @author
 *
 */
public abstract class CaTIESConstants
{

	/**
	 * MSH.
	 */
	public static final String MSH = "MSH";
	/**
	 * PID.
	 */
	public static final String PID = "PID";
	/**
	 * OBX.
	 */
	public static final String OBX = "OBX";
	/**
	 * OBR.
	 */
	public static final String OBR = "OBR";
	/**
	 * FTS.
	 */
	public static final String FTS = "FTS";
	/**
	 * MALE.
	 */
	public static final String MALE = "M";
	/**
	 * FEMALE.
	 */
	public static final String FEMALE = "F";
	/**
	 * MALE_GENDER.
	 */
	public static final String MALE_GENDER = "Male Gender";
	/**
	 * FEMALE_GENDER.
	 */
	public static final String FEMALE_GENDER = "Female Gender";

	//Report observation section constants
	/**
	 * FINAL_SECTION.
	 */
	public static final String FINAL_SECTION = "FIN";
	/**
	 * TEXT_SECTION.
	 */
	public static final String TEXT_SECTION = "TX";
	/**
	 * NAME_SECTION_INDEX.
	 */
	public static final int NAME_SECTION_INDEX = 3;
	/**
	 * DOCUMENT_FRAGMENT_INDEX.
	 */
	public static final int DOCUMENT_FRAGMENT_INDEX = 5;
	/**
	 * PARTICIPANT_ETHNICITY_INDEX.
	 */
	public static final int PARTICIPANT_ETHNICITY_INDEX = 10;
	/**
	 * PARTICIPANT_MEDICAL_RECORD_INDEX.
	 */
	public static final int PARTICIPANT_MEDICAL_RECORD_INDEX = 3;
	/**
	 * PARTICIPANT_NAME_INDEX.
	 */
	public static final int PARTICIPANT_NAME_INDEX = 5;
	/**
	 * PARTICIPANT_DATEOFBIRTH_INDEX.
	 */
	public static final int PARTICIPANT_DATEOFBIRTH_INDEX = 7;
	/**
	 * PARTICIPANT_GENDER_INDEX.
	 */
	public static final int PARTICIPANT_GENDER_INDEX = 8;
	/**
	 * PARTICIPANT_SSN_INDEX.
	 */
	public static final int PARTICIPANT_SSN_INDEX = 19;
	/**
	 * PARTICIPANT_SITE_INDEX.
	 */
	public static final int PARTICIPANT_SITE_INDEX = 2;
	/**
	 * REPORT_ACCESSIONNUMBER_INDEX.
	 */
	public static final int REPORT_ACCESSIONNUMBER_INDEX = 3;
	/**
	 * REPORT_DATE_INDEX.
	 */
	public static final int REPORT_DATE_INDEX = 14;

	//CaTIESConstants Types
	/**
	 * HL7_PARSER.
	 */
	public static final String HL7_PARSER = "HL7";

	// Constant for input file extension
	/**
	 * INPUT_FILE_EXTENSION.
	 */
	public static final String INPUT_FILE_EXTENSION = ".dat";

	//Constants for report status
	/**
	 * PENDING_FOR_DEID.
	 */
	public static final String PENDING_FOR_DEID = "PENDING_FOR_DEID";
	/**
	 * PENDING_FOR_XML.
	 */
	public static final String PENDING_FOR_XML = "PENDING_FOR_XML";
	/**
	 * IDENTIFIED.
	 */
	public static final String IDENTIFIED = "IDENTIFIED";
	/**
	 * DEIDENTIFIED.
	 */
	public static final String DEIDENTIFIED = "DEIDENTIFIED";
	/**
	 * DEID_PROCESS_FAILED.
	 */
	public static final String DEID_PROCESS_FAILED = "DEID_PROCESS_FAILED";
	/**
	 * CC_PROCESS_FAILED.
	 */
	public static final String CC_PROCESS_FAILED = "CC_PROCESS_FAILED";
	/**
	 * CONCEPT_CODED.
	 */
	public static final String CONCEPT_CODED = "CONCEPT_CODED";

	//constans for report queue
	/**
	 * PENDING.
	 */
	public static final String PENDING = "PENDING";
	/**
	 * PENDING.
	 */
	public static final String NEW = "ADDED_TO_QUEUE";
	/**
	 * FAILURE.
	 */
	public static final String FAILURE = "FAILURE";
	/**
	 * STATUS_PARTICIPANT_CONFLICT.
	 */
	public static final String STATUS_PARTICIPANT_CONFLICT = "PARTICIPANT_CONFLICT";
	/**
	 * OVERWRITE_REPORT.
	 */
	public static final String OVERWRITE_REPORT = "OVERWRITE_REPORT";
	/**
	 * STATUS_SCG_CONFLICT.
	 */
	public static final String STATUS_SCG_CONFLICT = "SCG_CONFLICT";
	/**
	 * STATUS_SCG_PARTIAL_CONFLICT.
	 */
	public static final String STATUS_SCG_PARTIAL_CONFLICT = "SCG_PARTIAL_CONFLICT";
	/**
	 * SITE_NOT_FOUND.
	 */
	public static final String SITE_NOT_FOUND = "SITE_NOT_FOUND";
	/**
	 * INVALID_REPORT_SECTION.
	 */
	public static final String INVALID_REPORT_SECTION = "INVALID_REPORT_SECTION";
	/**
	 * DB_ERROR.
	 */
	public static final String DB_ERROR = "DB_ERROR";
	/**
	 * API_ERROR.
	 */
	public static final String API_ERROR = "API_ERROR";
	/**
	 * CP_NOT_FOUND.
	 */
	public static final String CP_NOT_FOUND = "CP_NOT_FOUND";
	/**
	 * CP_NOT_FOUND_ERROR_MSG.
	 */
	public static final String CP_NOT_FOUND_ERROR_MSG = "CP not found with specified Titile in DB";
	/**
	 * PARTICIPANT_CREATION_ERROR.
	 */
	public static final String PARTICIPANT_CREATION_ERROR = "PARTICIPANT_CREATION_ERROR";

	//constants for Site info
	/**
	 * SITE_NAME.
	 */
	public static final String SITE_NAME = "SITE_NAME";
	/**
	 * SITE_ABBRIVIATION.
	 */
	public static final String SITE_ABBRIVIATION = "SITE_ABBRIVIATION";
	/**
	 * DEFAULT_SITE_NAME.
	 */
	public static final String DEFAULT_SITE_NAME = "DEFAULT_SITE_NAME";

	//constants for tag names in de-identified report elelment
	/**
	 * REPORT.
	 */
	public static final String REPORT = "Report";
	/**
	 * REPORT_TYPE.
	 */
	public static final String REPORT_TYPE = "Report_Type";
	/**
	 * REPORT_HEADER.
	 */
	public static final String REPORT_HEADER = "Report_Header";
	/**
	 * REPORT_ID.
	 */
	public static final String REPORT_ID = "Report_ID";
	/**
	 * PATIENT_ID.
	 */
	public static final String PATIENT_ID = "Patient_ID";
	/**
	 * PARTICIPANT_NAME.
	 */
	public static final String PARTICIPANT_NAME = "Patient Name";
	/**
	 * PARTICIPANT_ROLE.
	 */
	public static final String PARTICIPANT_ROLE = "patient";
	/**
	 * PARTICIPANT_MRN.
	 */
	public static final String PARTICIPANT_MRN = "participant_mrn";
	/**
	 * REPORT_TEXT.
	 */
	public static final String REPORT_TEXT = "Report_Text";
	/**
	 * HEADER_PERSON.
	 */
	public static final String HEADER_PERSON = "Header_Person";
	/**
	 * HEADER_DATA.
	 */
	public static final String HEADER_DATA = "Header_Data";
	/**
	 * VARIABLE.
	 */
	public static final String VARIABLE = "Variable";
	/**
	 * VALUE.
	 */
	public static final String VALUE = "Value";
	/**
	 * ROLE.
	 */
	public static final String ROLE = "role";
	/**
	 * REPORT_TYPE_VALUE.
	 */
	public static final String REPORT_TYPE_VALUE = "SP";

	//constants for logger
	/**
	 * LOGGER_GENERAL.
	 */
	public static final String LOGGER_GENERAL = "general";
	/**
	 * LOGGER_FILE_POLLER.
	 */
	public static final String LOGGER_FILE_POLLER = "filePoller";
	/**
	 * LOGGER_QUEUE_PROCESSOR.
	 */
	public static final String LOGGER_QUEUE_PROCESSOR = "queueProcessor";
	/**
	 * LOGGER_DEID_SERVER.
	 */
	public static final String LOGGER_DEID_SERVER = "deidServer";
	/**
	 * LOGGER_CONCEPT_CODER.
	 */
	public static final String LOGGER_CONCEPT_CODER = "conceptCoder";
	/**
	 * CSVLOGGER_DATETIME.
	 */
	public static final String CSVLOGGER_DATETIME = " Date/Time ";
	/**
	 * CSVLOGGER_FILENAME.
	 */
	public static final String CSVLOGGER_FILENAME = " FileName ";
	/**
	 * CSVLOGGER_STATUS.
	 */
	public static final String CSVLOGGER_STATUS = " Status ";
	/**
	 * CSVLOGGER_MESSAGE.
	 */
	public static final String CSVLOGGER_MESSAGE = " Message ";
	/**
	 * CSVLOGGER_SEPARATOR.
	 */
	public static final String CSVLOGGER_SEPARATOR = ",";
	/**
	 * CSVLOGGER_REPORTQUEUE.
	 */
	public static final String CSVLOGGER_REPORTQUEUE = " Report Loder Queue ID ";
	/**
	 * CSVLOGGER_IDENTIFIED_REPORT.
	 */
	public static final String CSVLOGGER_IDENTIFIED_REPORT = " Identified Report ID";
	/**
	 * CSVLOGGER_DEIDENTIFIED_REPORT.
	 */
	public static final String CSVLOGGER_DEIDENTIFIED_REPORT = " Deidentified Report ID";
	/**
	 * CSVLOGGER_PROCESSING_TIME.
	 */
	public static final String CSVLOGGER_PROCESSING_TIME = "Processing Time (ms)";

	//constants for concept coding
	/**
	 * ERROR_GATE.
	 */
	public static final String ERROR_GATE = "ERROR_FAILED_GATE_PIPE";

	//Constants for tag names in Concept code process
	/**
	 * TAG_GATEXML.
	 */
	public static final String TAG_GATEXML = "GateXML";
	/**
	 * TAG_GATEDOCUMENT.
	 */
	public static final String TAG_GATEDOCUMENT = "GateDocument";
	/**
	 * TAG_ENVELOPE.
	 */
	public static final String TAG_ENVELOPE = "Envelope";
	/**
	 * TAG_CHIRPSXML.
	 */
	public static final String TAG_CHIRPSXML = "ChirpsXML";
	/**
	 * TAG_REPORTCODES.
	 */
	public static final String TAG_REPORTCODES = "ReportCodes";
	/**
	 * TAG_INDEXED_CONCEPT.
	 */
	public static final String TAG_INDEXED_CONCEPT = "IndexedConcept";
	/**
	 * TAG_CONCEPT_REFERENT.
	 */
	public static final String TAG_CONCEPT_REFERENT = "ConceptReferent";
	/**
	 * TAG_CONCEPT.
	 */
	public static final String TAG_CONCEPT = "Concept";
	/**
	 * TAG_CONCEPT_CLASSIFICATION.
	 */
	public static final String TAG_CONCEPT_CLASSIFICATION = "ConceptClassification";
	/**
	 * TAG_ATTRIBUTE_NAME.
	 */
	public static final String TAG_ATTRIBUTE_NAME = "name";
	/**
	 * TAG_ATTRIBUTE_END_OFFSET.
	 */
	public static final String TAG_ATTRIBUTE_END_OFFSET = "endOffset";
	/**
	 * TAG_ATTRIBUTE_START_OFFSET.
	 */
	public static final String TAG_ATTRIBUTE_START_OFFSET = "startOffset";
	/**
	 * TAG_ATTRIBUTE_ISMODIFIER.
	 */
	public static final String TAG_ATTRIBUTE_ISMODIFIER = "isModifier";
	/**
	 * TAG_ATTRIBUTE_ISNEGATED.
	 */
	public static final String TAG_ATTRIBUTE_ISNEGATED = "isNegated";
	/***
	 * TAG_ATTRIBUTE_CUI.
	 */
	public static final String TAG_ATTRIBUTE_CUI = "cui";
	/**
	 * TAG_ATTRIBUTE_SEMANTICTYPE.
	 */
	public static final String TAG_ATTRIBUTE_SEMANTICTYPE = "semanticType";
	/**
	 * COLUMN_NAME_REPORT_STATUS.
	 */
	public static final String COLUMN_NAME_REPORT_STATUS = "reportStatus";
	/**
	 * PARSER_CLASS.
	 */
	public static final String PARSER_CLASS = "edu.wustl.catissuecore.reportloader.HL7Parser";
	/**
	 * INPUT_DIR.
	 */
	public static final String INPUT_DIR = "inputDir";
	/**
	 * BAD_FILE_DIR.
	 */
	public static final String BAD_FILE_DIR = "badFilesDir";
	/**
	 * PROCESSED_FILE_DIR.
	 */
	public static final String PROCESSED_FILE_DIR = "processFileDir";
	/**
	 * POLLER_SLEEPTIME.
	 */
	public static final String POLLER_SLEEPTIME = "filePollerSleepTime";
	/**
	 * SITE_INFO_FILENAME.
	 */
	public static final String SITE_INFO_FILENAME = "siteInfoFilename";
	/**
	 * XML_PROPERTY_FILENAME.
	 */
	public static final String XML_PROPERTY_FILENAME = "xmlPropertyFilename";
	/**
	 * FILE_POLLER_PORT.
	 */
	public static final String FILE_POLLER_PORT = "filePollerPort";
	/**
	 * COLLECTION_PROTOCOL_TITLE.
	 */
	public static final String COLLECTION_PROTOCOL_TITLE = "collectionProtocolTitle";
	/**
	 * SITE_NAME_FROM_PROPERTIES.
	 */
	public static final String SITE_NAME_FROM_PROPERTIES = "siteName";
	/**
	 * SECTION_HEADER_FILENAME.
	 */
	public static final String SECTION_HEADER_FILENAME = "sectionHeaderPriorityFilename";
	/**
	 * USER_NAME.
	 */
	public static final String USER_NAME = "userName";
	/**
	 * PASSWORD.
	 */
	public static final String PASSWORD = "password";
	/**
	 * KEYSTORE_FILE_PATH.
	 */
	public static final String KEYSTORE_FILE_PATH = "keystoreFilePath";
	/**
	 * DEIDENTIFIER_CLASSNAME.
	 */
	public static final String DEIDENTIFIER_CLASSNAME = "deidentifierClassName";
	/**
	 * PARTICIPANT_LOOKUP_CUTTOFF.
	 */
	public static final String PARTICIPANT_LOOKUP_CUTTOFF = "participantLookupCutOff";
	/**
	 * DEID_HOME.
	 */
	public static final String DEID_HOME = "deidHome";
	/**
	 * DEID_DCTIONARY_FOLDER.
	 */
	public static final String DEID_DCTIONARY_FOLDER = "deidDnyFolder";
	/**
	 * DEID_SLEEPTIME.
	 */
	public static final String DEID_SLEEPTIME = "deidentifierSleepTime";
	/**
	 * DEID_CONFIG_FILE_NAME.
	 */
	public static final String DEID_CONFIG_FILE_NAME = "deidConfigFilename";
	/**
	 * DEID_DTD_FILENAME.
	 */
	public static final String DEID_DTD_FILENAME = "deidDTDFilename";
	/**
	 * MAX_THREADPOOL_SIZE.
	 */
	public static final String MAX_THREADPOOL_SIZE = "maxThreadPoolSize";
	/**
	 * DEID_PORT.
	 */
	public static final String DEID_PORT = "deidentifierPort";
	/**
	 * CONCEPT_CODER_SLEEPTIME.
	 */
	public static final String CONCEPT_CODER_SLEEPTIME = "conceptCoderSleepTime";
	/**
	 * CONCEPT_CODER_PORT.
	 */
	public static final String CONCEPT_CODER_PORT = "conceptCoderPort";

	//keys for caTIES.properties file
	/**
	 * CATIES_CODER_VERSION.
	 */
	public static final String CATIES_CODER_VERSION = "caties.coder.version";
	/**
	 * CATIES_GATE_HOME.
	 */
	public static final String CATIES_GATE_HOME = "caties.gate.home";
	/**
	 * CATIES_CREOLE_URL_NAME.
	 */
	public static final String CATIES_CREOLE_URL_NAME = "caties.creole.url.name";
	/**
	 * CATIES_CASE_INSEN_GAZ_NAME.
	 */
	public static final String CATIES_CASE_INSEN_GAZ_NAME = "caties.case.insensitive.gazetteer.url.name";
	/**
	 * CATIES_CASE_SEN_GAZ_NAME.
	 */
	public static final String CATIES_CASE_SEN_GAZ_NAME = "caties.case.sensitive.gazetteer.url.name";
	/**
	 * CATIES_SECTION_CHUMNKER_URL_NAME.
	 */
	public static final String CATIES_SECTION_CHUMNKER_URL_NAME = "caties.section.chunker.url.name";
	/**
	 * CATIES_CONCEPT_FILTER_URL_NAME.
	 */
	public static final String CATIES_CONCEPT_FILTER_URL_NAME = "caties.concept.filter.url.name";
	/**
	 * CATIES_CONCEPT_NEG_EX_URL_NAME.
	 */
	public static final String CATIES_CONCEPT_NEG_EX_URL_NAME = "caties.neg.ex.url.name";
	/**
	 * CATIES_CONCEPT_CATEGORIZER_URL_NAME.
	 */
	public static final String CATIES_CONCEPT_CATEGORIZER_URL_NAME = "caties.concept.categorizer.url.name";
	/**
	 * CATIES_SAVE_BI_CONTENT.
	 */
	public static final String CATIES_SAVE_BI_CONTENT = "saveBinaryContent";
	/**
	 * CATIES_SAVE_XML_CONTENT.
	 */
	public static final String CATIES_SAVE_XML_CONTENT = "saveXMLContent";
	/**
	 * DEID_XPATH.
	 */
	public static final String DEID_XPATH = "//Report";
	/**
	 * DEID_REPORT_TEXT_TAG_NAME.
	 */
	public static final String DEID_REPORT_TEXT_TAG_NAME = "Report_Text";
	/**
	 * HARVARD_SCRUBBER_XPATH.
	 */
	public static final String HARVARD_SCRUBBER_XPATH = "//Envelope/Body/PathologyCase";
	/**
	 * HARVARD_SCRUBBER_DTD_FILENAME.
	 */
	public static final String HARVARD_SCRUBBER_DTD_FILENAME = "harvardScrubberDTDFilename";
	/**
	 * HARVARD_SCRUBBER_CONFIG_FILENAME.
	 */
	public static final String HARVARD_SCRUBBER_CONFIG_FILENAME = "harvardScrubberConfigFilename";
	/**
	 * TAG_HEADER.
	 */
	public static final String TAG_HEADER = "Header";
	/**
	 * TAG_IDENTIFIERS.
	 */
	public static final String TAG_IDENTIFIERS = "Identfiers";
	/**
	 * TAG_FIRST_NAME.
	 */
	public static final String TAG_FIRST_NAME = "FirstName";
	/**
	 * TAG_LAST_NAME.
	 */
	public static final String TAG_LAST_NAME = "LastName";
	/**
	 * TAG_DATE_OF_BIRTH.
	 */
	public static final String TAG_DATE_OF_BIRTH = "DateOfBirth";
	/**
	 * TAG_SSN.
	 */
	public static final String TAG_SSN = "SSN";
	/**
	 * TAG_ACCESSION_NUMBER.
	 */
	public static final String TAG_ACCESSION_NUMBER = "AccessionNumber";
	/**
	 * TAG_LOCALMRN.
	 */
	public static final String TAG_LOCALMRN = "LocalMRN";
	/**
	 * TAG_SOURCE.
	 */
	public static final String TAG_SOURCE = "Source";
	/**
	 * TAG_BODY.
	 */
	public static final String TAG_BODY = "Body";
	/**
	 * TAG_PATHOLOGY_CASE.
	 */
	public static final String TAG_PATHOLOGY_CASE = "PathologyCase";
	/**
	 * TAG_TISSUE_ACQUISITION_DATE.
	 */
	public static final String TAG_TISSUE_ACQUISITION_DATE = "TissueAcquisitionDate";
	/**
	 * TAG_CODES.
	 */
	public static final String TAG_CODES = "Codes";
	/**
	 * TAG_CLINICAL.
	 */
	public static final String TAG_CLINICAL = "Clinical";
	/**
	 * TAG_PATIENT.
	 */
	public static final String TAG_PATIENT = "Patient";
	/**
	 * TAG_AGE.
	 */
	public static final String TAG_AGE = "age";
	/**
	 * TAG_GENDER.
	 */
	public static final String TAG_GENDER = "Gender";
	/**
	 * TAG_FULL_REPORT_TEXT.
	 */
	public static final String TAG_FULL_REPORT_TEXT = "FullReportText";

}
