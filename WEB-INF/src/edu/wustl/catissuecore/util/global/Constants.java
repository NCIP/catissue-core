/**
* <p>Title: Constants Class>
* <p>Description:  This class stores the constants used in the operations in the application.</p>
* Copyright:    Copyright (c) year
* Company: Washington University, School of Medicine, St. Louis.
* @author Gautam Shetty
* @version 1.00
* Created on Mar 16, 2005
*/

package edu.wustl.catissuecore.util.global;

import java.util.Locale;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;

// TODO: Auto-generated Javadoc
/**
 * This class stores the constants used in the operations in the application.
 *
 * @author gautam_shetty
 */

public class Constants
{

	/** Specify SELECT_OPTION. */
	public static final String SELECT_OPTION = "-- Select --";

	/** The Constant SHOW_ALL_VALUES. */
	public static final String SHOW_ALL_VALUES = "-- Show all clinical diagnosis values --";

	/** The Constant SHOW_SUBSET. */
	public static final String SHOW_SUBSET = "-- Show subset --";

	/** The Constant APPLICATION_NAME. */
	public static final String APPLICATION_NAME = CommonServiceLocator.getInstance().getAppName();
	//caTissue DataSource name added.
	/** The Constant DATASOURCE. */
	public static final String DATASOURCE = "DataSource";

	//Constants added for Catissuecore V1.2
	/** The Constant PAGE_OF_ADMINISTRATOR. */
	public static final String PAGE_OF_ADMINISTRATOR = "pageOfAdministrator";

	/** The Constant PAGE_OF_SUPERVISOR. */
	public static final String PAGE_OF_SUPERVISOR = "pageOfSupervisor";

	/** The Constant PAGE_OF_TECHNICIAN. */
	public static final String PAGE_OF_TECHNICIAN = "pageOfTechnician";

	/** The Constant PAGE_OF_SCIENTIST. */
	public static final String PAGE_OF_SCIENTIST = "pageOfScientist";

	//Constants added for Validation
	/** The Constant DEFAULT_TISSUE_SITE. */
	public static final String DEFAULT_TISSUE_SITE = "defaultTissueSite";

	/** The Constant DEFAULT_CLINICAL_STATUS. */
	public static final String DEFAULT_CLINICAL_STATUS = "defaultClinicalStatus";

	/** The Constant DEFAULT_GENDER. */
	public static final String DEFAULT_GENDER = "defaultGender";

	/** The Constant DEFAULT_GENOTYPE. */
	public static final String DEFAULT_GENOTYPE = "defaultGenotype";

	/** The Constant DEFAULT_SPECIMEN. */
	public static final String DEFAULT_SPECIMEN = "defaultSpecimen";

	/** The Constant DEFAULT_TISSUE_SIDE. */
	public static final String DEFAULT_TISSUE_SIDE = "defaultTissueSide";

	/** The Constant DEFAULT_PATHOLOGICAL_STATUS. */
	public static final String DEFAULT_PATHOLOGICAL_STATUS = "defaultPathologicalStatus";

	/** The Constant DEFAULT_RECEIVED_QUALITY. */
	public static final String DEFAULT_RECEIVED_QUALITY = "defaultReceivedQuality";

	/** The Constant DEFAULT_FIXATION_TYPE. */
	public static final String DEFAULT_FIXATION_TYPE = "defaultFixationType";

	/** The Constant DEFAULT_COLLECTION_PROCEDURE. */
	public static final String DEFAULT_COLLECTION_PROCEDURE = "defaultCollectionProcedure";

	/** The Constant DEFAULT_CONTAINER. */
	public static final String DEFAULT_CONTAINER = "defaultContainer";

	/** The Constant DEFAULT_METHOD. */
	public static final String DEFAULT_METHOD = "defaultMethod";

	/** The Constant DEFAULT_EMBEDDING_MEDIUM. */
	public static final String DEFAULT_EMBEDDING_MEDIUM = "defaultEmbeddingMedium";

	/** The Constant DEFAULT_BIOHAZARD. */
	public static final String DEFAULT_BIOHAZARD = "defaultBiohazard";

	/** The Constant DEFAULT_SITE_TYPE. */
	public static final String DEFAULT_SITE_TYPE = "defaultSiteType";

	/** The Constant DEFAULT_SPECIMEN_TYPE. */
	public static final String DEFAULT_SPECIMEN_TYPE = "defaultSpecimenType";

	/** The Constant DEFAULT_ETHNICITY. */
	public static final String DEFAULT_ETHNICITY = "defaultEthnicity";

	/** The Constant DEFAULT_RACE. */
	public static final String DEFAULT_RACE = "defaultRace";

	/** The Constant DEFAULT_CLINICAL_DIAGNOSIS. */
	public static final String DEFAULT_CLINICAL_DIAGNOSIS = "defaultClinicalDiagnosis";

	/** The Constant DEFAULT_STATES. */
	public static final String DEFAULT_STATES = "defaultStates";

	/** The Constant DEFAULT_COUNTRY. */
	public static final String DEFAULT_COUNTRY = "defaultCountry";
	
	public static final String DEFAULT_COUNTRY_NAME = Locale.getDefault().getDisplayCountry(); 
			
	/** The Constant DEFAULT_HISTOLOGICAL_QUALITY. */
	public static final String DEFAULT_HISTOLOGICAL_QUALITY = "defaultHistologicalQuality";

	/** The Constant DEFAULT_VITAL_STATUS. */
	public static final String DEFAULT_VITAL_STATUS = "defaultVitalStatus";

	/** The Constant DEFAULT_PRINTER_TYPE. */
	public static final String DEFAULT_PRINTER_TYPE = "defaultPrinterType";

	/** The Constant DEFAULT_PRINTER_LOCATION. */
	public static final String DEFAULT_PRINTER_LOCATION = "defaultPrinterLocation";

	//Consent tracking
	/** The Constant SHOW_CONSENTS. */
	public static final String SHOW_CONSENTS = "showConsents";

	/** The Constant SPECIMEN_CONSENTS. */
	public static final String SPECIMEN_CONSENTS = "specimenConsents";

	/** The Constant YES. */
	public static final String YES = "yes";

	/** The Constant CP_ID. */
	public static final String CP_ID = "cpID";

	/** The Constant BARCODE_LABLE. */
	public static final String BARCODE_LABLE = "barcodelabel";

	/** The Constant DISTRIBUTION_ON. */
	public static final String DISTRIBUTION_ON = "labelBarcode";

	/** The Constant POPUP. */
	public static final String POPUP = "popup";

	/** The Constant ERROR. */
	public static final String ERROR = "error";

	/** The Constant ERROR_SHOWCONSENTS. */
	public static final String ERROR_SHOWCONSENTS = "errorShowConsent";

	/** The Constant COMPLETE. */
	public static final String COMPLETE = "Complete";
	public static final String WAIVED_CONSENTS ="Waived";
	/** The Constant VIEW_CONSENTS. */
	public static final String VIEW_CONSENTS = "View";

	/** The Constant APPLY. */
	public static final String APPLY = "Apply";

	/** The Constant APPLY_ALL. */
	public static final String APPLY_ALL = "ApplyAll";

	/** The Constant APPLY_NONE. */
	public static final String APPLY_NONE = "ApplyNone";

	/** The Constant PREDEFINED_CADSR_CONSENTS. */
	public static final String PREDEFINED_CADSR_CONSENTS = "predefinedConsentsList";

	/** The Constant DISABLED. */
	public static final String DISABLED = "disabled";

	/** The Constant VIEWAll. */
	public static final String VIEWAll = "ViewAll";

	/** The Constant BARCODE_DISTRIBUTION. */
	public static final String BARCODE_DISTRIBUTION = "1";

	/** The Constant LABLE_DISTRIBUTION. */
	public static final String LABLE_DISTRIBUTION = "2";

	/** The Constant CONSENT_WAIVED. */
	public static final String CONSENT_WAIVED = "Consent Waived";

	public static final String IS_EMPI_ENABLED = "eMPI Enabled";
	/** The Constant NO_CONSENTS. */
	public static final String NO_CONSENTS = "No Consents";

	/** The Constant NO_CONSENTS_DEFINED. */
	public static final String NO_CONSENTS_DEFINED = "None Defined";

	/** The Constant INVALID. */
	public static final String INVALID = "Invalid";

	/** The Constant VALID. */
	public static final String VALID = "valid";

	/** The Constant FALSE. */
	public static final String FALSE = "false";

	/** The Constant TRUE. */
	public static final String TRUE = "true";

	/** The Constant NULL. */
	public static final String NULL = "null";

	/** The Constant CONSENT_TABLE. */
	public static final String CONSENT_TABLE = "tableId4";

	/** The Constant DISABLE. */
	public static final String DISABLE = "Disabled";

	/** The Constant SCG_ID. */
	public static final String SCG_ID = "-1";

	/** The Constant SELECTED_TAB. */
	public static final String SELECTED_TAB = "tab";

	/** The Constant TAB_SELECTED. */
	public static final String TAB_SELECTED = "tabSelected";

	/** The Constant NEWSPECIMEN_FORM. */
	public static final String NEWSPECIMEN_FORM = "newSpecimenForm";

	/** The Constant CONSENT_TABLE_SHOWHIDE. */
	public static final String CONSENT_TABLE_SHOWHIDE = "tableStatus";

	/** The Constant SPECIMEN_RESPONSELIST. */
	public static final String SPECIMEN_RESPONSELIST = "specimenResponseList";

	/** The Constant PROTOCOL_EVENT_ID. */
	public static final String PROTOCOL_EVENT_ID = "protocolEventId";

	/** The Constant SCG_DROPDOWN. */
	public static final String SCG_DROPDOWN = "value";

	/** The Constant HASHED_OUT. */
	public static final String HASHED_OUT = "####";

	/** The Constant HASHED_NODE_ID. */
	public static final String HASHED_NODE_ID = "-1";

	/** The Constant VERIFIED. */
	public static final String VERIFIED = "Verified";

	/** The Constant STATUS. */
	public static final String STATUS = "status";

	/** The Constant NOT_APPLICABLE. */
	public static final String NOT_APPLICABLE = "Not Applicable";

	/** The Constant WITHDRAW_ALL. */
	public static final String WITHDRAW_ALL = "withrawall";

	/** The Constant RESPONSE. */
	public static final String RESPONSE = "response";

	/** The Constant WITHDRAW. */
	public static final String WITHDRAW = "withdraw";

	/** The Constant VIRTUALLY_LOCATED. */
	public static final String VIRTUALLY_LOCATED = "Virtually Located";

	/** The Constant LABLE. */
	public static final String LABLE = "Lable";

	/** The Constant STORAGE_CONTAINER_LOCATION. */
	public static final String STORAGE_CONTAINER_LOCATION = "Storage Container Location";

	/** The Constant CLASS_NAME. */
	public static final String CLASS_NAME = "Class Name";

	/** The Constant SPECIMEN_LIST. */
	public static final String SPECIMEN_LIST = "specimenDetails";

	/** The Constant COLUMNLIST. */
	public static final String COLUMNLIST = "columnList";

	/** The Constant CONSENT_RESPONSE_KEY. */
	public static final String CONSENT_RESPONSE_KEY = "CR_";

	/** The Constant CONSENT_RESPONSE. */
	public static final String CONSENT_RESPONSE = "ConsentResponse";

	/** The Constant STORAGE_TYPE_POSITION_VIRTUAL. */
	public static final String STORAGE_TYPE_POSITION_VIRTUAL = "Virtual";
	
	public static final String DIGESTER_RULES_XML = "OrderItemRule.xml";

	/** The Constant STORAGE_TYPE_POSITION_VIRTUAL_VALUE. */
	public static final int STORAGE_TYPE_POSITION_VIRTUAL_VALUE = 1;

	/** The Constant STORAGE_TYPE_POSITION_AUTO. */
	public static final String STORAGE_TYPE_POSITION_AUTO = "Auto";

	/** The Constant STORAGE_TYPE_POSITION_AUTO_VALUE. */
	public static final int STORAGE_TYPE_POSITION_AUTO_VALUE = 2;

	/** The Constant STORAGE_TYPE_POSITION_MANUAL. */
	public static final String STORAGE_TYPE_POSITION_MANUAL = "Manual";

	/** The Constant STORAGE_TYPE_POSITION_MANUAL_VALUE. */
	public static final int STORAGE_TYPE_POSITION_MANUAL_VALUE = 3;

	/** The Constant STORAGE_TYPE_POSITION_AUTO_VALUE_FOR_TRANSFER_EVENT. */
	public static final int STORAGE_TYPE_POSITION_AUTO_VALUE_FOR_TRANSFER_EVENT = 1;

	/** The Constant STORAGE_TYPE_POSITION_MANUAL_VALUE_FOR_TRANSFER_EVENT. */
	public static final int STORAGE_TYPE_POSITION_MANUAL_VALUE_FOR_TRANSFER_EVENT = 2;

	/** The Constant defaultValueKeys. */
	public static final String[][] defaultValueKeys = {
			{Constants.DEFAULT_TISSUE_SITE, Constants.CDE_NAME_TISSUE_SITE},
			{Constants.DEFAULT_CLINICAL_STATUS, Constants.CDE_NAME_CLINICAL_STATUS},
			{Constants.DEFAULT_GENDER, Constants.CDE_NAME_GENDER},
			{Constants.DEFAULT_GENOTYPE, Constants.CDE_NAME_GENOTYPE},
			{Constants.DEFAULT_SPECIMEN, Constants.CDE_NAME_SPECIMEN_CLASS},
			{Constants.DEFAULT_TISSUE_SIDE, Constants.CDE_NAME_TISSUE_SIDE},
			{Constants.DEFAULT_PATHOLOGICAL_STATUS, Constants.CDE_NAME_PATHOLOGICAL_STATUS},
			{Constants.DEFAULT_RECEIVED_QUALITY, Constants.CDE_NAME_RECEIVED_QUALITY},
			{Constants.DEFAULT_FIXATION_TYPE, Constants.CDE_NAME_FIXATION_TYPE},
			{Constants.DEFAULT_COLLECTION_PROCEDURE, Constants.CDE_NAME_COLLECTION_PROCEDURE},
			{Constants.DEFAULT_CONTAINER, Constants.CDE_NAME_CONTAINER},
			{Constants.DEFAULT_METHOD, Constants.CDE_NAME_METHOD},
			{Constants.DEFAULT_EMBEDDING_MEDIUM, Constants.CDE_NAME_EMBEDDING_MEDIUM},
			{Constants.DEFAULT_BIOHAZARD, Constants.CDE_NAME_BIOHAZARD},
			{Constants.DEFAULT_SITE_TYPE, Constants.CDE_NAME_SITE_TYPE},
			{Constants.DEFAULT_SPECIMEN_TYPE, Constants.CDE_NAME_SPECIMEN_TYPE},
			{Constants.DEFAULT_ETHNICITY, Constants.CDE_NAME_ETHNICITY},
			{Constants.DEFAULT_RACE, Constants.CDE_NAME_RACE},
			{Constants.DEFAULT_CLINICAL_DIAGNOSIS, Constants.CDE_NAME_CLINICAL_DIAGNOSIS},
			{Constants.DEFAULT_STATES, Constants.CDE_NAME_STATE_LIST},
			{Constants.DEFAULT_COUNTRY, Constants.CDE_NAME_COUNTRY_LIST},
			{Constants.DEFAULT_HISTOLOGICAL_QUALITY, Constants.CDE_NAME_HISTOLOGICAL_QUALITY},
			{Constants.DEFAULT_VITAL_STATUS, Constants.CDE_VITAL_STATUS},
			{Constants.DEFAULT_PRINTER_LOCATION, Constants.DEFAULT_PRINTER_LOCATION},
			{Constants.DEFAULT_PRINTER_TYPE, Constants.DEFAULT_PRINTER_TYPE},
			{Constants.IS_BARCODE_EDITABLE, Constants.NULL}};

	//Constants added for Catissuecore V1.2
	/** The Constant MYSQL_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION. */
	public static final String MYSQL_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION = "cast(label as signed)";

	/** The Constant ORACLE_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION. */
	public static final String ORACLE_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION = "catissue_label_to_num(label)";

	/** The Constant ORACLE_MAX_BARCODE_COL. */
	public static final String ORACLE_MAX_BARCODE_COL = "catissue_label_to_num(barcode)";

	/** The Constant MYSQL_MAX_BARCODE_COL. */
	public static final String MYSQL_MAX_BARCODE_COL = "cast(barcode as signed)";
	// Query Module Interface UI constants
	/** The Constant ViewSearchResultsAction. */
	public static final String ViewSearchResultsAction = "ViewSearchResultsAction.do";

	/** The Constant categorySearchForm. */
	public static final String categorySearchForm = "categorySearchForm";

	/** The Constant SearchCategory. */
	public static final String SearchCategory = "SearchCategory.do";

	/** The Constant DefineSearchResultsViewAction. */
	public static final String DefineSearchResultsViewAction = "DefineSearchResultsView.do";

	/** The Constant DefineSearchResultsViewJSPAction. */
	public static final String DefineSearchResultsViewJSPAction = "ViewSearchResultsJSPAction.do";

	/** The Constant QUERY_DAG_VIEW_APPLET. */
	public static final String QUERY_DAG_VIEW_APPLET = "edu/wustl/catissuecore/applet/ui/querysuite/DiagrammaticViewApplet.class";

	/** The Constant QUERY_DAG_VIEW_APPLET_NAME. */
	public static final String QUERY_DAG_VIEW_APPLET_NAME = "Dag View Applet";

	/** The Constant APP_DYNAMIC_UI_XML. */
	public static final String APP_DYNAMIC_UI_XML = "xmlfile.dynamicUI";

	/** The Constant QUERY_CONDITION_DELIMITER. */
	public static final String QUERY_CONDITION_DELIMITER = "@#condition#@";

	/** The Constant QUERY_OPERATOR_DELIMITER. */
	public static final String QUERY_OPERATOR_DELIMITER = "!*=*!";

	/** The Constant QUERY_VALUES_DELIMITER. */
	public static final String QUERY_VALUES_DELIMITER = "&";
	//public static final String SEARCHED_ENTITIES_MAP = "searchedEntitiesMap";
	/** The Constant SUCCESS. */
	public static final String SUCCESS = "success";
	
	public static final String JSON_CONTENT_TYPE = "text/html";
	
	/** The Constant FAILURE. */
	public static final String FAILURE = "failure";

	/** The Constant LIST_OF_ENTITIES_IN_QUERY. */
	public static final String LIST_OF_ENTITIES_IN_QUERY = "ListOfEntitiesInQuery";

	/** The Constant DYNAMIC_UI_XML. */
	public static final String DYNAMIC_UI_XML = "dynamicUI.xml";

	/** The Constant TREE_DATA. */
	public static final String TREE_DATA = "treeData";

	/** The Constant ZERO_ID. */
	public static final String ZERO_ID = "0";

	/** The Constant TEMP_OUPUT_TREE_TABLE_NAME. */
	public static final String TEMP_OUPUT_TREE_TABLE_NAME = "TEMP_OUTPUTTREE";

	/** The Constant CREATE_TABLE. */
	public static final String CREATE_TABLE = "Create table ";

	/** The Constant AS. */
	public static final String AS = "as";

	/** The Constant UNDERSCORE. */
	public static final String UNDERSCORE = "_";

	/** The Constant ID_NODES_MAP. */
	public static final String ID_NODES_MAP = "idNodesMap";

	/** The Constant ID_COLUMNS_MAP. */
	public static final String ID_COLUMNS_MAP = "idColumnsMap";

	/** The Constant COLUMN_NAME. */
	public static final String COLUMN_NAME = "Column";

	/** The Constant ATTRIBUTE_NAMES_FOR_TREENODE_LABEL. */
	public static final String[] ATTRIBUTE_NAMES_FOR_TREENODE_LABEL = {"firstName", "lastName",
			"title", "name", "label", "shorttitle"};

	/** The Constant ROOT. */
	public static final String ROOT = "root";

	/** The Constant MISSING_TWO_VALUES. */
	public static final String MISSING_TWO_VALUES = "missingTwoValues";

	/** The Constant DATE. */
	public static final String DATE = "date";

	/** The Constant DEFINE_RESULTS_VIEW. */
	public static final String DEFINE_RESULTS_VIEW = "DefineResultsView";

	/** The Constant CURRENT_PAGE. */
	public static final String CURRENT_PAGE = "currentPage";

	/** The Constant ADD_LIMITS. */
	public static final String ADD_LIMITS = "AddLimits";

	/** The Constant LABEL_TREE_NODE. */
	public static final String LABEL_TREE_NODE = "Label";

	/** The Constant ENTITY_NAME. */
	public static final String ENTITY_NAME = "Entity Name";

	/** The Constant COUNT. */
	public static final String COUNT = "Count";

	/** The Constant TREE_NODE_FONT. */
	public static final String TREE_NODE_FONT = "<font color='#FF9BFF' face='Verdana'><i>";

	/** The Constant TREE_NODE_FONT_CLOSE. */
	public static final String TREE_NODE_FONT_CLOSE = "</i></font>";

	/** The Constant NULL_ID. */
	public static final String NULL_ID = "NULL";

	/** The Constant NODE_SEPARATOR. */
	public static final String NODE_SEPARATOR = "::";

	/** The Constant EXPRESSION_ID_SEPARATOR. */
	public static final String EXPRESSION_ID_SEPARATOR = ":";

	/** The Constant UNIQUE_ID_SEPARATOR. */
	public static final String UNIQUE_ID_SEPARATOR = "UQ";

	/** The Constant DEFINE_SEARCH_RULES. */
	public static final String DEFINE_SEARCH_RULES = "Define Limits For";

	/** The Constant DATE_FORMAT. */
//	public static final String DATE_FORMAT = "MM-dd-yyyy";

	/** The Constant NEW_DATE_FROMAT. */
//	public static final String NEW_DATE_FROMAT = "MM/DD/YYYY";

	/** The Constant OUTPUT_TREE_MAP. */
	public static final String OUTPUT_TREE_MAP = "outputTreeMap";

	/** The Constant CHECK_ALL_PAGES. */
	public static final String CHECK_ALL_PAGES = "checkAllPages";

	/** The Constant CHECK_ALL_ACROSS_ALL_PAGES. */
	public static final String CHECK_ALL_ACROSS_ALL_PAGES = "isCheckAllAcrossAllChecked";

	/** The Constant CLASSES_PRESENT_IN_QUERY. */
	public static final String CLASSES_PRESENT_IN_QUERY = "Objects Present In Query";

	/** The Constant DEFINE_QUERY_RESULTS_VIEW_ACTION. */
	public static final String DEFINE_QUERY_RESULTS_VIEW_ACTION = "DefineQueryResultsView.do";

	/** The Constant CONFIGURE_GRID_VIEW_ACTION. */
	public static final String CONFIGURE_GRID_VIEW_ACTION = "ConfigureGridView.do";

	/** The ATTRIBUT e_ map. */
	public static String ATTRIBUTE_MAP = "attributeMap";

	/** The Constant CLASS. */
	public static final String CLASS = "class";

	/** The Constant ATTRIBUTE. */
	public static final String ATTRIBUTE = "attribute";

	/** The Constant SELECT_DISTINCT. */
	public static final String SELECT_DISTINCT = "select distinct ";

	/** The Constant FROM. */
	public static final String FROM = " from ";

	/** The Constant WHERE. */
	public static final String WHERE = " where ";

	/** The Constant SELECTED_COLUMN_META_DATA. */
	public static final String SELECTED_COLUMN_META_DATA = "selectedColumnMetaData";

	/** The Constant CURRENT_SELECTED_OBJECT. */
	public static final String CURRENT_SELECTED_OBJECT = "currentSelectedObject";

	/** The Constant SELECTED_COLUMN_NAME_VALUE_BEAN_LIST. */
	public static final String SELECTED_COLUMN_NAME_VALUE_BEAN_LIST = "selectedColumnNameValueBeanList";

	/** The Constant OPERATION. */
	public static final String OPERATION = "operation";

	/** The Constant FINISH. */
	public static final String FINISH = "finish";

	/** The Constant BACK. */
	public static final String BACK = "back";

	/** The Constant RESTORE. */
	public static final String RESTORE = "restore";

	/** The Constant VIEW_ALL_RECORDS. */
	public static final String VIEW_ALL_RECORDS = "viewAllRecords";

	/** The Constant VIEW_LIMITED_RECORDS. */
	public static final String VIEW_LIMITED_RECORDS = "viewLimitedRecords";

	/** The Constant SAVE_QUERY_ACTION. */
	public static final String SAVE_QUERY_ACTION = "SaveQueryAction.do";

	/** The Constant RETRIEVE_QUERY_ACTION. */
	public static final String RETRIEVE_QUERY_ACTION = "RetrieveQueryAction.do";

	/** The Constant FETCH_AND_EXECUTE_QUERY_ACTION. */
	public static final String FETCH_AND_EXECUTE_QUERY_ACTION = "ExecuteQueryAction.do";

	/** The Constant TREE_NODE_LIMIT_EXCEEDED_RECORDS. */
	public static final String TREE_NODE_LIMIT_EXCEEDED_RECORDS = "treeNodeLimitExceededRecords";

	/** The Constant MAXIMUM_TREE_NODE_LIMIT. */
	public static final String MAXIMUM_TREE_NODE_LIMIT = "resultView.maximumTreeNodeLimit";
	

	/** The Constant MAXIMUM_TREE_NODE_LIMIT_FOR_CHILD_NODE. */
	public static final String MAXIMUM_TREE_NODE_LIMIT_FOR_CHILD_NODE = "resultView.maximumTreeNodeLimitForChildNode";

	/** The Constant DEFINED_VIEW_ATTRIBUTES. */
	public static final String DEFINED_VIEW_ATTRIBUTES = "definedViewAttributes";

	/** The Constant IS_SAVED_QUERY. */
	public static final String IS_SAVED_QUERY = "isSavedQuery";

	/** The Constant RANDOM_NUMBER. */
	public static final String RANDOM_NUMBER = "randomNumber";

	/** The Constant MULTIUSER. */
	public static final String MULTIUSER = "multiuser";

	/** The Constant SAVE_TREE_NODE_LIST. */
	public static final String SAVE_TREE_NODE_LIST = "rootOutputTreeNodeList";

	/** The Constant ATTRIBUTE_COLUMN_NAME_MAP. */
	public static final String ATTRIBUTE_COLUMN_NAME_MAP = "attributeColumnNameMap";

	/** The Constant SAVE_GENERATED_SQL. */
	public static final String SAVE_GENERATED_SQL = "sql";

	/** The Constant POPUP_MESSAGE. */
	public static final String POPUP_MESSAGE = "popupMessage";

	/** The Constant BUTTON_CLICKED. */
	public static final String BUTTON_CLICKED = "buttonClicked";

	/** The Constant KEY_CODE. */
	public static final String KEY_CODE = "key";

	/** The Constant ENTITY_SEPARATOR. */
	public static final String ENTITY_SEPARATOR = ";";

	/** The Constant ATTRIBUTE_SEPARATOR. */
	public static final String ATTRIBUTE_SEPARATOR = "|";

	/** The Constant KEY_SEPARATOR. */
	public static final String KEY_SEPARATOR = "*&*";

	/** The Constant TOTAL_FILE_TYPE_ATTRIBUTES. */
	public static final String TOTAL_FILE_TYPE_ATTRIBUTES = "totalFileTypeAttributes";

	/** The Constant CLOBTYPE_ID_LIST. */
	public static final String CLOBTYPE_ID_LIST = "clobTypeIdList";

	/** The Constant GETREPORTID. */
	public static final String GETREPORTID = "getReportID.do";

	/** The Constant DOUBLE_QUOTES. */
	public static final String DOUBLE_QUOTES = "";

	/** The Constant OVERRIDE. */
	public static final String OVERRIDE = "override";

	/** The Constant OVERRIDE_TRUE. */
	public static final String OVERRIDE_TRUE = "true";

	/** The Constant OVERRIDE_FALSE. */
	public static final String OVERRIDE_FALSE = "false";

	/** Name: Abhishek Mehta Reviewer Name : Deepti Bug ID: 5661 Patch ID: 5661_1 See also: 1-4 Description : Constants added for Query Module. */

	public static final String IS_QUERY_SAVED = "isQuerySaved";

	/** The Constant CONDITIONLIST. */
	public static final String CONDITIONLIST = "conditionList";

	/** The Constant QUERY_SAVED. */
	public static final String QUERY_SAVED = "querySaved";

	/** The Constant DISPLAY_NAME_FOR_CONDITION. */
	public static final String DISPLAY_NAME_FOR_CONDITION = "_displayName";

	/** The Constant SHOW_ALL_LINK. */
	public static final String SHOW_ALL_LINK = "showAllLink";

	// Query Module Interface UI constants---ends here
	//	 Frame names in Query Module Results page.
	/** The Constant GRID_DATA_VIEW_FRAME. */
	public static final String GRID_DATA_VIEW_FRAME = "gridFrame";

	/** The Constant TREE_VIEW_FRAME. */
	public static final String TREE_VIEW_FRAME = "treeViewFrame";

	/** The Constant QUERY_TREE_VIEW_ACTION. */
	public static final String QUERY_TREE_VIEW_ACTION = "QueryTreeView.do";

	/** The Constant QUERY_GRID_VIEW_ACTION. */
	public static final String QUERY_GRID_VIEW_ACTION = "QueryGridView.do";

	/** The Constant NO_OF_TREES. */
	public static final String NO_OF_TREES = "noOfTrees";

	/** The Constant TREE_ROOTS. */
	public static final String TREE_ROOTS = "treeRoots";
	//	 Frame names in Query Module Results page.--ends here
	/** The Constant MAX_IDENTIFIER. */
	public static final String MAX_IDENTIFIER = "maxIdentifier";

	/** The Constant AND_JOIN_CONDITION. */
	public static final String AND_JOIN_CONDITION = "AND";

	/** The Constant OR_JOIN_CONDITION. */
	public static final String OR_JOIN_CONDITION = "OR";
	//Sri: Changed the format for displaying in Specimen Event list (#463)
	/** The Constant TIMESTAMP_PATTERN. */
//	public static final String TIMESTAMP_PATTERN = "MM-dd-yyyy HH:mm";

	/** The Constant TIMESTAMP_PATTERN_MM_SS. */
//	public static final String TIMESTAMP_PATTERN_MM_SS = "HH:mm";

	/** The Constant DATE_PATTERN_YYYY_MM_DD. */
//	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

	/** The Constant QUERY_ID. */
	public static final String QUERY_ID = "queryId";

	// Mandar: Used for Date Validations in Validator Class
	/** The Constant DATE_SEPARATOR. */
	public static final String DATE_SEPARATOR = "-";

	/** The Constant DATE_SEPARATOR_DOT. */
	public static final String DATE_SEPARATOR_DOT = ".";

	/** The Constant MIN_YEAR. */
	public static final String MIN_YEAR = "1900";

	/** The Constant MAX_YEAR. */
	public static final String MAX_YEAR = "9999";

	/** The Constant VIEW. */
	public static final String VIEW = "view";

	/** The Constant DELETE. */
	public static final String DELETE = "delete";

	/** The Constant EXPORT. */
	public static final String EXPORT = "export";

	/** The Constant SHOPPING_CART_ADD. */
	public static final String SHOPPING_CART_ADD = "shoppingCartAdd";

	/** The Constant SHOPPING_CART_DELETE. */
	public static final String SHOPPING_CART_DELETE = "shoppingCartDelete";

	/** The Constant SHOPPING_CART_EXPORT. */
	public static final String SHOPPING_CART_EXPORT = "shoppingCartExport";

	/** The Constant NEWUSERFORM. */
	public static final String NEWUSERFORM = "newUserForm";

	/** The Constant REDIRECT_TO_SPECIMEN. */
	public static final String REDIRECT_TO_SPECIMEN = "specimenRedirect";

	/** The Constant CALLED_FROM. */
	public static final String CALLED_FROM = "calledFrom";

	/** The Constant ACCESS. */
	public static final String ACCESS = "access";

	//Constants required for Forgot Password
	/** The Constant FORGOT_PASSWORD. */
	public static final String FORGOT_PASSWORD = "forgotpassword";

	/** The Constant LOGINNAME. */
	public static final String LOGINNAME = "loginName";

	/** The Constant LASTNAME. */
	public static final String LASTNAME = "lastName";

	/** The Constant FIRSTNAME. */
	public static final String FIRSTNAME = "firstName";

	/** The Constant INSTITUTION. */
	public static final String INSTITUTION = "institution";

	/** The Constant EMAIL. */
	public static final String EMAIL = "email";

	/** The Constant DEPARTMENT. */
	public static final String DEPARTMENT = "department";

	/** The Constant ADDRESS. */
	public static final String ADDRESS = "address";

	/** The Constant CITY. */
	public static final String CITY = "city";

	/** The Constant STATE. */
	public static final String STATE = "state";

	/** The Constant COUNTRY. */
	public static final String COUNTRY = "country";

	/** The Constant NEXT_CONTAINER_NO. */
	public static final String NEXT_CONTAINER_NO = "startNumber";

	/** The Constant CSM_USER_ID. */
	public static final String CSM_USER_ID = "csmUserId";

	/** The Constant INSTITUTIONLIST. */
	public static final String INSTITUTIONLIST = "institutionList";

	/** The Constant DEPARTMENTLIST. */
	public static final String DEPARTMENTLIST = "departmentList";

	/** The Constant STATELIST. */
	public static final String STATELIST = "stateList";

	/** The Constant COUNTRYLIST. */
	public static final String COUNTRYLIST = "countryList";

	/** The Constant ROLELIST. */
	public static final String ROLELIST = "roleList";

	/** The Constant ROLEIDLIST. */
	public static final String ROLEIDLIST = "roleIdList";

	/** The Constant CANCER_RESEARCH_GROUP_LIST. */
	public static final String CANCER_RESEARCH_GROUP_LIST = "cancerResearchGroupList";

	/** The Constant GENDER_LIST. */
	public static final String GENDER_LIST = "genderList";

	/** The Constant GENOTYPE_LIST. */
	public static final String GENOTYPE_LIST = "genotypeList";

	/** The Constant ETHNICITY_LIST. */
	public static final String ETHNICITY_LIST = "ethnicityList";

	/** The Constant PARTICIPANT_MEDICAL_RECORD_SOURCE_LIST. */
	public static final String PARTICIPANT_MEDICAL_RECORD_SOURCE_LIST = "participantMedicalRecordSourceList";

	/** The Constant RACELIST. */
	public static final String RACELIST = "raceList";

	/** The Constant VITAL_STATUS_LIST. */
	public static final String VITAL_STATUS_LIST = "vitalStatusList";

	/** The Constant PARTICIPANT_LIST. */
	public static final String PARTICIPANT_LIST = "participantList";

	/** The Constant PARTICIPANT_ID_LIST. */
	public static final String PARTICIPANT_ID_LIST = "participantIdList";

	/** The Constant PROTOCOL_LIST. */
	public static final String PROTOCOL_LIST = "protocolList";

	/** The Constant TIMEHOURLIST. */
	public static final String TIMEHOURLIST = "timeHourList";

	/** The Constant TIMEMINUTESLIST. */
	public static final String TIMEMINUTESLIST = "timeMinutesList";

	/** The Constant TIMEAMPMLIST. */
	public static final String TIMEAMPMLIST = "timeAMPMList";

	/** The Constant RECEIVEDBYLIST. */
	public static final String RECEIVEDBYLIST = "receivedByList";

	/** The Constant COLLECTEDBYLIST. */
	public static final String COLLECTEDBYLIST = "collectedByList";

	/** The Constant COLLECTIONSITELIST. */
	public static final String COLLECTIONSITELIST = "collectionSiteList";

	/** The Constant RECEIVEDSITELIST. */
	public static final String RECEIVEDSITELIST = "receivedSiteList";

	/** The Constant RECEIVEDMODELIST. */
	public static final String RECEIVEDMODELIST = "receivedModeList";

	/** The Constant ACTIVITYSTATUSLIST. */
	public static final String ACTIVITYSTATUSLIST = "activityStatusList";

	/** The Constant COLLECTIONSTATUSLIST. */
	public static final String COLLECTIONSTATUSLIST = "collectionStatusList";

	/** The Constant USERLIST. */
	public static final String USERLIST = "userList";

	/** The Constant ACTIONLIST. */
	public static final String ACTIONLIST = "actionsList";

	/** The Constant SITETYPELIST. */
	public static final String SITETYPELIST = "siteTypeList";

	/** The Constant STORAGETYPELIST. */
	public static final String STORAGETYPELIST = "storageTypeList";

	/** The Constant STORAGECONTAINERLIST. */
	public static final String STORAGECONTAINERLIST = "storageContainerList";

	/** The Constant SITELIST. */
	public static final String SITELIST = "siteList";

	/** The Constant CPLIST. */
	public static final String CPLIST = "cpList";

	/** The Constant REPOSITORY. */
	public static final String REPOSITORY = "Repository";

	//	public static final String SITEIDLIST="siteIdList";
	/** The Constant USERIDLIST. */
	public static final String USERIDLIST = "userIdList";

	/** The Constant STORAGETYPEIDLIST. */
	public static final String STORAGETYPEIDLIST = "storageTypeIdList";

	/** The Constant SPECIMENCOLLECTIONLIST. */
	public static final String SPECIMENCOLLECTIONLIST = "specimentCollectionList";

	/** The Constant PARTICIPANT_IDENTIFIER_IN_CPR. */
	public static final String PARTICIPANT_IDENTIFIER_IN_CPR = "participant";

	/** The Constant APPROVE_USER_STATUS_LIST. */
	public static final String APPROVE_USER_STATUS_LIST = "statusList";

	/** The Constant EVENT_PARAMETERS_LIST. */
	public static final String EVENT_PARAMETERS_LIST = "eventParametersList";

	/** The Constant PRIVILEGE_DATA_LIST_ONLOAD. */
	public static final String PRIVILEGE_DATA_LIST_ONLOAD = "listOfPrivilegeDataOnLoad";

	//New Specimen lists.
	/** The Constant SPECIMEN_COLLECTION_GROUP_LIST. */
	public static final String SPECIMEN_COLLECTION_GROUP_LIST = "specimenCollectionGroupIdList";

	/** The Constant SPECIMEN_TYPE_LIST. */
	public static final String SPECIMEN_TYPE_LIST = "specimenTypeList";
	
	public static final String TISSUE_TYPE_LIST = "tissueTypeList";
	public static final String CELL_TYPE_LIST = "cellTypeList";
	public static final String FLUID_TYPE_LIST = "fluiTypeList";
	public static final String MOLECULAR_TYPE_LIST = "molecularTypeList";
	

	/** The Constant SPECIMEN_SUB_TYPE_LIST. */
	public static final String SPECIMEN_SUB_TYPE_LIST = "specimenSubTypeList";

	/** The Constant TISSUE_SITE_LIST. */
	public static final String TISSUE_SITE_LIST = "tissueSiteList";

	/** The Constant TISSUE_SIDE_LIST. */
	public static final String TISSUE_SIDE_LIST = "tissueSideList";

	/** The Constant PATHOLOGICAL_STATUS_LIST. */
	public static final String PATHOLOGICAL_STATUS_LIST = "pathologicalStatusList";

	/** The Constant BIOHAZARD_TYPE_LIST. */
	public static final String BIOHAZARD_TYPE_LIST = "biohazardTypeList";

	/** The Constant BIOHAZARD_NAME_LIST. */
	public static final String BIOHAZARD_NAME_LIST = "biohazardNameList";

	/** The Constant BIOHAZARD_ID_LIST. */
	public static final String BIOHAZARD_ID_LIST = "biohazardIdList";

	/** The Constant BIOHAZARD_TYPES_LIST. */
	public static final String BIOHAZARD_TYPES_LIST = "biohazardTypesList";

	/** The Constant PARENT_SPECIMEN_ID_LIST. */
	public static final String PARENT_SPECIMEN_ID_LIST = "parentSpecimenIdList";

	/** The Constant RECEIVED_QUALITY_LIST. */
	public static final String RECEIVED_QUALITY_LIST = "receivedQualityList";

	/** The Constant SPECIMEN_COLL_GP_NAME. */
	public static final String SPECIMEN_COLL_GP_NAME = "specimenCollectionGroupName";

	//SpecimenCollecionGroup lists.
	/** The Constant PROTOCOL_TITLE_LIST. */
	public static final String PROTOCOL_TITLE_LIST = "protocolTitleList";

	/** The Constant PARTICIPANT_NAME_LIST. */
	public static final String PARTICIPANT_NAME_LIST = "participantNameList";

	/** The Constant PROTOCOL_PARTICIPANT_NUMBER_LIST. */
	public static final String PROTOCOL_PARTICIPANT_NUMBER_LIST = "protocolParticipantNumberList";
	//public static final String PROTOCOL_PARTICIPANT_NUMBER_ID_LIST = "protocolParticipantNumberIdList";
	/** The Constant STUDY_CALENDAR_EVENT_POINT_LIST. */
	public static final String STUDY_CALENDAR_EVENT_POINT_LIST = "studyCalendarEventPointList";
	//public static final String STUDY_CALENDAR_EVENT_POINT_ID_LIST="studyCalendarEventPointIdList";
	/** The Constant PARTICIPANT_MEDICAL_IDNETIFIER_LIST. */
	public static final String PARTICIPANT_MEDICAL_IDNETIFIER_LIST = "participantMedicalIdentifierArray";
	//public static final String PARTICIPANT_MEDICAL_IDNETIFIER_ID_LIST = "participantMedicalIdentifierIdArray";
	/** The Constant SPECIMEN_COLLECTION_GROUP_ID. */
	public static final String SPECIMEN_COLLECTION_GROUP_ID = "specimenCollectionGroupId";

	/** The Constant REQ_PATH. */
	public static final String REQ_PATH = "redirectTo";

	/** The Constant CLINICAL_STATUS_LIST. */
	public static final String CLINICAL_STATUS_LIST = "cinicalStatusList";
	
	/** The Constant CDE_NAME_COLLECTION_STATUS. */
	public static final String CDE_NAME_COLLECTION_STATUS = "Collection Status";
	
	
	/** The Constant SPECIMEN_CLASS_LIST. */
	public static final String SPECIMEN_CLASS_LIST = "specimenClassList";

	/** The Constant SPECIMEN_CLASS_ID_LIST. */
	public static final String SPECIMEN_CLASS_ID_LIST = "specimenClassIdList";

	/** The Constant SPECIMEN_TYPE_MAP. */
	public static final String SPECIMEN_TYPE_MAP = "specimenTypeMap";
	//Simple Query Interface Lists
	/** The Constant OBJECT_COMPLETE_NAME_LIST. */
	public static final String OBJECT_COMPLETE_NAME_LIST = "objectCompleteNameList";

	/** The Constant SIMPLE_QUERY_INTERFACE_TITLE. */
	public static final String SIMPLE_QUERY_INTERFACE_TITLE = "simpleQueryInterfaceTitle";

	/** The Constant MAP_OF_STORAGE_CONTAINERS. */
	public static final String MAP_OF_STORAGE_CONTAINERS = "storageContainerMap";

	/** The Constant STORAGE_CONTAINER_GRID_OBJECT. */
	public static final String STORAGE_CONTAINER_GRID_OBJECT = "storageContainerGridObject";

	/** The Constant STORAGE_CONTAINER_CHILDREN_STATUS. */
	public static final String STORAGE_CONTAINER_CHILDREN_STATUS = "storageContainerChildrenStatus";

	/** The Constant START_NUMBER. */
	public static final String START_NUMBER = "startNumber";

	/** The Constant CHILD_CONTAINER_SYSTEM_IDENTIFIERS. */
	public static final String CHILD_CONTAINER_SYSTEM_IDENTIFIERS = "childContainerIds";

	/** The Constant STORAGE_CONTAINER_FIRST_ROW. */
	public static final int STORAGE_CONTAINER_FIRST_ROW = 1;

	/** The Constant STORAGE_CONTAINER_FIRST_COLUMN. */
	public static final int STORAGE_CONTAINER_FIRST_COLUMN = 1;

	/** The Constant MAP_COLLECTION_PROTOCOL_LIST. */
	public static final String MAP_COLLECTION_PROTOCOL_LIST = "collectionProtocolList";
	
	/** The list of Collection Protocols Association Types with Another CP*/
	public static final String[] CP_ASSOCIATION_TYPE_VALUES = {"Parent", "Phase", "Arm", "Cycle"};
	
	public static final String CP_ASSOCIATION_TYPE_LIST = "associationTypeList";

	/** The Constant MAP_SPECIMEN_CLASS_LIST. */
	public static final String MAP_SPECIMEN_CLASS_LIST = "specimenClassList";

	/** The Constant MAP_SPECIMEN_TYPE_LIST. */
	public static final String MAP_SPECIMEN_TYPE_LIST = "specimenTypeList";

	/** The Constant CONTENT_OF_CONTAINNER. */
	public static final String CONTENT_OF_CONTAINNER = "contentOfContainer";

	//event parameters lists
	/** The Constant METHOD_LIST. */
	public static final String METHOD_LIST = "methodList";

	/** The Constant HOUR_LIST. */
	public static final String HOUR_LIST = "hourList";

	/** The Constant MINUTES_LIST. */
	public static final String MINUTES_LIST = "minutesList";

	/** The Constant EMBEDDING_MEDIUM_LIST. */
	public static final String EMBEDDING_MEDIUM_LIST = "embeddingMediumList";

	/** The Constant PROCEDURE_LIST. */
	public static final String PROCEDURE_LIST = "procedureList";

	/** The Constant PROCEDUREIDLIST. */
	public static final String PROCEDUREIDLIST = "procedureIdList";

	/** The Constant CONTAINER_LIST. */
	public static final String CONTAINER_LIST = "containerList";

	/** The Constant CONTAINERIDLIST. */
	public static final String CONTAINERIDLIST = "containerIdList";

	/** The Constant FROMCONTAINERLIST. */
	public static final String FROMCONTAINERLIST = "fromContainerList";

	/** The Constant TOCONTAINERLIST. */
	public static final String TOCONTAINERLIST = "toContainerList";

	/** The Constant FIXATION_LIST. */
	public static final String FIXATION_LIST = "fixationList";

	/** The Constant FROM_SITE_LIST. */
	public static final String FROM_SITE_LIST = "fromsiteList";

	/** The Constant TO_SITE_LIST. */
	public static final String TO_SITE_LIST = "tositeList";

	/** The Constant ITEMLIST. */
	public static final String ITEMLIST = "itemList";

	/** The Constant DISTRIBUTIONPROTOCOLLIST. */
	public static final String DISTRIBUTIONPROTOCOLLIST = "distributionProtocolList";

	/** The Constant TISSUE_SPECIMEN_ID_LIST. */
	public static final String TISSUE_SPECIMEN_ID_LIST = "tissueSpecimenIdList";

	/** The Constant MOLECULAR_SPECIMEN_ID_LIST. */
	public static final String MOLECULAR_SPECIMEN_ID_LIST = "molecularSpecimenIdList";

	/** The Constant CELL_SPECIMEN_ID_LIST. */
	public static final String CELL_SPECIMEN_ID_LIST = "cellSpecimenIdList";

	/** The Constant FLUID_SPECIMEN_ID_LIST. */
	public static final String FLUID_SPECIMEN_ID_LIST = "fluidSpecimenIdList";

	/** The Constant STORAGE_STATUS_LIST. */
	public static final String STORAGE_STATUS_LIST = "storageStatusList";

	/** The Constant CLINICAL_DIAGNOSIS_LIST. */
	public static final String CLINICAL_DIAGNOSIS_LIST = "clinicalDiagnosisList";

	/** The Constant HISTOLOGICAL_QUALITY_LIST. */
	public static final String HISTOLOGICAL_QUALITY_LIST = "histologicalQualityList";

	//For Specimen Event Parameters.
	/** The Constant REQUEST_TO_ORDER. */
	public static final String REQUEST_TO_ORDER = "requestToOrder";

	/** The Constant ADD_TO_ORDER_LIST. */
	public static final String ADD_TO_ORDER_LIST = "addToOrderList";

	/** The Constant REQUESTED_FOR_BIOSPECIMENS. */
	public static final String REQUESTED_FOR_BIOSPECIMENS = "RequestedBioSpecimens";

	/** The Constant DEFINE_ARRAY_FORM_OBJECTS. */
	public static final String DEFINE_ARRAY_FORM_OBJECTS = "DefineArrayFormObjects";

	/** The Constant SPECIMEN_ID. */
	public static final String SPECIMEN_ID = "specimenId";

	/** The Constant SPECIMEN_ARRAY_ID. */
	public static final String SPECIMEN_ARRAY_ID = "specimenArrayIds";

	/** The Constant PATHALOGICAL_CASE_ID. */
	public static final String PATHALOGICAL_CASE_ID = "pathalogicalCaseIds";

	/** The Constant DEIDENTIFIED_PATHALOGICAL_CASE_ID. */
	public static final String DEIDENTIFIED_PATHALOGICAL_CASE_ID = "deidentifiedPathalogicalCaseIds";

	/** The Constant SURGICAL_PATHALOGY_CASE_ID. */
	public static final String SURGICAL_PATHALOGY_CASE_ID = "surgicalPathalogicalCaseIds";

	/** The Constant FROM_POSITION_DATA. */
	public static final String FROM_POSITION_DATA = "fromPositionData";

	/** The Constant POS_ONE. */
	public static final String POS_ONE = "posOne";

	/** The Constant POS_TWO. */
	public static final String POS_TWO = "posTwo";

	/** The Constant STORAGE_CONTAINER_ID. */
	public static final String STORAGE_CONTAINER_ID = "storContId";

	/** The Constant IS_RNA. */
	public static final String IS_RNA = "isRNA";

	/** The Constant RNA. */
	public static final String RNA = "RNA";

	//	New Participant Event Parameters
	/** The Constant PARTICIPANT_ID. */
	public static final String PARTICIPANT_ID = "participantId";

	//Constants required in User.jsp Page
	/** The Constant USER_SEARCH_ACTION. */
	public static final String USER_SEARCH_ACTION = "UserSearch.do";

	/** The Constant USER_ADD_ACTION. */
	public static final String USER_ADD_ACTION = "UserAdd.do";

	/** The Constant USER_EDIT_ACTION. */
	public static final String USER_EDIT_ACTION = "UserEdit.do";

	/** The Constant APPROVE_USER_ADD_ACTION. */
	public static final String APPROVE_USER_ADD_ACTION = "ApproveUserAdd.do";

	/** The Constant APPROVE_USER_EDIT_ACTION. */
	public static final String APPROVE_USER_EDIT_ACTION = "ApproveUserEdit.do";

	/** The Constant SIGNUP_USER_ADD_ACTION. */
	public static final String SIGNUP_USER_ADD_ACTION = "SignUpUserAdd.do";

	/** The Constant USER_EDIT_PROFILE_ACTION. */
	public static final String USER_EDIT_PROFILE_ACTION = "UserEditProfile.do";

	/** The Constant UPDATE_PASSWORD_ACTION. */
	public static final String UPDATE_PASSWORD_ACTION = "UpdatePassword.do";

	/** The Constant FORGOT_PASSWORD_ACTION. */
	public static final String FORGOT_PASSWORD_ACTION = "ForgotPassword.do";

	/** The Constant OPEN_PAGE_IN_CPFRAME. */
	public static final String OPEN_PAGE_IN_CPFRAME = "openInCPFrame";

	/** The Constant DUMMY_PASSWORD. */
	public static final String DUMMY_PASSWORD = "XXXXXX";
	//Constants required in Accession.jsp Page
	/** The Constant ACCESSION_SEARCH_ACTION. */
	public static final String ACCESSION_SEARCH_ACTION = "AccessionSearch.do";

	/** The Constant ACCESSION_ADD_ACTION. */
	public static final String ACCESSION_ADD_ACTION = "AccessionAdd.do";

	/** The Constant ACCESSION_EDIT_ACTION. */
	public static final String ACCESSION_EDIT_ACTION = "AccessionEdit.do";

	//Constants required in StorageType.jsp Page
	/** The Constant STORAGE_TYPE_SEARCH_ACTION. */
	public static final String STORAGE_TYPE_SEARCH_ACTION = "StorageTypeSearch.do";

	/** The Constant STORAGE_TYPE_ADD_ACTION. */
	public static final String STORAGE_TYPE_ADD_ACTION = "StorageTypeAdd.do";

	/** The Constant STORAGE_TYPE_EDIT_ACTION. */
	public static final String STORAGE_TYPE_EDIT_ACTION = "StorageTypeEdit.do";

	//Constants required in StorageContainer.jsp Page
	/** The Constant STORAGE_CONTAINER_SEARCH_ACTION. */
	public static final String STORAGE_CONTAINER_SEARCH_ACTION = "StorageContainerSearch.do";

	/** The Constant STORAGE_CONTAINER_ADD_ACTION. */
	public static final String STORAGE_CONTAINER_ADD_ACTION = "StorageContainerAdd.do";

	/** The Constant STORAGE_CONTAINER_EDIT_ACTION. */
	public static final String STORAGE_CONTAINER_EDIT_ACTION = "StorageContainerEdit.do";

	/** The Constant HOLDS_LIST1. */
	public static final String HOLDS_LIST1 = "HoldsList1";

	/** The Constant HOLDS_LIST2. */
	public static final String HOLDS_LIST2 = "HoldsList2";

	/** The Constant HOLDS_LIST3. */
	public static final String HOLDS_LIST3 = "HoldsList3";

	/** The Constant HOLDS_LIST4. */
	public static final String HOLDS_LIST4 = "HoldsList4";
	//Constants required in Site.jsp Page
	/** The Constant SITE_SEARCH_ACTION. */
	public static final String SITE_SEARCH_ACTION = "SiteSearch.do";

	/** The Constant SITE_ADD_ACTION. */
	public static final String SITE_ADD_ACTION = "SiteAdd.do";

	/** The Constant SITE_EDIT_ACTION. */
	public static final String SITE_EDIT_ACTION = "SiteEdit.do";

	//Constants required in Site.jsp Page
	/** The Constant BIOHAZARD_SEARCH_ACTION. */
	public static final String BIOHAZARD_SEARCH_ACTION = "BiohazardSearch.do";

	/** The Constant BIOHAZARD_ADD_ACTION. */
	public static final String BIOHAZARD_ADD_ACTION = "BiohazardAdd.do";

	/** The Constant BIOHAZARD_EDIT_ACTION. */
	public static final String BIOHAZARD_EDIT_ACTION = "BiohazardEdit.do";

	//Constants required in Partcipant.jsp Page
	/** The Constant PARTICIPANT_SEARCH_ACTION. */
	public static final String PARTICIPANT_SEARCH_ACTION = "ParticipantSearch.do";

	/** The Constant PARTICIPANT_ADD_ACTION. */
	public static final String PARTICIPANT_ADD_ACTION = "ParticipantAdd.do";

	/** The Constant PARTICIPANT_EDIT_ACTION. */
	public static final String PARTICIPANT_EDIT_ACTION = "ParticipantEdit.do";

	/** The Constant PARTICIPANT_LOOKUP_ACTION. */
	public static final String PARTICIPANT_LOOKUP_ACTION = "ParticipantLookup.do";

	/** The Constant PARTICIPANT_CONSENT_ENTER_RESPONSE. */
	public static final String PARTICIPANT_CONSENT_ENTER_RESPONSE = "Enter Response";

	/** The Constant PARTICIPANT_CONSENT_EDIT_RESPONSE. */
	public static final String PARTICIPANT_CONSENT_EDIT_RESPONSE = "Edit Response";

	//Constants required in Institution.jsp Page
	/** The Constant INSTITUTION_SEARCH_ACTION. */
	public static final String INSTITUTION_SEARCH_ACTION = "InstitutionSearch.do";

	/** The Constant INSTITUTION_ADD_ACTION. */
	public static final String INSTITUTION_ADD_ACTION = "InstitutionAdd.do";

	/** The Constant INSTITUTION_EDIT_ACTION. */
	public static final String INSTITUTION_EDIT_ACTION = "InstitutionEdit.do";

	//Constants required in Department.jsp Page
	/** The Constant DEPARTMENT_SEARCH_ACTION. */
	public static final String DEPARTMENT_SEARCH_ACTION = "DepartmentSearch.do";

	/** The Constant DEPARTMENT_ADD_ACTION. */
	public static final String DEPARTMENT_ADD_ACTION = "DepartmentAdd.do";

	/** The Constant DEPARTMENT_EDIT_ACTION. */
	public static final String DEPARTMENT_EDIT_ACTION = "DepartmentEdit.do";

	//Constants required in CollectionProtocolRegistration.jsp Page
	/** The Constant COLLECTION_PROTOCOL_REGISTRATION_SEARCH_ACTION. */
	public static final String COLLECTION_PROTOCOL_REGISTRATION_SEARCH_ACTION = "CollectionProtocolRegistrationSearch.do";

	/** The Constant COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION. */
	public static final String COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION = "CollectionProtocolRegistrationAdd.do";

	/** The Constant COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION. */
	public static final String COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "CollectionProtocolRegistrationEdit.do";

	//Constants required in CancerResearchGroup.jsp Page
	/** The Constant CANCER_RESEARCH_GROUP_SEARCH_ACTION. */
	public static final String CANCER_RESEARCH_GROUP_SEARCH_ACTION = "CancerResearchGroupSearch.do";

	/** The Constant CANCER_RESEARCH_GROUP_ADD_ACTION. */
	public static final String CANCER_RESEARCH_GROUP_ADD_ACTION = "CancerResearchGroupAdd.do";

	/** The Constant CANCER_RESEARCH_GROUP_EDIT_ACTION. */
	public static final String CANCER_RESEARCH_GROUP_EDIT_ACTION = "CancerResearchGroupEdit.do";

	//Constants required for Approve user
	/** The Constant USER_DETAILS_SHOW_ACTION. */
	public static final String USER_DETAILS_SHOW_ACTION = "UserDetailsShow.do";

	/** The Constant APPROVE_USER_SHOW_ACTION. */
	public static final String APPROVE_USER_SHOW_ACTION = "ApproveUserShow.do";

	//Reported Problem Constants
	/** The Constant REPORTED_PROBLEM_ADD_ACTION. */
	public static final String REPORTED_PROBLEM_ADD_ACTION = "ReportedProblemAdd.do";

	/** The Constant REPORTED_PROBLEM_EDIT_ACTION. */
	public static final String REPORTED_PROBLEM_EDIT_ACTION = "ReportedProblemEdit.do";

	/** The Constant PROBLEM_DETAILS_ACTION. */
	public static final String PROBLEM_DETAILS_ACTION = "ProblemDetails.do";

	/** The Constant REPORTED_PROBLEM_SHOW_ACTION. */
	public static final String REPORTED_PROBLEM_SHOW_ACTION = "ReportedProblemShow.do";

	//Query Results view Actions
	/** The Constant TREE_VIEW_ACTION. */
	public static final String TREE_VIEW_ACTION = "TreeView.do";
	//-- StorageContainerTreeAction added for DHTMLX tree grid view of storage container
	/** The Constant STORAGE_CONTAINER_TREE_ACTION. */
	public static final String STORAGE_CONTAINER_TREE_ACTION = "StorageContainerTree.do";

	/** The Constant DATA_VIEW_FRAME_ACTION. */
	public static final String DATA_VIEW_FRAME_ACTION = "SpreadsheetView.do";

	/** The Constant SIMPLE_QUERY_INTERFACE_URL. */
	public static final String SIMPLE_QUERY_INTERFACE_URL = "SimpleQueryInterface.do?pageOf=pageOfSimpleQueryInterface&menuSelected=17";

	//New Specimen Data Actions.
	/** The Constant SPECIMEN_ADD_ACTION. */
	public static final String SPECIMEN_ADD_ACTION = "NewSpecimenAdd.do";

	/** The Constant SPECIMEN_EDIT_ACTION. */
	public static final String SPECIMEN_EDIT_ACTION = "NewSpecimenEdit.do";

	/** The Constant SPECIMEN_SEARCH_ACTION. */
	public static final String SPECIMEN_SEARCH_ACTION = "NewSpecimenSearch.do";

	/** The Constant SPECIMEN_EVENT_PARAMETERS_ACTION. */
	public static final String SPECIMEN_EVENT_PARAMETERS_ACTION = "ListSpecimenEventParameters.do";

	/** The Constant CREATE_SPECIMEN_EDIT_ACTION. */
	public static final String CREATE_SPECIMEN_EDIT_ACTION = "CreateSpecimenEdit.do";

	/** The Constant CREATE_SPECIMEN_SEARCH_ACTION. */
	public static final String CREATE_SPECIMEN_SEARCH_ACTION = "CreateSpecimenSearch.do";

	//ShoppingCart Actions.
	/** The Constant SHOPPING_CART_OPERATION. */
	public static final String SHOPPING_CART_OPERATION = "ShoppingCartOperation.do";

	/** The Constant SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION. */
	public static final String SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION = "SpecimenCollectionGroup.do";

	/** The Constant SPECIMEN_COLLECTION_GROUP_ADD_ACTION. */
	public static final String SPECIMEN_COLLECTION_GROUP_ADD_ACTION = "SpecimenCollectionGroupAdd.do";

	/** The Constant SPECIMEN_COLLECTION_GROUP_EDIT_ACTION. */
	public static final String SPECIMEN_COLLECTION_GROUP_EDIT_ACTION = "SpecimenCollectionGroupEdit.do";

	//Constants required in FrozenEventParameters.jsp Page
	/** The Constant FROZEN_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String FROZEN_EVENT_PARAMETERS_SEARCH_ACTION = "FrozenEventParametersSearch.do";

	/** The Constant FROZEN_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String FROZEN_EVENT_PARAMETERS_ADD_ACTION = "FrozenEventParametersAdd.do";

	/** The Constant FROZEN_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String FROZEN_EVENT_PARAMETERS_EDIT_ACTION = "FrozenEventParametersEdit.do";

	//Constants required in CheckInCheckOutEventParameters.jsp Page
	/** The Constant CHECKIN_CHECKOUT_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_SEARCH_ACTION = "CheckInCheckOutEventParametersSearch.do";

	/** The Constant CHECKIN_CHECKOUT_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_ADD_ACTION = "CheckInCheckOutEventParametersAdd.do";

	/** The Constant CHECKIN_CHECKOUT_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_EDIT_ACTION = "CheckInCheckOutEventParametersEdit.do";

	//Constants required in ReceivedEventParameters.jsp Page
	/** The Constant RECEIVED_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String RECEIVED_EVENT_PARAMETERS_SEARCH_ACTION = "receivedEventParametersSearch.do";

	/** The Constant RECEIVED_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String RECEIVED_EVENT_PARAMETERS_ADD_ACTION = "ReceivedEventParametersAdd.do";

	/** The Constant RECEIVED_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String RECEIVED_EVENT_PARAMETERS_EDIT_ACTION = "receivedEventParametersEdit.do";

	//Constants required in FluidSpecimenReviewEventParameters.jsp Page
	/** The Constant FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION = "FluidSpecimenReviewEventParametersSearch.do";

	/** The Constant FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION = "FluidSpecimenReviewEventParametersAdd.do";

	/** The Constant FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION = "FluidSpecimenReviewEventParametersEdit.do";

	//Constants required in CELLSPECIMENREVIEWParameters.jsp Page
	/** The Constant CELL_SPECIMEN_REVIEW_PARAMETERS_SEARCH_ACTION. */
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_SEARCH_ACTION = "CellSpecimenReviewParametersSearch.do";

	/** The Constant CELL_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION. */
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION = "CellSpecimenReviewParametersAdd.do";

	/** The Constant CELL_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION. */
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION = "CellSpecimenReviewParametersEdit.do";

	//Constants required in tissue SPECIMEN REVIEW event Parameters.jsp Page
	/** The Constant TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION = "TissueSpecimenReviewEventParametersSearch.do";

	/** The Constant TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION = "TissueSpecimenReviewEventParametersAdd.do";

	/** The Constant TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION = "TissueSpecimenReviewEventParametersEdit.do";

	//	Constants required in DisposalEventParameters.jsp Page
	/** The Constant DISPOSAL_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String DISPOSAL_EVENT_PARAMETERS_SEARCH_ACTION = "DisposalEventParametersSearch.do";

	/** The Constant DISPOSAL_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String DISPOSAL_EVENT_PARAMETERS_ADD_ACTION = "DisposalEventParametersAdd.do";

	/** The Constant DISPOSAL_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String DISPOSAL_EVENT_PARAMETERS_EDIT_ACTION = "DisposalEventParametersEdit.do";

	//	Constants required in DisposalEventParameters
	/** The Constant DISPOSAL_EVENT_PARAMETERS. */
	public static final String DISPOSAL_EVENT_PARAMETERS = "DisposalEventParameters";
	//	Constants required in ThawEventParameters.jsp Page
	/** The Constant THAW_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String THAW_EVENT_PARAMETERS_SEARCH_ACTION = "ThawEventParametersSearch.do";

	/** The Constant THAW_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String THAW_EVENT_PARAMETERS_ADD_ACTION = "ThawEventParametersAdd.do";

	/** The Constant THAW_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String THAW_EVENT_PARAMETERS_EDIT_ACTION = "ThawEventParametersEdit.do";

	//	Constants required in MOLECULARSPECIMENREVIEWParameters.jsp Page
	/** The Constant MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_SEARCH_ACTION. */
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_SEARCH_ACTION = "MolecularSpecimenReviewParametersSearch.do";

	/** The Constant MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION. */
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION = "MolecularSpecimenReviewParametersAdd.do";

	/** The Constant MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION. */
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION = "MolecularSpecimenReviewParametersEdit.do";

	//	Constants required in CollectionEventParameters.jsp Page
	/** The Constant COLLECTION_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String COLLECTION_EVENT_PARAMETERS_SEARCH_ACTION = "CollectionEventParametersSearch.do";

	/** The Constant COLLECTION_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String COLLECTION_EVENT_PARAMETERS_ADD_ACTION = "CollectionEventParametersAdd.do";

	/** The Constant COLLECTION_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String COLLECTION_EVENT_PARAMETERS_EDIT_ACTION = "CollectionEventParametersEdit.do";

	//	Constants required in SpunEventParameters.jsp Page
	/** The Constant SPUN_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String SPUN_EVENT_PARAMETERS_SEARCH_ACTION = "SpunEventParametersSearch.do";

	/** The Constant SPUN_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String SPUN_EVENT_PARAMETERS_ADD_ACTION = "SpunEventParametersAdd.do";

	/** The Constant SPUN_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String SPUN_EVENT_PARAMETERS_EDIT_ACTION = "SpunEventParametersEdit.do";

	//	Constants required in EmbeddedEventParameters.jsp Page
	/** The Constant EMBEDDED_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String EMBEDDED_EVENT_PARAMETERS_SEARCH_ACTION = "EmbeddedEventParametersSearch.do";

	/** The Constant EMBEDDED_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String EMBEDDED_EVENT_PARAMETERS_ADD_ACTION = "EmbeddedEventParametersAdd.do";

	/** The Constant EMBEDDED_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String EMBEDDED_EVENT_PARAMETERS_EDIT_ACTION = "EmbeddedEventParametersEdit.do";

	//	Constants required in TransferEventParameters.jsp Page
	/** The Constant TRANSFER_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String TRANSFER_EVENT_PARAMETERS_SEARCH_ACTION = "TransferEventParametersSearch.do";

	/** The Constant TRANSFER_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String TRANSFER_EVENT_PARAMETERS_ADD_ACTION = "TransferEventParametersAdd.do";

	/** The Constant TRANSFER_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String TRANSFER_EVENT_PARAMETERS_EDIT_ACTION = "TransferEventParametersEdit.do";

	//	Constants required in FixedEventParameters.jsp Page
	/** The Constant FIXED_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String FIXED_EVENT_PARAMETERS_SEARCH_ACTION = "FixedEventParametersSearch.do";

	/** The Constant FIXED_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String FIXED_EVENT_PARAMETERS_ADD_ACTION = "FixedEventParametersAdd.do";

	/** The Constant FIXED_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String FIXED_EVENT_PARAMETERS_EDIT_ACTION = "FixedEventParametersEdit.do";

	//	Constants required in ProcedureEventParameters.jsp Page
	/** The Constant PROCEDURE_EVENT_PARAMETERS_SEARCH_ACTION. */
	public static final String PROCEDURE_EVENT_PARAMETERS_SEARCH_ACTION = "ProcedureEventParametersSearch.do";

	/** The Constant PROCEDURE_EVENT_PARAMETERS_ADD_ACTION. */
	public static final String PROCEDURE_EVENT_PARAMETERS_ADD_ACTION = "ProcedureEventParametersAdd.do";

	/** The Constant PROCEDURE_EVENT_PARAMETERS_EDIT_ACTION. */
	public static final String PROCEDURE_EVENT_PARAMETERS_EDIT_ACTION = "ProcedureEventParametersEdit.do";

	//	Constants required in Distribution.jsp Page
	/** The Constant DISTRIBUTION_SEARCH_ACTION. */
	public static final String DISTRIBUTION_SEARCH_ACTION = "DistributionSearch.do";

	/** The Constant DISTRIBUTION_ADD_ACTION. */
	public static final String DISTRIBUTION_ADD_ACTION = "DistributionAdd.do";

	/** The Constant DISTRIBUTION_EDIT_ACTION. */
	public static final String DISTRIBUTION_EDIT_ACTION = "DistributionEdit.do";

	/** The Constant SPECIMENARRAYTYPE_ADD_ACTION. */
	public static final String SPECIMENARRAYTYPE_ADD_ACTION = "SpecimenArrayTypeAdd.do?operation=add";

	/** The Constant SPECIMENARRAYTYPE_EDIT_ACTION. */
	public static final String SPECIMENARRAYTYPE_EDIT_ACTION = "SpecimenArrayTypeEdit.do?operation=edit";

	/** The Constant ARRAY_DISTRIBUTION_ADD_ACTION. */
	public static final String ARRAY_DISTRIBUTION_ADD_ACTION = "ArrayDistributionAdd.do";

	/** The Constant SPECIMENARRAY_ADD_ACTION. */
	public static final String SPECIMENARRAY_ADD_ACTION = "SpecimenArrayAdd.do";

	/** The Constant SPECIMENARRAY_EDIT_ACTION. */
	public static final String SPECIMENARRAY_EDIT_ACTION = "SpecimenArrayEdit.do";

	//Constants required in Main menu

	/** The HEL p_ file. */
	public static String HELP_FILE = "caTissue_Suite_User_Manual.pdf";
	//	public static String USER_GUIDE = "userguide";
	//	public static String TECHNICAL_GUIDE = "technicalguide";
	//	public static String TRAINING_GUIDE = "trainingguide";
	//	public static String UMLMODEL_GUIDE = "umlmodelguide";
	//	public static String KNOWLEDGECENTER_GUIDE =  "knowledgecenterguide";
	//	public static String GFORGE_GUIDE = "gforgeguide";
	/** The USE r_ guid e_ lin k_ property. */
	public static String USER_GUIDE_LINK_PROPERTY = "userguide.link";

	/** The TECHNICA l_ guid e_ lin k_ property. */
	public static String TECHNICAL_GUIDE_LINK_PROPERTY = "technicalguide.link";

	/** The TRAININ g_ guid e_ lin k_ property. */
	public static String TRAINING_GUIDE_LINK_PROPERTY = "trainingguide.link";

	/** The UM l_ mode l_ lin k_ property. */
	public static String UML_MODEL_LINK_PROPERTY = "umlmodel.link";

	/** The KNOWLEDG e_ cente r_ lin k_ property. */
	public static String KNOWLEDGE_CENTER_LINK_PROPERTY = "knowledgecenter.link";

	/** The KNOWLEDG e_ cente r_ foru m_ lin k_ property. */
	public static String KNOWLEDGE_CENTER_FORUM_LINK_PROPERTY = "knowledgecenterforum.link";

	//Spreadsheet Export Action
	/** The Constant SPREADSHEET_EXPORT_ACTION. */
	public static final String SPREADSHEET_EXPORT_ACTION = "SpreadsheetExport.do";

	//Aliquots Action
	/** The Constant ALIQUOT_ACTION. */
	public static final String ALIQUOT_ACTION = "Aliquots.do";

	/** The Constant CREATE_ALIQUOT_ACTION. */
	public static final String CREATE_ALIQUOT_ACTION = "CreateAliquots.do";

	/** The Constant ALIQUOT_SUMMARY_ACTION. */
	public static final String ALIQUOT_SUMMARY_ACTION = "AliquotSummary.do";

	//Constants related to Aliquots functionality
	/** The Constant PAGE_OF_ALIQUOT. */
	public static final String PAGE_OF_ALIQUOT = "pageOfAliquot";
	
	//Constants related to Aliquots functionality
		/** The Constant PAGE_OF_ALIQUOT. */
		public static final String PAGE_OF_NEW_ALIQUOT = "pageOfNewAliquot";

	/** The Constant PAGE_OF_CREATE_ALIQUOT. */
	public static final String PAGE_OF_CREATE_ALIQUOT = "pageOfCreateAliquot";

	/** The Constant PAGE_OF_ALIQUOT_SUMMARY. */
	public static final String PAGE_OF_ALIQUOT_SUMMARY = "pageOfAliquotSummary";

	/** The Constant AVAILABLE_CONTAINER_MAP. */
	public static final String AVAILABLE_CONTAINER_MAP = "availableContainerMap";

	/** The Constant COMMON_ADD_EDIT. */
	public static final String COMMON_ADD_EDIT = "commonAddEdit";

	//Constants related to SpecimenArrayAliquots functionality
	/** The Constant STORAGE_TYPE_ID. */
	public static final String STORAGE_TYPE_ID = "storageTypeId";

	/** The Constant ALIQUOT_SPECIMEN_ARRAY_TYPE. */
	public static final String ALIQUOT_SPECIMEN_ARRAY_TYPE = "SpecimenArrayType";

	/** The Constant ALIQUOT_SPECIMEN_CLASS. */
	public static final String ALIQUOT_SPECIMEN_CLASS = "SpecimenClass";

	/** The Constant ALIQUOT_SPECIMEN_TYPES. */
	public static final String ALIQUOT_SPECIMEN_TYPES = "SpecimenTypes";

	/** The Constant ALIQUOT_ALIQUOT_COUNTS. */
	public static final String ALIQUOT_ALIQUOT_COUNTS = "AliquotCounts";

	/** The Constant QUERY_SESSION_DATA. */
	public static final String QUERY_SESSION_DATA = "querySessionData";

	//Specimen Array Aliquots pages
	/** The Constant PAGE_OF_SPECIMEN_ARRAY_ALIQUOT. */
	public static final String PAGE_OF_SPECIMEN_ARRAY_ALIQUOT = "pageOfSpecimenArrayAliquot";

	/** The Constant PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT. */
	public static final String PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT = "pageOfSpecimenArrayCreateAliquot";

	/** The Constant PAGE_OF_SPECIMEN_ARRAY_ALIQUOT_SUMMARY. */
	public static final String PAGE_OF_SPECIMEN_ARRAY_ALIQUOT_SUMMARY = "pageOfSpecimenArrayAliquotSummary";

	//Specimen Array Aliquots Action
	/** The Constant SPECIMEN_ARRAY_ALIQUOT_ACTION. */
	public static final String SPECIMEN_ARRAY_ALIQUOT_ACTION = "SpecimenArrayAliquots.do";

	/** The Constant SPECIMEN_ARRAY_CREATE_ALIQUOT_ACTION. */
	public static final String SPECIMEN_ARRAY_CREATE_ALIQUOT_ACTION = "SpecimenArrayCreateAliquots.do";

	//Constants related to QuickEvents functionality
	/** The Constant QUICKEVENTS_ACTION. */
	public static final String QUICKEVENTS_ACTION = "QuickEventsSearch.do";

	/** The Constant QUICKEVENTSPARAMETERS_ACTION. */
	public static final String QUICKEVENTSPARAMETERS_ACTION = "ListSpecimenEventParameters.do";

	//Constants related to Bulk Operation functionality
	/** The Constant CATISSUE_INTSALL_PROPERTIES_FILE. */
	public static final String CATISSUE_INTSALL_PROPERTIES_FILE = "./caTissueInstall.properties";

	/** The Constant PAGE_OF_BULK_OPERATION. */
	public static final String PAGE_OF_BULK_OPERATION = "pageOfBulkOperation";

	/** The Constant BULK_OPERATION_ACTION. */
	public static final String BULK_OPERATION_ACTION = "BulkOperation.do";

	//SimilarContainers Action
	/** The Constant SIMILAR_CONTAINERS_ACTION. */
	public static final String SIMILAR_CONTAINERS_ACTION = "SimilarContainers.do";

	/** The Constant CREATE_SIMILAR_CONTAINERS_ACTION. */
	public static final String CREATE_SIMILAR_CONTAINERS_ACTION = "CreateSimilarContainers.do";

	/** The Constant SIMILAR_CONTAINERS_ADD_ACTION. */
	public static final String SIMILAR_CONTAINERS_ADD_ACTION = "SimilarContainersAdd.do";

	//Constants related to Similar Containsers
	/** The Constant PAGE_OF_SIMILAR_CONTAINERS. */
	public static final String PAGE_OF_SIMILAR_CONTAINERS = "pageOfSimilarContainers";

	/** The Constant PAGE_OF_CREATE_SIMILAR_CONTAINERS. */
	public static final String PAGE_OF_CREATE_SIMILAR_CONTAINERS = "pageOfCreateSimilarContainers";

	//Levels of nodes in query results tree.
	/** The Constant MAX_LEVEL. */
	public static final int MAX_LEVEL = 5;

	/** The Constant MIN_LEVEL. */
	public static final int MIN_LEVEL = 1;

	/** The Constant TABLE_NAME_COLUMN. */
	public static final String TABLE_NAME_COLUMN = "TABLE_NAME";

	//Spreadsheet view Constants in DataViewAction.
	/** The Constant PARTICIPANT. */
	public static final String PARTICIPANT = "Participant";

	/** The Constant ACCESSION. */
	public static final String ACCESSION = "Accession";

	/** The Constant QUERY_PARTICIPANT_SEARCH_ACTION. */
	public static final String QUERY_PARTICIPANT_SEARCH_ACTION = "QueryParticipantSearch.do?id=";

	/** The Constant QUERY_PARTICIPANT_EDIT_ACTION. */
	public static final String QUERY_PARTICIPANT_EDIT_ACTION = "QueryParticipantEdit.do";

	/** The Constant QUERY_COLLECTION_PROTOCOL_SEARCH_ACTION. */
	public static final String QUERY_COLLECTION_PROTOCOL_SEARCH_ACTION = "QueryCollectionProtocolSearch.do?id=";

	/** The Constant QUERY_COLLECTION_PROTOCOL_EDIT_ACTION. */
	public static final String QUERY_COLLECTION_PROTOCOL_EDIT_ACTION = "QueryCollectionProtocolEdit.do";

	/** The Constant QUERY_SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION. */
	public static final String QUERY_SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION = "QuerySpecimenCollectionGroupSearch.do?id=";

	/** The Constant QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION. */
	public static final String QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION = "QuerySpecimenCollectionGroupEdit.do";

	/** The Constant QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION. */
	public static final String QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION = "QuerySpecimenCollectionGroupAdd.do";

	/** The Constant QUERY_SPECIMEN_SEARCH_ACTION. */
	public static final String QUERY_SPECIMEN_SEARCH_ACTION = "QuerySpecimenSearch.do?id=";

	/** The Constant QUERY_SPECIMEN_EDIT_ACTION. */
	public static final String QUERY_SPECIMEN_EDIT_ACTION = "QuerySpecimenEdit.do";
	//public static final String QUERY_ACCESSION_SEARCH_ACTION = "QueryAccessionSearch.do?id=";

	/** The Constant SPECIMEN. */
	public static final String SPECIMEN = "Specimen";

	/** The Constant SEGMENT. */
	public static final String SEGMENT = "Segment";

	/** The Constant SAMPLE. */
	public static final String SAMPLE = "Sample";

	/** The Constant COLLECTION_PROTOCOL_REGISTRATION. */
	public static final String COLLECTION_PROTOCOL_REGISTRATION = "CollectionProtocolRegistration";

	/** The Constant PARTICIPANT_ID_COLUMN. */
	public static final String PARTICIPANT_ID_COLUMN = "PARTICIPANT_ID";

	/** The Constant ACCESSION_ID_COLUMN. */
	public static final String ACCESSION_ID_COLUMN = "ACCESSION_ID";

	/** The Constant SPECIMEN_ID_COLUMN. */
	public static final String SPECIMEN_ID_COLUMN = "SPECIMEN_ID";

	/** The Constant SEGMENT_ID_COLUMN. */
	public static final String SEGMENT_ID_COLUMN = "SEGMENT_ID";

	/** The Constant SAMPLE_ID_COLUMN. */
	public static final String SAMPLE_ID_COLUMN = "SAMPLE_ID";

	//For getting the tables for Simple Query and Fcon Query.
	/** The Constant ADVANCE_QUERY_TABLES. */
	public static final int ADVANCE_QUERY_TABLES = 2;

	//Identifiers for various Form beans
	/** The Constant DEFAULT_BIZ_LOGIC. */
	public static final int DEFAULT_BIZ_LOGIC = 0;

	/** The Constant USER_FORM_ID. */
	public static final int USER_FORM_ID = 1;

	/** The Constant ACCESSION_FORM_ID. */
	public static final int ACCESSION_FORM_ID = 3;

	/** The Constant REPORTED_PROBLEM_FORM_ID. */
	public static final int REPORTED_PROBLEM_FORM_ID = 4;

	/** The Constant INSTITUTION_FORM_ID. */
	public static final int INSTITUTION_FORM_ID = 5;

	/** The Constant APPROVE_USER_FORM_ID. */
	public static final int APPROVE_USER_FORM_ID = 6;

	/** The Constant ACTIVITY_STATUS_FORM_ID. */
	public static final int ACTIVITY_STATUS_FORM_ID = 7;

	/** The Constant DEPARTMENT_FORM_ID. */
	public static final int DEPARTMENT_FORM_ID = 8;

	/** The Constant COLLECTION_PROTOCOL_FORM_ID. */
	public static final int COLLECTION_PROTOCOL_FORM_ID = 9;

	/** The Constant DISTRIBUTIONPROTOCOL_FORM_ID. */
	public static final int DISTRIBUTIONPROTOCOL_FORM_ID = 10;

	/** The Constant STORAGE_CONTAINER_FORM_ID. */
	public static final int STORAGE_CONTAINER_FORM_ID = 11;

	/** The Constant STORAGE_TYPE_FORM_ID. */
	public static final int STORAGE_TYPE_FORM_ID = 12;

	/** The Constant SITE_FORM_ID. */
	public static final int SITE_FORM_ID = 13;

	/** The Constant CANCER_RESEARCH_GROUP_FORM_ID. */
	public static final int CANCER_RESEARCH_GROUP_FORM_ID = 14;

	/** The Constant BIOHAZARD_FORM_ID. */
	public static final int BIOHAZARD_FORM_ID = 15;

	/** The Constant FROZEN_EVENT_PARAMETERS_FORM_ID. */
	public static final int FROZEN_EVENT_PARAMETERS_FORM_ID = 16;

	/** The Constant CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID. */
	public static final int CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID = 17;

	/** The Constant RECEIVED_EVENT_PARAMETERS_FORM_ID. */
	public static final int RECEIVED_EVENT_PARAMETERS_FORM_ID = 18;

	/** The Constant FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID. */
	public static final int FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID = 21;

	/** The Constant CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID. */
	public static final int CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID = 23;

	/** The Constant TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID. */
	public static final int TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID = 24;

	/** The Constant DISPOSAL_EVENT_PARAMETERS_FORM_ID. */
	public static final int DISPOSAL_EVENT_PARAMETERS_FORM_ID = 25;

	/** The Constant THAW_EVENT_PARAMETERS_FORM_ID. */
	public static final int THAW_EVENT_PARAMETERS_FORM_ID = 26;

	/** The Constant MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID. */
	public static final int MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID = 27;

	/** The Constant COLLECTION_EVENT_PARAMETERS_FORM_ID. */
	public static final int COLLECTION_EVENT_PARAMETERS_FORM_ID = 28;

	/** The Constant TRANSFER_EVENT_PARAMETERS_FORM_ID. */
	public static final int TRANSFER_EVENT_PARAMETERS_FORM_ID = 29;

	/** The Constant SPUN_EVENT_PARAMETERS_FORM_ID. */
	public static final int SPUN_EVENT_PARAMETERS_FORM_ID = 30;

	/** The Constant EMBEDDED_EVENT_PARAMETERS_FORM_ID. */
	public static final int EMBEDDED_EVENT_PARAMETERS_FORM_ID = 31;

	/** The Constant FIXED_EVENT_PARAMETERS_FORM_ID. */
	public static final int FIXED_EVENT_PARAMETERS_FORM_ID = 32;

	/** The Constant PROCEDURE_EVENT_PARAMETERS_FORM_ID. */
	public static final int PROCEDURE_EVENT_PARAMETERS_FORM_ID = 33;

	/** The Constant CREATE_SPECIMEN_FORM_ID. */
	public static final int CREATE_SPECIMEN_FORM_ID = 34;

	/** The Constant FORGOT_PASSWORD_FORM_ID. */
	public static final int FORGOT_PASSWORD_FORM_ID = 35;

	/** The Constant SIGNUP_FORM_ID. */
	public static final int SIGNUP_FORM_ID = 36;

	/** The Constant DISTRIBUTION_FORM_ID. */
	public static final int DISTRIBUTION_FORM_ID = 37;

	/** The Constant SPECIMEN_EVENT_PARAMETERS_FORM_ID. */
	public static final int SPECIMEN_EVENT_PARAMETERS_FORM_ID = 38;

	/** The Constant SHOPPING_CART_FORM_ID. */
	public static final int SHOPPING_CART_FORM_ID = 39;

	/** The Constant CONFIGURE_RESULT_VIEW_ID. */
	public static final int CONFIGURE_RESULT_VIEW_ID = 41;

	/** The Constant ADVANCE_QUERY_INTERFACE_ID. */
	public static final int ADVANCE_QUERY_INTERFACE_ID = 42;

	/** The Constant COLLECTION_PROTOCOL_REGISTRATION_FORM_ID. */
	public static final int COLLECTION_PROTOCOL_REGISTRATION_FORM_ID = 19;

	/** The Constant PARTICIPANT_FORM_ID. */
	public static final int PARTICIPANT_FORM_ID = 2;

	/** The Constant SPECIMEN_COLLECTION_GROUP_FORM_ID. */
	public static final int SPECIMEN_COLLECTION_GROUP_FORM_ID = 20;

	/** The Constant NEW_SPECIMEN_FORM_ID. */
	public static final int NEW_SPECIMEN_FORM_ID = 22;

	/** The Constant ALIQUOT_FORM_ID. */
	public static final int ALIQUOT_FORM_ID = 44;

	/** The Constant QUICKEVENTS_FORM_ID. */
	public static final int QUICKEVENTS_FORM_ID = 45;

	/** The Constant LIST_SPECIMEN_EVENT_PARAMETERS_FORM_ID. */
	public static final int LIST_SPECIMEN_EVENT_PARAMETERS_FORM_ID = 46;

	/** The Constant SIMILAR_CONTAINERS_FORM_ID. */
	public static final int SIMILAR_CONTAINERS_FORM_ID = 47; // chetan (13-07-2006)

	/** The Constant SPECIMEN_ARRAY_TYPE_FORM_ID. */
	public static final int SPECIMEN_ARRAY_TYPE_FORM_ID = 48;

	/** The Constant ARRAY_DISTRIBUTION_FORM_ID. */
	public static final int ARRAY_DISTRIBUTION_FORM_ID = 49;

	/** The Constant SPECIMEN_ARRAY_FORM_ID. */
	public static final int SPECIMEN_ARRAY_FORM_ID = 50;

	/** The Constant SPECIMEN_ARRAY_ALIQUOT_FORM_ID. */
	public static final int SPECIMEN_ARRAY_ALIQUOT_FORM_ID = 51;

	/** The Constant ASSIGN_PRIVILEGE_FORM_ID. */
	public static final int ASSIGN_PRIVILEGE_FORM_ID = 52;

	/** The Constant CDE_FORM_ID. */
	public static final int CDE_FORM_ID = 53;

	/** The Constant MULTIPLE_SPECIMEN_STOGAGE_LOCATION_FORM_ID. */
	public static final int MULTIPLE_SPECIMEN_STOGAGE_LOCATION_FORM_ID = 54;

	/** The Constant REQUEST_LIST_FILTERATION_FORM_ID. */
	public static final int REQUEST_LIST_FILTERATION_FORM_ID = 55;

	/** The Constant ORDER_FORM_ID. */
	public static final int ORDER_FORM_ID = 56;

	/** The Constant ORDER_ARRAY_FORM_ID. */
	public static final int ORDER_ARRAY_FORM_ID = 57;

	/** The Constant REQUEST_DETAILS_FORM_ID. */
	public static final int REQUEST_DETAILS_FORM_ID = 64;

	/** The Constant ORDER_PATHOLOGY_FORM_ID. */
	public static final int ORDER_PATHOLOGY_FORM_ID = 58;

	/** The Constant NEW_PATHOLOGY_FORM_ID. */
	public static final int NEW_PATHOLOGY_FORM_ID = 59;

	/** The Constant DEIDENTIFIED_SURGICAL_PATHOLOGY_REPORT_FORM_ID. */
	public static final int DEIDENTIFIED_SURGICAL_PATHOLOGY_REPORT_FORM_ID = 60;

	/** The Constant PATHOLOGY_REPORT_REVIEW_FORM_ID. */
	public static final int PATHOLOGY_REPORT_REVIEW_FORM_ID = 61;

	/** The Constant QUARANTINE_EVENT_PARAMETER_FORM_ID. */
	public static final int QUARANTINE_EVENT_PARAMETER_FORM_ID = 62;

	/** The Constant CONSENT_FORM_ID. */
	public static final int CONSENT_FORM_ID = 63;

	/** The Constant BULK_OPERATIONS_FORM_ID. */
	public static final int BULK_OPERATIONS_FORM_ID = 68;

	/** The Constant SUMMARY_BIZLOGIC_ID. */
	public static final int SUMMARY_BIZLOGIC_ID = 70;
	
	public static final int DISTRIBUTION_EVENT_PARAMETERS_FORM_ID = 71;
	//Misc
	/** The Constant SEPARATOR. */
	public static final String SEPARATOR = " : ";

	//Identifiers for JDBC DAO.
	/** The Constant QUERY_RESULT_TREE_JDBC_DAO. */
	public static final int QUERY_RESULT_TREE_JDBC_DAO = 1;

	//Activity Status values
	//public static final String ACTIVITY_STATUS_APPROVE = "Approve";
	//public static final String ACTIVITY_STATUS_REJECT = "Reject";
	//public static final String ACTIVITY_STATUS_NEW = "New";
	//public static final String ACTIVITY_STATUS_PENDING = "Pending";
	//public static final String ACTIVITY_STATUS_ACTIVE = "Active";
	//public static final String ACTIVITY_STATUS_CLOSED = "Closed";

	//Approve User status values.
	//public static final String APPROVE_USER_APPROVE_STATUS = "Approve";
	//public static final String APPROVE_USER_REJECT_STATUS = "Reject";
	//public static final String APPROVE_USER_PENDING_STATUS = "Pending";

	//Approve User Constants
	/** The Constant ZERO. */
	public static final int ZERO = 0;

	/** The Constant ONE. */
	public static final int ONE = 1;

	/** The Constant START_PAGE. */
	public static final int START_PAGE = 1;

	/** The Constant NUMBER_RESULTS_PER_PAGE. */
	public static final int NUMBER_RESULTS_PER_PAGE = 20;

	/** The Constant NUMBER_RESULTS_PER_PAGE_SEARCH. */
	public static final int NUMBER_RESULTS_PER_PAGE_SEARCH = 15;

	/** The Constant PAGE_NUMBER. */
	public static final String PAGE_NUMBER = "pageNum";

	/** The Constant RESULTS_PER_PAGE. */
	public static final String RESULTS_PER_PAGE = "numResultsPerPage";

	/** The Constant TOTAL_RESULTS. */
	public static final String TOTAL_RESULTS = "totalResults";

	/** The Constant PREVIOUS_PAGE. */
	public static final String PREVIOUS_PAGE = "prevpage";

	/** The Constant NEXT_PAGE. */
	public static final String NEXT_PAGE = "nextPage";

	/** The Constant ORIGINAL_DOMAIN_OBJECT_LIST. */
	public static final String ORIGINAL_DOMAIN_OBJECT_LIST = "originalDomainObjectList";

	/** The Constant SHOW_DOMAIN_OBJECT_LIST. */
	public static final String SHOW_DOMAIN_OBJECT_LIST = "showDomainObjectList";

	/** The Constant USER_DETAILS. */
	public static final String USER_DETAILS = "details";

	/** The Constant CURRENT_RECORD. */
	public static final String CURRENT_RECORD = "currentRecord";

	/** The Constant APPROVE_USER_EMAIL_SUBJECT. */
	public static final String APPROVE_USER_EMAIL_SUBJECT = "Your membership status in caTISSUE Core.";

	//Query Interface Results View Constants
	/** The Constant PAGE_OF_APPROVE_USER. */
	public static final String PAGE_OF_APPROVE_USER = "pageOfApproveUser";

	/** The Constant PAGE_OF_SIGNUP. */
	public static final String PAGE_OF_SIGNUP = "pageOfSignUp";

	/** The Constant PAGE_OF_USERADD. */
	public static final String PAGE_OF_USERADD = "pageOfUserAdd";

	/** The Constant PAGE_OF_USER_ADMIN. */
	public static final String PAGE_OF_USER_ADMIN = "pageOfUserAdmin";

	/** The Constant PAGE_OF_USER_PROFILE. */
	public static final String PAGE_OF_USER_PROFILE = "pageOfUserProfile";

	/** The Constant PAGE_OF_CHANGE_PASSWORD. */
	public static final String PAGE_OF_CHANGE_PASSWORD = "pageOfChangePassword";

	//For Simple Query Interface and Edit.
	/** The Constant PAGE_OF_EDIT_OBJECT. */
	public static final String PAGE_OF_EDIT_OBJECT = "pageOfEditObject";

	//Query results view temporary table columns.
	/** The Constant QUERY_RESULTS_PARTICIPANT_ID. */
	public static final String QUERY_RESULTS_PARTICIPANT_ID = "PARTICIPANT_ID";

	/** The Constant QUERY_RESULTS_COLLECTION_PROTOCOL_ID. */
	public static final String QUERY_RESULTS_COLLECTION_PROTOCOL_ID = "COLLECTION_PROTOCOL_ID";

	/** The Constant QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID. */
	public static final String QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID = "COLLECTION_PROTOCOL_EVENT_ID";

	/** The Constant QUERY_RESULTS_SPECIMEN_COLLECTION_GROUP_ID. */
	public static final String QUERY_RESULTS_SPECIMEN_COLLECTION_GROUP_ID = "SPECIMEN_COLLECTION_GROUP_ID";

	/** The Constant QUERY_RESULTS_SPECIMEN_ID. */
	public static final String QUERY_RESULTS_SPECIMEN_ID = "SPECIMEN_ID";

	/** The Constant QUERY_RESULTS_SPECIMEN_TYPE. */
	public static final String QUERY_RESULTS_SPECIMEN_TYPE = "SPECIMEN_TYPE";

	// Assign Privilege Constants.
	/** The Constant PRIVILEGE_DEASSIGN. */
	public static final boolean PRIVILEGE_DEASSIGN = false;

	/** The Constant OPERATION_DISALLOW. */
	public static final String OPERATION_DISALLOW = "Disallow";

	/** The Constant SUPERADMINISTRATOR. */
	public static final String SUPERADMINISTRATOR = "Super Administrator";

	/** The Constant ALL_CURRENT_AND_FUTURE. */
	public static final String ALL_CURRENT_AND_FUTURE = "All Current and Future";

	/** The Constant NA. */
	public static final String NA = "N/A";

	/** The Constant IS_ALL_CP_CHECKED. */
	public static final String IS_ALL_CP_CHECKED = "isAllCPChecked";

	/** The Constant IS_CUSTOM_CHECKBOX_CHECKED. */
	public static final String IS_CUSTOM_CHECKBOX_CHECKED = "isCustChecked";

	/** The Constant CUSTOM_ROLE. */
	public static final String CUSTOM_ROLE = "Custom";

	/** The Constant ROW_ID_OBJECT_BEAN_MAP. */
	public static final String ROW_ID_OBJECT_BEAN_MAP = "rowIdObjectBeanMap";

	/** The Constant OPERATION_GET_CPS_FORTHIS_SITES. */
	public static final String OPERATION_GET_CPS_FORTHIS_SITES = "getCPsForThisSites";

	/** The Constant OPERATION_GET_USERS_FORTHIS_SITES. */
	public static final String OPERATION_GET_USERS_FORTHIS_SITES = "getUsersForThisSites";

	/** The Constant OPERATION_GET_ACTIONS_FORTHIS_SITES. */
	public static final String OPERATION_GET_ACTIONS_FORTHIS_SITES = "getActionsForThisSites";

	/** The Constant OPERATION_GET_ACTIONS_FORTHIS_ROLE. */
	public static final String OPERATION_GET_ACTIONS_FORTHIS_ROLE = "getActionsForThisRole";

	/** The Constant OPERATION_GET_ACTIONS_FORTHIS_CPS. */
	public static final String OPERATION_GET_ACTIONS_FORTHIS_CPS = "getActionsForThisCPs";

	/** The Constant OPERATION_EDIT_ROW_FOR_USERPAGE. */
	public static final String OPERATION_EDIT_ROW_FOR_USERPAGE = "editRowForUserPage";

	/** The Constant OPERATION_ADD_PRIVILEGE. */
	public static final String OPERATION_ADD_PRIVILEGE = "addPrivilege";

	/** The Constant OPERATION_ADD_PRIVILEGE_ON_USERPAGE. */
	public static final String OPERATION_ADD_PRIVILEGE_ON_USERPAGE = "addPrivOnUserPage";

	/** The Constant OPERATION_DELETE_ROW. */
	public static final String OPERATION_DELETE_ROW = "deleteRow";

	/** The Constant OPERATION_EDIT_ROW. */
	public static final String OPERATION_EDIT_ROW = "editRow";

	/** The Constant SELECTED_ACTION_IDS. */
	public static final String SELECTED_ACTION_IDS = "selectedActionIds";

	/** The Constant SELECTED_USER_IDS. */
	public static final String SELECTED_USER_IDS = "selectedUserIds";

	/** The Constant SELECTED_SITE_IDS. */
	public static final String SELECTED_SITE_IDS = "selectedSiteIds";

	/** The Constant SELECTED_ROLE_IDS. */
	public static final String SELECTED_ROLE_IDS = "selectedRoleIds";

	/** The Constant SELECTED_CP_IDS. */
	public static final String SELECTED_CP_IDS = "selectedCPIds";

	/** The Constant ERROR_MESSAGE_FOR_ROLE. */
	public static final String ERROR_MESSAGE_FOR_ROLE = "errorMessageForRole";

	/** The Constant ERROR_MESSAGE_FOR_SITE. */
	public static final String ERROR_MESSAGE_FOR_SITE = "errorMessageForSite";

	/** The Constant ERROR_MESSAGE_FOR_USER. */
	public static final String ERROR_MESSAGE_FOR_USER = "errorMessageForUser";

	/** The Constant ERROR_MESSAGE_FOR_CP. */
	public static final String ERROR_MESSAGE_FOR_CP = "errorMessageForCP";

	/** The Constant ERROR_MESSAGE_FOR_ACTION. */
	public static final String ERROR_MESSAGE_FOR_ACTION = "errorMessageForPrivilege";

	/** The Constant DEFAULT_ROLE. */
	public static final String DEFAULT_ROLE = "Default role";
	
	/** The Constant DEFAULT_ASSOCIATION_TYPE for Collection Protocol*/
	public static final String DEFAULT_ASSOCIATION_TYPE = "Parent";
	
	/** The Constant PAGE_OF_ASSIGN_PRIVILEGE. */
	public static final String PAGE_OF_ASSIGN_PRIVILEGE = "pageOfAssignPrivilegePage";

	/** The Constant ALL_DEFAULT_PRIVILEGES. */
	public static final String ALL_DEFAULT_PRIVILEGES = "All Default Privileges";

	/** The Constant ALL_DEFAULT_USERS. */
	public static final String ALL_DEFAULT_USERS = "All Default Users";

	//Constants for default column names to be shown for query result.
	/** The Constant DEFAULT_SPREADSHEET_COLUMNS. */
	public static final String[] DEFAULT_SPREADSHEET_COLUMNS = {
	//	        	QUERY_RESULTS_PARTICIPANT_ID,QUERY_RESULTS_COLLECTION_PROTOCOL_ID,
			//	        	QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID,QUERY_RESULTS_SPECIMEN_COLLECTION_GROUP_ID,
			//	        	QUERY_RESULTS_SPECIMEN_ID,QUERY_RESULTS_SPECIMEN_TYPE
			"IDENTIFIER", "TYPE", "ONE_DIMENSION_LABEL"};

	//Query results edit constants - MakeEditableAction.
	/** The Constant EDITABLE. */
	public static final String EDITABLE = "editable";

	//URL paths for Applet in TreeView.jsp
	/** The Constant QUERY_TREE_APPLET. */
	public static final String QUERY_TREE_APPLET = "edu/wustl/common/treeApplet/TreeApplet.class";

	/** The Constant APPLET_CODEBASE. */
	public static final String APPLET_CODEBASE = "Applet";

	//Shopping Cart
	/** The Constant SHOPPING_CART. */
	public static final String SHOPPING_CART = "shoppingCart";

	/** The Constant QUERY_SHOPPING_CART. */
	public static final String QUERY_SHOPPING_CART = "queryShoppingCart";

	/** The Constant DELIMETER. */
	public static final String DELIMETER = ",";

	/** The Constant SELECT_OPTION_VALUE. */
	public static final int SELECT_OPTION_VALUE = -1;

	/** The Constant TIME_HOUR_AMPM_ARRAY. */
	public static final String[] TIME_HOUR_AMPM_ARRAY = {"AM", "PM"};

	//	Constants required in CollectionProtocol.jsp Page
	/** The Constant COLLECTIONPROTOCOL_SEARCH_ACTION. */
	public static final String COLLECTIONPROTOCOL_SEARCH_ACTION = "CollectionProtocolSearch.do";

	/** The Constant COLLECTIONPROTOCOL_ADD_ACTION. */
	public static final String COLLECTIONPROTOCOL_ADD_ACTION = "CollectionProtocolAdd.do";

	/** The Constant COLLECTIONPROTOCOL_EDIT_ACTION. */
	public static final String COLLECTIONPROTOCOL_EDIT_ACTION = "CollectionProtocolEdit.do";

	//	Constants required in DistributionProtocol.jsp Page
	/** The Constant DISTRIBUTIONPROTOCOL_SEARCH_ACTION. */
	public static final String DISTRIBUTIONPROTOCOL_SEARCH_ACTION = "DistributionProtocolSearch.do";

	/** The Constant DISTRIBUTIONPROTOCOL_ADD_ACTION. */
	public static final String DISTRIBUTIONPROTOCOL_ADD_ACTION = "DistributionProtocolAdd.do";

	/** The Constant DISTRIBUTIONPROTOCOL_EDIT_ACTION. */
	public static final String DISTRIBUTIONPROTOCOL_EDIT_ACTION = "DistributionProtocolEdit.do";

	//Collection Status values
	/** The Constant COLLECTION_STATUS_PENDING. */
	public static final String COLLECTION_STATUS_PENDING = "Pending";

	/** The Constant COLLECTION_STATUS_COLLECTED. */
	public static final String COLLECTION_STATUS_COLLECTED = "Collected";

	/** The Constant ACTIVITY_STATUS_VALUES. */
	public static final String[] ACTIVITY_STATUS_VALUES = {SELECT_OPTION, "Active", "Closed",
			"Disabled"};

	/** The Constant SCG_COLLECTION_STATUS_VALUES. */
	public static final String[] SCG_COLLECTION_STATUS_VALUES = {SELECT_OPTION, "Pending",
			"Pending-Partially Complete", "Overdue", "Overdue-Partially Complete", "Complete",
			"Complete-Late", "Incomplete", "Not Collected"};

	/** The Constant SPECIMEN_COLLECTION_STATUS_VALUES. */
	public static final String[] SPECIMEN_COLLECTION_STATUS_VALUES = {SELECT_OPTION, "Pending",
			"Overdue", "Collected", "Not Collected"};

	/** The Constant SPECIMEN_ACTIVITY_STATUS_VALUES. */
	public static final String[] SPECIMEN_ACTIVITY_STATUS_VALUES = {"Active", "Closed"};

	/** The Constant SITE_ACTIVITY_STATUS_VALUES. */
	public static final String[] SITE_ACTIVITY_STATUS_VALUES = {SELECT_OPTION, "Active", "Closed"};

	/** The Constant USER_ACTIVITY_STATUS_VALUES. */
	public static final String[] USER_ACTIVITY_STATUS_VALUES = {SELECT_OPTION, "Active", "Closed"};

	/** The Constant APPROVE_USER_STATUS_VALUES. */
	public static final String[] APPROVE_USER_STATUS_VALUES = {SELECT_OPTION,
			Status.APPROVE_USER_APPROVE_STATUS.toString(),
			Status.APPROVE_USER_REJECT_STATUS.toString(),
			Status.APPROVE_USER_PENDING_STATUS.toString(),};

	/** The Constant REPORTED_PROBLEM_ACTIVITY_STATUS_VALUES. */
	public static final String[] REPORTED_PROBLEM_ACTIVITY_STATUS_VALUES = {SELECT_OPTION,
			"Closed", "Pending"};

	/** The Constant DISPOSAL_EVENT_ACTIVITY_STATUS_VALUES. */
	public static final String[] DISPOSAL_EVENT_ACTIVITY_STATUS_VALUES = {"Closed", "Disabled"};

	/** The Constant TISSUE. */
	public static final String TISSUE = "Tissue";

	/** The Constant FLUID. */
	public static final String FLUID = "Fluid";

	/** The Constant CELL. */
	public static final String CELL = "Cell";

	/** The Constant MOLECULAR. */
	public static final String MOLECULAR = "Molecular";

	/** The Constant SPECIMEN_TYPE_VALUES. */
	public static final String[] SPECIMEN_TYPE_VALUES = {SELECT_OPTION, TISSUE, FLUID, CELL,
			MOLECULAR};

	/** The Constant HOUR_ARRAY. */
	public static final String[] HOUR_ARRAY = {"00", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"

	};

	/** The Constant MINUTES_ARRAY. */
	public static final String[] MINUTES_ARRAY = {"00", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
			"23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
			"37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
			"51", "52", "53", "54", "55", "56", "57", "58", "59"};

	/** The Constant BULK_OPERATION_LIST. */
	public static final String BULK_OPERATION_LIST = "bulkOperationList";

	/** The Constant UNIT_GM. */
	public static final String UNIT_GM = "gm";

	/** The Constant UNIT_ML. */
	public static final String UNIT_ML = "ml";

	/** The Constant UNIT_CC. */
	public static final String UNIT_CC = "cell count";

	/** The Constant UNIT_MG. */
	public static final String UNIT_MG = "\u00B5" + "g";

	/** The Constant UNIT_CN. */
	public static final String UNIT_CN = "count";

	/** The Constant UNIT_CL. */
	public static final String UNIT_CL = "cells";

	/** The Constant CDE_NAME_CLINICAL_STATUS. */
	public static final String CDE_NAME_CLINICAL_STATUS = "Clinical Status";

	/** The Constant CDE_NAME_GENDER. */
	public static final String CDE_NAME_GENDER = "Gender";

	/** The Constant CDE_NAME_GENOTYPE. */
	public static final String CDE_NAME_GENOTYPE = "Genotype";

	/** The Constant CDE_NAME_SPECIMEN_CLASS. */
	public static final String CDE_NAME_SPECIMEN_CLASS = "Specimen";

	/** The Constant CDE_NAME_SPECIMEN_TYPE. */
	public static final String CDE_NAME_SPECIMEN_TYPE = "Specimen Type";

	/** The Constant CDE_NAME_TISSUE_SIDE. */
	public static final String CDE_NAME_TISSUE_SIDE = "Tissue Side";

	/** The Constant CDE_NAME_PATHOLOGICAL_STATUS. */
	public static final String CDE_NAME_PATHOLOGICAL_STATUS = "Pathological Status";

	/** The Constant CDE_NAME_RECEIVED_QUALITY. */
	public static final String CDE_NAME_RECEIVED_QUALITY = "Received Quality";

	/** The Constant CDE_NAME_FIXATION_TYPE. */
	public static final String CDE_NAME_FIXATION_TYPE = "Fixation Type";

	/** The Constant CDE_NAME_COLLECTION_PROCEDURE. */
	public static final String CDE_NAME_COLLECTION_PROCEDURE = "Collection Procedure";

	/** The Constant CDE_NAME_CONTAINER. */
	public static final String CDE_NAME_CONTAINER = "Container";

	/** The Constant CDE_NAME_METHOD. */
	public static final String CDE_NAME_METHOD = "Method";

	/** The Constant CDE_NAME_EMBEDDING_MEDIUM. */
	public static final String CDE_NAME_EMBEDDING_MEDIUM = "Embedding Medium";

	/** The Constant CDE_NAME_BIOHAZARD. */
	public static final String CDE_NAME_BIOHAZARD = "Biohazard";

	/** The Constant CDE_NAME_ETHNICITY. */
	public static final String CDE_NAME_ETHNICITY = "Ethnicity";

	/** The Constant CDE_NAME_RACE. */
	public static final String CDE_NAME_RACE = "Race";

	/** The Constant CDE_VITAL_STATUS. */
	public static final String CDE_VITAL_STATUS = "Vital Status";

	/** The Constant CDE_NAME_CLINICAL_DIAGNOSIS. */
	public static final String CDE_NAME_CLINICAL_DIAGNOSIS = "Clinical Diagnosis";

	/** The Constant CDE_NAME_SITE_TYPE. */
	public static final String CDE_NAME_SITE_TYPE = "Site Type";

	/** The Constant CDE_NAME_COUNTRY_LIST. */
	public static final String CDE_NAME_COUNTRY_LIST = "Countries";

	/** The Constant CDE_NAME_STATE_LIST. */
	public static final String CDE_NAME_STATE_LIST = "States";

	/** The Constant CDE_NAME_HISTOLOGICAL_QUALITY. */
	public static final String CDE_NAME_HISTOLOGICAL_QUALITY = "Histological Quality";
	//Constants for Advanced Search
	/** The Constant STRING_OPERATORS. */
	public static final String STRING_OPERATORS = "StringOperators";

	/** The Constant DATE_NUMERIC_OPERATORS. */
	public static final String DATE_NUMERIC_OPERATORS = "DateNumericOperators";

	/** The Constant ENUMERATED_OPERATORS. */
	public static final String ENUMERATED_OPERATORS = "EnumeratedOperators";

	/** The Constant MULTI_ENUMERATED_OPERATORS. */
	public static final String MULTI_ENUMERATED_OPERATORS = "MultiEnumeratedOperators";

	/** The Constant CHARACTERS_IN_ONE_LINE. */
	public static final int CHARACTERS_IN_ONE_LINE = 110;

	/** The Constant SINGLE_QUOTE_ESCAPE_SEQUENCE. */
	public static final String SINGLE_QUOTE_ESCAPE_SEQUENCE = "&#096;";

	/** The Constant COLUMN_NAMES. */
	public static final String COLUMN_NAMES = "columnNames";

	/** The Constant INDEX. */
	public static final String INDEX = "index";

	/** The Constant STORAGE_STATUS_ARRAY. */
	public static final String[] STORAGE_STATUS_ARRAY = {SELECT_OPTION, "CHECK IN", "CHECK OUT"};

	// constants for Data required in query
	/** The Constant ALIAS_NAME_TABLE_NAME_MAP. */
	public static final String ALIAS_NAME_TABLE_NAME_MAP = "objectTableNames";

	/** The Constant SYSTEM_IDENTIFIER_COLUMN_NAME. */
	public static final String SYSTEM_IDENTIFIER_COLUMN_NAME = "IDENTIFIER";

	/** The Constant NAME. */
	public static final String NAME = "name";

	/** The Constant EVENT_PARAMETERS. */
	public static final String EVENT_PARAMETERS[] = {Constants.SELECT_OPTION,
			"Cell Specimen Review", "Check In Check Out", "Collection", "Disposal", "Embedded",
			"Fixed", "Fluid Specimen Review", "Frozen", "Molecular Specimen Review", "Procedure",
			"Received", "Spun", "Thaw", "Tissue Specimen Review", "Transfer"};
	
	public static final String NEW_EVENT_PARAMETERS[] = {Constants.SELECT_OPTION,
		"Disposal", "Transfer"};
	
	public static final String QUICK_EVENT_PARAMETERS[] = {Constants.SELECT_OPTION,
		"Cell Specimen Review", "Check In Check Out", "Collection", "Disposal", "Embedded",
		"Fixed", "Fluid Specimen Review", "Frozen", "Molecular Specimen Review", "Procedure",
		"Received", "Spun", "Thaw", "Tissue Specimen Review"};

	/** The Constant EVENT_PARAMETERS_COLUMNS. */
	public static final String EVENT_PARAMETERS_COLUMNS[] = {"Identifier", "Event Parameter",
			"User", "Date / Time", "PageOf"};

	/** The Constant DERIVED_SPECIMEN_COLUMNS. */
	public static final String DERIVED_SPECIMEN_COLUMNS[] = {"Label", "Class", "Type", "Quantity",
			"rowSelected"};

	/** The Constant SHOPPING_CART_COLUMNS. */
	public static final String[] SHOPPING_CART_COLUMNS = {"", "Identifier", "Type", "Subtype",
			"Tissue Site", "Tissue Side", "Pathological Status"};

	//Constants required in AssignPrivileges.jsp
	/** The Constant ASSIGN. */
	public static final String ASSIGN = "assignOperation";

	/** The Constant PRIVILEGES. */
	public static final String PRIVILEGES = "privileges";

	/** The Constant OBJECT_TYPES. */
	public static final String OBJECT_TYPES = "objectTypes";

	/** The Constant OBJECT_TYPE_VALUES. */
	public static final String OBJECT_TYPE_VALUES = "objectTypeValues";

	/** The Constant RECORD_IDS. */
	public static final String RECORD_IDS = "recordIds";

	/** The Constant ATTRIBUTES. */
	public static final String ATTRIBUTES = "attributes";

	/** The Constant GROUPS. */
	public static final String GROUPS = "groups";

	/** The Constant USERS_FOR_USE_PRIVILEGE. */
	public static final String USERS_FOR_USE_PRIVILEGE = "usersForUsePrivilege";

	/** The Constant USERS_FOR_READ_PRIVILEGE. */
	public static final String USERS_FOR_READ_PRIVILEGE = "usersForReadPrivilege";

	/** The Constant ASSIGN_PRIVILEGES_ACTION. */
	public static final String ASSIGN_PRIVILEGES_ACTION = "AssignPrivileges.do";

	/** The Constant CONTAINER_IN_ANOTHER_CONTAINER. */
	public static final int CONTAINER_IN_ANOTHER_CONTAINER = 2;

	/**
	 * Gets the user pg name.
	 *
	 * @param identifier the identifier
	 *
	 * @return the user pg name
	 */
	public static String getUserPGName(Long identifier)
	{
		if (identifier == null)
		{
			return "USER_";
		}
		return "USER_" + identifier;
	}

	/**
	 * Gets the user group name.
	 *
	 * @param identifier the identifier
	 *
	 * @return the user group name
	 */
	public static String getUserGroupName(Long identifier)
	{
		if (identifier == null)
		{
			return "USER_";
		}
		return "USER_" + identifier;
	}

	//Mandar 25-Apr-06 : bug 1414 : Tissue units as per type
	// tissue types with unit= count
	/** The Constant FROZEN_TISSUE_BLOCK. */
	public static final String FROZEN_TISSUE_BLOCK = "Frozen Tissue Block"; // PREVIOUS FROZEN BLOCK

	/** The Constant FROZEN_TISSUE_SLIDE. */
	public static final String FROZEN_TISSUE_SLIDE = "Frozen Tissue Slide"; // SLIDE

	/** The Constant FIXED_TISSUE_BLOCK. */
	public static final String FIXED_TISSUE_BLOCK = "Fixed Tissue Block"; // PARAFFIN BLOCK

	/** The Constant NOT_SPECIFIED. */
	public static final String NOT_SPECIFIED = "Not Specified";

	/** The Constant WITHDRAWN. */
	public static final String WITHDRAWN = "Withdrawn";
	// tissue types with unit= g
	/** The Constant FRESH_TISSUE. */
	public static final String FRESH_TISSUE = "Fresh Tissue";

	/** The Constant FROZEN_TISSUE. */
	public static final String FROZEN_TISSUE = "Frozen Tissue";

	/** The Constant FIXED_TISSUE. */
	public static final String FIXED_TISSUE = "Fixed Tissue";

	/** The Constant FIXED_TISSUE_SLIDE. */
	public static final String FIXED_TISSUE_SLIDE = "Fixed Tissue Slide";
	//tissue types with unit= cc
	/** The Constant MICRODISSECTED. */
	public static final String MICRODISSECTED = "Microdissected";

	//	 constants required for Distribution Report
	/** The Constant CONFIGURATION_TABLES. */
	public static final String CONFIGURATION_TABLES = "configurationTables";

	/** The Constant DISTRIBUTION_TABLE_AlIAS. */
	public static final String DISTRIBUTION_TABLE_AlIAS[] = {"CollectionProtReg", "Participant",
			"Specimen", "SpecimenCollectionGroup", "DistributedItem"};

	/** The Constant TABLE_COLUMN_DATA_MAP. */
	public static final String TABLE_COLUMN_DATA_MAP = "tableColumnDataMap";

	/** The Constant CONFIGURE_RESULT_VIEW_ACTION. */
	public static final String CONFIGURE_RESULT_VIEW_ACTION = "ConfigureResultView.do";

	/** The Constant TABLE_NAMES_LIST. */
	public static final String TABLE_NAMES_LIST = "tableNamesList";

	/** The Constant COLUMN_NAMES_LIST. */
	public static final String COLUMN_NAMES_LIST = "columnNamesList";

	/** The Constant SPECIMEN_COLUMN_NAMES_LIST. */
	public static final String SPECIMEN_COLUMN_NAMES_LIST = "specimenColumnNamesList";

	/** The Constant DISTRIBUTION_ID. */
	public static final String DISTRIBUTION_ID = "distributionId";

	/** The Constant CONFIGURE_DISTRIBUTION_ACTION. */
	public static final String CONFIGURE_DISTRIBUTION_ACTION = "ConfigureDistribution.do";

	/** The Constant DISTRIBUTION_REPORT_ACTION. */
	public static final String DISTRIBUTION_REPORT_ACTION = "DistributionReport.do";

	/** The Constant ARRAY_DISTRIBUTION_REPORT_ACTION. */
	public static final String ARRAY_DISTRIBUTION_REPORT_ACTION = "ArrayDistributionReport.do";

	/** The Constant DISTRIBUTION_REPORT_SAVE_ACTION. */
	public static final String DISTRIBUTION_REPORT_SAVE_ACTION = "DistributionReportSave.do";

	/** The Constant ARRAY_DISTRIBUTION_REPORT_SAVE_ACTION. */
	public static final String ARRAY_DISTRIBUTION_REPORT_SAVE_ACTION = "ArrayDistributionReportSave.do";

	//bug#4981 :kalpana
	/** The Constant SELECTED_COLUMNS. */
	public static final String SELECTED_COLUMNS[] = {"Print : Specimen",
			"Specimen.LABEL.Label : Specimen", "AbstractSpecimen.SPECIMEN_TYPE.Type : Specimen",
			"SpecimenCharacteristics.TISSUE_SITE.Tissue Site : Specimen",
			"SpecimenCharacteristics.TISSUE_SIDE.Tissue Side : Specimen",
			"AbstractSpecimen.PATHOLOGICAL_STATUS.Pathological Status : Specimen",
			"DistributedItem.QUANTITY.Quantity : Distribution"};
	//"SpecimenCharacteristics.PATHOLOGICAL_STATUS.Pathological Status : Specimen",

	/** The Constant SPECIMEN_IN_ARRAY_SELECTED_COLUMNS. */
	public static final String SPECIMEN_IN_ARRAY_SELECTED_COLUMNS[] = {"Print : Specimen",
			"Specimen.LABEL.Label : Specimen", "Specimen.BARCODE.barcode : Specimen",
			"SpecimenArrayContent.PositionDimensionOne.PositionDimensionOne : Specimen",
			"SpecimenArrayContent.PositionDimensionTwo.PositionDimensionTwo : Specimen",
			"Specimen.CLASS.CLASS : Specimen", "Specimen.TYPE.Type : Specimen",
			"SpecimenCharacteristics.TISSUE_SIDE.Tissue Side : Specimen",
			"SpecimenCharacteristics.TISSUE_SITE.Tissue Site : Specimen",};

	/** The Constant ARRAY_SELECTED_COLUMNS. */
	public static final String ARRAY_SELECTED_COLUMNS[] = {"Print : Specimen",
			"SpecimenArray.Name.Name : SpecimenArray", "Container.barcode.Barcode : SpecimenArray",
			"ContainerType.name.ArrayType : ContainerType",
			"Container.PositionDimensionOne.Position One: Container",
			"Container.PositionDimensionTwo.Position Two: Container",
			"Container.CapacityOne.Dimension One: Container",
			"Container.CapacityTwo.Dimension Two: Container",
			"ContainerType.SpecimenClass.specimen class : ContainerType",
			"ContainerType.SpecimenTypes.specimen Types : ContainerType",
			"Container.Comment.comment: Container",};

	/** The Constant SPECIMEN_ID_LIST. */
	public static final String SPECIMEN_ID_LIST = "specimenIdList";

	/** The Constant DISTRIBUTION_ACTION. */
	public static final String DISTRIBUTION_ACTION = "Distribution.do?pageOf=pageOfDistribution";

	/** The Constant DISTRIBUTION_REPORT_NAME. */
	public static final String DISTRIBUTION_REPORT_NAME = "Distribution Report.csv";

	/** The Constant DISTRIBUTION_REPORT_FORM. */
	public static final String DISTRIBUTION_REPORT_FORM = "distributionReportForm";

	/** The Constant DISTRIBUTED_ITEMS_DATA. */
	public static final String DISTRIBUTED_ITEMS_DATA = "distributedItemsData";

	/** The Constant DISTRIBUTED_ITEM. */
	public static final String DISTRIBUTED_ITEM = "DistributedItem";
	//constants for Simple Query Interface Configuration
	/** The Constant CONFIGURE_SIMPLE_QUERY_ACTION. */
	public static final String CONFIGURE_SIMPLE_QUERY_ACTION = "ConfigureSimpleQuery.do";

	/** The Constant CONFIGURE_SIMPLE_QUERY_VALIDATE_ACTION. */
	public static final String CONFIGURE_SIMPLE_QUERY_VALIDATE_ACTION = "ConfigureSimpleQueryValidate.do";

	/** The Constant CONFIGURE_SIMPLE_SEARCH_ACTION. */
	public static final String CONFIGURE_SIMPLE_SEARCH_ACTION = "ConfigureSimpleSearch.do";

	/** The Constant SIMPLE_SEARCH_ACTION. */
	public static final String SIMPLE_SEARCH_ACTION = "SimpleSearch.do";

	/** The Constant SIMPLE_SEARCH_AFTER_CONFIGURE_ACTION. */
	public static final String SIMPLE_SEARCH_AFTER_CONFIGURE_ACTION = "SimpleSearchAfterConfigure.do";

	/** The Constant RESULT_VIEW_VECTOR. */
	public static final String RESULT_VIEW_VECTOR = "resultViewVector";

	/** The Constant SPECIMENT_VIEW_ATTRIBUTE. */
	public static final String SPECIMENT_VIEW_ATTRIBUTE = "defaultViewAttribute";
	//public static final String SIMPLE_QUERY_COUNTER = "simpleQueryCount";

	/** The Constant UNDEFINED. */
	public static final String UNDEFINED = "Undefined";

	/** The Constant UNKNOWN. */
	public static final String UNKNOWN = "Unknown";

	/** The Constant UNSPECIFIED. */
	public static final String UNSPECIFIED = "Unspecified";

	/** The Constant NOTSPECIFIED. */
	public static final String NOTSPECIFIED = "Not Specified";

	/** The Constant SEARCH_RESULT. */
	public static final String SEARCH_RESULT = "SearchResult.csv";

	//	Mandar : LightYellow and Green colors for CollectionProtocol SpecimenRequirements. Bug id: 587
	//	public static final String ODD_COLOR = "#FEFB85";
	//	public static final String EVEN_COLOR = "#A7FEAB";
	//	Light and dark shades of GREY.
	/** The Constant ODD_COLOR. */
	public static final String ODD_COLOR = "#E5E5E5";

	/** The Constant EVEN_COLOR. */
	public static final String EVEN_COLOR = "#F7F7F7";

	// TO FORWARD THE REQUEST ON SUBMIT IF STATUS IS DISABLED
	/** The Constant BIO_SPECIMEN. */
	public static final String BIO_SPECIMEN = "/ManageBioSpecimen.do";

	/** The Constant ADMINISTRATIVE. */
	public static final String ADMINISTRATIVE = "/ManageAdministrativeData.do";

	/** The Constant PARENT_SPECIMEN_ID. */
	public static final String PARENT_SPECIMEN_ID = "parentSpecimenId";

	/** The Constant COLLECTION_REGISTRATION_ID. */
	public static final String COLLECTION_REGISTRATION_ID = "collectionProtocolId";

	/** The Constant FORWARDLIST. */
	public static final String FORWARDLIST = "forwardToList";

	/** The Constant SPECIMEN_FORWARD_TO_LIST. */
	public static final String[][] SPECIMEN_FORWARD_TO_LIST = {{"Submit", "success"},
			{"Derive", "createNew"}, {"Add Events", "eventParameters"},
			{"More", "sameCollectionGroup"}, {"Distribute", "distribution"},
			{"Derive Multiple", "deriveMultiple"}};

	/** The Constant SPECIMEN_BUTTON_TIPS. */
	public static final String[] SPECIMEN_BUTTON_TIPS = {"Submit only", "Submit and derive",
			"Submit and add events", "Submit and add more to same group", "Submit and distribute",
			"Submit and derive multiple"};

	/** The Constant SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST. */
	public static final String[][] SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST = {
			{"Submit", "success"}, {"Add Specimen", "createNewSpecimen"},
			{"Add Multiple Specimens", "createMultipleSpecimen"}};

	/** The Constant PROTOCOL_REGISTRATION_FORWARD_TO_LIST. */
	public static final String[][] PROTOCOL_REGISTRATION_FORWARD_TO_LIST = {{"Submit", "success"},
			{"Specimen Collection Group", "createSpecimenCollectionGroup"}};

	/** The Constant PARTICIPANT_FORWARD_TO_LIST. */
	public static final String[][] PARTICIPANT_FORWARD_TO_LIST = {{"Submit", "success"},
			{"Submit", "createParticipantRegistration"},
			{"Specimen Collection Group", "specimenCollectionGroup"},
			{"Submit", "pageOfParticipantCPQuery"}

	};

	/** The Constant STORAGE_TYPE_FORWARD_TO_LIST. */
	public static final String[][] STORAGE_TYPE_FORWARD_TO_LIST = {{"Submit", "success"},
			{"Add Container", "storageContainer"}};

	//Constants Required for Advance Search
	//Tree related
	//public static final String PARTICIPANT ='Participant';
	/** The Constant ADVANCE_QUERY_TREE_HEIRARCHY. */
	public static final String[] ADVANCE_QUERY_TREE_HEIRARCHY = { //Represents the Advance Query tree Heirarchy.
	Constants.PARTICIPANT, Constants.COLLECTION_PROTOCOL, Constants.SPECIMEN_COLLECTION_GROUP,
			Constants.SPECIMEN};

	/** The Constant MENU_COLLECTION_PROTOCOL. */
	public static final String MENU_COLLECTION_PROTOCOL = "Collection Protocol";

	/** The Constant MENU_SPECIMEN_COLLECTION_GROUP. */
	public static final String MENU_SPECIMEN_COLLECTION_GROUP = "Specimen Collection Group";

	/** The Constant MENU_DISTRIBUTION_PROTOCOL. */
	public static final String MENU_DISTRIBUTION_PROTOCOL = "Distribution Protocol";

	/** The Constant SPECIMEN_COLLECTION_GROUP. */
	public static final String SPECIMEN_COLLECTION_GROUP = "SpecimenCollectionGroup";

	/** The Constant DISTRIBUTION. */
	public static final String DISTRIBUTION = "Distribution";

	/** The Constant DISTRIBUTION_PROTOCOL. */
	public static final String DISTRIBUTION_PROTOCOL = "DistributionProtocol";

	/** The Constant CP. */
	public static final String CP = "CP";

	/** The Constant SCG. */
	public static final String SCG = "SCG";

	/** The Constant D. */
	public static final String D = "D";

	/** The Constant DP. */
	public static final String DP = "DP";

	/** The Constant C. */
	public static final String C = "C";

	/** The Constant S. */
	public static final String S = "S";

	/** The Constant P. */
	public static final String P = "P";

	/** The Constant ADVANCED_CONDITIONS_QUERY_VIEW. */
	public static final String ADVANCED_CONDITIONS_QUERY_VIEW = "advancedCondtionsQueryView";

	/** The Constant ADVANCED_SEARCH_ACTION. */
	public static final String ADVANCED_SEARCH_ACTION = "AdvanceSearch.do";

	/** The Constant ADVANCED_SEARCH_RESULTS_ACTION. */
	public static final String ADVANCED_SEARCH_RESULTS_ACTION = "AdvanceSearchResults.do";

	/** The Constant CONFIGURE_ADVANCED_SEARCH_RESULTS_ACTION. */
	public static final String CONFIGURE_ADVANCED_SEARCH_RESULTS_ACTION = "ConfigureAdvanceSearchResults.do";

	/** The Constant ADVANCED_QUERY_ADD. */
	public static final String ADVANCED_QUERY_ADD = "Add";

	/** The Constant ADVANCED_QUERY_EDIT. */
	public static final String ADVANCED_QUERY_EDIT = "Edit";

	/** The Constant ADVANCED_QUERY_DELETE. */
	public static final String ADVANCED_QUERY_DELETE = "Delete";

	/** The Constant ADVANCED_QUERY_OPERATOR. */
	public static final String ADVANCED_QUERY_OPERATOR = "Operator";

	/** The Constant ADVANCED_QUERY_OR. */
	public static final String ADVANCED_QUERY_OR = "OR";

	/** The Constant ADVANCED_QUERY_AND. */
	public static final String ADVANCED_QUERY_AND = "pAND";

	/** The Constant EVENT_CONDITIONS. */
	public static final String EVENT_CONDITIONS = "eventConditions";

	/** The Constant IDENTIFIER_COLUMN_ID_MAP. */
	public static final String IDENTIFIER_COLUMN_ID_MAP = "identifierColumnIdsMap";

	/** The Constant PAGE_OF_PARTICIPANT_QUERY_EDIT. */
	public static final String PAGE_OF_PARTICIPANT_QUERY_EDIT = "pageOfParticipantQueryEdit";

	/** The Constant PAGE_OF_COLLECTION_PROTOCOL_QUERY_EDIT. */
	public static final String PAGE_OF_COLLECTION_PROTOCOL_QUERY_EDIT = "pageOfCollectionProtocolQueryEdit";

	/** The Constant PAGE_OF_SPECIMEN_COLLECTION_GROUP_QUERY_EDIT. */
	public static final String PAGE_OF_SPECIMEN_COLLECTION_GROUP_QUERY_EDIT = "pageOfSpecimenCollectionGroupQueryEdit";

	/** The Constant PAGE_OF_SPECIMEN_QUERY_EDIT. */
	public static final String PAGE_OF_SPECIMEN_QUERY_EDIT = "pageOfSpecimenQueryEdit";

	/** The Constant PARTICIPANT_COLUMNS. */
	public static final String PARTICIPANT_COLUMNS = "particpantColumns";

	/** The Constant COLLECTION_PROTOCOL_COLUMNS. */
	public static final String COLLECTION_PROTOCOL_COLUMNS = "collectionProtocolColumns";

	/** The Constant SPECIMEN_COLLECTION_GROUP_COLUMNS. */
	public static final String SPECIMEN_COLLECTION_GROUP_COLUMNS = "SpecimenCollectionGroupColumns";

	/** The Constant SPECIMEN_COLUMNS. */
	public static final String SPECIMEN_COLUMNS = "SpecimenColumns";

	/** The Constant USER_ID_COLUMN. */
	public static final String USER_ID_COLUMN = "USER_ID";

	/** The Constant GENERIC_SECURITYMANAGER_ERROR. */
	public static final String GENERIC_SECURITYMANAGER_ERROR = "The Security Violation error occured during a database operation. Please report this problem to the adminstrator";

	/** The Constant BOOLEAN_YES. */
	public static final String BOOLEAN_YES = "Yes";

	/** The Constant BOOLEAN_NO. */
	public static final String BOOLEAN_NO = "No";

	/** The Constant PACKAGE_DOMAIN. */
	public static final String PACKAGE_DOMAIN = "edu.wustl.catissuecore.domain";

	//Constants for isAuditable and isSecureUpdate required for Dao methods in Bozlogic
	/** The Constant IS_AUDITABLE_TRUE. */
	public static final boolean IS_AUDITABLE_TRUE = true;

	/** The Constant IS_SECURE_UPDATE_TRUE. */
	public static final boolean IS_SECURE_UPDATE_TRUE = true;

	/** The Constant HAS_OBJECT_LEVEL_PRIVILEGE_FALSE. */
	public static final boolean HAS_OBJECT_LEVEL_PRIVILEGE_FALSE = false;

	//Constants for HTTP-API
	/** The Constant CONTENT_TYPE. */
	public static final String CONTENT_TYPE = "CONTENT-TYPE";

	// For StorageContainer isFull status
	/** The Constant IS_CONTAINER_FULL_LIST. */
	public static final String IS_CONTAINER_FULL_LIST = "isContainerFullList";

	/** The Constant IS_CONTAINER_FULL_VALUES. */
	public static final String[] IS_CONTAINER_FULL_VALUES = {SELECT_OPTION, "True", "False"};

	/** The Constant STORAGE_CONTAINER_DIM_ONE_LABEL. */
	public static final String STORAGE_CONTAINER_DIM_ONE_LABEL = "oneDimLabel";

	/** The Constant STORAGE_CONTAINER_DIM_TWO_LABEL. */
	public static final String STORAGE_CONTAINER_DIM_TWO_LABEL = "twoDimLabel";

	//public static final String SPECIMEN_TYPE_TISSUE = "Tissue";
	//public static final String SPECIMEN_TYPE_FLUID = "Fluid";
	//public static final String SPECIMEN_TYPE_CELL = "Cell";
	//public static final String SPECIMEN_TYPE_MOL = "Molecular";
	/** The Constant SPECIMEN_TYPE_COUNT. */
	public static final String SPECIMEN_TYPE_COUNT = "Count";

	/** The Constant SPECIMEN_TYPE_QUANTITY. */
	public static final String SPECIMEN_TYPE_QUANTITY = "Quantity";

	/** The Constant SPECIMEN_TYPE_DETAILS. */
	public static final String SPECIMEN_TYPE_DETAILS = "Details";

	/** The Constant SPECIMEN_COUNT. */
	public static final String SPECIMEN_COUNT = "totalSpecimenCount";

	/** The Constant TOTAL. */
	public static final String TOTAL = "Total";

	/** The Constant SPECIMENS. */
	public static final String SPECIMENS = "Specimens";

	//User Roles
	/** The Constant TECHNICIAN. */
	public static final String TECHNICIAN = "Technician";

	/** The Constant SUPERVISOR. */
	public static final String SUPERVISOR = "Supervisor";

	/** The Constant SCIENTIST. */
	public static final String SCIENTIST = "Scientist";

	/** The Constant CHILD_CONTAINER_TYPE. */
	public static final String CHILD_CONTAINER_TYPE = "childContainerType";

	/** The Constant UNUSED. */
	public static final String UNUSED = "Unused";

	/** The Constant TYPE. */
	public static final String TYPE = "Type";

	//Mandar: 28-Apr-06 Bug 1129
	/** The Constant DUPLICATE_SPECIMEN. */
	public static final String DUPLICATE_SPECIMEN = "duplicateSpecimen";

	//Constants required in ParticipantLookupAction
	/** The Constant PARTICIPANT_MRN_COL_NAME. */
	public static final String PARTICIPANT_MRN_COL_NAME = "partMRNColName";

	/** The Constant PARTICIPANT_NAME_SEPARATOR. */
	public static final String PARTICIPANT_NAME_SEPARATOR = "~";

	/** The Constant PARTICIPANT_NAME_HEADERLABEL. */
	public static final String PARTICIPANT_NAME_HEADERLABEL = "Name";

	/** The Constant PARTICIPANT_LOOKUP_ALGO_FOR_SPR. */
	public static final String PARTICIPANT_LOOKUP_ALGO_FOR_SPR = "ParticipantLookupAlgoForSPR";

	/** The Constant PARTICIPANT_LOOKUP_PARAMETER. */
	public static final String PARTICIPANT_LOOKUP_PARAMETER = "ParticipantLookupParameter";

	/** The Constant PARTICIPANT_LOOKUP_CUTOFF. */
	public static final String PARTICIPANT_LOOKUP_CUTOFF = "lookup.cutoff";

	/** The Constant PARTICIPANT_LOOKUP_ALGO. */
	public static final String PARTICIPANT_LOOKUP_ALGO = "ParticipantLookupAlgo";

	/** The Constant PARTICIPANT_LOOKUP_SUCCESS. */
	public static final String PARTICIPANT_LOOKUP_SUCCESS = "success";

	/** The Constant PARTICIPANT_ADD_FORWARD. */
	public static final String PARTICIPANT_ADD_FORWARD = "participantAdd";

	/** The Constant PARTICIPANT_SYSTEM_IDENTIFIER. */
	public static final String PARTICIPANT_SYSTEM_IDENTIFIER = "IDENTIFIER";

	/** The Constant PARTICIPANT_LAST_NAME. */
	public static final String PARTICIPANT_LAST_NAME = "LAST_NAME";

	/** The Constant PARTICIPANT_FIRST_NAME. */
	public static final String PARTICIPANT_FIRST_NAME = "FIRST_NAME";

	/** The Constant PARTICIPANT_MIDDLE_NAME. */
	public static final String PARTICIPANT_MIDDLE_NAME = "MIDDLE_NAME";

	/** The Constant PARTICIPANT_BIRTH_DATE. */
	public static final String PARTICIPANT_BIRTH_DATE = "BIRTH_DATE";

	/** The Constant PARTICIPANT_DEATH_DATE. */
	public static final String PARTICIPANT_DEATH_DATE = "DEATH_DATE";

	/** The Constant PARTICIPANT_VITAL_STATUS. */
	public static final String PARTICIPANT_VITAL_STATUS = "VITAL_STATUS";

	/** The Constant PARTICIPANT_GENDER. */
	public static final String PARTICIPANT_GENDER = "GENDER";

	/** The Constant PARTICIPANT_SEX_GENOTYPE. */
	public static final String PARTICIPANT_SEX_GENOTYPE = "SEX_GENOTYPE";

	/** The Constant PARTICIPANT_RACE. */
	public static final String PARTICIPANT_RACE = "RACE";

	/** The Constant PARTICIPANT_ETHINICITY. */
	public static final String PARTICIPANT_ETHINICITY = "ETHINICITY";

	/** The Constant PARTICIPANT_SOCIAL_SECURITY_NUMBER. */
	public static final String PARTICIPANT_SOCIAL_SECURITY_NUMBER = "SOCIAL_SECURITY_NUMBER";

	/** The Constant PARTICIPANT_PROBABLITY_MATCH. */
	public static final String PARTICIPANT_PROBABLITY_MATCH = "Probability";

	/** The Constant PARTICIPANT_MEDICAL_RECORD_NO. */
	public static final String PARTICIPANT_MEDICAL_RECORD_NO = "MEDICAL_RECORD_NUMBER";

	/** The Constant PARTICIPANT_SSN_EXACT. */
	public static final String PARTICIPANT_SSN_EXACT = "SSNExact";

	/** The Constant PARTICIPANT_SSN_PARTIAL. */
	public static final String PARTICIPANT_SSN_PARTIAL = "SSNPartial";

	/** The Constant PARTICIPANT_PMI_EXACT. */
	public static final String PARTICIPANT_PMI_EXACT = "PMIExact";

	/** The Constant PARTICIPANT_PMI_PARTIAL. */
	public static final String PARTICIPANT_PMI_PARTIAL = "PMIPartial";

	/** The Constant PARTICIPANT_DOB_EXACT. */
	public static final String PARTICIPANT_DOB_EXACT = "DOBExact";

	/** The Constant PARTICIPANT_DOB_PARTIAL. */
	public static final String PARTICIPANT_DOB_PARTIAL = "DOBPartial";

	/** The Constant PARTICIPANT_LAST_NAME_EXACT. */
	public static final String PARTICIPANT_LAST_NAME_EXACT = "LastNameExact";

	/** The Constant PARTICIPANT_LAST_NAME_PARTIAL. */
	public static final String PARTICIPANT_LAST_NAME_PARTIAL = "LastNamePartial";

	/** The Constant PARTICIPANT_FIRST_NAME_EXACT. */
	public static final String PARTICIPANT_FIRST_NAME_EXACT = "NameExact";

	/** The Constant PARTICIPANT_FIRST_NAME_PARTIAL. */
	public static final String PARTICIPANT_FIRST_NAME_PARTIAL = "NamePartial";

	/** The Constant PARTICIPANT_MIDDLE_NAME_EXACT. */
	public static final String PARTICIPANT_MIDDLE_NAME_EXACT = "MiddleNameExact";

	/** The Constant PARTICIPANT_MIDDLE_NAME_PARTIAL. */
	public static final String PARTICIPANT_MIDDLE_NAME_PARTIAL = "MiddleNamePartial";

	/** The Constant PARTICIPANT_GENDER_EXACT. */
	public static final String PARTICIPANT_GENDER_EXACT = "GenderExact";

	/** The Constant PARTICIPANT_RACE_EXACT. */
	public static final String PARTICIPANT_RACE_EXACT = "RaceExact";

	/** The Constant PARTICIPANT_RACE_PARTIAL. */
	public static final String PARTICIPANT_RACE_PARTIAL = "RacePartial";

	/** The Constant PARTICIPANT_BONUS. */
	public static final String PARTICIPANT_BONUS = "Bonus";

	/** The Constant PARTICIPANT_TOTAL_POINTS. */
	public static final String PARTICIPANT_TOTAL_POINTS = "TotalPoints";

	/** The Constant PARTICIPANT_MATCH_CHARACTERS_FOR_LAST_NAME. */
	public static final String PARTICIPANT_MATCH_CHARACTERS_FOR_LAST_NAME = "MatchCharactersForLastName";

	// Constants for SPR
	/** The Constant SPR_LOOKUP_PARAMETER. */
	public static final String SPR_LOOKUP_PARAMETER = "SPRLookupParameter";

	/** The Constant SPR_SSN_EXACT. */
	public static final String SPR_SSN_EXACT = "SPRSSNExact";

	/** The Constant SPR_SSN_PARTIAL. */
	public static final String SPR_SSN_PARTIAL = "SPRSSNPartial";

	/** The Constant SPR_PMI_EXACT. */
	public static final String SPR_PMI_EXACT = "SPRPMIExact";

	/** The Constant SPR_PMI_PARTIAL. */
	public static final String SPR_PMI_PARTIAL = "SPRPMIPartial";

	/** The Constant SPR_DOB_EXACT. */
	public static final String SPR_DOB_EXACT = "SPRDOBExact";

	/** The Constant SPR_DOB_PARTIAL. */
	public static final String SPR_DOB_PARTIAL = "SPRDOBPartial";

	/** The Constant SPR_LAST_NAME_EXACT. */
	public static final String SPR_LAST_NAME_EXACT = "SPRLastNameExact";

	/** The Constant SPR_LAST_NAME_PARTIAL. */
	public static final String SPR_LAST_NAME_PARTIAL = "SPRLastNamePartial";

	/** The Constant SPR_FIRST_NAME_EXACT. */
	public static final String SPR_FIRST_NAME_EXACT = "SPRNameExact";

	/** The Constant SPR_FIRST_NAME_PARTIAL. */
	public static final String SPR_FIRST_NAME_PARTIAL = "SPRNamePartial";

	/** The Constant SPR_CUT_OFF. */
	public static final String SPR_CUT_OFF = "SPRCutoff";

	/** The Constant SPR_THRESHOLD1. */
	public static final String SPR_THRESHOLD1 = "SPRThreshold1";

	/** The Constant SPR_THRESHOLD2. */
	public static final String SPR_THRESHOLD2 = "SPRThreshold2";

	// end here

	//Constants for integration of caTies and CAE with caTissue Core
	/** The Constant LINKED_DATA. */
	public static final String LINKED_DATA = "linkedData";

	/** The Constant APPLICATION_ID. */
	public static final String APPLICATION_ID = "applicationId";

	/** The Constant CATIES. */
	public static final String CATIES = "caTies";

	/** The Constant CAE. */
	public static final String CAE = "cae";

	/** The Constant EDIT_TAB_LINK. */
	public static final String EDIT_TAB_LINK = "editTabLink";

	/** The Constant CATIES_PUBLIC_SERVER_NAME. */
	public static final String CATIES_PUBLIC_SERVER_NAME = "CaTiesPublicServerName";

	/** The Constant CATIES_PRIVATE_SERVER_NAME. */
	public static final String CATIES_PRIVATE_SERVER_NAME = "CaTiesPrivateServerName";

	//Constants for StorageContainerMap Applet
	/** The Constant CONTAINER_STYLEID. */
	public static final String CONTAINER_STYLEID = "containerStyleId";

	/** The Constant CONTAINER_STYLE. */
	public static final String CONTAINER_STYLE = "containerStyle";

	/** The Constant XDIM_STYLEID. */
	public static final String XDIM_STYLEID = "xDimStyleId";

	/** The Constant YDIM_STYLEID. */
	public static final String YDIM_STYLEID = "yDimStyleId";

	/** The Constant SELECTED_CONTAINER_NAME. */
	public static final String SELECTED_CONTAINER_NAME = "selectedContainerName";

	/** The Constant CONTAINERID. */
	public static final String CONTAINERID = "containerId";

	/** The Constant POS1. */
	public static final String POS1 = "pos1";

	/** The Constant POS2. */
	public static final String POS2 = "pos2";

	//Constants for QuickEvents
	/** The Constant EVENT_SELECTED. */
	public static final String EVENT_SELECTED = "eventSelected";

	//Constant for SpecimenEvents page.
	/** The Constant EVENTS_TITLE_MESSAGE. */
	public static final String EVENTS_TITLE_MESSAGE = "Existing events for the specimen with label {0}";

	/** The Constant SURGICAL_PATHOLOGY_REPORT. */
	public static final String SURGICAL_PATHOLOGY_REPORT = "Surgical Pathology Report";

	/** The Constant CLINICAL_ANNOTATIONS. */
	public static final String CLINICAL_ANNOTATIONS = "Clinical Annotations";

	//Constants for Specimen Collection Group name- new field
	/** The Constant RESET_NAME. */
	public static final String RESET_NAME = "resetName";

	// Labels for Storage Containers
	/** The Constant STORAGE_CONTAINER_LABEL. */
	public static final String[] STORAGE_CONTAINER_LABEL = {" Name", " Pos1", " Pos2"};
	//Constans for Any field
	/** The Constant HOLDS_ANY. */
	public static final String HOLDS_ANY = "--All--";

	//Constants : Specimen -> lineage
	/** The Constant NEW_SPECIMEN. */
	public static final String NEW_SPECIMEN = "New";

	/** The Constant DERIVED_SPECIMEN. */
	public static final String DERIVED_SPECIMEN = "Derived";

	/** The Constant ALIQUOT. */
	public static final String ALIQUOT = "Aliquot";

	//Constant for length of messageBody in Reported problem page
	/** The Constant MESSAGE_LENGTH. */
	public static final int MESSAGE_LENGTH = 500;

	/** The Constant NEXT_NUMBER. */
	public static final String NEXT_NUMBER = "nextNumber";

	//	public static final String getCollectionProtocolPIGroupName(Long identifier)
	//	{
	//	    if(identifier == null)
	//	    {
	//	        return "PI_COLLECTION_PROTOCOL_";
	//	    }
	//	    return "PI_COLLECTION_PROTOCOL_"+identifier;
	//	}
	//
	//	public static final String getCollectionProtocolCoordinatorGroupName(Long identifier)
	//	{
	//	    if(identifier == null)
	//	    {
	//	        return "COORDINATORS_COLLECTION_PROTOCOL_";
	//	    }
	//	    return "COORDINATORS_COLLECTION_PROTOCOL_"+identifier;
	//	}
	//
	//	public static final String getDistributionProtocolPIGroupName(Long identifier)
	//	{
	//	    if(identifier == null)
	//	    {
	//	        return "PI_DISTRIBUTION_PROTOCOL_";
	//	    }
	//	    return "PI_DISTRIBUTION_PROTOCOL_"+identifier;
	//	}
	//
	//	public static final String getCollectionProtocolPGName(Long identifier)
	//	{
	//	    if(identifier == null)
	//	    {
	//	        return "COLLECTION_PROTOCOL_";
	//	    }
	//	    return "COLLECTION_PROTOCOL_"+identifier;
	//	}
	//
	//	public static final String getDistributionProtocolPGName(Long identifier)
	//	{
	//	    if(identifier == null)
	//	    {
	//	        return "DISTRIBUTION_PROTOCOL_";
	//	    }
	//	    return "DISTRIBUTION_PROTOCOL_"+identifier;
	//	}
	/** The Constant ALL. */
	public static final String ALL = "All";

	/** The Constant NONE. */
	public static final String NONE = "None";

	//constant for pagination data list
	/** The Constant PAGINATION_DATA_LIST. */
	public static final String PAGINATION_DATA_LIST = "paginationDataList";

	/** The Constant SPECIMEN_DISTRIBUTION_TYPE. */
	public static final int SPECIMEN_DISTRIBUTION_TYPE = 1;

	/** The Constant SPECIMEN_ARRAY_DISTRIBUTION_TYPE. */
	public static final int SPECIMEN_ARRAY_DISTRIBUTION_TYPE = 2;

	/** The Constant BARCODE_BASED_DISTRIBUTION. */
	public static final int BARCODE_BASED_DISTRIBUTION = 1;

	/** The Constant LABEL_BASED_DISTRIBUTION. */
	public static final int LABEL_BASED_DISTRIBUTION = 2;

	/** The Constant DISTRIBUTION_TYPE_LIST. */
	public static final String DISTRIBUTION_TYPE_LIST = "DISTRIBUTION_TYPE_LIST";

	/** The Constant DISTRIBUTION_BASED_ON. */
	public static final String DISTRIBUTION_BASED_ON = "DISTRIBUTION_BASED_ON";

	/** The Constant SYSTEM_LABEL. */
	public static final String SYSTEM_LABEL = "label";

	/** The Constant SYSTEM_BARCODE. */
	public static final String SYSTEM_BARCODE = "barcode";

	/** The Constant SYSTEM_NAME. */
	public static final String SYSTEM_NAME = "name";

	//Mandar : 05Sep06 Array for multiple specimen field names
	/** The Constant DERIVED_OPERATION. */
	public static final String DERIVED_OPERATION = "derivedOperation";

	/** The Constant MULTIPLE_SPECIMEN_FIELD_NAMES. */
	public static final String[] MULTIPLE_SPECIMEN_FIELD_NAMES = {"Collection Group", "Parent ID",
			"Name", "Barcode", "Class", "Type", "Tissue Site", "Tissue Side",
			"Pathological Status", "Concentration", "Quantity", "Storage Location", "Comments",
			"Events", "External Identifier", "Biohazards"
	//			"Derived",
	//			"Aliquots"
	};

	/** The Constant PAGE_OF_MULTIPLE_SPECIMEN. */
	public static final String PAGE_OF_MULTIPLE_SPECIMEN = "pageOfMultipleSpecimen";

	/** The Constant PAGE_OF_MULTIPLE_SPECIMEN_WITHOUT_MENU. */
	public static final String PAGE_OF_MULTIPLE_SPECIMEN_WITHOUT_MENU = "pageOfMultipleSpWithoutMenu";

	/** The Constant PAGE_OF_MULTIPLE_SPECIMEN_MAIN. */
	public static final String PAGE_OF_MULTIPLE_SPECIMEN_MAIN = "pageOfMultipleSpecimenMain";

	/** The Constant MULTIPLE_SPECIMEN_ACTION. */
	public static final String MULTIPLE_SPECIMEN_ACTION = "MultipleSpecimen.do";

	/** The Constant INIT_MULTIPLE_SPECIMEN_ACTION. */
	public static final String INIT_MULTIPLE_SPECIMEN_ACTION = "InitMultipleSpecimen.do";

	/** The Constant MULTIPLE_SPECIMEN_APPLET_ACTION. */
	public static final String MULTIPLE_SPECIMEN_APPLET_ACTION = "MultipleSpecimenAppletAction.do";

	/** The Constant NEW_MULTIPLE_SPECIMEN_ACTION. */
	public static final String NEW_MULTIPLE_SPECIMEN_ACTION = "NewMultipleSpecimenAction.do";

	/** The Constant MULTIPLE_SPECIMEN_RESULT. */
	public static final String MULTIPLE_SPECIMEN_RESULT = "multipleSpecimenResult";

	/** The Constant SAVED_SPECIMEN_COLLECTION. */
	public static final String SAVED_SPECIMEN_COLLECTION = "savedSpecimenCollection";

	/** The Constant MULTIPLE_SPECIMEN_FORM_FIELD_NAMES. */
	public static final String[] MULTIPLE_SPECIMEN_FORM_FIELD_NAMES = {"CollectionGroup",
			"ParentID", "Name", "Barcode", "Class", "Type", "TissueSite", "TissueSide",
			"PathologicalStatus", "Concentration", "Quantity", "StorageLocation", "Comments",
			"Events", "ExternalIdentifier", "Biohazards"
	//			"Derived",
	//			"Aliquots"
	};

	/** The Constant MULTIPLE_SPECIMEN_MAP_KEY. */
	public static final String MULTIPLE_SPECIMEN_MAP_KEY = "MULTIPLE_SPECIMEN_MAP_KEY";

	/** The Constant MULTIPLE_SPECIMEN_EVENT_MAP_KEY. */
	public static final String MULTIPLE_SPECIMEN_EVENT_MAP_KEY = "MULTIPLE_SPECIMEN_EVENT_MAP_KEY";

	/** The Constant MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY. */
	public static final String MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY = "MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY";

	/** The Constant MULTIPLE_SPECIMEN_BUTTONS_MAP_KEY. */
	public static final String MULTIPLE_SPECIMEN_BUTTONS_MAP_KEY = "MULTIPLE_SPECIMEN_BUTTONS_MAP_KEY";

	/** The Constant MULTIPLE_SPECIMEN_LABEL_MAP_KEY. */
	public static final String MULTIPLE_SPECIMEN_LABEL_MAP_KEY = "MULTIPLE_SPECIMEN_LABEL_MAP_KEY";

	/** The Constant DERIVED_FORM. */
	public static final String DERIVED_FORM = "DerivedForm";

	/** The Constant SPECIMEN_ATTRIBUTE_KEY. */
	public static final String SPECIMEN_ATTRIBUTE_KEY = "specimenAttributeKey";

	/** The Constant SPECIMEN_CLASS. */
	public static final String SPECIMEN_CLASS = "specimenClass";

	/** The Constant SPECIMEN_CALL_BACK_FUNCTION. */
	public static final String SPECIMEN_CALL_BACK_FUNCTION = "specimenCallBackFunction";

	/** The Constant APPEND_COUNT. */
	public static final String APPEND_COUNT = "_count";

	/** The Constant EXTERNALIDENTIFIER_TYPE. */
	public static final String EXTERNALIDENTIFIER_TYPE = "ExternalIdentifier";

	/** The Constant BIOHAZARD_TYPE. */
	public static final String BIOHAZARD_TYPE = "BioHazard";

	/** The Constant COMMENTS_TYPE. */
	public static final String COMMENTS_TYPE = "comments";

	/** The Constant EVENTS_TYPE. */
	public static final String EVENTS_TYPE = "Events";

	/** The Constant MULTIPLE_SPECIMEN_APPLET_NAME. */
	public static final String MULTIPLE_SPECIMEN_APPLET_NAME = "MultipleSpecimenApplet";

	/** The Constant INPUT_APPLET_DATA. */
	public static final String INPUT_APPLET_DATA = "inputAppletData";

	// Start Specimen Array Applet related constants

	/** The Constant SPECIMEN_ARRAY_APPLET. */
	public static final String SPECIMEN_ARRAY_APPLET = "edu/wustl/catissuecore/applet/ui/SpecimenArrayApplet.class";

	/** The Constant SPECIMEN_ARRAY_APPLET_NAME. */
	public static final String SPECIMEN_ARRAY_APPLET_NAME = "specimenArrayApplet";

	/** The Constant SPECIMEN_ARRAY_CONTENT_KEY. */
	public static final String SPECIMEN_ARRAY_CONTENT_KEY = "SpecimenArrayContentKey";

	/** The Constant SPECIMEN_LABEL_COLUMN_NAME. */
	public static final String SPECIMEN_LABEL_COLUMN_NAME = "label";

	/** The Constant SPECIMEN_BARCODE_COLUMN_NAME. */
	public static final String SPECIMEN_BARCODE_COLUMN_NAME = "barcode";
	/*public static final String ARRAY_SPECIMEN_DOES_NOT_EXIST_EXCEPTION_MESSAGE = "Please enter valid specimen for specimen array!!specimen does not exist  ";
	public static final String ARRAY_SPECIMEN_NOT_ACTIVE_EXCEPTION_MESSAGE = "Please enter valid specimen for specimen array!! Specimen is closed/disabled  ";
	public static final String ARRAY_NO_SPECIMEN__EXCEPTION_MESSAGE = "Specimen Array should contain at least one specimen";
	public static final String ARRAY_SPEC_NOT_COMPATIBLE_EXCEPTION_MESSAGE = "Please add compatible specimens to specimen array (belong to same specimen class & specimen types of Array)";
	public static final String ARRAY_MOLECULAR_QUAN_EXCEPTION_MESSAGE = "Please enter quantity for Molecular Specimen ---->";
	*//** Specify the SPECIMEN_ARRAY_LIST as key for specimen array type list. */
	public static final String SPECIMEN_ARRAY_TYPE_LIST = "specimenArrayList";

	/** The Constant SPECIMEN_ARRAY_CLASSNAME. */
	public static final String SPECIMEN_ARRAY_CLASSNAME = "edu.wustl.catissuecore.domain.SpecimenArray";

	/** The Constant SPECIMEN_CLASSNAME. */
	public static final String SPECIMEN_CLASSNAME = "edu.wustl.catissuecore.domain.Specimen";

	/** The Constant SPECIMEN_ARRAY_TYPE_CLASSNAME. */
	public static final String SPECIMEN_ARRAY_TYPE_CLASSNAME = "edu.wustl.catissuecore.domain.SpecimenArrayType";

	/** The Constant TISSUE_SPECIMEN. */
	public static final String TISSUE_SPECIMEN = "edu.wustl.catissuecore.domain.TissueSpecimen";

	/** The Constant MOLECULAR_SPECIMEN. */
	public static final String MOLECULAR_SPECIMEN = "edu.wustl.catissuecore.domain.MolecularSpecimen";

	/** The Constant FLUID_SPECIMEN. */
	public static final String FLUID_SPECIMEN = "edu.wustl.catissuecore.domain.FluidSpecimen";

	/** The Constant CELL_SPECIMEN. */
	public static final String CELL_SPECIMEN = "edu.wustl.catissuecore.domain.CellSpecimen";

	// End

	// Common Applet Constants
	/** The Constant APPLET_SERVER_HTTP_START_STR. */
	public static final String APPLET_SERVER_HTTP_START_STR = "http://";

	/** The Constant APPLET_SERVER_URL_PARAM_NAME. */
	public static final String APPLET_SERVER_URL_PARAM_NAME = "serverURL";

	//operators for simple query and advanced query
	/** The Constant IS_NOT_NULL. */
	public static final String IS_NOT_NULL = "is not null";

	/** The Constant IS_NULL. */
	public static final String IS_NULL = "is null";

	/** The Constant In. */
	public static final String In = "In";

	/** The Constant Not_In. */
	public static final String Not_In = "Not In";

	/** The Constant Equals. */
	public static final String Equals = "Equals";

	/** The Constant Not_Equals. */
	public static final String Not_Equals = "Not Equals";

	/** The Constant Between. */
	public static final String Between = "Between";

	/** The Constant Less_Than. */
	public static final String Less_Than = "Less than";

	/** The Constant Less_Than_Or_Equals. */
	public static final String Less_Than_Or_Equals = "Less than or Equal to";

	/** The Constant Greater_Than. */
	public static final String Greater_Than = "Greater than";

	/** The Constant Greater_Than_Or_Equals. */
	public static final String Greater_Than_Or_Equals = "Greater than or Equal to";

	/** The Constant Contains. */
	public static final String Contains = "Contains";

	/** The Constant STRATS_WITH. */
	public static final String STRATS_WITH = "Starts With";

	/** The Constant ENDS_WITH. */
	public static final String ENDS_WITH = "Ends With";

	/** The Constant NOT_BETWEEN. */
	public static final String NOT_BETWEEN = "Not Between";

	// Used in array action
	/** The Constant ARRAY_TYPE_ANY_VALUE. */
	public static final String ARRAY_TYPE_ANY_VALUE = "2";

	/** The Constant ARRAY_TYPE_ANY_NAME. */
	public static final String ARRAY_TYPE_ANY_NAME = "Any";
	// end

	// Array Type All Id in table
	/** The Constant ARRAY_TYPE_ALL_ID. */
	public static final short ARRAY_TYPE_ALL_ID = 2;

	// constants required for caching mechanism of ParticipantBizLogic

	/** The Constant MAP_OF_PARTICIPANTS. */
	public static final String MAP_OF_PARTICIPANTS = "listOfParticipants";

	/** The Constant LIST_OF_REGISTRATION_INFO. */
	public static final String LIST_OF_REGISTRATION_INFO = "listOfParticipantRegistrations";

	/** The Constant EHCACHE_FOR_CATISSUE_CORE. */
	public static final String EHCACHE_FOR_CATISSUE_CORE = "cacheForCaTissueCore";

	/** The Constant MAP_OF_DISABLED_CONTAINERS. */
	public static final String MAP_OF_DISABLED_CONTAINERS = "listOfDisabledContainers";

	/** The Constant MAP_OF_CONTAINER_FOR_DISABLED_SPECIEN. */
	public static final String MAP_OF_CONTAINER_FOR_DISABLED_SPECIEN = "listOfContainerForDisabledContainerSpecimen";

	/** The Constant MAP_OF_CONTAINER_FOR_DISABLED_SPECIENARRAY. */
	public static final String MAP_OF_CONTAINER_FOR_DISABLED_SPECIENARRAY = "listOfContainerForDisabledContainerSpecimenArray";

	/** The Constant ADD. */
	public static final String ADD = "add";

	/** The Constant EDIT. */
	public static final String EDIT = "edit";

	/** The Constant ID. */
	public static final String ID = "id";

	/** The Constant FILE. */
	public static final String FILE = "file";

	/** The Constant MANAGE_BIO_SPECIMEN_ACTION. */
	public static final String MANAGE_BIO_SPECIMEN_ACTION = "/ManageBioSpecimen.do";

	/** The Constant CREATE_PARTICIPANT_REGISTRATION. */
	public static final String CREATE_PARTICIPANT_REGISTRATION = "createParticipantRegistration";

	/** The Constant CREATE_PARTICIPANT_REGISTRATION_ADD. */
	public static final String CREATE_PARTICIPANT_REGISTRATION_ADD = "createParticipantRegistrationAdd";

	/** The Constant CREATE_PARTICIPANT_REGISTRATION_EDIT. */
	public static final String CREATE_PARTICIPANT_REGISTRATION_EDIT = "createParticipantRegistrationEdit";

	/** The Constant VIEW_ANNOTATION. */
	public static final String VIEW_ANNOTATION = "viewAnnotations";

	/** The Constant CAN_HOLD_CONTAINER_TYPE. */
	public static final String CAN_HOLD_CONTAINER_TYPE = "holdContainerType";

	/** The Constant CAN_HOLD_SPECIMEN_CLASS. */
	public static final String CAN_HOLD_SPECIMEN_CLASS = "holdSpecimenClass";

	/** The Constant CAN_HOLD_SPECIMEN_TYPE. */
	public static final String CAN_HOLD_SPECIMEN_TYPE = "holdSpecimenType";

	/** The Constant CAN_HOLD_COLLECTION_PROTOCOL. */
	public static final String CAN_HOLD_COLLECTION_PROTOCOL = "holdCollectionProtocol";

	/** The Constant CAN_HOLD_SPECIMEN_ARRAY_TYPE. */
	public static final String CAN_HOLD_SPECIMEN_ARRAY_TYPE = "holdSpecimenArrayType";

	/** The Constant COLLECTION_PROTOCOL_ID. */
	public static final String COLLECTION_PROTOCOL_ID = "collectionProtocolId";

	/** The Constant SPECIMEN_CLASS_NAME. */
	public static final String SPECIMEN_CLASS_NAME = "specimeClassName";

	/** The Constant ENABLE_STORAGE_CONTAINER_GRID_PAGE. */
	public static final String ENABLE_STORAGE_CONTAINER_GRID_PAGE = "enablePage";

	/** The Constant ALL_STORAGE_TYPE_ID. */
	public static final int ALL_STORAGE_TYPE_ID = 1; //Constant for the "All" storage type, which can hold all container type

	/** The Constant ALL_SPECIMEN_ARRAY_TYPE_ID. */
	public static final int ALL_SPECIMEN_ARRAY_TYPE_ID = 2;//Constant for the "All" storage type, which can hold all specimen array type

	/** The Constant SPECIMEN_LABEL_CONTAINER_MAP. */
	public static final String SPECIMEN_LABEL_CONTAINER_MAP = "Specimen : ";

	/** The Constant CONTAINER_LABEL_CONTAINER_MAP. */
	public static final String CONTAINER_LABEL_CONTAINER_MAP = "Container : ";

	/** The Constant SPECIMEN_ARRAY_LABEL_CONTAINER_MAP. */
	public static final String SPECIMEN_ARRAY_LABEL_CONTAINER_MAP = "Array : ";

	/** The Constant SPECIMEN_PROTOCOL. */
	public static final String SPECIMEN_PROTOCOL = "SpecimenProtocol";

	/** The Constant SPECIMEN_PROTOCOL_SHORT_TITLE. */
	public static final String SPECIMEN_PROTOCOL_SHORT_TITLE = "SHORT_TITLE";

	/** The Constant SPECIMEN_COLLECTION_GROUP_NAME. */
	public static final String SPECIMEN_COLLECTION_GROUP_NAME = "NAME";

	/** The Constant SPECIMEN_LABEL. */
	public static final String SPECIMEN_LABEL = "LABEL";
	// Patch ID: Bug#3184_14
	/** The Constant NUMBER_OF_SPECIMEN. */
	public static final String NUMBER_OF_SPECIMEN = "numberOfSpecimen";

	//Constants required for max limit on no. of containers in the drop down
	/** The Constant CONTAINERS_MAX_LIMIT. */
	public static final String CONTAINERS_MAX_LIMIT = "containers_max_limit";

	/** The Constant EXCEEDS_MAX_LIMIT. */
	public static final String EXCEEDS_MAX_LIMIT = "exceedsMaxLimit";

	//MultipleSpecimen Constants
	/** The Constant MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE. */
	public static final String MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE = "multipleSpecimen.ColumnsPerPage";

	/** The Constant MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION. */
	public static final String MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION = "MultipleSpecimenStorageLocationAdd.do";

	/** The Constant MULTIPLE_SPECIMEN_STORAGE_LOCATION_AVAILABLE_MAP. */
	public static final String MULTIPLE_SPECIMEN_STORAGE_LOCATION_AVAILABLE_MAP = "locationMap";

	/** The Constant MULTIPLE_SPECIMEN_STORAGE_LOCATION_SPECIMEN_MAP. */
	public static final String MULTIPLE_SPECIMEN_STORAGE_LOCATION_SPECIMEN_MAP = "specimenMap";

	/** The Constant MULTIPLE_SPECIMEN_STORAGE_LOCATION_KEY_SEPARATOR. */
	public static final String MULTIPLE_SPECIMEN_STORAGE_LOCATION_KEY_SEPARATOR = "$";

	/** The Constant PAGE_OF_MULTIPLE_SPECIMEN_STORAGE_LOCATION. */
	public static final String PAGE_OF_MULTIPLE_SPECIMEN_STORAGE_LOCATION = "formSubmitted";

	/** The Constant MULTIPLE_SPECIMEN_SUBMIT_SUCCESSFUL. */
	public static final String MULTIPLE_SPECIMEN_SUBMIT_SUCCESSFUL = "submitSuccessful";

	/** The Constant MULTIPLE_SPECIMEN_SPECIMEN_ORDER_LIST. */
	public static final String MULTIPLE_SPECIMEN_SPECIMEN_ORDER_LIST = "specimenOrderList";

	/** The Constant MULTIPLE_SPECIMEN_DELETELAST_SPECIMEN_ID. */
	public static final String MULTIPLE_SPECIMEN_DELETELAST_SPECIMEN_ID = "SpecimenId";

	/** The Constant MULTIPLE_SPECIMEN_PARENT_COLLECTION_GROUP. */
	public static final String MULTIPLE_SPECIMEN_PARENT_COLLECTION_GROUP = "ParentSpecimenCollectionGroup";

	/** The Constant NO_OF_RECORDS_PER_PAGE. */
	public static final String NO_OF_RECORDS_PER_PAGE = "resultView.noOfRecordsPerPage";

	/** Name: Prafull Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation. object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages. Changes resultper page options, removed 5000 from the array & added 500 as another option. */
	public static final int[] RESULT_PERPAGE_OPTIONS = {10, 50, 100, 500, 1000};

	/** Specify the SPECIMEN_MAP_KEY field ) used in multiple specimen applet action. */
	public static final String SPECIMEN_MAP_KEY = "Specimen_derived_map_key";

	/** Specify the SPECIMEN_MAP_KEY field ) used in multiple specimen applet action. */
	public static final String CONTAINER_MAP_KEY = "container_map_key";

	/** Used to saperate storage container, xPos, yPos. */
	public static final String STORAGE_LOCATION_SAPERATOR = "@";

	/** The Constant METHOD_NAME. */
	public static final String METHOD_NAME = "method";

	/** The Constant GRID_FOR_EVENTS. */
	public static final String GRID_FOR_EVENTS = "eventParameter";

	/** The Constant GRID_FOR_EDIT_SEARCH. */
	public static final String GRID_FOR_EDIT_SEARCH = "editSearch";

	/** The Constant GRID_FOR_DERIVED_SPECIMEN. */
	public static final String GRID_FOR_DERIVED_SPECIMEN = "derivedSpecimen";
	/*
	 * used to find whether iframe needs to be reloaded or not
	 */
	/** The Constant RELOAD. */
	public static final String RELOAD = "false";

	/** The Constant CHILD_CONTAINER_NAME. */
	public static final String CHILD_CONTAINER_NAME = "childContainerName";

	//CpBasedSearch Constants
	/** The Constant CP_QUERY. */
	public static final String CP_QUERY = "CPQuery";

	/** The Constant CP_QUERY_PARTICIPANT_EDIT_ACTION. */
	public static final String CP_QUERY_PARTICIPANT_EDIT_ACTION = "CPQueryParticipantEdit.do";

	/** The Constant CP_QUERY_PARTICIPANT_ADD_ACTION. */
	public static final String CP_QUERY_PARTICIPANT_ADD_ACTION = "CPQueryParticipantAdd.do";

	/** The Constant CP_QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION. */
	public static final String CP_QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION = "CPQuerySpecimenCollectionGroupAdd.do";

	/** The Constant CP_QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION. */
	public static final String CP_QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION = "CPQuerySpecimenCollectionGroupEdit.do";

	/** The Constant CP_AND_PARTICIPANT_VIEW. */
	public static final String CP_AND_PARTICIPANT_VIEW = "cpAndParticipantView";

	/** The Constant DATA_DETAILS_VIEW. */
	public static final String DATA_DETAILS_VIEW = "dataDetailsView";

	/** The Constant SHOW_CP_AND_PARTICIPANTS_ACTION. */
	public static final String SHOW_CP_AND_PARTICIPANTS_ACTION = "showCpAndParticipants.do";

	/** The Constant PAGE_OF_CP_QUERY_RESULTS. */
	public static final String PAGE_OF_CP_QUERY_RESULTS = "pageOfCpQueryResults";

	/** The Constant CP_LIST. */
	public static final String CP_LIST = "cpList";

	/** The Constant CP_ID_TITLE_MAP. */
	public static final String CP_ID_TITLE_MAP = "cpIDTitleMap";

	/** The Constant REGISTERED_PARTICIPANT_LIST. */
	public static final String REGISTERED_PARTICIPANT_LIST = "participantList";

	/** The Constant PAGE_OF_PARTICIPANT_CP_QUERY. */
	public static final String PAGE_OF_PARTICIPANT_CP_QUERY = "pageOfParticipantCPQuery";

	/** The Constant PAGE_OF_SCG_CP_QUERY. */
	public static final String PAGE_OF_SCG_CP_QUERY = "pageOfSpecimenCollectionGroupCPQuery";

	/** The Constant CP_SEARCH_PARTICIPANT_ID. */
	public static final String CP_SEARCH_PARTICIPANT_ID = "cpSearchParticipantId";

	/** The Constant CP_SEARCH_CP_ID. */
	public static final String CP_SEARCH_CP_ID = "cpSearchCpId";

	/** The Constant CP_TREE_VIEW. */
	public static final String CP_TREE_VIEW = "cpTreeView";

	/** The Constant CP_TREE_VIEW_ACTION. */
	public static final String CP_TREE_VIEW_ACTION = "showTree.do";

	/** The Constant PAGE_OF_SPECIMEN_CP_QUERY. */
	public static final String PAGE_OF_SPECIMEN_CP_QUERY = "pageOfNewSpecimenCPQuery";

	/** The Constant CP_QUERY_SPECIMEN_ADD_ACTION. */
	public static final String CP_QUERY_SPECIMEN_ADD_ACTION = "CPQueryNewSpecimenAdd.do";

	/** The Constant CP_QUERY_CREATE_SPECIMEN_ACTION. */
	public static final String CP_QUERY_CREATE_SPECIMEN_ACTION = "CPQueryCreateSpecimen.do";

	/** The Constant CP_QUERY_SPECIMEN_EDIT_ACTION. */
	public static final String CP_QUERY_SPECIMEN_EDIT_ACTION = "CPQueryNewSpecimenEdit.do";

	/** The Constant PAGE_OF_CREATE_SPECIMEN_CP_QUERY. */
	public static final String PAGE_OF_CREATE_SPECIMEN_CP_QUERY = "pageOfCreateSpecimenCPQuery";

	/** The Constant PAGE_OF_ALIQUOT_CP_QUERY. */
	public static final String PAGE_OF_ALIQUOT_CP_QUERY = "pageOfAliquotCPQuery";

	/** The Constant PAGE_OF_CREATE_ALIQUOT_CP_QUERY. */
	public static final String PAGE_OF_CREATE_ALIQUOT_CP_QUERY = "pageOfCreateAliquotCPQuery";

	/** The Constant PAGE_OF_ALIQUOT_SUMMARY_CP_QUERY. */
	public static final String PAGE_OF_ALIQUOT_SUMMARY_CP_QUERY = "pageOfAliquotSummaryCPQuery";

	/** The Constant CP_QUERY_CREATE_ALIQUOT_ACTION. */
	public static final String CP_QUERY_CREATE_ALIQUOT_ACTION = "CPQueryCreateAliquots.do";

	/** The Constant CP_QUERY_ALIQUOT_SUMMARY_ACTION. */
	public static final String CP_QUERY_ALIQUOT_SUMMARY_ACTION = "CPQueryAliquotSummary.do";

	/** The Constant PAGE_OF_DISTRIBUTION_CP_QUERY. */
	public static final String PAGE_OF_DISTRIBUTION_CP_QUERY = "pageOfDistributionCPQuery";

	/** The Constant CP_QUERY_DISTRIBUTION_EDIT_ACTION. */
	public static final String CP_QUERY_DISTRIBUTION_EDIT_ACTION = "CPQueryDistributionEdit.do";

	/** The Constant CP_QUERY_DISTRIBUTION_ADD_ACTION. */
	public static final String CP_QUERY_DISTRIBUTION_ADD_ACTION = "CPQueryDistributionAdd.do";

	/** The Constant CP_QUERY_DISTRIBUTION_REPORT_SAVE_ACTION. */
	public static final String CP_QUERY_DISTRIBUTION_REPORT_SAVE_ACTION = "CPQueryDistributionReportSave.do";

	/** The Constant CP_QUERY_ARRAY_DISTRIBUTION_REPORT_SAVE_ACTION. */
	public static final String CP_QUERY_ARRAY_DISTRIBUTION_REPORT_SAVE_ACTION = "CPQueryArrayDistributionReportSave.do";

	/** The Constant CP_QUERY_CONFIGURE_DISTRIBUTION_ACTION. */
	public static final String CP_QUERY_CONFIGURE_DISTRIBUTION_ACTION = "CPQueryConfigureDistribution.do";

	/** The Constant CP_QUERY_DISTRIBUTION_REPORT_ACTION. */
	public static final String CP_QUERY_DISTRIBUTION_REPORT_ACTION = "CPQueryDistributionReport.do";

	/** The Constant PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY. */
	public static final String PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY = "pageOfListSpecimenEventParametersCPQuery";

	/** The Constant PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS. */
	public static final String PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS = "pageOfListSpecimenEventParameters";

	/** The Constant PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY. */
	public static final String PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY = "pageOfCollectionProtocolRegistrationCPQuery";

	/** The Constant PAGE_OF_MULTIPLE_SPECIMEN_CP_QUERY. */
	public static final String PAGE_OF_MULTIPLE_SPECIMEN_CP_QUERY = "pageOfMultipleSpecimenCPQuery";

	/** The Constant CP_QUERY_NEW_MULTIPLE_SPECIMEN_ACTION. */
	public static final String CP_QUERY_NEW_MULTIPLE_SPECIMEN_ACTION = "CPQueryNewMultipleSpecimenAction.do";

	/** The Constant CP_QUERY_MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION. */
	public static final String CP_QUERY_MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION = "CPQueryMultipleSpecimenStorageLocationAdd.do";

	/** The Constant CP_QUERY_PAGE_OF_MULTIPLE_SPECIMEN_STORAGE_LOCATION. */
	public static final String CP_QUERY_PAGE_OF_MULTIPLE_SPECIMEN_STORAGE_LOCATION = "CPQueryformSubmitted";

	/** The Constant CP_QUERY_COLLECTION_PROTOCOL_REGISTRATION_ADD_ACTION. */
	public static final String CP_QUERY_COLLECTION_PROTOCOL_REGISTRATION_ADD_ACTION = "CPQueryCollectionProtocolRegistrationAdd.do";

	/** The Constant CP_QUERY_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION. */
	public static final String CP_QUERY_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "CPQueryCollectionProtocolRegistrationEdit.do";

	/** The Constant CP_QUERY_PARTICIPANT_LOOKUP_ACTION. */
	public static final String CP_QUERY_PARTICIPANT_LOOKUP_ACTION = "CPQueryParticipantLookup.do";

	/** The Constant CP_QUERY_BIO_SPECIMEN. */
	public static final String CP_QUERY_BIO_SPECIMEN = "/QueryManageBioSpecimen.do";

	//Mandar : 15-Jan-07
	/** The Constant WITHDRAW_RESPONSE_NOACTION. */
	public static final String WITHDRAW_RESPONSE_NOACTION = "No Action";

	/** The Constant WITHDRAW_RESPONSE_DISCARD. */
	public static final String WITHDRAW_RESPONSE_DISCARD = "Discard";

	/** The Constant WITHDRAW_RESPONSE_RETURN. */
	public static final String WITHDRAW_RESPONSE_RETURN = "Return";

	/** The Constant WITHDRAW_RESPONSE_RESET. */
	public static final String WITHDRAW_RESPONSE_RESET = "Reset";

	/** The Constant WITHDRAW_RESPONSE_REASON. */
	public static final String WITHDRAW_RESPONSE_REASON = "Specimen consents withdrawn";

	/** The Constant SEARCH_CATEGORY_LIST_SELECT_TAG_NAME. */
	public static final String SEARCH_CATEGORY_LIST_SELECT_TAG_NAME = "selectCategoryList";

	/** The Constant SEARCH_CATEGORY_LIST_FUNCTION_NAME. */
	public static final String SEARCH_CATEGORY_LIST_FUNCTION_NAME = "getSelectedEntities";

	/** The Constant EDIT_CONDN. */
	public static final String EDIT_CONDN = "Edit";

	/** The Constant SPLITTER_STATUS_REQ_PARAM. */
	public static final String SPLITTER_STATUS_REQ_PARAM = "SPLITTER_STATUS";

	//mulltiple specimen Applet constants
	/** The Constant VALIDATE_TEXT. */
	public static final int VALIDATE_TEXT = 1;

	/** The Constant VALIDATE_COMBO. */
	public static final int VALIDATE_COMBO = 2;

	/** The Constant VALIDATE_DATE. */
	public static final int VALIDATE_DATE = 3;

	/** Constants required for maintaining toolTipText for event button on multiple specimen page. */
	public static final String TOOLTIP_TEXT = "TOOLTIPTEXT";

	/** The Constant MULTIPLE_SPECIMEN_TOOLTIP_MAP_KEY. */
	public static final String MULTIPLE_SPECIMEN_TOOLTIP_MAP_KEY = "multipleSpecimenTooltipMapKey";

	/** The Constant DEFAULT_TOOLTIP_TEXT. */
	public static final String DEFAULT_TOOLTIP_TEXT = "DEFAULTTOOLTIPTEXT";

	/** Patch ID: Bug#3184_15 (for Multiple Specimen) Description: The following constants are used as key in the Map. */
	public static final String KEY_SPECIMEN_CLASS = "SpecimenClass";

	/** The Constant KEY_SPECIMEN_TYPE. */
	public static final String KEY_SPECIMEN_TYPE = "SpecimenType";

	/** The Constant KEY_TISSUE_SITE. */
	public static final String KEY_TISSUE_SITE = "TissueSite";

	/** The Constant KEY_PATHOLOGICAL_STATUS. */
	public static final String KEY_PATHOLOGICAL_STATUS = "PathologicalStatus";

	/** The Constant KEY_SPECIMEN_REQUIREMENT_PREFIX. */
	public static final String KEY_SPECIMEN_REQUIREMENT_PREFIX = "SpecimenRequirement_";

	/** The Constant KEY_RESTRICTED_VALUES. */
	public static final String KEY_RESTRICTED_VALUES = "RestictedValues";

	/** The Constant KEY_QUANTITY. */
	public static final String KEY_QUANTITY = "Quantity";

	// The following constants are used for multiple specimens applet classes
	/** The Constant NUMBER_OF_SPECIMEN_REQUIREMENTS. */
	public static final String NUMBER_OF_SPECIMEN_REQUIREMENTS = "numberOfSpecimenRequirements";

	/** The Constant RESTRICT_SCG_CHECKBOX. */
	public static final String RESTRICT_SCG_CHECKBOX = "restrictSCGCheckbox";

	/** The Constant ON_COLL_OR_CLASSCHANGE. */
	public static final String ON_COLL_OR_CLASSCHANGE = "onCollOrClassChange";

	/** The Constant NUMBER_OF_SPECIMENS. */
	public static final String NUMBER_OF_SPECIMENS = "numberOfSpecimens";

	/** The Constant CHANGE_ON. */
	public static final String CHANGE_ON = "changeOn";

	// Patch ID: Bug#4180_3
	/** The Constant EVENT_NAME. */
	public static final String EVENT_NAME = "eventName";

	/** The Constant USER_NAME. */
	public static final String USER_NAME = "userName";

	/** The Constant EVENT_DATE. */
	public static final String EVENT_DATE = "eventDate";

	/** The Constant PAGE_OF. */
	public static final String PAGE_OF = "pageOf";

	/** The Constant IS_SCG_SUBMIT. */
	public static final String IS_SCG_SUBMIT = "isSCGSubmit";

	/** The Constant SPECIMEN_SUBMIT. */
	public static final String SPECIMEN_SUBMIT = "specimenSubmit";

	/** The Constant SCG_SUBMIT. */
	public static final String SCG_SUBMIT = "SCGSubmit";

	/** Patch ID: for Future SCG_15 Description: The following constants are used as id for future scg in tree. */
	public static final String FUTURE_SCG = "future";

	// Patch ID: SimpleSearchEdit_5
	// AliasName constants, used in Edit in Simple Search feature.
	/** The Constant SPREADSHEET_COLUMN_LIST. */
	public static final String SPREADSHEET_COLUMN_LIST = "spreadsheetColumnList";

	/** The Constant ALIAS_COLLECTION_PROTOCOL. */
	public static final String ALIAS_COLLECTION_PROTOCOL = "CollectionProtocol";

	/** The Constant ALIAS_BIOHAZARD. */
	public static final String ALIAS_BIOHAZARD = "Biohazard";

	/** The Constant ALIAS_CANCER_RESEARCH_GROUP. */
	public static final String ALIAS_CANCER_RESEARCH_GROUP = "CancerResearchGroup";

	/** The Constant ALIAS_COLLECTION_PROTOCOL_REG. */
	public static final String ALIAS_COLLECTION_PROTOCOL_REG = "CollectionProtReg";

	/** The Constant ALIAS_DEPARTMENT. */
	public static final String ALIAS_DEPARTMENT = "Department";

	/** The Constant ALIAS_DISTRIBUTION. */
	public static final String ALIAS_DISTRIBUTION = "Distribution";

	/** The Constant ALIAS_DISTRIBUTION_PROTOCOL. */
	public static final String ALIAS_DISTRIBUTION_PROTOCOL = "DistributionProtocol";

	/** The Constant ALIAS_DISTRIBUTION_ARRAY. */
	public static final String ALIAS_DISTRIBUTION_ARRAY = "Distribution_array";

	/** The Constant ALIAS_INSTITUTE. */
	public static final String ALIAS_INSTITUTE = "Institution";

	/** The Constant ALIAS_PARTICIPANT. */
	public static final String ALIAS_PARTICIPANT = "Participant";

	/** The Constant ALIAS_SITE. */
	public static final String ALIAS_SITE = "Site";

	/** The Constant ALIAS_SPECIMEN. */
	public static final String ALIAS_SPECIMEN = "Specimen";

	/** The Constant ALIAS_SPECIMEN_ARRAY. */
	public static final String ALIAS_SPECIMEN_ARRAY = "SpecimenArray";

	/** The Constant ALIAS_SPECIMEN_ARRAY_TYPE. */
	public static final String ALIAS_SPECIMEN_ARRAY_TYPE = "SpecimenArrayType";

	/** The Constant ALIAS_SPECIMEN_COLLECTION_GROUP. */
	public static final String ALIAS_SPECIMEN_COLLECTION_GROUP = "SpecimenCollectionGroup";

	/** The Constant ALIAS_STORAGE_CONTAINER. */
	public static final String ALIAS_STORAGE_CONTAINER = "StorageContainer";

	/** The Constant ALIAS_STORAGE_TYPE. */
	public static final String ALIAS_STORAGE_TYPE = "StorageType";

	/** The Constant ALIAS_USER. */
	public static final String ALIAS_USER = "User";

	/** The Constant PAGE_OF_BIOHAZARD. */
	public static final String PAGE_OF_BIOHAZARD = "pageOfBioHazard";

	/** The Constant PAGE_OF_CANCER_RESEARCH_GROUP. */
	public static final String PAGE_OF_CANCER_RESEARCH_GROUP = "pageOfCancerResearchGroup";

	/** The Constant PAGE_OF_COLLECTION_PROTOCOL. */
	public static final String PAGE_OF_COLLECTION_PROTOCOL = "pageOfCollectionProtocol";

	/** The Constant PAGE_OF_COLLECTION_PROTOCOL_REG. */
	public static final String PAGE_OF_COLLECTION_PROTOCOL_REG = "pageOfCollectionProtocolRegistration";

	/** The Constant PAGE_OF_DEPARTMENT. */
	public static final String PAGE_OF_DEPARTMENT = "pageOfDepartment";

	/** The Constant PAGE_OF_DISTRIBUTION. */
	public static final String PAGE_OF_DISTRIBUTION = "pageOfDistribution";

	/** The Constant PAGE_OF_DISTRIBUTION_PROTOCOL. */
	public static final String PAGE_OF_DISTRIBUTION_PROTOCOL = "pageOfDistributionProtocol";

	/** The Constant PAGE_OF_DISTRIBUTION_ARRAY. */
	public static final String PAGE_OF_DISTRIBUTION_ARRAY = "pageOfArrayDistribution";

	/** The Constant PAGE_OF_INSTITUTE. */
	public static final String PAGE_OF_INSTITUTE = "pageOfInstitution";

	/** The Constant PAGE_OF_PARTICIPANT. */
	public static final String PAGE_OF_PARTICIPANT = "pageOfParticipant";

	/** The Constant PAGE_OF_SITE. */
	public static final String PAGE_OF_SITE = "pageOfSite";

	/** The Constant PAGE_OF_SPECIMEN_ARRAY. */
	public static final String PAGE_OF_SPECIMEN_ARRAY = "pageOfSpecimenArray";

	/** The Constant PAGE_OF_SPECIMEN_ARRAY_TYPE. */
	public static final String PAGE_OF_SPECIMEN_ARRAY_TYPE = "pageOfSpecimenArrayType";

	/** The Constant PAGE_OF_SPECIMEN_COLLECTION_GROUP. */
	public static final String PAGE_OF_SPECIMEN_COLLECTION_GROUP = "pageOfSpecimenCollectionGroup";

	/** The Constant PAGE_OF_STORAGE_CONTAINER. */
	public static final String PAGE_OF_STORAGE_CONTAINER = "pageOfStorageContainer";

	/** The Constant PAGE_OF_STORAGE_TYPE. */
	public static final String PAGE_OF_STORAGE_TYPE = "pageOfStorageType";

	/** The Constant PAGE_OF_USER. */
	public static final String PAGE_OF_USER = "pageOfUserAdmin";

	//Patch ID: Bug#4227_3
	/** The Constant SUBMIT_AND_ADD_MULTIPLE. */
	public static final String SUBMIT_AND_ADD_MULTIPLE = "submitAndAddMultiple";

	//constants for Storage container map radio button identification  Patch id: 4283_3
	/** The Constant RADIO_BUTTON_VIRTUALLY_LOCATED. */
	public static final int RADIO_BUTTON_VIRTUALLY_LOCATED = 1;

	/** The Constant RADIO_BUTTON_FOR_MAP. */
	public static final int RADIO_BUTTON_FOR_MAP = 3;
	//	constant for putting blank screen in the framed pages
	/** The Constant BLANK_SCREEN_ACTION. */
	public static final String BLANK_SCREEN_ACTION = "blankScreenAction.do";

	/** The Constant COLUMN_NAME_SPECIMEN_ID. */
	public static final String COLUMN_NAME_SPECIMEN_ID = "specimen.id";

	/** The Constant PARTICIPANT_MEDICAL_IDENTIFIER. */
	public static final String PARTICIPANT_MEDICAL_IDENTIFIER = "ParticipantMedicalIdentifier:";

	/** The Constant PARTICIPANT_MEDICAL_IDENTIFIER_SITE_ID. */
	public static final String PARTICIPANT_MEDICAL_IDENTIFIER_SITE_ID = "_Site_id";

	/** The Constant PARTICIPANT_MEDICAL_IDENTIFIER_MEDICAL_NUMBER. */
	public static final String PARTICIPANT_MEDICAL_IDENTIFIER_MEDICAL_NUMBER = "_medicalRecordNumber";

	/** The Constant PARTICIPANT_MEDICAL_IDENTIFIER_ID. */
	public static final String PARTICIPANT_MEDICAL_IDENTIFIER_ID = "_id";

	/** The Constant COLUMN_NAME_SPECIEMEN_REQUIREMENT_COLLECTION. */
	public static final String COLUMN_NAME_SPECIEMEN_REQUIREMENT_COLLECTION = "elements(specimenRequirementCollection)";

	/** The Constant COLUMN_NAME_PARTICIPANT. */
	public static final String COLUMN_NAME_PARTICIPANT = "participant";

	/** The Constant ADDNEW_LINK. */
	public static final String ADDNEW_LINK = "AddNew";

	/** The Constant COLUMN_NAME_STORAGE_CONTAINER. */
	public static final String COLUMN_NAME_STORAGE_CONTAINER = "storageContainer";

	/** The Constant COLUMN_NAME_SCG_CPR_CP_ID. */
	public static final String COLUMN_NAME_SCG_CPR_CP_ID = "specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.id";

	/** The Constant COLUMN_NAME_CPR_CP_ID. */
	public static final String COLUMN_NAME_CPR_CP_ID = "collectionProtocolRegistration.collectionProtocol.id";

	/** The Constant EQUALS. */
	public static final String EQUALS = "=";

	/** The Constant COLUMN_NAME_SCG_NAME. */
	public static final String COLUMN_NAME_SCG_NAME = "specimenCollectionGroup.name";

	/** The Constant COLUMN_NAME_SPECIMEN. */
	public static final String COLUMN_NAME_SPECIMEN = "specimen";

	/** The Constant COLUMN_NAME_SCG. */
	public static final String COLUMN_NAME_SCG = "specimenCollectionGroup";

	/** The Constant COLUMN_NAME_CHILDREN. */
	public static final String COLUMN_NAME_CHILDREN = "elements(children)";

	/** The Constant COLUMN_NAME_SCG_ID. */
	public static final String COLUMN_NAME_SCG_ID = "specimenCollectionGroup.id";

	/** The Constant COLUMN_NAME_PART_MEDICAL_ID_COLL. */
	public static final String COLUMN_NAME_PART_MEDICAL_ID_COLL = "elements(participantMedicalIdentifierCollection)";

	/** The Constant COLUMN_NAME_PART_RACE_COLL. */
	public static final String COLUMN_NAME_PART_RACE_COLL = "elements(raceCollection)";

	/** The Constant COLUMN_NAME_CPR_COLL. */
	public static final String COLUMN_NAME_CPR_COLL = "elements(collectionProtocolRegistrationCollection)";

	/** The Constant COLUMN_NAME_SCG_COLL. */
	public static final String COLUMN_NAME_SCG_COLL = "elements(specimenCollectionGroupCollection)";

	/** The Constant COLUMN_NAME_COLL_PROT_EVENT_COLL. */
	public static final String COLUMN_NAME_COLL_PROT_EVENT_COLL = "elements(collectionProtocolEventCollection)";

	/** The Constant COLUMN_NAME_CONCEPT_REF_COLL. */
	public static final String COLUMN_NAME_CONCEPT_REF_COLL = "elements(conceptReferentCollection)";

	/** The Constant COLUMN_NAME_DEID_REPORT. */
	public static final String COLUMN_NAME_DEID_REPORT = "deIdentifiedSurgicalPathologyReport";

	/** The Constant COLUMN_NAME_REPORT_SOURCE. */
	public static final String COLUMN_NAME_REPORT_SOURCE = "reportSource";

	/** The Constant COLUMN_NAME_TEXT_CONTENT. */
	public static final String COLUMN_NAME_TEXT_CONTENT = "textContent";

	/** The Constant COLUMN_NAME_TITLE. */
	public static final String COLUMN_NAME_TITLE = "title";

	/** The Constant COLUMN_NAME_PARTICIPANT_ID. */
	public static final String COLUMN_NAME_PARTICIPANT_ID = "participant.id";

	/** The Constant COLUMN_NAME_REPORT_SECTION_COLL. */
	public static final String COLUMN_NAME_REPORT_SECTION_COLL = "elements(reportSectionCollection)";

	/** The Constant COLUMN_NAME_SCG_SITE. */
	public static final String COLUMN_NAME_SCG_SITE = "specimenCollectionSite";

	/** The Constant COLUMN_NAME_STATUS. */
	public static final String COLUMN_NAME_STATUS = "status";

	/** The Constant COLUMN_NAME_CPR. */
	public static final String COLUMN_NAME_CPR = "collectionProtocolRegistration";

	/** The Constant COLUMN_NAME_COLL_PROT_EVENT. */
	public static final String COLUMN_NAME_COLL_PROT_EVENT = "collectionProtocolEvent";

	/** The Constant COLUMN_NAME_SCG_CPR_PARTICIPANT_ID. */
	public static final String COLUMN_NAME_SCG_CPR_PARTICIPANT_ID = "specimenCollectionGroup.collectionProtocolRegistration.participant.id";

	//Bug 2833. Field for the length of CP Title
	/** The Constant COLLECTION_PROTOCOL_TITLE_LENGTH. */
	public static final int COLLECTION_PROTOCOL_TITLE_LENGTH = 30;
	//Bug ID 4794: Field for advance time to warn a user about session expiry
	/** The Constant SESSION_EXPIRY_WARNING_ADVANCE_TIME. */
	public static final String SESSION_EXPIRY_WARNING_ADVANCE_TIME = "session.expiry.warning.advanceTime";
	//Bug 12735: Field for sessiom time out
	/** The Constant SESSION_TIME_OUT. */
	public static final String SESSION_TIME_OUT = "session.time.out";
	//	Constants required in RequestDetailsPage
	/** The Constant SUBMIT_REQUEST_DETAILS_ACTION. */
	public static final String SUBMIT_REQUEST_DETAILS_ACTION = "SubmitRequestDetails.do";

	/** The Constant REQUEST_HEADER_OBJECT. */
	public static final String REQUEST_HEADER_OBJECT = "requestHeaderObject";

	/** The Constant SITE_LIST_OBJECT. */
	public static final String SITE_LIST_OBJECT = "siteList";

	/** The Constant REQUEST_DETAILS_PAGE. */
	public static final String REQUEST_DETAILS_PAGE = "RequestDetails.do";

	/** The Constant ARRAYREQUEST_DETAILS_PAGE. */
	public static final String ARRAYREQUEST_DETAILS_PAGE = "ArrayRequests.do";

	/** The Constant ARRAY_REQUESTS_LIST. */
	public static final String ARRAY_REQUESTS_LIST = "arrayRequestsList";

	/** The Constant EXISISTINGARRAY_REQUESTS_LIST. */
	public static final String EXISISTINGARRAY_REQUESTS_LIST = "existingArrayRequestDetailsList";

	/** The Constant DEFINEDARRAY_REQUESTS_LIST. */
	public static final String DEFINEDARRAY_REQUESTS_LIST = "DefinedRequestDetailsMapList";

	/** The Constant ITEM_STATUS_LIST. */
	public static final String ITEM_STATUS_LIST = "itemsStatusList";

	/** The Constant ITEM_STATUS_LIST_WO_DISTRIBUTE. */
	public static final String ITEM_STATUS_LIST_WO_DISTRIBUTE = "itemsStatusListWithoutDistribute";

	/** The Constant ITEM_STATUS_LIST_FOR_ITEMS_IN_ARRAY. */
	public static final String ITEM_STATUS_LIST_FOR_ITEMS_IN_ARRAY = "itemsStatusListForItemsInArray";

	/** The Constant REQUEST_FOR_LIST. */
	public static final String REQUEST_FOR_LIST = "requestForList";

	//	Constants for Order Request Status.
	/** The Constant ORDER_REQUEST_STATUS_NEW. */
	public static final String ORDER_REQUEST_STATUS_NEW = "New";

	/** The Constant ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW. */
	public static final String ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW = "Pending - Protocol Review";

	/** The Constant ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION. */
	public static final String ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION = "Pending - Specimen Preparation";

	/** The Constant ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION. */
	public static final String ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION = "Pending - For Distribution";

	/** The Constant ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST. */
	public static final String ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST = "Rejected - Inappropriate Request";

	/** The Constant ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE. */
	public static final String ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE = "Rejected - Specimen Unavailable";

	/** The Constant ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE. */
	public static final String ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE = "Rejected - Unable to Create";

	/** The Constant ORDER_REQUEST_STATUS_DISTRIBUTED. */
	public static final String ORDER_REQUEST_STATUS_DISTRIBUTED = "Distributed";

	/** The Constant ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION. */
	public static final String ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION = "Ready For Array Preparation";
	//	Used for tree display in RequestDetails page
	/** The Constant TREE_DATA_LIST. */
	public static final String TREE_DATA_LIST = "treeDataList";

	//Constants for Order Status
	/** The Constant ORDER_STATUS_NEW. */
	public static final String ORDER_STATUS_NEW = "New";

	/** The Constant ORDER_STATUS_PENDING. */
	public static final String ORDER_STATUS_PENDING = "Pending";

	/** The Constant ORDER_STATUS_REJECTED. */
	public static final String ORDER_STATUS_REJECTED = "Rejected";

	/** The Constant ORDER_STATUS_COMPLETED. */
	public static final String ORDER_STATUS_COMPLETED = "Completed";

	//	Ordering System Status
	/** The Constant CDE_NAME_REQUEST_STATUS. */
	public static final String CDE_NAME_REQUEST_STATUS = "Request Status";

	/** The Constant CDE_NAME_REQUESTED_ITEMS_STATUS. */
	public static final String CDE_NAME_REQUESTED_ITEMS_STATUS = "Requested Items Status";

	/** The Constant REQUEST_LIST. */
	public static final String REQUEST_LIST = "requestStatusList";

	/** The Constant REQUESTED_ITEMS_STATUS_LIST. */
	public static final String REQUESTED_ITEMS_STATUS_LIST = "requestedItemsStatusList";

	/** The Constant ARRAY_STATUS_LIST. */
	public static final String ARRAY_STATUS_LIST = "arrayStatusList";

	/** The Constant REQUEST_OBJECT. */
	public static final String REQUEST_OBJECT = "requestObjectList";

	/** The Constant REQUEST_DETAILS_LIST. */
	public static final String REQUEST_DETAILS_LIST = "requestDetailsList";

	/** The Constant ARRAY_REQUESTS_BEAN_LIST. */
	public static final String ARRAY_REQUESTS_BEAN_LIST = "arrayRequestsBeanList";

	/** The Constant SPECIMEN_ORDER_FORM_TYPE. */
	public static final String SPECIMEN_ORDER_FORM_TYPE = "specimen";

	/** The Constant ARRAY_ORDER_FORM_TYPE. */
	public static final String ARRAY_ORDER_FORM_TYPE = "specimenArray";

	/** The Constant PATHOLOGYCASE_ORDER_FORM_TYPE. */
	public static final String PATHOLOGYCASE_ORDER_FORM_TYPE = "pathologyCase";

	/** The Constant REQUESTED_BIOSPECIMENS. */
	public static final String REQUESTED_BIOSPECIMENS = "RequestedBioSpecimens";

	//Constants required in Ordering System.
	/** The Constant ACTION_ORDER_LIST. */
	public static final String ACTION_ORDER_LIST = "OrderExistingSpecimen.do";

	/** The Constant SPECIMEN_TREE_SPECIMEN_ID. */
	public static final String SPECIMEN_TREE_SPECIMEN_ID = "specimenId";

	/** The Constant SPECIMEN_TREE_SPECCOLLGRP_ID. */
	public static final String SPECIMEN_TREE_SPECCOLLGRP_ID = "specimenCollGrpId";

	/** The Constant ACTION_REMOVE_ORDER_ITEM. */
	public static final String ACTION_REMOVE_ORDER_ITEM = "AddToOrderListSpecimen.do?remove=yes";

	/** The Constant ACTION_REMOVE_ORDER_ITEM_ARRAY. */
	public static final String ACTION_REMOVE_ORDER_ITEM_ARRAY = "AddToOrderListArray.do?remove=yes";

	/** The Constant ACTION_REMOVE_ORDER_ITEM_PATHOLOGYCASE. */
	public static final String ACTION_REMOVE_ORDER_ITEM_PATHOLOGYCASE = "AddToOrderListPathologyCase.do?remove=yes";

	/** The Constant DEFINEARRAY_REQUESTMAP_LIST. */
	public static final String DEFINEARRAY_REQUESTMAP_LIST = "definedArrayRequestMapList";

	/** The Constant CREATE_DEFINED_ARRAY. */
	public static final String CREATE_DEFINED_ARRAY = "CreateDefinedArray.do";

	/** The Constant ACTION_SAVE_ORDER_ITEM. */
	public static final String ACTION_SAVE_ORDER_ITEM = "SaveOrderItems.do";

	/** The Constant ORDERTO_LIST_ARRAY. */
	public static final String ORDERTO_LIST_ARRAY = "orderToListArrayList";

	/** The Constant ACTION_SAVE_ORDER_ARRAY_ITEM. */
	public static final String ACTION_SAVE_ORDER_ARRAY_ITEM = "SaveOrderArrayItems.do";

	/** The Constant ACTION_SAVE_ORDER_PATHOLOGY_ITEM. */
	public static final String ACTION_SAVE_ORDER_PATHOLOGY_ITEM = "SaveOrderPathologyItems.do";

	/** The Constant ACTION_ADD_ORDER_SPECIMEN_ITEM. */
	public static final String ACTION_ADD_ORDER_SPECIMEN_ITEM = "AddToOrderListSpecimen.do";

	/** The Constant ACTION_ADD_ORDER_ARRAY_ITEM. */
	public static final String ACTION_ADD_ORDER_ARRAY_ITEM = "AddToOrderListArray.do";

	/** The Constant ACTION_ADD_ORDER_PATHOLOGY_ITEM. */
	public static final String ACTION_ADD_ORDER_PATHOLOGY_ITEM = "AddToOrderListPathologyCase.do";

	/** The Constant ACTION_DEFINE_ARRAY. */
	public static final String ACTION_DEFINE_ARRAY = "DefineArraySubmit.do";

	/** The Constant ACTION_ORDER_SPECIMEN. */
	public static final String ACTION_ORDER_SPECIMEN = "OrderExistingSpecimen.do";

	/** The Constant ACTION_ORDER_BIOSPECIMENARRAY. */
	public static final String ACTION_ORDER_BIOSPECIMENARRAY = "OrderBiospecimenArray.do";

	/** The Constant ACTION_ORDER_PATHOLOGYCASE. */
	public static final String ACTION_ORDER_PATHOLOGYCASE = "OrderPathologyCase.do";

	//Specimen Label generation realted constants
	/** The Constant PARENT_SPECIMEN_ID_KEY. */
	public static final String PARENT_SPECIMEN_ID_KEY = "parentSpecimenID";

	/** The Constant PARENT_SPECIMEN_LABEL_KEY. */
	public static final String PARENT_SPECIMEN_LABEL_KEY = "parentSpecimenLabel";

	/** The Constant SCG_NAME_KEY. */
	public static final String SCG_NAME_KEY = "SCGName";

	//ordering system
	/** The Constant TAB_INDEX_ID. */
	public static final String TAB_INDEX_ID = "tabIndexId";

	/** The Constant FORWARD_TO_HASHMAP. */
	public static final String FORWARD_TO_HASHMAP = "forwardToHashMap";

	/** The Constant SELECTED_COLLECTION_PROTOCOL_ID. */
	public static final String SELECTED_COLLECTION_PROTOCOL_ID = "0";

	/** The Constant LIST_OF_SPECIMEN_COLLECTION_GROUP. */
	public static final String LIST_OF_SPECIMEN_COLLECTION_GROUP = "specimenCollectionGroupResponseList";

	/** The Constant PARTICIPANT_PROTOCOL_ID. */
	public static final String PARTICIPANT_PROTOCOL_ID = "participantProtocolId";

	// caTIES
	/** The Constant CONCEPT_REFERENT_CLASSIFICATION_LIST. */
	public static final String CONCEPT_REFERENT_CLASSIFICATION_LIST = "conceptRefernetClassificationList";

	/** The Constant CONCEPT_LIST. */
	public static final String CONCEPT_LIST = "conceptList";

	/** The Constant CATEGORY_HIGHLIGHTING_COLOURS. */
	public static final String[] CATEGORY_HIGHLIGHTING_COLOURS = {"#ff6d5f", "#db9fe5", "#2dcc00",
			"#69bbf7", "#f8e754"};

	/** The Constant CONCEPT_BEAN_LIST. */
	public static final String CONCEPT_BEAN_LIST = "conceptBeanList";

	//Admin View
	/** The Constant IDENTIFIER_NO. */
	public static final String IDENTIFIER_NO = "#";

	/** The Constant REQUEST_DATE. */
	public static final String REQUEST_DATE = "Request Date";

	/** The Constant USER_NAME_ADMIN_VIEW. */
	public static final String USER_NAME_ADMIN_VIEW = "User Name";

	/** The Constant SCG_NAME. */
	public static final String SCG_NAME = "Specimen Collection Group";

	/** The Constant ACCESSION_NO. */
	public static final String ACCESSION_NO = "Accession Number";

	/** The Constant PAGE_OF_REVIEW_SPR. */
	public static final String PAGE_OF_REVIEW_SPR = "pageOfReviewSPR";

	/** The Constant PAGE_OF_QUARANTINE_SPR. */
	public static final String PAGE_OF_QUARANTINE_SPR = "pageOfQuarantineSPR";

	/** The Constant SITE. */
	public static final String SITE = "Site";

	/** The Constant REQUEST_FOR. */
	public static final String REQUEST_FOR = "requestFor";
	public static final String GRID_DATA = "gridData";

	/** The Constant REPORT_ACTION. */
	public static final String REPORT_ACTION = "reportAction";

	/** The Constant REPORT_STATUS_LIST. */
	public static final String REPORT_STATUS_LIST = "reportStatusList";

	/** The Constant COLUMN_LIST. */
	public static final String COLUMN_LIST = "columnList";

	/** The Constant NO_PENDING_REQUEST. */
	public static final String NO_PENDING_REQUEST = "noPendingRequest";

	//Surgical Pathology Report UI constants
	/** The Constant VIEW_SPR_ACTION. */
	public static final String VIEW_SPR_ACTION = "ViewSurgicalPathologyReport.do";

	/** The Constant SPR_EVENT_PARAM_ACTION. */
	public static final String SPR_EVENT_PARAM_ACTION = "SurgicalPathologyReportEventParam.do";

	/** The Constant VIEW_SURGICAL_PATHOLOGY_REPORT. */
	public static final String VIEW_SURGICAL_PATHOLOGY_REPORT = "viewSPR";

	/** The Constant PAGE_OF_NEW_SPECIMEN. */
	public static final String PAGE_OF_NEW_SPECIMEN = "pageOfNewSpecimen";

	/** The Constant REVIEW. */
	public static final String REVIEW = "review";

	/** The Constant QUARANTINE. */
	public static final String QUARANTINE = "quarantine";

	/** The Constant COMMENT_STATUS_RENDING. */
	public static final String COMMENT_STATUS_RENDING = "PENDING";

	/** The Constant COMMENT_STATUS_REVIEWED. */
	public static final String COMMENT_STATUS_REVIEWED = "REVIEWED";

	/** The Constant COMMENT_STATUS_REPLIED. */
	public static final String COMMENT_STATUS_REPLIED = "REPLIED";

	/** The Constant COMMENT_STATUS_NOT_REVIEWED. */
	public static final String COMMENT_STATUS_NOT_REVIEWED = "NOT_REVIEWED";

	/** The Constant COMMENT_STATUS_QUARANTINED. */
	public static final String COMMENT_STATUS_QUARANTINED = "QUARANTINED";

	/** The Constant COMMENT_STATUS_NOT_QUARANTINED. */
	public static final String COMMENT_STATUS_NOT_QUARANTINED = "DEQUARANTINED";

	/** The Constant ROLE_ADMINISTRATOR. */
	public static final String ROLE_ADMINISTRATOR = "Administrator";

	/** The Constant REPORT_LIST. */
	public static final String REPORT_LIST = "reportIdList";

	/** The Constant QUARANTINE_REQUEST. */
	public static final String QUARANTINE_REQUEST = "QUARANTINEREQUEST";

	/** The Constant IDENTIFIED_REPORT_NOT_FOUND_MSG. */
	public static final String IDENTIFIED_REPORT_NOT_FOUND_MSG = "Indentified Report Not Found!";
	
	public static final String UPLOADED_REPORT_MSG= "Identified report cannot be displayed since it was manually uploaded. Please click on Download to view.";

	/** The Constant DEID_REPORT_NOT_FOUND_MSG. */
	public static final String DEID_REPORT_NOT_FOUND_MSG = "De-Indentified Report Not Found!";

	//  Local extensions constants
	/** The Constant LOCAL_EXT. */
	public static final String LOCAL_EXT = "localExt";

	/** The Constant LINK. */
	public static final String LINK = "link";

	/** The Constant LOAD_INTEGRATION_PAGE. */
	public static final String LOAD_INTEGRATION_PAGE = "loadIntegrationPage";

	/** The Constant DEFINE_ENTITY. */
	public static final String DEFINE_ENTITY = "defineEntity";

	/** The Constant SELECTED_ENTITY_ID. */
	public static final String SELECTED_ENTITY_ID = "selectedStaticEntityId";

	/** The Constant CONTAINER_NAME. */
	public static final String CONTAINER_NAME = "containerName";

	/** The Constant STATIC_ENTITY_NAME. */
	public static final String STATIC_ENTITY_NAME = "staticEntityName";

	/** The Constant SPREADSHEET_DATA_GROUP. */
	public static final String SPREADSHEET_DATA_GROUP = "spreadsheetDataGroup";

	/** The Constant SPREADSHEET_DATA_ENTITY. */
	public static final String SPREADSHEET_DATA_ENTITY = "spreadsheetDataEntity";

	/** The Constant SPREADSHEET_DATA_RECORD. */
	public static final String SPREADSHEET_DATA_RECORD = "spreadsheetDataRecord";

	/** The Constant TITLE. */
	public static final String TITLE = "title";

	//clinportal constants
	/** The Constant CLINICALSTUDY_FORM_ID. */
	public static final int CLINICALSTUDY_FORM_ID = 65;

	/** The Constant CLINICAL_STUDY_REGISTRATION_FORM_ID. */
	public static final int CLINICAL_STUDY_REGISTRATION_FORM_ID = 66;

	// Save query constant
	/** The Constant CATISSUECORE_QUERY_INTERFACE_ID. */
	public static final int CATISSUECORE_QUERY_INTERFACE_ID = 67;

	//Ordering constants
	/** The Constant ARRAY_NAME. */
	public static final String ARRAY_NAME = "array";

	//Query Shopping cart constants
	/** The Constant SHOPPING_CART_FILE_NAME. */
	public static final String SHOPPING_CART_FILE_NAME = "MyList.csv";

	/** The Constant DUPLICATE_OBJECT. */
	public static final String DUPLICATE_OBJECT = "duplicateObject";

	/** The Constant DIFFERENT_VIEW_IN_CART. */
	public static final String DIFFERENT_VIEW_IN_CART = "differentCartView";

	/** The Constant IS_SPECIMENID_PRESENT. */
	public static final String IS_SPECIMENID_PRESENT = "isSpecimenIdPresent";

	/** The Constant IS_SPECIMENARRAY_PRESENT. */
	public static final String IS_SPECIMENARRAY_PRESENT = "isSpecimenArrayPresent";

	/** The Constant VALIDATION_MESSAGE_FOR_ORDERING. */
	public static final String VALIDATION_MESSAGE_FOR_ORDERING = "validationMessageForOrdering";

	/** The Constant UPDATE_SESSION_DATA. */
	public static final String UPDATE_SESSION_DATA = "updateSessionData";

	/** The Constant IS_LIST_EMPTY. */
	public static final String IS_LIST_EMPTY = "isListEmpty";

	/** The Constant DOT_CSV. */
	public static final String DOT_CSV = ".csv";

	/** The Constant APPLICATION_DOWNLOAD. */
	public static final String APPLICATION_DOWNLOAD = "application/download";

	//For SNT
	/** The Constant IS_CONTAINER_PRESENT. */
	public static final String IS_CONTAINER_PRESENT = "isContainerPresent";

	// Constants for CP Based Entry
	/** The Constant COLLECTION_PROTOCOL_EVENT_ID. */
	public static final String COLLECTION_PROTOCOL_EVENT_ID = "Event_Id";

	/** The Constant SPECIMEN_LIST_SESSION_MAP. */
	public static final String SPECIMEN_LIST_SESSION_MAP = "SpecimenListBean";

	/** The Constant COLLECTION_PROTOCOL_EVENT_SESSION_MAP. */
	public static final String COLLECTION_PROTOCOL_EVENT_SESSION_MAP = "collectionProtocolEventMap";

	/** The Constant COLLECTION_PROTOCOL_SESSION_BEAN. */
	public static final String COLLECTION_PROTOCOL_SESSION_BEAN = "collectionProtocolBean";

	/** The Constant STORAGE_CONTAINER_SESSION_BEAN. */
	public static final String STORAGE_CONTAINER_SESSION_BEAN = "storageContainerBean";

	/** The Constant EDIT_SPECIMEN_REQUIREMENT_BEAN. */
	public static final String EDIT_SPECIMEN_REQUIREMENT_BEAN = "EditSpecimenRequirementBean";

	/** The Constant VIEW_SUMMARY. */
	public static final String VIEW_SUMMARY = "ViewSummary";

	/** The Constant EVENT_KEY. */
	public static final String EVENT_KEY = "key";

	/** The Constant TREE_NODE_ID. */
	public static final String TREE_NODE_ID = "nodeId";

	/** The Constant PAGE_OF_DEFINE_EVENTS. */
	public static final String PAGE_OF_DEFINE_EVENTS = "defineEvents";

	/** The Constant PAGE_OF_ADD_NEW_EVENT. */
	public static final String PAGE_OF_ADD_NEW_EVENT = "newEvent";

	/** The Constant PAGE_OF_SPECIMEN_REQUIREMENT. */
	public static final String PAGE_OF_SPECIMEN_REQUIREMENT = "specimenRequirement";

	/** The Constant ADD_NEW_EVENT. */
	public static final String ADD_NEW_EVENT = "-1";

	/** The Constant NEW_EVENT_KEY. */
	public static final String NEW_EVENT_KEY = "listKey";

	/** The Constant LINEAGE_NEW_SPECIMEN. */
	public static final String LINEAGE_NEW_SPECIMEN = "New Specimen";

	/** The Constant UNIQUE_IDENTIFIER_FOR_DERIVE. */
	public static final String UNIQUE_IDENTIFIER_FOR_DERIVE = "_D";

	/** The Constant UNIQUE_IDENTIFIER_FOR_ALIQUOT. */
	public static final String UNIQUE_IDENTIFIER_FOR_ALIQUOT = "_A";

	/** The Constant UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN. */
	public static final String UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN = "_S";

	/** The Constant UNIQUE_IDENTIFIER_FOR_EVENTS. */
	public static final String UNIQUE_IDENTIFIER_FOR_EVENTS = "E";

	/** The Constant CLICKED_NODE. */
	public static final String CLICKED_NODE = "clickedNode";
	/*
	 * Conflict resolver constants
	 *
	 */

	/** The Constant CONFLICT_LIST_HEADER. */
	public static final String[] CONFLICT_LIST_HEADER = {"Participant name", "ID",
			"Surgical pathological number", "Report loaded on", "Status", "Site Name",
			"Report Collection Date"};

	/** The Constant CONFLICT_FILTER_LIST. */
	public static final String[] CONFLICT_FILTER_LIST = {"All", "Ambiguous Participants",
			"Ambiguous Specimen Collection Groups"};

	/** The Constant FILTER_LIST. */
	public static final String FILTER_LIST = "filterList";

	/** The Constant REPORT_ID. */
	public static final String REPORT_ID = "reportQueueId";

	/** The Constant CONFLICT_STATUS. */
	public static final String CONFLICT_STATUS = "conflictStatus";

	/** The Constant REPORT_INFORMATION. */
	public static final String REPORT_INFORMATION = "ReportInformation";

	/** The Constant SELECTED_FILTER. */
	public static final String SELECTED_FILTER = "selectedFilter";

	/** The Constant CONFLICT_COMMON_VIEW. */
	public static final String CONFLICT_COMMON_VIEW = "conflictCommonView";

	/** The Constant CONFLICT_TREE_VIEW. */
	public static final String CONFLICT_TREE_VIEW = "conflictTreeView";

	/** The Constant CONFLICT_DATA_VIEW. */
	public static final String CONFLICT_DATA_VIEW = "conflictDataView";

	/** The Constant CONFLICT_COMMON_VIEW_ACTION. */
	public static final String CONFLICT_COMMON_VIEW_ACTION = "ConflictCommon.do";

	/** The Constant CONFLICT_TREE_VIEW_ACTION. */
	public static final String CONFLICT_TREE_VIEW_ACTION = "ConflictParticipantSCGTree.do";

	/** The Constant CONFLICT_DATA_VIEW_ACTION. */
	public static final String CONFLICT_DATA_VIEW_ACTION = "ConflictParticipantDataDetails.do";

	/** The Constant SURGICAL_PATHOLOGY_NUMBER. */
	public static final String SURGICAL_PATHOLOGY_NUMBER = "surgicalPathologyNumber";

	/** The Constant REPORT_DATE. */
	public static final String REPORT_DATE = "reportDate";

	/** The Constant SITE_NAME. */
	public static final String SITE_NAME = "siteName";
	
	/** The Constant PARTICIPANT_NAME. */
	public static final String PARTICIPANT_NAME = "participantName";

	/** The Constant COLUMN_NAME_PART_COLL. */
	public static final String COLUMN_NAME_PART_COLL = "elements(participantCollection)";

	/** The Constant PARTICIPANT_ID_TO_ASSOCIATE. */
	public static final String PARTICIPANT_ID_TO_ASSOCIATE = "participantIdToAssociate";

	/** The Constant SCG_ID_TO_ASSOCIATE. */
	public static final String SCG_ID_TO_ASSOCIATE = "scgIdToAssociate";

	/** The Constant REPORT_QUEUE_LIST. */
	public static final String REPORT_QUEUE_LIST = "ReportQueueList";

	/** The Constant USE_SELECTED_PARTICIPANT. */
	public static final String USE_SELECTED_PARTICIPANT = "createNewSCG";

	/** The Constant USE_SELECTED_SCG. */
	public static final String USE_SELECTED_SCG = "associateSCG";

	/** The Constant CREATE_NEW_PARTICIPANT. */
	public static final String CREATE_NEW_PARTICIPANT = "creatNewParticipant";

	/** The Constant OVERWRITE_REPORT. */
	public static final String OVERWRITE_REPORT = "overwriteReport";

	/** The Constant IGNORE_NEW_REPORT. */
	public static final String IGNORE_NEW_REPORT = "ignoreNewReport";

	/** The Constant CONFLICT_BUTTON. */
	public static final String CONFLICT_BUTTON = "conflictButton";

	/** The Constant IDENTIFIED_SURGICAL_PATHOLOGY_REPORT. */
	public static final String IDENTIFIED_SURGICAL_PATHOLOGY_REPORT = "identifiedSurgicalPathologyReport";

	/** The Constant REPORT_COLLECTION_DATE. */
	public static final String REPORT_COLLECTION_DATE = "reportCollectionDate";

	//Save Query Constants
	/** The Constant HTML_CONTENTS. */
	public static final String HTML_CONTENTS = "HTMLContents";

	/** The Constant SHOW_ALL. */
	public static final String SHOW_ALL = "showall";

	/** The Constant SHOW_ALL_ATTRIBUTE. */
	public static final String SHOW_ALL_ATTRIBUTE = "Show all attributes";

	/** The Constant SHOW_SELECTED_ATTRIBUTE. */
	public static final String SHOW_SELECTED_ATTRIBUTE = "Show selected attributes";

	/** The Constant ADD_EDIT_PAGE. */
	public static final String ADD_EDIT_PAGE = "Add Edit Page";

	/** The Constant SAVE_QUERY_PAGE. */
	public static final String SAVE_QUERY_PAGE = "Save Query Page";

	/** The Constant EXECUTE_QUERY_PAGE. */
	public static final String EXECUTE_QUERY_PAGE = "Execute Query Page";

	/** The Constant FETCH_QUERY_ACTION. */
	public static final String FETCH_QUERY_ACTION = "FetchQuery.do";

	/** The Constant EXECUTE_QUERY_ACTION. */
	public static final String EXECUTE_QUERY_ACTION = "ExecuteQueryAction.do";

	/** The Constant EXECUTE_QUERY. */
	public static final String EXECUTE_QUERY = "executeQuery";

	/** The Constant INVALID_CONDITION_VALUES. */
	public static final String INVALID_CONDITION_VALUES = "InvalidValues";
	//	Save Query Constants ends

	/** The Constant NEW_SPECIMEN_TYPE. */
	public static final String NEW_SPECIMEN_TYPE = "New_Specimen";

	/** The Constant DERIVED_SPECIMEN_TYPE. */
	public static final String DERIVED_SPECIMEN_TYPE = "Derived_Specimen";

	/** The Constant SPECIMEN_TYPE_NAME. */
	public static final String SPECIMEN_TYPE_NAME = "NAME";

	/** The Constant SPECIMEN_TYPE_ABBRIVIATION. */
	public static final String SPECIMEN_TYPE_ABBRIVIATION = "ABBRIVIATION";

	/** The Constant DEFAULT_TYPE_NAME. */
	public static final String DEFAULT_TYPE_NAME = "DEFAULT_NAME";

	/** The Constant ABBREVIATION_XAML_FILE_NAME. */
	public static final String ABBREVIATION_XAML_FILE_NAME = "Abbreviations.xml";

	/** The Constant LABEL_TOKEN_PROP_FILE_NAME. */
	public static final String LABEL_TOKEN_PROP_FILE_NAME = "LabelTokens.Properties";

	/** The Constant CUSTOM_SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME. */
	public static final String CUSTOM_SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME = "customSpecimenLabelGeneratorClass";

	/** The Constant SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME. */
	public static final String SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME = "specimenLabelGeneratorClass";

	/** The Constant SPECIMEN_BARCODE_GENERATOR_PROPERTY_NAME. */
	public static final String SPECIMEN_BARCODE_GENERATOR_PROPERTY_NAME = "specimenBarcodeGeneratorClass";

	/** The Constant STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME. */
	public static final String STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME = "storageContainerLabelGeneratorClass";

	/** The Constant STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME. */
	public static final String STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME = "storageContainerBarcodeGeneratorClass";

	/** The Constant SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME. */
	public static final String SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME = "speicmenCollectionGroupLabelGeneratorClass";

	/** The Constant SPECIMEN_COLL_GROUP_BARCODE_GENERATOR_PROPERTY_NAME. */
	public static final String SPECIMEN_COLL_GROUP_BARCODE_GENERATOR_PROPERTY_NAME = "speicmenCollectionGroupBarcodeGeneratorClass";

	/** The Constant COLL_PROT_REG_BARCODE_GENERATOR_PROPERTY_NAME. */
	public static final String COLL_PROT_REG_BARCODE_GENERATOR_PROPERTY_NAME = "collectionProtocolRegistrationBarcodeGeneratorClass";

	/** The Constant PROTOCOL_PARTICIPANT_IDENTIFIER_LABEL_GENERATOR_PROPERTY_NAME. */
	public static final String PROTOCOL_PARTICIPANT_IDENTIFIER_LABEL_GENERATOR_PROPERTY_NAME = "protocolParticipantIdentifierLabelGeneratorClass";
	//Added By Geeta
	//public static final String ECMC_LABEL_GENERATOR_PROPERTY_NAME="LabelGeneratorForECMCClass";
	/** The Constant IS_STATE_REQUIRED. */
	public static final String IS_STATE_REQUIRED = "isStateRequired";

	/** The Constant IS_POST_CODE. */
	public static final String IS_POST_CODE = "isPostCode";

	/** The Constant CHANGE_DATE_FORMAT. */
	public static final String CHANGE_DATE_FORMAT = "changeDateFormat";

	/** The Constant IS_CP_TITLE_CHANGE. */
	public static final String IS_CP_TITLE_CHANGE = "isCPTitleChange";

	/** The Constant IS_REMOVE_SSN. */
	public static final String IS_REMOVE_SSN = "isRemoveSSN";

	/** The Constant IS_REMOVE_SEX_GENOTYPE. */
	public static final String IS_REMOVE_SEX_GENOTYPE = "isRemoveSexGenotype";

	/** The Constant IS_REMOVE_RACE. */
	public static final String IS_REMOVE_RACE = "isRemoveRace";

	/** The Constant IS_REMOVE_ETHNICITY. */
	public static final String IS_REMOVE_ETHNICITY = "isRemoveEthnicity";

	/** The Constant IS_PHONENO_TO_BE_VALIDATED. */
	public static final String IS_PHONENO_TO_BE_VALIDATED = "isPhoneNumberToBeValidated";

	/** The Constant DATEFORMAT. */
	public static final String DATEFORMAT = "dateFormat";

	// Added by Geeta for DFCI
	/** The Constant IS_LAST_NAME_NULL. */
	public static final String IS_LAST_NAME_NULL = "isLastNameNull";

	//SPECIMEN COllection Status
	/** The Constant SPECIMEN_COLLECTED. */
	public static final String SPECIMEN_COLLECTED = "Collected";

	/** The Constant SPECIMEN_ANTICOPATED. */
	public static final String SPECIMEN_ANTICOPATED = "Anticipated";

	//Shopping cart constants.
	/** The Constant SPECIMEN_ARRAY_CLASS_NAME. */
	public static final String SPECIMEN_ARRAY_CLASS_NAME = SpecimenArray.class.getName();

	/** The Constant IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME. */
	public static final String IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME = IdentifiedSurgicalPathologyReport.class
			.getName();

	/** The Constant DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME. */
	public static final String DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME = DeidentifiedSurgicalPathologyReport.class
			.getName();

	/** The Constant SURGICAL_PATHALOGY_REPORT_CLASS_NAME. */
	public static final String SURGICAL_PATHALOGY_REPORT_CLASS_NAME = SurgicalPathologyReport.class
			.getName();

	/** The Constant SPECIMEN_NAME. */
	public static final String SPECIMEN_NAME = Specimen.class.getName();

	//Shopping cart constants for Shipping And Tracking
	/** The Constant STORAGE_CONTAINER_CLASS_NAME. */
	public static final String STORAGE_CONTAINER_CLASS_NAME = StorageContainer.class.getName();

	/** The Constant specimenNameArray. */
	public static final String[] specimenNameArray = {SPECIMEN_NAME};

	/** The Constant entityNameArray. */
	public static final String[] entityNameArray = {SPECIMEN_NAME, SPECIMEN_ARRAY_CLASS_NAME,
			IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
			DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME, SURGICAL_PATHALOGY_REPORT_CLASS_NAME};

	/** The Constant IDENTIFIED_REPORT_ID. */
	public static final String IDENTIFIED_REPORT_ID = "identifiedReportId";

	/** The Constant BULK_TRANSFERS. */
	public static final String BULK_TRANSFERS = "bulkTransfers";

	/** The Constant BULK_DISPOSALS. */
	public static final String BULK_DISPOSALS = "bulkDisposals";

	/** The Constant EDIT_MULTIPLE_SPECIMEN. */
	public static final String EDIT_MULTIPLE_SPECIMEN = "editMultipleSp";

	/** The Constant PRINT_LABELS. */
	public static final String PRINT_LABELS = "printLabels";

	/** The Constant REQUEST_TO_DISTRIBUTE. */
	public static final String REQUEST_TO_DISTRIBUTE = "requestToDistribute";

	/** The Constant CATISSUE_ENTITY_GROUP. */
	public static final int CATISSUE_ENTITY_GROUP = 1;

	//Query-CSM related constants
	/** The Constant QUERY_REASUL_OBJECT_DATA_MAP. */
	public static final String QUERY_REASUL_OBJECT_DATA_MAP = "queryReasultObjectDataMap";

	/** The Constant DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP. */
	public static final String DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP = "defineViewQueryReasultObjectDataMap";

	/** The Constant HAS_CONDITION_ON_IDENTIFIED_FIELD. */
	public static final String HAS_CONDITION_ON_IDENTIFIED_FIELD = "hasConditionOnIdentifiedField";

	/** The Constant CONTAINTMENT_ASSOCIATION. */
	public static final String CONTAINTMENT_ASSOCIATION = "CONTAINTMENT";

	/** The Constant MAIN_ENTITY_MAP. */
	public static final String MAIN_ENTITY_MAP = "mainEntityMap";

	/** The Constant PAGE_OF_SPECIMEN_COLLECTION_REQUIREMENT_GROUP. */
	public static final String PAGE_OF_SPECIMEN_COLLECTION_REQUIREMENT_GROUP = "pageOfSpecimenCollectionRequirementGroup";

	/** The Constant NO_MAIN_OBJECT_IN_QUERY. */
	public static final String NO_MAIN_OBJECT_IN_QUERY = "noMainObjectInQuery";

	/** The Constant QUERY_ALREADY_DELETED. */
	public static final String QUERY_ALREADY_DELETED = "queryAlreadyDeleted";

	/** The Constant INHERITED_ENTITY_NAMES. */
//	public static final String[] INHERITED_ENTITY_NAMES = {FluidSpecimen.class.getName(),
//			MolecularSpecimen.class.getName(), TissueSpecimen.class.getName(),
//			CellSpecimen.class.getName(),
//	//IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
//	//DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
//	};

	//For enhanced CP
	/** The Constant PARENT_CP_TYPE. */
	public static final String CP_TYPE_PARENT = "Parent";

	/** The Constant ARM_CP_TYPE. */
	public static final String ARM_CP_TYPE = "Arm";

	/** The Constant PHASE_CP_TYPE. */
	public static final String PHASE_CP_TYPE = "Phase";

	/** The Constant CYCLE_CP_TYPE. */
	public static final String CYCLE_CP_TYPE = "Cycle";

	/** The Constant SUB_COLLECTION_PROTOCOL. */
	public static final String SUB_COLLECTION_PROTOCOL = "SubProtocol";

	/** The Constant CP_TYPE. */
	public static final String CP_TYPE = "type";

	/** The Constant DAYS_TO_ADD_CP. */
	public static final int DAYS_TO_ADD_CP = 1;

	/** The Constant REG_DATE. */
	public static final String REG_DATE = "regDate";

	/** The Constant CHILD_COLLECTION_PROTOCOL_COLLECTION. */
	public static final String CHILD_COLLECTION_PROTOCOL_COLLECTION = "childCollectionProtocolCollection";

	//	Constants required in SubCollectionProtocolRegistration.jsp Page
	/** The Constant SUB_COLLECTION_PROTOCOL_REGISTRATION_SEARCH_ACTION. */
	public static final String SUB_COLLECTION_PROTOCOL_REGISTRATION_SEARCH_ACTION = "SubCollectionProtocolRegistrationSearch.do";

	/** The Constant SUB_COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION. */
	public static final String SUB_COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION = "SubCollectionProtocolRegistrationAdd.do";

	/** The Constant SUB_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION. */
	public static final String SUB_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "SubCollectionProtocolRegistrationEdit.do";

	/** The Constant CP_QUERY_SUB_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION. */
	public static final String CP_QUERY_SUB_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "CPQuerySubCollectionProtocolRegistrationEdit.do";

	/** The Constant CP_QUERY_SUB_COLLECTION_PROTOCOL_REGISTRATION_ADD_ACTION. */
	public static final String CP_QUERY_SUB_COLLECTION_PROTOCOL_REGISTRATION_ADD_ACTION = "CPQuerySubCollectionProtocolRegistrationAdd.do";

	/** The Constant ID_COLUMN_ID. */
	public static final String ID_COLUMN_ID = "ID_COLUMN_ID";

	/** The Constant SQL. */
	public static final String SQL = "SQL";

	/** The Constant SCGFORM. */
	public static final String SCGFORM = "SCGFORM";

	/** The Constant SPECIMENFORM. */
	public static final String SPECIMENFORM = "SpecimenForm";

	/** The Constant FILE_TYPE. */
	public static final String FILE_TYPE = "file";

	/** The Constant ORDER_REQUEST_STATUS_DISTRIBUTED_AND_DISPOSE. */
	public static final String ORDER_REQUEST_STATUS_DISTRIBUTED_AND_DISPOSE = "orderRequestStatusAndDistributedAndDispose";

	/** The Constant EXPORT_FILE_NAME_START. */
	public static final String EXPORT_FILE_NAME_START = "Report_Content_";

	/** The Constant EXPORT_ZIP_NAME. */
	public static final String EXPORT_ZIP_NAME = "SearchResult.zip";

	/** The Constant ZIP_FILE_EXTENTION. */
	public static final String ZIP_FILE_EXTENTION = ".zip";

	/** The Constant CSV_FILE_EXTENTION. */
	public static final String CSV_FILE_EXTENTION = ".csv";

	/** The Constant EXPORT_DATA_LIST. */
	public static final String EXPORT_DATA_LIST = "exportDataList";

	/** The Constant ENTITY_IDS_MAP. */
	public static final String ENTITY_IDS_MAP = "entityIdsMap";

	/** The Constant DUMMY_NODE_NAME. */
	public static final String DUMMY_NODE_NAME = "Loading...";

	/** The Constant NODE_NAME. */
	public static final String NODE_NAME = "nodeName";

	/** The Constant CONTAINER_IDENTIFIER. */
	public static final String CONTAINER_IDENTIFIER = "containerId";

	/** The Constant PARENT_IDENTIFIER. */
	public static final String PARENT_IDENTIFIER = "parentId";

	/** The Constant COLLECTION_PROTOCOL. */
	public static final String COLLECTION_PROTOCOL = "edu.wustl.catissuecore.domain.CollectionProtocol";

	/** The Constant ENTITY_GROUP. */
	public static final String ENTITY_GROUP = "EntityGroup";

	/** The Constant ENTITY. */
	public static final String ENTITY = "Entity";

	/** The Constant FORM. */
	public static final String FORM = "Form";

	/** The Constant CATEGORY. */
	public static final String CATEGORY = "Category";

	/** Constant required for Specimen Array Delete Action. */
	public static final String DELETE_SPECIMEN_ARRAY = "DeleteSpecimenArray.do";

	/** The Constant LIST_SPECIMEN. */
	public static final String LIST_SPECIMEN = "specimenList";

	/** The Constant CP_QUERY_PRINT_ALIQUOT. */
	public static final String CP_QUERY_PRINT_ALIQUOT = "CPQueryPrintAliquot";

	/** The Constant CP_QUERY_ALIQUOT_ADD. */
	public static final String CP_QUERY_ALIQUOT_ADD = "CPQueryAliquotAdd";

	/** The Constant FROM_PRINT_ACTION. */
	public static final String FROM_PRINT_ACTION = "fromPrintAction";

	/** The Constant PRINT_ALIQUOT. */
	public static final String PRINT_ALIQUOT = "printAliquot";

	/** The Constant PRINTER_TYPE. */
	public static final String PRINTER_TYPE = "printerType";

	/** The Constant PRINTER_LOCATION. */
	public static final String PRINTER_LOCATION = "printerLocation";

	//constant required for setting the participant id on pathReport
	/** The Constant PARTICIPANTIDFORREPORT. */
	public static final String PARTICIPANTIDFORREPORT = "participantIdForReport";

	/** The Constant FIRST_COUNT_1. */
	public static final int FIRST_COUNT_1 = 1;

	/** The Constant INSTITUTION_NAME. */
	public static final String INSTITUTION_NAME = "instituteName";

	/** The Constant DEPARTMENT_NAME. */
	public static final String DEPARTMENT_NAME = "departmentName";

	/** The Constant CRG_NAME. */
	public static final String CRG_NAME = "crgName";

	/** The Constant RESPONSE_SEPARATOR. */
	public static final String RESPONSE_SEPARATOR = "#@#";

	//	Constants required to differentiate between patological cases.
	/** The Constant IDENTIFIED_SURGPATH_REPORT. */
	public static final int IDENTIFIED_SURGPATH_REPORT = 1;

	/** The Constant DEIDENTIFIED_SURGPATH_REPORT. */
	public static final int DEIDENTIFIED_SURGPATH_REPORT = 2;

	/** The Constant SURGPATH_REPORT. */
	public static final int SURGPATH_REPORT = 3;

	/** The Constant CHILD_STATUS. */
	public static final String CHILD_STATUS = "Child_Status";

	/** The Constant CHILD_DATE. */
	public static final String CHILD_DATE = "Child_Date";

	/** The Constant CHILD_OFFSET. */
	public static final String CHILD_OFFSET = "Child_Offset";
	// COnstants required for Collection Protocol Tree
	/** The Constant ROOTNODE_FOR_CPTREE. */
	public static final String ROOTNODE_FOR_CPTREE = "rootnode";

	/** The Constant OBJECTNAME_FOR_CP. */
	public static final String OBJECTNAME_FOR_CP = "cpName";

	//	Constants required for temporal query
	/** The Constant DATE_TYPE. */
	public static final String DATE_TYPE = "Date";

	/** The Constant INTEGER_TYPE. */
	public static final String INTEGER_TYPE = "Integer";

	/** The Constant FLOAT_TYPE. */
	public static final String FLOAT_TYPE = "Float";

	/** The Constant DOUBLE_TYPE. */
	public static final String DOUBLE_TYPE = "Double";

	/** The Constant LONG_TYPE. */
	public static final String LONG_TYPE = "Long";

	/** The Constant SHORT_TYPE. */
	public static final String SHORT_TYPE = "Short";

	/** The Constant FIRST_NODE_ATTRIBUTES. */
	public static final String FIRST_NODE_ATTRIBUTES = "firstDropDown";

	/** The Constant ARITHMETIC_OPERATORS. */
	public static final String ARITHMETIC_OPERATORS = "secondDropDown";

	/** The Constant SECOND_NODE_ATTRIBUTES. */
	public static final String SECOND_NODE_ATTRIBUTES = "thirdDropDown";

	/** The Constant RELATIONAL_OPERATORS. */
	public static final String RELATIONAL_OPERATORS = "fourthDropDown";

	/** The Constant TIME_INTERVALS_LIST. */
	public static final String TIME_INTERVALS_LIST = "timeIntervals";

	/** The Constant ENTITY_LABEL_LIST. */
	public static final String ENTITY_LABEL_LIST = "entityList";

	/** The Constant LEFT_OPERAND. */
	public static final String LEFT_OPERAND = "leftTerm";

	/** The Constant RIGHT_OPERAND. */
	public static final String RIGHT_OPERAND = "rightTerm";

	/** The Constant ARITHMETIC_OPERATOR. */
	public static final String ARITHMETIC_OPERATOR = "arithmaticOperator";

	/** The Constant RELATIONAL_OPERATOR. */
	public static final String RELATIONAL_OPERATOR = "relationalOperator";

	/** The Constant TIME_VALUE. */
	public static final String TIME_VALUE = "timeValue";

	/** The Constant TIME_INTERVAL_VALUE. */
	public static final String TIME_INTERVAL_VALUE = "timeIntervalValue";

	/** The Constant LEFT_OPERAND_TYPE. */
	public static final String LEFT_OPERAND_TYPE = "leftOperandType";

	/** The Constant RIGHT_OPERAND_TYPE. */
	public static final String RIGHT_OPERAND_TYPE = "rightOperandType";

	/** The Constant PLUS. */
	public static final String PLUS = "plus";

	/** The Constant MINUS. */
	public static final String MINUS = "Minus";

	/** The Constant MULTIPLYBY. */
	public static final String MULTIPLYBY = "MultipliedBy";

	/** The Constant DIVIDEBY. */
	public static final String DIVIDEBY = "DividedBy";

	/** The Constant CART_COLUMN_LIST. */
	public static final String CART_COLUMN_LIST = "cartColumnList";

	/** The Constant CART_ATTRIBUTE_LIST. */
	public static final String CART_ATTRIBUTE_LIST = "cartAttributeList";

	/** The Constant ADD_SPECIMEN_TO_CART. */
	public static final String ADD_SPECIMEN_TO_CART = "addSpecimenToCart";

	/** The Constant ADD_MULTIPLE_SPECIMEN_TO_CART. */
	public static final String ADD_MULTIPLE_SPECIMEN_TO_CART = "addMltipleSpecimenToCart";

	/** The Constant ADD_MULTIPLE_SPECIMEN_TO_CART_WITHOUT_MENU. */
	public static final String ADD_MULTIPLE_SPECIMEN_TO_CART_WITHOUT_MENU = "addMltipleSpecimenToCartWithoutMenu";

	/** The Constant ADD_MULTIPLE_SPECIMEN_TO_CART_AND_PRINT. */
	public static final String ADD_MULTIPLE_SPECIMEN_TO_CART_AND_PRINT = "addMltipleSpecimenToCartAndPrint";

	/** The Constant PRINT_SPECIMEN_FROM_LISTVIEW. */
	public static final String PRINT_SPECIMEN_FROM_LISTVIEW = "printSpecimenLabelFromListView";

	/** The Constant PRINT_SPECIMEN_DISTRIBUTION_REPORT. */
	public static final String PRINT_SPECIMEN_DISTRIBUTION_REPORT = "printSpecimenLabelFromDistribution";

	/** The Constant PRINT_ANTICIPATORY_SPECIMENS. */
	public static final String PRINT_ANTICIPATORY_SPECIMENS = "printAnticipatorySpecimens";

	/** The Constant ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE. */
	public static final String ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE = "Distributed And Close";

	public static final String ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE_SPECIAL="Distributed And Close(Special)";
	/** The Constant shortTitle. */
	public static final String shortTitle = "shortTitle";

	// MSR purpose
	// Used for Authentication, Authorization through CSM
	/** The Constant ADMIN_USER. */
	public static final String ADMIN_USER = "1";

	/** The Constant NON_ADMIN_USER. */
	public static final String NON_ADMIN_USER = "7";

	/** The Constant SUPER_ADMIN_USER. */
	public static final String SUPER_ADMIN_USER = "13";

	/** The Constant ADD_GLOBAL_PARTICIPANT. */
	public static final String ADD_GLOBAL_PARTICIPANT = "ADD_GLOBAL_PARTICIPANT";

	/** The Constant cannotCreateSuperAdmin. */
	public static final String cannotCreateSuperAdmin = "cannotCreateSuperAdmin";

	/** The Constant siteIsRequired. */
	public static final String siteIsRequired = "siteIsRequired";

	/** The Constant roleIsRequired. */
	public static final String roleIsRequired = "roleIsRequired";

	/**
	 * Gets the current and future pg and pe name.
	 *
	 * @param siteId the site id
	 *
	 * @return the current and future pg and pe name
	 */
	public static final String getCurrentAndFuturePGAndPEName(long siteId)
	{
		final String roleName = "SITE_" + siteId + "_All_CP";
		return roleName;
	}

	/**
	 * Gets the current and future role name.
	 *
	 * @param siteId the site id
	 * @param userId the user id
	 * @param defaultRole the default role
	 *
	 * @return the current and future role name
	 */
	public static final String getCurrentAndFutureRoleName(long siteId, long userId,
			String defaultRole)
	{
		final String roleName = defaultRole + "_" + "All CP's for SITE_" + siteId + "_For User"
				+ userId;
		return roleName;
	}

	/**
	 * Gets the cP role name.
	 *
	 * @param collectionProtocolId the collection protocol id
	 * @param userId the user id
	 * @param defaultRole the default role
	 *
	 * @return the cP role name
	 */
	public static final String getCPRoleName(long collectionProtocolId, long userId,
			String defaultRole)
	{
		final String roleName = defaultRole + "_" + "CP_" + collectionProtocolId + "_USER_"
				+ userId;
		return roleName;
	}

	/**
	 * Gets the site role name.
	 *
	 * @param siteId the site id
	 * @param userId the user id
	 * @param defaultRole the default role
	 *
	 * @return the site role name
	 */
	public static final String getSiteRoleName(long siteId, long userId, String defaultRole)
	{
		final String roleName = defaultRole + "_" + "SITE_" + siteId + "_USER_" + userId;
		return roleName;
	}

	/**
	 * Gets the site user group name.
	 *
	 * @param siteId the site id
	 * @param csmUserId the csm user id
	 *
	 * @return the site user group name
	 */
	public static final String getSiteUserGroupName(long siteId, long csmUserId)
	{
		return "SITE_" + siteId + "_USER_" + csmUserId;
	}

	/**
	 * Gets the cP user group name.
	 *
	 * @param cpId the cp id
	 * @param csmUserId the csm user id
	 *
	 * @return the cP user group name
	 */
	public static final String getCPUserGroupName(long cpId, long csmUserId)
	{
		return "CP_" + cpId + "_USER_" + csmUserId;
	}

	// Constants used from file 'PermissionMapDetails.xml'
	/** The Constant ADMIN_PROTECTION_ELEMENT. */
	public static final String ADMIN_PROTECTION_ELEMENT = "ADMIN_PROTECTION_ELEMENT";

	/** The Constant ADD_EDIT_USER. */
	public static final String ADD_EDIT_USER = "ADD_EDIT_USER";

	/** The Constant ADD_EDIT_INSTITUTION. */
	public static final String ADD_EDIT_INSTITUTION = "ADD_EDIT_INSTITUTION";

	/** The Constant ADD_EDIT_DEPARTMENT. */
	public static final String ADD_EDIT_DEPARTMENT = "ADD_EDIT_DEPARTMENT";

	/** The Constant ADD_EDIT_CRG. */
	public static final String ADD_EDIT_CRG = "ADD_EDIT_CRG";

	/** The Constant ADD_EDIT_SITE. */
	public static final String ADD_EDIT_SITE = "ADD_EDIT_SITE";

	/** The Constant ADD_EDIT_STORAGE_TYPE. */
	public static final String ADD_EDIT_STORAGE_TYPE = "ADD_EDIT_STORAGE_TYPE";

	/** The Constant ADD_EDIT_STORAGE_CONTAINER. */
	public static final String ADD_EDIT_STORAGE_CONTAINER = "ADD_EDIT_STORAGE_CONTAINER";

	/** The Constant ADD_EDIT_SPECIMEN_ARRAY_TYPE. */
	public static final String ADD_EDIT_SPECIMEN_ARRAY_TYPE = "ADD_EDIT_SPECIMEN_ARRAY_TYPE";

	/** The Constant ADD_EDIT_BIOHAZARD. */
	public static final String ADD_EDIT_BIOHAZARD = "ADD_EDIT_BIOHAZARD";

	/** The Constant ADD_EDIT_CP. */
	public static final String ADD_EDIT_CP = "ADD_EDIT_CP";

	/** The Constant ADD_EDIT_DP. */
	public static final String ADD_EDIT_DP = "ADD_EDIT_DP";

	/** The Constant ADD_EDIT_PARTICIPANT. */
	public static final String ADD_EDIT_PARTICIPANT = "ADD_EDIT_PARTICIPANT";

	/** The Constant ADD_EDIT_SPECIMEN. */
	public static final String ADD_EDIT_SPECIMEN = "ADD_EDIT_SPECIMEN";

	/** The Constant ALIQUOT_SPECIMEN. */
	public static final String ALIQUOT_SPECIMEN = "ALIQUOT_SPECIMEN";

	/** The Constant DERIVE_SPECIMEN. */
	public static final String DERIVE_SPECIMEN = "DERIVE_SPECIMEN";

	/** The Constant ADD_EDIT_SCG. */
	public static final String ADD_EDIT_SCG = "ADD_EDIT_SCG";

	/** The Constant ADD_EDIT_SPECIMEN_ARRAY. */
	public static final String ADD_EDIT_SPECIMEN_ARRAY = "ADD_EDIT_SPECIMEN_ARRAY";

	/** The Constant ADD_EDIT_SPECIMEN_EVENTS. */
	public static final String ADD_EDIT_SPECIMEN_EVENTS = "ADD_EDIT_SPECIMEN_EVENTS";

	/** The Constant DISTRIBUTE_SPECIMENS. */
	public static final String DISTRIBUTE_SPECIMENS = "DISTRIBUTE_SPECIMENS";

	/** The Constant OUTPUT_TERMS_COLUMNS. */
	public static final String OUTPUT_TERMS_COLUMNS = "outputTermsColumns";

	/** The Constant CP_BASED_VIEW_FILTRATION. */
	public static final String CP_BASED_VIEW_FILTRATION = "CP_BASED_VIEW_FILTRATION";

	/** The Constant EDIT_PROFILE_PRIVILEGE. */
	public static final String EDIT_PROFILE_PRIVILEGE = "EDIT_PROFILE_PRIVILEGE";

	/** The Constant CP_CHILD_SUBMIT. */
	public static final String CP_CHILD_SUBMIT = "cpChildSubmit";

	/** The Constant SITE_CLASS_NAME. */
	public static final String SITE_CLASS_NAME = "edu.wustl.catissuecore.domain.Site";//CollectionProtocol.class.getName();

	/** The Constant USER_ROW_ID_BEAN_MAP. */
	public static final String USER_ROW_ID_BEAN_MAP = "rowIdBeanMapForUserPage";

	/** The Constant ON. */
	public static final String ON = "on";

	/** The Constant OFF. */
	public static final String OFF = "off";

	/** The Constant TEXT_RADIOBUTTON. */
	public static final String TEXT_RADIOBUTTON = "text_radioButton";

	/** The Constant TREENO_ZERO. */
	public static final String TREENO_ZERO = "zero";

	/** The Constant PAGE_OF_QUERY_MODULE. */
	public static final String PAGE_OF_QUERY_MODULE = "pageOfQueryModule";

	/** The Constant ORDER_DETAILS. */
	public static final String ORDER_DETAILS = "orderDetails";

	/** The Constant Association. */
	public static final String Association = "ASSOCIATION";

	//For Shipping And Tracking My List
	/** The Constant SPECIMEN_LABELS_LIST. */
	public static final String SPECIMEN_LABELS_LIST = "specimenLabelName";

	/** The Constant CONTAINER_NAMES_LIST. */
	public static final String CONTAINER_NAMES_LIST = "containerNames";

	/** The Constant CONTAINER_DELETE_MAPPING. */
	public static final String CONTAINER_DELETE_MAPPING = "/StorageContainerDelete.do";

	/** The Constant CONTAINER_ERROR_MSG. */
	public static final String CONTAINER_ERROR_MSG = "The specimen {0} cannot be placed in container {1} since the position {2},{3} is not free";

	/** The Constant CONTAINER_ERROR_MSG. */
	public static final String POSITION_NOT_AVAILABLE_ERROR_MSG = "Container {1} since the position {2},{3} is not free";
	
	public static final String INVALID_STORAGE_CONTAINER_NAME = "Please enter valid container name.";
	
	/** The Constant CONTAINER_ERROR_MSG. */
	public static final String CONTAINER_ERROR_MSG_FOR_ALIQUOT = "Aliquot cannot be placed in container %s since the position %s,%s is not free";

	

	// COnstant for API
	/** The Constant API_FILTERED_OBJECT_LIMIT. */
	public static final String API_FILTERED_OBJECT_LIMIT = "apiFilteredObjectLimit";

	/** The Constant CP_DEFAULT. */
	public static final String CP_DEFAULT = "Use CP Defaults";

	/** The Constant REGISTRATION_FOR_REPORT_LOADER. */
	public static final String REGISTRATION_FOR_REPORT_LOADER = "REGISTRATION_FOR_REPORT_LOADER";

	/** The Constant REPORT_LOADER_SCG. */
	public static final String REPORT_LOADER_SCG = "SurgPReport_";

	/** The Constant DISPOSAL_REASON. */
	public static final String DISPOSAL_REASON = "Specimen was Distributed";

	/** The Constant SPECIMEN_DISPOSAL_REASON. */
	public static final String SPECIMEN_DISPOSAL_REASON = "Specimen was Aliquoted";

	/** The Constant DERIVED_SPECIMEN_DISPOSAL_REASON. */
	public static final String DERIVED_SPECIMEN_DISPOSAL_REASON = "Specimen was Derived";

	/** The Constant SPECIMEN_DISCARD_DISPOSAL_REASON. */
	public static final String SPECIMEN_DISCARD_DISPOSAL_REASON = "Specimen was Discarded";

	// Constants for ACORN
	/** The Constant MSSQLSERVER_DATABASE. */
	public static final String MSSQLSERVER_DATABASE = "MSSQLSERVER";

	/** The Constant FORMAT_FILE_EXTENTION. */
	public static final String FORMAT_FILE_EXTENTION = "_FormatFile.txt";

	/** The Constant MSSQLSERVER_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION. */
	public static final String MSSQLSERVER_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION = "CAST(label as bigint)";

	/** The Constant MSSQLSERVER_MAX_BARCODE_COL. */
	public static final String MSSQLSERVER_MAX_BARCODE_COL = "CAST(barcode as bigint)";

	/** The Constant MSSQLSERVER_QRY_DT_CONVERSION_FOR_LABEL_APPEND_STR. */
	public static final String MSSQLSERVER_QRY_DT_CONVERSION_FOR_LABEL_APPEND_STR = " where ISNUMERIC(label)=1";

	/** The Constant MSSQLSERVER_QRY_DT_CONVERSION_FOR_BARCODE_APPEND_STR. */
	public static final String MSSQLSERVER_QRY_DT_CONVERSION_FOR_BARCODE_APPEND_STR = " where ISNUMERIC(barcode)=1";

	//Constants for barcode editable
	/** The Constant IS_BARCODE_EDITABLE. */
	public static final String IS_BARCODE_EDITABLE = "barcode.isEditable";

	//------- Mandar : 21Jan09 : Summary related constants ----------
	/** The Constant SP_PATHSTAT. */
	public static final String SP_PATHSTAT = "PathStatSpec";

	/** The Constant SP_TSITE. */
	public static final String SP_TSITE = "TSiteSpec";

	/** The Constant P_BYCD. */
	public static final String P_BYCD = "PbyCD";

	/** The Constant P_BYCS. */
	public static final String P_BYCS = "PbyCS";

	/** The Constant TOTAL_PART_COUNT. */
	public static final String TOTAL_PART_COUNT = "TotalParticipantCount";

	/** The Constant TOTAL_CP_COUNT. */
	public static final String TOTAL_CP_COUNT = "TotalCPCount";

	/** The Constant TOTAL_DP_COUNT. */
	public static final String TOTAL_DP_COUNT = "TotalDPCount";

	/** The Constant REPO_SITE_COUNT. */
	public static final String REPO_SITE_COUNT = "TotalRepoSiteCount";

	/** The Constant LAB_SITE_COUNT. */
	public static final String LAB_SITE_COUNT = "TotalLabSiteCount";

	/** The Constant COLL_SITE_COUNT. */
	public static final String COLL_SITE_COUNT = "TotalCollSiteCount";

	/** The Constant REPO_SITE. */
	public static final String REPO_SITE = "Repository";

	/** The Constant LAB_SITE. */
	public static final String LAB_SITE = "Laboratory";

	/** The Constant COL_SITE. */
	public static final String COL_SITE = "Collection Site";

	/** The Constant TOTAL_USER_COUNT. */
	public static final String TOTAL_USER_COUNT = "TotalUserCount";

	/** The Constant USER_DATA. */
	public static final String USER_DATA = "UserData";

	//Mandar:16Feb09 : For  exception Handling Action
	/** The Constant EXCEPTION_OCCURED. */
	public static final String EXCEPTION_OCCURED = "ExceptionOccured";

	/** The Constant USER_ROLE. */
	public static final String USER_ROLE = "USER_ROLE";

	/** The Constant KEYWORD_CONFIGURED. */
	public static final String KEYWORD_CONFIGURED = "KeywordSearchConfigured";

	//merged constants......................................Deepti plz confirm
	/** The Constant COLLECTION_PROTOCOL_CLASS_NAME. */
	public static final String COLLECTION_PROTOCOL_CLASS_NAME = "edu.wustl.catissuecore.domain.CollectionProtocol";//CollectionProtocol.class.getName();

	/** The Constant DISTRIBUTION_PROTOCOL_CLASS_NAME. */
	public static final String DISTRIBUTION_PROTOCOL_CLASS_NAME = "edu.wustl.catissuecore.domain.DistributionProtocol";//DistributionProtocol.class.getName();

	/** The Constant COLUMN. */
	public static final String COLUMN = "Column";

	/** The Constant COLUMN_ID_MAP. */
	public static final String COLUMN_ID_MAP = "columnIdsMap";

	/** The Constant CONFIGURED_COLUMN_DISPLAY_NAMES. */
	public static final String CONFIGURED_COLUMN_DISPLAY_NAMES = "configuredColumnDisplayNames";

	/** The Constant CONFIGURED_COLUMN_NAMES. */
	public static final String CONFIGURED_COLUMN_NAMES = "configuredColumnNames";

	/** The Constant CONFIGURED_SELECT_COLUMN_LIST. */
	public static final String CONFIGURED_SELECT_COLUMN_LIST = "configuredSelectColumnList";

	/** The Constant IDENTIFIER. */
	public static final String IDENTIFIER = "IDENTIFIER";

	/** The Constant MENU_SELECTED. */
	public static final String MENU_SELECTED = "menuSelected";

	/** The Constant PAGE_OF_STORAGE_LOCATION. */
	public static final String PAGE_OF_STORAGE_LOCATION = "pageOfStorageLocation";

	/** The Constant PAGE_OF_SPECIMEN. */
	public static final String PAGE_OF_SPECIMEN = "pageOfSpecimen";

	/** The Constant PAGE_OF_STORAGECONTAINER. */
	public static final String PAGE_OF_STORAGECONTAINER = "pageOfStorageContainer";

	/** The Constant PAGE_OF_TISSUE_SITE. */
	public static final String PAGE_OF_TISSUE_SITE = "pageOfTissueSite";

	/** The Constant PAGE_OF_SPECIMEN_TREE. */
	public static final String PAGE_OF_SPECIMEN_TREE = "pageOfSpecimenTree";
	// TissueSite Tree View Constants.
	/** The Constant PROPERTY_NAME. */
	public static final String PROPERTY_NAME = "propertyName";

	// Query results view temporary table name.
	/** The Constant QUERY_RESULTS_TABLE. */
	public static final String QUERY_RESULTS_TABLE = "CATISSUE_QUERY_RESULTS";

	/** The Constant QUERY. */
	public static final String QUERY = "query";

	/** The Constant SELECT_COLUMN_LIST. */
	public static final String SELECT_COLUMN_LIST = "selectColumnList";

	/** The Constant SELECTED_NODE. */
	public static final String SELECTED_NODE = "selectedNode";

	/** The Constant SESSION_DATA. */
	public static final String SESSION_DATA = "sessionData";

	/** The Constant TEMP_SESSION_DATA. */
	public static final String TEMP_SESSION_DATA = "tempSessionData";

	/** The Constant PASSWORD_CHANGE_IN_SESSION. */
	public static final String PASSWORD_CHANGE_IN_SESSION = "changepassword";

	/** The Constant SIMPLE_QUERY_INTERFACE_ID. */
	public static final int SIMPLE_QUERY_INTERFACE_ID = 40;

	/** The Constant SPECIMEN_TYPE. */
	public static final String SPECIMEN_TYPE = "type";

	/** The Constant SPREADSHEET_VIEW. */
	public static final String SPREADSHEET_VIEW = "Spreadsheet View";

	//Status message key Constants
	/** The Constant STATUS_MESSAGE_KEY. */
	public static final String STATUS_MESSAGE_KEY = "statusMessageKey";

	// Constants for Storage Container.
	/** The Constant STORAGE_CONTAINER_TYPE. */
	public static final String STORAGE_CONTAINER_TYPE = "storageType";

	/** The Constant STORAGE_CONTAINER_TO_BE_SELECTED. */
	public static final String STORAGE_CONTAINER_TO_BE_SELECTED = "storageToBeSelected";

	/** The Constant STORAGE_CONTAINER_POSITION. */
	public static final String STORAGE_CONTAINER_POSITION = "position";

	/** The Constant SYSTEM_IDENTIFIER. */
	public static final String SYSTEM_IDENTIFIER = "id";

	/** The Constant CDE_NAME. */
	public static final String CDE_NAME = "cdeName";

	/** The Constant CDE_NAME_TISSUE_SITE. */
	public static final String CDE_NAME_TISSUE_SITE = "Tissue Site";

	/** Constant for ACCESS_DENIED. */
	public static final String ACCESS_DENIED = "access_denied";

	/** Constant for LOGIN. */
	public static final String LOGIN = "login";

	/** Constant for LOGOUT. */
	public static final String LOGOUT = "logout";

	/** The Constant ACTIVITY_STATUS_COLUMN. */
	public static final String ACTIVITY_STATUS_COLUMN = "ACTIVITY_STATUS";

	/** The Constant ADD_NEW_SITE_ID. */
	public static final String ADD_NEW_SITE_ID = "addNewSiteId";

	/** The Constant ADD_NEW_STORAGE_TYPE_ID. */
	public static final String ADD_NEW_STORAGE_TYPE_ID = "addNewStorageTypeId";

	/** The Constant ADD_NEW_COLLECTION_PROTOCOL_ID. */
	public static final String ADD_NEW_COLLECTION_PROTOCOL_ID = "addNewCollectionProtocolId";

	/** The Constant ADD_NEW_USER_ID. */
	public static final String ADD_NEW_USER_ID = "addNewUserId";

	/** The Constant ADD_NEW_USER_TO. */
	public static final String ADD_NEW_USER_TO = "addNewUserTo";

	/** The Constant SUBMITTED_FOR. */
	public static final String SUBMITTED_FOR = "submittedFor";

	/** The Constant SUBMITTED_FOR_ADD_NEW. */
	public static final String SUBMITTED_FOR_ADD_NEW = "AddNew";

	/** The Constant SUBMITTED_FOR_FORWARD_TO. */
	public static final String SUBMITTED_FOR_FORWARD_TO = "ForwardTo";

	/** The Constant SUBMITTED_FOR_DEFAULT. */
	public static final String SUBMITTED_FOR_DEFAULT = "Default";

	/** The Constant FORM_BEAN_STACK. */
	public static final String FORM_BEAN_STACK = "formBeanStack";

	/** The Constant ADD_NEW_FORWARD_TO. */
	public static final String ADD_NEW_FORWARD_TO = "addNewForwardTo";

	/** The Constant FORWARD_TO. */
	public static final String FORWARD_TO = "forwardTo";

	/** The Constant ADD_NEW_FOR. */
	public static final String ADD_NEW_FOR = "addNewFor";

	/** The Constant ERROR_DETAIL. */
	public static final String ERROR_DETAIL = "Error Detail";

	/** The Constant ANY. */
	public static final String ANY = "Any";

	/** The Constant FIELD_TYPE_BIGINT. */
	public static final String FIELD_TYPE_BIGINT = "bigint";

	/** The Constant FIELD_TYPE_VARCHAR. */
	public static final String FIELD_TYPE_VARCHAR = "varchar";

	/** The Constant FIELD_TYPE_TEXT. */
	public static final String FIELD_TYPE_TEXT = "text";

	/** The Constant FIELD_TYPE_TINY_INT. */
	public static final String FIELD_TYPE_TINY_INT = "tinyint";

	/** The Constant FIELD_TYPE_DATE. */
	public static final String FIELD_TYPE_DATE = "date";

	/** The Constant FIELD_TYPE_TIMESTAMP_DATE. */
	public static final String FIELD_TYPE_TIMESTAMP_DATE = "timestampdate";

	/** The Constant TABLE_ALIAS_NAME_COLUMN. */
	public static final String TABLE_ALIAS_NAME_COLUMN = "ALIAS_NAME";

	/** The Constant TABLE_DATA_TABLE_NAME. */
	public static final String TABLE_DATA_TABLE_NAME = "CATISSUE_QUERY_TABLE_DATA";

	/** The Constant TABLE_DISPLAY_NAME_COLUMN. */
	public static final String TABLE_DISPLAY_NAME_COLUMN = "DISPLAY_NAME";

	/** The Constant TABLE_TABLE_NAME_COLUMN. */
	public static final String TABLE_TABLE_NAME_COLUMN = "TABLE_NAME";

	/** The Constant TABLE_FOR_SQI_COLUMN. */
	public static final String TABLE_FOR_SQI_COLUMN = "FOR_SQI";

	/** The Constant TABLE_ID_COLUMN. */
	public static final String TABLE_ID_COLUMN = "TABLE_ID";

	//	constants for TiTLi Search
	/** The Constant TITLI_SORTED_RESULT_MAP. */
	public static final String TITLI_SORTED_RESULT_MAP = "sortedResultMap";

	/** The Constant TITLI_INSERT_OPERATION. */
	public static final String TITLI_INSERT_OPERATION = "insert";

	/** The Constant TITLI_UPDATE_OPERATION. */
	public static final String TITLI_UPDATE_OPERATION = "update";

	/** The Constant TITLI_DELETE_OPERATION. */
	public static final String TITLI_DELETE_OPERATION = "delete";

	/** The Constant TITLI_SINGLE_RESULT. */
	public static final String TITLI_SINGLE_RESULT = "singleResult";

	/** The Constant TITLI_FETCH_ACTION. */
	public static final String TITLI_FETCH_ACTION = "/TitliFetch.do";

	/** The Constant PARENT_SPECIMEN_ID_COLUMN. */
	public static final String PARENT_SPECIMEN_ID_COLUMN = "PARENT_SPECIMEN_ID";

	/** The Constant RECORDS_PER_PAGE_PROPERTY_NAME. */
	public static final String RECORDS_PER_PAGE_PROPERTY_NAME = "resultView.noOfRecordsPerPage";

	/** The Constant TABLE_ALIAS_NAME. */
	public static final String TABLE_ALIAS_NAME = "aliasName";

	/** The Constant VIEW_TYPE. */
	public static final String VIEW_TYPE = "viewType";

	//have to ask deepti
	/** The Constant IS_SIMPLE_SEARCH. */
	public static final String IS_SIMPLE_SEARCH = "isSimpleSearch";

	/** The Constant SEARCH_OBJECT_ACTION. */
	public static final String SEARCH_OBJECT_ACTION = "/SearchObject.do";

	/** The Constant ADMINISTRATOR. */
	public static final String ADMINISTRATOR = "Administrator";

	/** The Constant ROLE_SUPER_ADMINISTRATOR. */
	public static final String ROLE_SUPER_ADMINISTRATOR = "SUPERADMINISTRATOR";

	/** The Constant PAGE_OF_SIMPLE_QUERY_INTERFACE. */
	public static final String PAGE_OF_SIMPLE_QUERY_INTERFACE = "pageOfSimpleQueryInterface";

	/** The Constant allowOperation. */
	public static final String allowOperation = "allowOperation";

	//Constants for Summary Page
	/** The Constant MOLECULE. */
	public static final String MOLECULE = "Molecular";

	/** The Constant SEARCH. */
	public static final String SEARCH = "search";

	//  Local extensions integration constants
	/** The Constant DEFAULT_CONDITION. */
	public static final String DEFAULT_CONDITION = "-1";

	/** The Constant ORACLE_DATABASE. */
	public static final String ORACLE_DATABASE = "ORACLE";

	/** The Constant MYSQL_DATABASE. */
	public static final String MYSQL_DATABASE = "MYSQL";

	/** The Constant POSTGRESQL_DATABASE. */
	public static final String POSTGRESQL_DATABASE = "POSTGRESQL";

	/** The Constant DB2_DATABASE. */
	public static final String DB2_DATABASE = "DB2";

	/** The Constant FIELD_TYPE_TIMESTAMP_TIME. */
	public static final String FIELD_TYPE_TIMESTAMP_TIME = "timestamptime";

	/** The Constant SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION. */
	public static final String SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION = "ShowStorageGridView.do";

	/** The Constant DHTMLXGRID_DELIMETER. */
	public static final String DHTMLXGRID_DELIMETER = "|@|";

	/** The Constant PAGE_OF_QUERY_RESULTS. */
	public static final String PAGE_OF_QUERY_RESULTS = "pageOfQueryResults";

	/** The Constant STORAGE_CONTAINER. */
	public static final String STORAGE_CONTAINER = "storageContainerName";
	// Frame names in Query Results page.
	/** The Constant DATA_VIEW_FRAME. */
	public static final String DATA_VIEW_FRAME = "myframe1";

	/** The Constant APPLET_VIEW_FRAME. */
	public static final String APPLET_VIEW_FRAME = "appletViewFrame";

	/** The Constant TREE_APPLET_NAME. */
	public static final String TREE_APPLET_NAME = "treeApplet";

	/** The Constant MAXIMUM_RECORDS_FOR_KEYWORD_RESULTS. */
	public static final String MAXIMUM_RECORDS_FOR_KEYWORD_RESULTS = "keywordSearch.result.maximumRecords";

	/** The Constant MAX_LIMIT_FOR_KEYWORDSEARCH. */
	public static final String MAX_LIMIT_FOR_KEYWORDSEARCH = "keywordSearch.max.limit";

	/** The Constant MAX_LIMIT_EXCEEDED. */
	public static final String MAX_LIMIT_EXCEEDED = "maxLimitExceeded";

	/** The Constant IDP_ENABLED. */
	public static final String IDP_ENABLED = "idp.enabled";

	/** The Constant APP_ADDITIONAL_INFO. */
	public static final String APP_ADDITIONAL_INFO = "app.additional.info";

	/** The Constant MAX_RECORDS_PER_CACORE_QUERY_ALLOWED. */
	public static final String MAX_RECORDS_PER_CACORE_QUERY_ALLOWED = "maxRecordsPercaCOREQueryAllowed";

	/** The Constant MAX_NO_OF_PARTICIPANTS_TO_RETURN. */
	public static final String MAX_NO_OF_PARTICIPANTS_TO_RETURN = "maxNoOfParticipantToReturn";

	/** The Constant CUTTOFFPOINTS. */
	public static final String CUTTOFFPOINTS = "cutoffPoints";

	/** The Constant CAS_LOGOUT_URL. */
	public static final String CAS_LOGOUT_URL = "cas.logout.url";

	/** The Constant for setting precision when rounding off initial/available quantity. */
	public static final int QUANTITY_PRECISION = 5;

	/** The Constant key for setting the catissue cdms integration class name. */
	public static final String CDMS_INTEGRATION_CLASSNAME = "cdms.integrator.class";

	/** The Constant key for setting the catissue cdms integration properties file in system properties. */
	public static final String CATISSUE_CDMS_INTEGRATION_PROP_FILE_NAME = "catissue.cdms.integration.class";

	/** The Constant ERROR_PAGE_FOR_CP. */
	public static final String ERROR_PAGE_FOR_CP = "isErrorPage";

	/** The Constant REFRESH_WHOLE_PAGE. */
	public static final String REFRESH_WHOLE_PAGE = "refreshWholePage";

	/**
	 * Constant for directory.
	 */
	public static final String TEMPLATE_DIR = "XMLAndCSVTemplate";

	public static final String PAGEOF = "pageOf";
	public static final String IDENTIFIER_FIELD_INDEX = "identifierFieldIndex";

	/** The Constant HL7_LISTENER_ADMIN_USER. */
	public static final String HL7_LISTENER_ADMIN_USER = "HL7ListenerAdminUser";
	public static final String ACTIVITY_STATUS_ACTIVE = "Active";
	public static final String ACTIVITY_STATUS_CLOSED = "Closed";
	public static final String ACTIVITY_STATUS_DISABLED = "Disabled";

	/**
	 * Constant for XML extension.
	 */
	public static final String XML_SUFFIX = ".xml";

	public static final String PARTICIPANT_CONTINUE_LOOK_UP = "continueLookup";
	public static final String GENERATE_EMPI_ID_NAME = "generateEMPIButtonName";
	public static final String PARTICIPANT_ACTIVITY_PRIVILEGE = "ParticipantActivityStatus";
	public static final String QUERY_PARTICIPANT_EDIT = "CPQueryParticipantEdit.do";
	public static final String CS_ACTIVITY_STATUS = "CSActivityStatus";
	public static final String CP_PARTICIPANT_LOOKUP_ACTION = "CPQueryParticipantLookup.do";
	public static final String SPREADSHEET_DATA_LIST = "spreadsheetDataList";
	public static final String QUERY_PARTICIPANT_ADD = "CPQueryParticipantAdd.do";
//	public static final String DATE_PATTERN_MM_DD_YYYY = "MM-dd-yyyy";

	public static final String CLICKED_ROW_SELECTED = "clickedRowSelected";
	public static final String JSON_DATA_ROW  = "rows";
	public static final String JSON_DATA_COLUMN  = "data";

	public static final String ACCESS_DENIED_MSG = "access.execute.action.denied";
	public static final String REDIRECT_HOME = "/Home.do";
	
	//Constants for creating a standard xml file for dhtmlx grid
	public static final String XML_START = "<?xml version='1.0' encoding='utf-8' ?>";
	public static final String XML_ROWS = "<rows>";
	public static final String XML_ROWS_TOTAL_COUNT = "<rows total_count='";
	public static final String XML_ROWS_TOTAL_COUNT_START = "total_count='";
	public static final String XML_ROWS_END = "</rows>";
	public static final String XML_ROW_ID_START = "<row id='";
	public static final String XML_TAG_END = "'>";
	public static final String XML_ROW_END = "</row>";
	public static final String XML_CELL_START = "<cell>";
	public static final String XML_CELL_END = "</cell>";
	public static final String XML_CDATA_START = "<![CDATA[";
	public static final String XML_CDATA_END = "]]>"; 
	public static final String CONTENT_TYPE_XML = "text/xml";
	
	public static final String DHTMLX_CONNECTOR_ERROR = "dhtmlx.connector.error";
	public static final String GRID_SETUP_PROP_FILE = "gridsetup.properties";
	
	public static final String CP_COORDINATOR_IDS = "coordinatorIds";
	public static final String LABELLING_SCHEME_ALPHABETS_UPPER_CASE="Alphabets Upper Case";
	public static final String LABELLING_SCHEME_ALPHABETS_LOWER_CASE="Alphabets Lower Case";
	public static final String LABELLING_SCHEME_ROMAN_UPPER_CASE="Roman Upper Case";
	public static final String LABELLING_SCHEME_ROMAN_LOWER_CASE="Roman Lower Case";
	public static final String LABELLING_SCHEME_NUMBERS="Numbers";
	 public static final String CREATE_DUPLICATE_EVENT = "createDuplicateEvent";
	 public static final String CREATE_DUPLICATE_SPECIMEN = "createDuplicateSpecimen";
	 public static final String PARENT_NODE_ID = "parentNodeId";

	 public static final String HELP_URL_KEY="helpURLKey";
	 public static final String RESET_PASSWORD_TOKEN="resetPasswordToken";
	 public static final String FORGOT_PASSWORD_ENABLED="forgot.password.enabled";
	 public static final String PAGE_OF_RESET_PASSWORD="pageOfResetPassword";
	 public static final String PASSWORD_TOKEN_NOT_EXISTS="password.token.not.exist";
	 
	 
	 //Error message String For Mobile
	 
	 public static final String INVALID_ALIQUOT_QUANTITY = "Invalid aliquot quantity.";
	 public static final String INSUFFICIENT_AVAILABLE_QUANTITY = "Total quantity of all aliquots cannot be greater than available quantity of parent specimen.";
	 public static final String INSUFFICIEN_STORAGE_LOCATION = "The system cannot store all the aliquots in the same container due to insufficient number of storage locations.";
	 public static final String SUCCESS_MSG_ALIQUOT_CREATION = "Aliquots created successfully.";
	 public static final String INVALID_LABEL_BARCODE = "Invalid specimen label or barcode.";
	 public static final String INVALID_CONTAINER_NAME = "Invalid container name or barcode.";
	 public static final String NO_PERMISSION_TRANSFER_SPECIMEN= "You do not have permission to transfered specimen.";
	 public static final String TRANSFERED_SUCCESSFUL = "Specimen transfered successfully.";

	 public static final String EMPI_ENABLED="empi.Enabled";
	 public static final String HELP_HOME_PAGE="helpHomePage";
	 
	public static final String PAGE_OF_COLLECTION_PROTOCOL_QUERY="pageOfCollectionProtocolQuery";
	
	public static final String DEFINE_EVENTS_PAGE="pageOfDefineEvents";
	
	public static final String PAGE_OF_CREATE_DERIVATIVE="pageOfCreateDerivative";
	
	public static final String PAGE_OF_SPECIMEN_SUMMARY="specimenSummaryPage";
	
	//SPR Reports String
	public static final String IDENTIFIED = "Identified";
	public static final String DEIDENTIFIED = "Deidentified";
	public static final String CONTENT_TYPE_PDF = "application/pdf";
	public static final String CONTENT_DISPOSITION ="Content-Disposition";
	public static final String SPR_DIR_LOACTION ="spr.dir.location";
	public static final String PARTICIPANT_CONSENT_DOC_DIR_LOCATION ="participant.consent.dir.location";
	public static final String DWD_ERROR_MESSAGE = "Problem occured while downloading report.";
	public static final String DASHBOARD_ITEMS_FILE_PATH = "dashboard.items.file";
	public static final String USER_DEFINED_LABEL = "userDefinedLabel";
	public static final String LABEL_ID = "labelId";
	public static final String SEQ_ORDER = "seqOrder";
	public static final String ASSOC_ID = "assocId";
	
	public static final String INSTITUTE_LINK = "institute.logo.hyperlink";
	public static final String INSTITUTE_LOGO_WEB_PATH = "images/";
	
	public static final String UPLOAD_ERROR_MESSAGE = "Problem occured while uploading report.";

	public static final String ENTITY_SPECIMEN_TAG = "SpecimenListTag";
	public static final String ENTITY_SPECIMEN_TAGITEM = "SpecimenListTagItem";
	
	
	public static final String SHARE_SPECIMEN_LIST_EMAIL_TMPL = "specimen.shareSpecimenListTemplate";
	public static final String USER_APPROVAL_EMAIL_TMPL = "user.approvalTemplate";
	public static final String USER_SIGNUP_EMAIL_TMPL = "user.signUpTemplate";
	public static final String USER_REJECTION_EMAIL_TMPL = "user.rejectionTemplate"; 
	public static final String USER_REPORTEDPROB_EMAIL_TMPL = "user.reportedProblemTemplate";
	public static final String USER_FORGOT_PASSWORD_EMAIL_TMPL = "user.forgotPasswordTemplate";
	public static final String EMPI_ADMINUSER_CLOSED_EMAIL_TMPL = "eMPI.adminUserClosedTemplate";  
	public static final String EMPI_ADMINUSER_NOTEXISTS_EMAIL_TMPL = "eMPI.adminUserNotExitsTemplate"; 
	public static final String ORDER_PLACEMENT_EMAIL_TMPL = "order.orderPlacementTemplate";
	public static final String ORDER_DISTRIBTION_EMAIL_TMPL="order.submission";
	public static final String SHIPMENT_CREATED="shipment.created";
	public static final String SHIPMENT_REQUEST="shipment.requested";
	public static final String SHIPMENT_ACCEPTED="shipment.accepted";	
	public static final String FROM_CONTAINER_NAME="fromContainerName";
	public static final String FROM_POSITION_DIMENSION_ONE="fromPos1";
	public static final String FROM_POSITION_DIMENSION_TWO="fromPos2";
	
	public static final String TO_CONTAINER_NAME="toContainerName";
	public static final String TO_POSITION_DIMENSION_ONE="toPos1";
	public static final String TO_POSITION_DIMENSION_TWO="toPos2";
	
	public static final String[] STATUS_LIST = {"-Select Status For All--","Distributed","Distributed And Close"
		,"Distributed And Close(Special)","New","Pending - For Distribution","Pending - Protocol Review","Specimen Preparation",
		"Rejected - Inappropriate Request","Rejected - Specimen Unavailable","Rejected - Unable to Create"};

	/** Constant for BIOHAZARD_TYPE_NAME_LIST_JSON **/
	public static final String BIOHAZARD_TYPE_NAME_LIST_JSON = "biohazardTypeNameListJSON";

	/** Constant for SPECIMEN_DTO **/
	public static final String SPECIMEN_DTO = "specimenDTO";

	/** Constant for TISSUE_TYPE_LIST_JSON **/
	public static final String TISSUE_TYPE_LIST_JSON = "tissueTypeListJSON";

	/** Constant for FLUID_TYPE_LIST_JSON **/
	public static final String FLUID_TYPE_LIST_JSON = "fluidTypeListJSON";

	/** Constant for CELL_TYPE_LIST_JSON **/
	public static final String CELL_TYPE_LIST_JSON = "cellTypeListJSON";

	/** Constant for MOLECULAR_TYPE_LIST_JSON **/
	public static final String MOLECULAR_TYPE_LIST_JSON = "molecularTypeListJSON";
	
	public static final String CONTAINER_INVALID_MESSAGE = " container cannot hold container of type ";
	
    public static final String SITE_NOT_TRANSFRED_MESSAGE = "Site cannot be transferred.";
		
	public static final String ALIQUOTS_CREATION_SUCCESS_MSG = "Aliquots created succesfully.";
	
	public static final String ALIQUOT_COUNT_ERROR = "Please enter aliquot count.";
	
	public static final String VALID_ALIQUOT_COUNT_ERROR= "Please enter valid aliquot count or quantity per aliquot.";

	/** Constant for controlName **/
	public static final String CONTROL_NAME = "controlName";

	/** Constant for pos1ControlName **/
	public static final String POS1_CONTROL_NAME = "pos1ControlName";

	/** Constant for pos2ControlName **/
	public static final String POS2_CONTROL_NAME = "pos2ControlName";

	/** Constant for dimensionOneLabels **/
	public static final String DIMENSION_ONE_LABELS = "dimensionOneLabels";

	/** Constant for dimensionTwoLabels **/
	public static final String DIMENSION_TWO_LABELS = "dimensionTwoLabels";

	/** Constant for containerDTO **/
	public static final String CONTAINER_DTO = "containerDTO";

    /** Constant for NIGHTLY_CRON_JOB_EXECUTION_TIME_HOURS **/
    public static final String NIGHTLY_CRON_JOB_EXECUTION_TIME_HOURS = "nightly.cron.job.execution.time.hrs";

    /** Constant for NIGHTLY_CRON_JOB_EXECUTION_TIME_MINUTES **/
    public static final String NIGHTLY_CRON_JOB_EXECUTION_TIME_MINUTES = "nightly.cron.job.execution.time.min";

    /** Constant for DEFAULT_NIGHTLY_CRON_JOB_EXECUTION_TIME_HOURS **/
    public static final Integer DEFAULT_NIGHTLY_CRON_JOB_EXECUTION_TIME_HOURS = 22;

    /** Constant for DEFAULT_NIGHTLY_CRON_JOB_EXECUTION_TIME_MINUTES **/
    public static final Integer DEFAULT_NIGHTLY_CRON_JOB_EXECUTION_TIME_MINUTES = 0;

	/** Constant for number of milliseconds in a day **/
	public static final Long DAILY_INTERVAL_MILLISECONDS = 86400000L;

    /** Constant for count **/
    public static final String SPEC_COUNT = "count";

    /** Constant for capacity **/
    public static final String CAPACITY = "capacity";

    /** Constant for percentage **/
    public static final String PERCENTAGE = "percentage";

    /** Constant for showUtilizationAlert **/
    public static final String SHOW_UTILIZATION_ALERT = "showUtilizationAlert";

    /** Constant for name **/
    public static final String CHART_SERIES_NAME = "name";

    /** Constant for data **/
    public static final String CHART_SERIES_DATA = "data";
    
    public static final String PAGE_OF_EDIT_SPECIMEN="pageOfEditSpecimen";
    
    public static final String RED_LINE_VALUE = "graph.red.line.value";
    public static final String PAGE_OF_SPECIMEN_SUMMARY_PAGE="pageOfSpecimenSummary";
    public static final String ACTIVITY_STATUS_LOCKED = "Locked";
    public static final String LOGIN_FAILURE_ATTEMPTS_LIMIT = "loginFailureAttemptsLimit";
    public static final String THROTTLING_TIME_INTERVAL= "throttlingTimeIntervalInMinutes";
  	
  	public static final String THROTTLING_MAX_LIMIT="throttlingMaxLimits";
    

}