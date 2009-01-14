
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

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;

 
/**
 * This class stores the constants used in the operations in the application.
 * @author gautam_shetty
 */

public class Constants extends edu.wustl.common.util.global.Constants
{
	
	
	//Constants added for Catissuecore V1.2
	public static final String PAGE_OF_ADMINISTRATOR = "pageOfAdministrator";
	public static final String PAGE_OF_SUPERVISOR = "pageOfSupervisor";
	public static final String PAGE_OF_TECHNICIAN = "pageOfTechnician";
	public static final String PAGE_OF_SCIENTIST = "pageOfScientist";
	
    //Constants added for Validation
	public static final String DEFAULT_TISSUE_SITE ="defaultTissueSite";
	public static final String DEFAULT_CLINICAL_STATUS ="defaultClinicalStatus"; 
	public static final String DEFAULT_GENDER = "defaultGender";
	public static final String DEFAULT_GENOTYPE = "defaultGenotype";
	public static final String DEFAULT_SPECIMEN = "defaultSpecimen";
	public static final String DEFAULT_TISSUE_SIDE = "defaultTissueSide";
	public static final String DEFAULT_PATHOLOGICAL_STATUS = "defaultPathologicalStatus";
	public static final String DEFAULT_RECEIVED_QUALITY= "defaultReceivedQuality";
	public static final String DEFAULT_FIXATION_TYPE = "defaultFixationType";
	public static final String DEFAULT_COLLECTION_PROCEDURE="defaultCollectionProcedure";
	public static final String DEFAULT_CONTAINER= "defaultContainer";
	public static final String DEFAULT_METHOD= "defaultMethod";
	public static final String DEFAULT_EMBEDDING_MEDIUM="defaultEmbeddingMedium";
	public static final String DEFAULT_BIOHAZARD="defaultBiohazard";
	public static final String DEFAULT_SITE_TYPE ="defaultSiteType";
	public static final String DEFAULT_SPECIMEN_TYPE ="defaultSpecimenType";
	public static final String DEFAULT_ETHNICITY ="defaultEthnicity";
	public static final String DEFAULT_RACE ="defaultRace";
	public static final String DEFAULT_CLINICAL_DIAGNOSIS = "defaultClinicalDiagnosis";
	public static final String DEFAULT_STATES ="defaultStates";
	public static final String DEFAULT_COUNTRY ="defaultCountry";
	public static final String DEFAULT_HISTOLOGICAL_QUALITY="defaultHistologicalQuality";
	public static final String DEFAULT_VITAL_STATUS ="defaultVitalStatus";
	public static final String DEFAULT_PRINTER_TYPE ="defaultPrinterType";
	public static final String DEFAULT_PRINTER_LOCATION ="defaultPrinterLocation";
	
	  //Consent tracking
	public static final String 	SHOW_CONSENTS="showConsents";
	public static final String 	SPECIMEN_CONSENTS="specimenConsents";
	public static final String 	YES="yes";
	public static final String 	CP_ID="cpID";
	public static final String 	BARCODE_LABLE="barcodelabel";
	public static final String  DISTRIBUTION_ON="labelBarcode";
	public static final String 	POPUP="popup";
	public static final String 	ERROR="error";
	public static final String 	ERROR_SHOWCONSENTS="errorShowConsent";
	public static final String 	COMPLETE="Complete";
	public static final String 	VIEW_CONSENTS="View";
	public static final String 	APPLY="Apply";
	public static final String 	APPLY_ALL="ApplyAll";
	public static final String 	APPLY_NONE="ApplyNone";
	public static final String 	PREDEFINED_CADSR_CONSENTS="predefinedConsentsList";
	public static final String 	DISABLED="disabled";
	public static final String 	VIEWAll="ViewAll";
	public static final String 	BARCODE_DISTRIBUTION="1";
	public static final String 	LABLE_DISTRIBUTION="2";
	public static final String 	CONSENT_WAIVED="Consent Waived";
	public static final String 	NO_CONSENTS="No Consents";
	public static final String 	NO_CONSENTS_DEFINED="None Defined";
	public static final String 	INVALID="Invalid";
	public static final String 	VALID="valid";
	public static final String 	FALSE="false";
	public static final String 	TRUE="true";
	public static final String 	NULL="null";
	public static final String 	CONSENT_TABLE="tableId4";
	public static final String 	DISABLE="disable";
	public static final String 	SCG_ID="-1";
	public static final String 	SELECTED_TAB="tab";
	public static final String 	TAB_SELECTED="tabSelected";
	public static final String 	NEWSPECIMEN_FORM="newSpecimenForm";
	public static final String 	CONSENT_TABLE_SHOWHIDE="tableStatus";
	public static final String 	SPECIMEN_RESPONSELIST="specimenResponseList";
	public static final String 	PROTOCOL_EVENT_ID="protocolEventId";
	public static final String 	SCG_DROPDOWN="value";
	public static final String 	HASHED_OUT="####";
	public static final String 	HASHED_NODE_ID = "-1";
	public static final String 	VERIFIED="Verified";
	public static final String  STATUS="status";
	public static final String 	NOT_APPLICABLE="Not Applicable";
	public static final String 	WITHDRAW_ALL="withrawall";
	public static final String 	RESPONSE="response";
	public static final String 	WITHDRAW="withdraw";
	public static final String 	VIRTUALLY_LOCATED="Virtually Located";
	public static final String 	LABLE="Lable";
	public static final String  STORAGE_CONTAINER_LOCATION="Storage Container Location";
	public static final String  CLASS_NAME="Class Name";
	public static final String  SPECIMEN_LIST="specimenDetails";
	public static final String  COLUMNLIST="columnList";
	public static final String  CONSENT_RESPONSE_KEY="CR_";
	public static final String  CONSENT_RESPONSE="ConsentResponse";
	public static final String STORAGE_TYPE_POSITION_VIRTUAL = "Virtual";
	public static final int STORAGE_TYPE_POSITION_VIRTUAL_VALUE = 1;
	public static final String STORAGE_TYPE_POSITION_AUTO = "Auto";
	public static final int STORAGE_TYPE_POSITION_AUTO_VALUE = 2;
	public static final String STORAGE_TYPE_POSITION_MANUAL = "Manual";
	public static final int STORAGE_TYPE_POSITION_MANUAL_VALUE = 3;	
	public static final int STORAGE_TYPE_POSITION_AUTO_VALUE_FOR_TRANSFER_EVENT = 1;
	public static final int STORAGE_TYPE_POSITION_MANUAL_VALUE_FOR_TRANSFER_EVENT = 2;

		
	public static final String [][] defaultValueKeys= {
		{Constants.DEFAULT_TISSUE_SITE, edu.wustl.common.util.global.Constants.CDE_NAME_TISSUE_SITE},
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
		{Constants.DEFAULT_EMBEDDING_MEDIUM,Constants.CDE_NAME_EMBEDDING_MEDIUM},
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
		{Constants.DEFAULT_PRINTER_LOCATION,Constants.DEFAULT_PRINTER_LOCATION},
		{Constants.DEFAULT_PRINTER_TYPE,Constants.DEFAULT_PRINTER_TYPE}
   };
   
	//Constants added for Catissuecore V1.2
	public static final String MYSQL_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION= "cast(label as signed)";
	public static final String ORACLE_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION = "catissue_label_to_num(label)";
	public static final String ORACLE_MAX_BARCODE_COL = "catissue_label_to_num(barcode)";
	public static final String MYSQL_MAX_BARCODE_COL= "cast(barcode as signed)";
	// Query Module Interface UI constants
	public static final String ViewSearchResultsAction = "ViewSearchResultsAction.do";
	public static final String categorySearchForm = "categorySearchForm";
	public static final String SearchCategory = "SearchCategory.do";
	public static final String DefineSearchResultsViewAction = "DefineSearchResultsView.do";
	public static final String DefineSearchResultsViewJSPAction = "ViewSearchResultsJSPAction.do";
	public static final String QUERY_DAG_VIEW_APPLET = "edu/wustl/catissuecore/applet/ui/querysuite/DiagrammaticViewApplet.class";
	public static final String QUERY_DAG_VIEW_APPLET_NAME = "Dag View Applet";
	public static final String APP_DYNAMIC_UI_XML = "xmlfile.dynamicUI";
	public static final String QUERY_CONDITION_DELIMITER = "@#condition#@";
	public static final String QUERY_OPERATOR_DELIMITER = "!*=*!";
	public static final String QUERY_VALUES_DELIMITER = "&";
	//public static final String SEARCHED_ENTITIES_MAP = "searchedEntitiesMap";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String LIST_OF_ENTITIES_IN_QUERY = "ListOfEntitiesInQuery";
	public static final String DYNAMIC_UI_XML = "dynamicUI.xml";
	public static final String TREE_DATA = "treeData";
	public static final String ZERO_ID	= "0";
	public static final String TEMP_OUPUT_TREE_TABLE_NAME = "TEMP_OUTPUTTREE";
	public static final String CREATE_TABLE = "Create table "; 
	public static final String AS = "as";
	public static final String UNDERSCORE = "_";
	public static final String ID_NODES_MAP = "idNodesMap";
	public static final String ID_COLUMNS_MAP = "idColumnsMap";
	public static final String COLUMN_NAME = "Column";
	public static final String[] ATTRIBUTE_NAMES_FOR_TREENODE_LABEL = {
		"firstName",
		"lastName",
		"title",
		"name",
		"label",
		"shorttitle"
	};
	public static final String ROOT = "root";
	public static final String MISSING_TWO_VALUES = "missingTwoValues";
	public static final String DATE = "date";
	public static final String DEFINE_RESULTS_VIEW = "DefineResultsView";
	public static final String CURRENT_PAGE = "currentPage"; 
	public static final String ADD_LIMITS = "AddLimits";
	public static final String LABEL_TREE_NODE = "Label";
	public static final String ENTITY_NAME = "Entity Name";
	public static final String COUNT = "Count";
	public static final String TREE_NODE_FONT = "<font color='#FF9BFF' face='Verdana'><i>";  
	public static final String TREE_NODE_FONT_CLOSE = "</i></font>";  
	public static final String NULL_ID = "NULL";
	public static final String NODE_SEPARATOR = "::";
	public static final String EXPRESSION_ID_SEPARATOR = ":";
	public static final String UNIQUE_ID_SEPARATOR = "UQ";
	public static final String DEFINE_SEARCH_RULES = "Define Limits For";
	public static final String DATE_FORMAT = "MM-dd-yyyy";
	public static final String NEW_DATE_FROMAT ="MM/DD/YYYY";
	public static final String OUTPUT_TREE_MAP = "outputTreeMap";
	public static final String CHECK_ALL_PAGES = "checkAllPages";
	public static final String CHECK_ALL_ACROSS_ALL_PAGES = "isCheckAllAcrossAllChecked";
	public static final String CLASSES_PRESENT_IN_QUERY = "Objects Present In Query";
	public static final String DEFINE_QUERY_RESULTS_VIEW_ACTION = "DefineQueryResultsView.do";
	public static final String CONFIGURE_GRID_VIEW_ACTION = "ConfigureGridView.do";
	public static String ATTRIBUTE_MAP = "attributeMap";
	public static final String 	CLASS = "class";
	public static final String ATTRIBUTE = "attribute";
	public static final String SELECT_DISTINCT = "select distinct ";
	public static final String FROM = " from ";
	public static final String WHERE = " where ";
	public static final String SELECTED_COLUMN_META_DATA = "selectedColumnMetaData";
	public static final String CURRENT_SELECTED_OBJECT = "currentSelectedObject";
	public static final String SELECTED_COLUMN_NAME_VALUE_BEAN_LIST = "selectedColumnNameValueBeanList";
	public static final String OPERATION = "operation";
	public static final String FINISH = "finish";
	public static final String BACK = "back";
	public static final String RESTORE = "restore";
	public static final String VIEW_ALL_RECORDS = "viewAllRecords";
	public static final String VIEW_LIMITED_RECORDS = "viewLimitedRecords";
	public static final String SAVE_QUERY_ACTION = "SaveQueryAction.do";
	public static final String RETRIEVE_QUERY_ACTION = "RetrieveQueryAction.do";
	public static final String FETCH_AND_EXECUTE_QUERY_ACTION = "ExecuteQueryAction.do";
	public static final String TREE_NODE_LIMIT_EXCEEDED_RECORDS = "treeNodeLimitExceededRecords";
	public static final String MAXIMUM_TREE_NODE_LIMIT = "resultView.maximumTreeNodeLimit";
	public static final String DEFINED_VIEW_ATTRIBUTES = "definedViewAttributes";
	public static final String IS_SAVED_QUERY = "isSavedQuery";
	public static final String RANDOM_NUMBER = "randomNumber";
	public static final String MULTIUSER ="multiuser";
	public static final String SAVE_TREE_NODE_LIST = "rootOutputTreeNodeList";
	public static final String ATTRIBUTE_COLUMN_NAME_MAP = "attributeColumnNameMap";
	public static final String SAVE_GENERATED_SQL = "sql";
	public static final String POPUP_MESSAGE = "popupMessage";
	public static final String BUTTON_CLICKED = "buttonClicked";
	public static final String KEY_CODE = "key";
	public static final String ENTITY_SEPARATOR = ";";
	public static final String ATTRIBUTE_SEPARATOR = "|";
	public static final String KEY_SEPARATOR = "*&*";
	public static final String TOTAL_FILE_TYPE_ATTRIBUTES = "totalFileTypeAttributes";
	public static final String CLOBTYPE_ID_LIST = "clobTypeIdList"; 
	public static final String GETREPORTID = "getReportID.do";
	public static final String DOUBLE_QUOTES = "";
	public static final String OVERRIDE= "override";
	public static final String OVERRIDE_TRUE="true";
	public static final String OVERRIDE_FALSE="false";	
	/**
	 * Name: Abhishek Mehta
	 * Reviewer Name : Deepti 
	 * Bug ID: 5661
	 * Patch ID: 5661_1
	 * See also: 1-4 
	 * Description : Constants added for Query Module
	 */
    
	public static final String IS_QUERY_SAVED = "isQuerySaved";
	public static final String CONDITIONLIST = "conditionList";
	public static final String QUERY_SAVED = "querySaved";
	public static final String DISPLAY_NAME_FOR_CONDITION = "_displayName";
	public static final String SHOW_ALL_LINK = "showAllLink";
	
	// Query Module Interface UI constants---ends here
	//	 Frame names in Query Module Results page.
	public static final String GRID_DATA_VIEW_FRAME = "gridFrame";
	public static final String TREE_VIEW_FRAME = "treeViewFrame";
    public static final String QUERY_TREE_VIEW_ACTION = "QueryTreeView.do";
    public static final String QUERY_GRID_VIEW_ACTION = "QueryGridView.do";
    public static final String NO_OF_TREES = "noOfTrees";
    public static final String PAGEOF_QUERY_MODULE = "pageOfQueryModule";
    public static final String TREE_ROOTS = "treeRoots";
    //	 Frame names in Query Module Results page.--ends here
	public static final String MAX_IDENTIFIER = "maxIdentifier";
    public static final String AND_JOIN_CONDITION = "AND";
	public static final String OR_JOIN_CONDITION = "OR";
	//Sri: Changed the format for displaying in Specimen Event list (#463)
	public static final String TIMESTAMP_PATTERN = "MM-dd-yyyy HH:mm";
	public static final String TIMESTAMP_PATTERN_MM_SS = "HH:mm";
	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String QUERY_ID = "queryId";
	
	// Mandar: Used for Date Validations in Validator Class
	public static final String DATE_SEPARATOR = "-";
	public static final String DATE_SEPARATOR_DOT = ".";	
	public static final String MIN_YEAR = "1900";
	public static final String MAX_YEAR = "9999";
	
	public static final String VIEW = "view";
	public static final String DELETE = "delete";
	public static final String EXPORT = "export";
	public static final String SHOPPING_CART_ADD = "shoppingCartAdd";
	public static final String SHOPPING_CART_DELETE = "shoppingCartDelete";
	public static final String SHOPPING_CART_EXPORT = "shoppingCartExport";
	public static final String NEWUSERFORM = "newUserForm";
	public static final String REDIRECT_TO_SPECIMEN = "specimenRedirect";
	public static final String CALLED_FROM = "calledFrom";
	public static final String ACCESS = "access";
	
	//Constants required for Forgot Password
	public static final String FORGOT_PASSWORD = "forgotpassword";
	
	public static final String LOGINNAME = "loginName";
	public static final String LASTNAME = "lastName";
	public static final String FIRSTNAME = "firstName";
	public static final String INSTITUTION = "institution";
	public static final String EMAIL = "email";
	public static final String DEPARTMENT = "department";
	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String COUNTRY = "country";
	public static final String NEXT_CONTAINER_NO = "startNumber";
	public static final String CSM_USER_ID = "csmUserId";
	
	public static final String INSTITUTIONLIST = "institutionList";
	public static final String DEPARTMENTLIST = "departmentList";
	public static final String STATELIST = "stateList";
	public static final String COUNTRYLIST = "countryList";
	public static final String ROLELIST = "roleList";
	public static final String ROLEIDLIST = "roleIdList";
	public static final String CANCER_RESEARCH_GROUP_LIST = "cancerResearchGroupList";
	public static final String GENDER_LIST = "genderList";
	public static final String GENOTYPE_LIST = "genotypeList";
	public static final String ETHNICITY_LIST = "ethnicityList";
	public static final String PARTICIPANT_MEDICAL_RECORD_SOURCE_LIST = "participantMedicalRecordSourceList";
	public static final String RACELIST = "raceList";
	public static final String VITAL_STATUS_LIST = "vitalStatusList";
	public static final String PARTICIPANT_LIST = "participantList";
	public static final String PARTICIPANT_ID_LIST = "participantIdList";
	public static final String PROTOCOL_LIST = "protocolList";
	public static final String TIMEHOURLIST = "timeHourList";
	public static final String TIMEMINUTESLIST = "timeMinutesList";
	public static final String TIMEAMPMLIST = "timeAMPMList";
	public static final String RECEIVEDBYLIST = "receivedByList";
	public static final String COLLECTEDBYLIST = "collectedByList";
	public static final String COLLECTIONSITELIST = "collectionSiteList";
	public static final String RECEIVEDSITELIST = "receivedSiteList";
	public static final String RECEIVEDMODELIST = "receivedModeList";
	public static final String ACTIVITYSTATUSLIST = "activityStatusList";
	public static final String COLLECTIONSTATUSLIST = "collectionStatusList";
	public static final String USERLIST = "userList";
	public static final String ACTIONLIST = "actionsList";
	public static final String SITETYPELIST = "siteTypeList";
	public static final String STORAGETYPELIST="storageTypeList";
	public static final String STORAGECONTAINERLIST="storageContainerList";
	public static final String SITELIST="siteList";
	public static final String CPLIST="cpList";
	public static final String REPOSITORY = "Repository";
	
//	public static final String SITEIDLIST="siteIdList";
	public static final String USERIDLIST = "userIdList";
	public static final String STORAGETYPEIDLIST="storageTypeIdList";
	public static final String SPECIMENCOLLECTIONLIST="specimentCollectionList";
	public static final String PARTICIPANT_IDENTIFIER_IN_CPR = "participant";
	public static final String APPROVE_USER_STATUS_LIST = "statusList";
	public static final String EVENT_PARAMETERS_LIST = "eventParametersList";
	public static final String PRIVILEGE_DATA_LIST_ONLOAD="listOfPrivilegeDataOnLoad";
			
	//New Specimen lists.
	public static final String SPECIMEN_COLLECTION_GROUP_LIST = "specimenCollectionGroupIdList";
	public static final String SPECIMEN_TYPE_LIST = "specimenTypeList";
	public static final String SPECIMEN_SUB_TYPE_LIST = "specimenSubTypeList";
	public static final String TISSUE_SITE_LIST = "tissueSiteList";
	public static final String TISSUE_SIDE_LIST = "tissueSideList";
	public static final String PATHOLOGICAL_STATUS_LIST = "pathologicalStatusList";
	public static final String BIOHAZARD_TYPE_LIST = "biohazardTypeList";
	public static final String BIOHAZARD_NAME_LIST = "biohazardNameList";
	public static final String BIOHAZARD_ID_LIST = "biohazardIdList";
	public static final String BIOHAZARD_TYPES_LIST = "biohazardTypesList";
	public static final String PARENT_SPECIMEN_ID_LIST = "parentSpecimenIdList";
	public static final String RECEIVED_QUALITY_LIST = "receivedQualityList";
	public static final String SPECIMEN_COLL_GP_NAME = "specimenCollectionGroupName";
	
	//SpecimenCollecionGroup lists.
	public static final String PROTOCOL_TITLE_LIST = "protocolTitleList";
	public static final String PARTICIPANT_NAME_LIST = "participantNameList";
	public static final String PROTOCOL_PARTICIPANT_NUMBER_LIST = "protocolParticipantNumberList";
	//public static final String PROTOCOL_PARTICIPANT_NUMBER_ID_LIST = "protocolParticipantNumberIdList";
	public static final String STUDY_CALENDAR_EVENT_POINT_LIST = "studyCalendarEventPointList";
   	//public static final String STUDY_CALENDAR_EVENT_POINT_ID_LIST="studyCalendarEventPointIdList";
	public static final String PARTICIPANT_MEDICAL_IDNETIFIER_LIST = "participantMedicalIdentifierArray";
	//public static final String PARTICIPANT_MEDICAL_IDNETIFIER_ID_LIST = "participantMedicalIdentifierIdArray";
	public static final String SPECIMEN_COLLECTION_GROUP_ID = "specimenCollectionGroupId";
	public static final String REQ_PATH = "redirectTo";
	
	public static final String CLINICAL_STATUS_LIST = "cinicalStatusList";
	public static final String SPECIMEN_CLASS_LIST = "specimenClassList";
	public static final String SPECIMEN_CLASS_ID_LIST = "specimenClassIdList";
	public static final String SPECIMEN_TYPE_MAP = "specimenTypeMap";
	//Simple Query Interface Lists
	public static final String OBJECT_COMPLETE_NAME_LIST = "objectCompleteNameList";
	public static final String SIMPLE_QUERY_INTERFACE_TITLE = "simpleQueryInterfaceTitle";
	
	public static final String MAP_OF_STORAGE_CONTAINERS = "storageContainerMap";
	public static final String STORAGE_CONTAINER_GRID_OBJECT = "storageContainerGridObject";
	public static final String STORAGE_CONTAINER_CHILDREN_STATUS = "storageContainerChildrenStatus";
	public static final String START_NUMBER = "startNumber";
	public static final String CHILD_CONTAINER_SYSTEM_IDENTIFIERS = "childContainerIds";
	public static final int STORAGE_CONTAINER_FIRST_ROW = 1;
	public static final int STORAGE_CONTAINER_FIRST_COLUMN = 1;
	public static final String MAP_COLLECTION_PROTOCOL_LIST = "collectionProtocolList";
	public static final String MAP_SPECIMEN_CLASS_LIST = "specimenClassList";
	public static final String CONTENT_OF_CONTAINNER="contentOfContainer";
	
	//event parameters lists
	public static final String METHOD_LIST = "methodList";
	public static final String HOUR_LIST = "hourList";
	public static final String MINUTES_LIST = "minutesList";
	public static final String EMBEDDING_MEDIUM_LIST = "embeddingMediumList";
	public static final String PROCEDURE_LIST = "procedureList";
	public static final String PROCEDUREIDLIST = "procedureIdList";
	public static final String CONTAINER_LIST = "containerList";
	public static final String CONTAINERIDLIST = "containerIdList";
	public static final String FROMCONTAINERLIST="fromContainerList";
	public static final String TOCONTAINERLIST="toContainerList";
	public static final String FIXATION_LIST = "fixationList";	
	public static final String FROM_SITE_LIST="fromsiteList";
	public static final String TO_SITE_LIST="tositeList";	
	public static final String ITEMLIST="itemList";
	public static final String DISTRIBUTIONPROTOCOLLIST="distributionProtocolList";
	public static final String TISSUE_SPECIMEN_ID_LIST="tissueSpecimenIdList";
	public static final String MOLECULAR_SPECIMEN_ID_LIST="molecularSpecimenIdList";
	public static final String CELL_SPECIMEN_ID_LIST="cellSpecimenIdList";
	public static final String FLUID_SPECIMEN_ID_LIST="fluidSpecimenIdList";
	public static final String STORAGE_STATUS_LIST="storageStatusList";
	public static final String CLINICAL_DIAGNOSIS_LIST = "clinicalDiagnosisList";
	public static final String HISTOLOGICAL_QUALITY_LIST="histologicalQualityList";
	
	//For Specimen Event Parameters.	
	public static final String REQUEST_TO_ORDER = "requestToOrder";
	public static final String ADD_TO_ORDER_LIST = "addToOrderList";
	public static final String REQUESTED_FOR_BIOSPECIMENS = "RequestedBioSpecimens";
	public static final String DEFINE_ARRAY_FORM_OBJECTS = "DefineArrayFormObjects";
	public static final String SPECIMEN_ID = "specimenId";
	public static final String SPECIMEN_ARRAY_ID = "specimenArrayIds";
	public static final String PATHALOGICAL_CASE_ID = "pathalogicalCaseIds";
	public static final String DEIDENTIFIED_PATHALOGICAL_CASE_ID = "deidentifiedPathalogicalCaseIds";
	public static final String SURGICAL_PATHALOGY_CASE_ID ="surgicalPathalogicalCaseIds";
	public static final String FROM_POSITION_DATA = "fromPositionData";
	public static final String POS_ONE ="posOne";
	public static final String POS_TWO ="posTwo";
	public static final String STORAGE_CONTAINER_ID ="storContId";	
	public static final String IS_RNA = "isRNA";
	public static final String RNA = "RNA";
	
	//	New Participant Event Parameters
	public static final String PARTICIPANT_ID="participantId";
	
	//Constants required in User.jsp Page
	public static final String USER_SEARCH_ACTION = "UserSearch.do";
	public static final String USER_ADD_ACTION = "UserAdd.do";
	public static final String USER_EDIT_ACTION = "UserEdit.do";
	public static final String APPROVE_USER_ADD_ACTION = "ApproveUserAdd.do";
	public static final String APPROVE_USER_EDIT_ACTION = "ApproveUserEdit.do";
	public static final String SIGNUP_USER_ADD_ACTION = "SignUpUserAdd.do";
	public static final String USER_EDIT_PROFILE_ACTION = "UserEditProfile.do";
	public static final String UPDATE_PASSWORD_ACTION = "UpdatePassword.do";
	public static final String OPEN_PAGE_IN_CPFRAME ="openInCPFrame";
	public static final String DUMMY_PASSWORD ="XXXXXX";	
	//Constants required in Accession.jsp Page
	public static final String ACCESSION_SEARCH_ACTION = "AccessionSearch.do";
	public static final String ACCESSION_ADD_ACTION = "AccessionAdd.do";
	public static final String ACCESSION_EDIT_ACTION = "AccessionEdit.do";
	
	//Constants required in StorageType.jsp Page
	public static final String STORAGE_TYPE_SEARCH_ACTION = "StorageTypeSearch.do";
	public static final String STORAGE_TYPE_ADD_ACTION = "StorageTypeAdd.do";
	public static final String STORAGE_TYPE_EDIT_ACTION = "StorageTypeEdit.do";
	
	//Constants required in StorageContainer.jsp Page
	public static final String STORAGE_CONTAINER_SEARCH_ACTION = "StorageContainerSearch.do";
	public static final String STORAGE_CONTAINER_ADD_ACTION = "StorageContainerAdd.do";
	public static final String STORAGE_CONTAINER_EDIT_ACTION = "StorageContainerEdit.do";
	
	public static final String HOLDS_LIST1 = "HoldsList1";
	public static final String HOLDS_LIST2 = "HoldsList2";
	public static final String HOLDS_LIST3 = "HoldsList3";
	//Constants required in Site.jsp Page
	public static final String SITE_SEARCH_ACTION = "SiteSearch.do";
	public static final String SITE_ADD_ACTION = "SiteAdd.do";
	public static final String SITE_EDIT_ACTION = "SiteEdit.do";
	
	//Constants required in Site.jsp Page
	public static final String BIOHAZARD_SEARCH_ACTION = "BiohazardSearch.do";
	public static final String BIOHAZARD_ADD_ACTION = "BiohazardAdd.do";
	public static final String BIOHAZARD_EDIT_ACTION = "BiohazardEdit.do";
	
	//Constants required in Partcipant.jsp Page
	public static final String PARTICIPANT_SEARCH_ACTION = "ParticipantSearch.do";
	public static final String PARTICIPANT_ADD_ACTION = "ParticipantAdd.do";
	public static final String PARTICIPANT_EDIT_ACTION = "ParticipantEdit.do";
	public static final String PARTICIPANT_LOOKUP_ACTION= "ParticipantLookup.do";
	public static final String PARTICIPANT_CONSENT_ENTER_RESPONSE= "Enter Response";
	public static final String PARTICIPANT_CONSENT_EDIT_RESPONSE= "Edit Response";
	
	//Constants required in Institution.jsp Page
	public static final String INSTITUTION_SEARCH_ACTION = "InstitutionSearch.do";
	public static final String INSTITUTION_ADD_ACTION = "InstitutionAdd.do";
	public static final String INSTITUTION_EDIT_ACTION = "InstitutionEdit.do";

	//Constants required in Department.jsp Page
	public static final String DEPARTMENT_SEARCH_ACTION = "DepartmentSearch.do";
	public static final String DEPARTMENT_ADD_ACTION = "DepartmentAdd.do";
	public static final String DEPARTMENT_EDIT_ACTION = "DepartmentEdit.do";
	
    //Constants required in CollectionProtocolRegistration.jsp Page
	public static final String COLLECTION_PROTOCOL_REGISTRATION_SEARCH_ACTION = "CollectionProtocolRegistrationSearch.do";
	public static final String COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION = "CollectionProtocolRegistrationAdd.do";
	public static final String COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "CollectionProtocolRegistrationEdit.do";
	
	//Constants required in CancerResearchGroup.jsp Page
	public static final String CANCER_RESEARCH_GROUP_SEARCH_ACTION = "CancerResearchGroupSearch.do";
	public static final String CANCER_RESEARCH_GROUP_ADD_ACTION = "CancerResearchGroupAdd.do";
	public static final String CANCER_RESEARCH_GROUP_EDIT_ACTION = "CancerResearchGroupEdit.do";
	
	//Constants required for Approve user
	public static final String USER_DETAILS_SHOW_ACTION = "UserDetailsShow.do";
	public static final String APPROVE_USER_SHOW_ACTION = "ApproveUserShow.do";
	
	//Reported Problem Constants
	public static final String REPORTED_PROBLEM_ADD_ACTION = "ReportedProblemAdd.do";
	public static final String REPORTED_PROBLEM_EDIT_ACTION = "ReportedProblemEdit.do";
	public static final String PROBLEM_DETAILS_ACTION = "ProblemDetails.do";
	public static final String REPORTED_PROBLEM_SHOW_ACTION = "ReportedProblemShow.do";
	
	//Query Results view Actions
	public static final String TREE_VIEW_ACTION = "TreeView.do";
	//-- TreeNodeDataAction added for DHTMLX tree grid view of storage container
	public static final String TREE_NODE_DATA_ACTION = "TreeNodeData.do";
	public static final String DATA_VIEW_FRAME_ACTION = "SpreadsheetView.do";
	public static final String SIMPLE_QUERY_INTERFACE_URL = "SimpleQueryInterface.do?pageOf=pageOfSimpleQueryInterface&menuSelected=17";
	
	//New Specimen Data Actions.
	public static final String SPECIMEN_ADD_ACTION = "NewSpecimenAdd.do";
	public static final String SPECIMEN_EDIT_ACTION = "NewSpecimenEdit.do";
	public static final String SPECIMEN_SEARCH_ACTION = "NewSpecimenSearch.do";
	
	public static final String SPECIMEN_EVENT_PARAMETERS_ACTION = "ListSpecimenEventParameters.do";
	
	//Create Specimen Data Actions.
	public static final String CREATE_SPECIMEN_ADD_ACTION = "AddSpecimen.do";
	public static final String CREATE_SPECIMEN_EDIT_ACTION = "CreateSpecimenEdit.do";
	public static final String CREATE_SPECIMEN_SEARCH_ACTION = "CreateSpecimenSearch.do";
	
	//ShoppingCart Actions.
	public static final String SHOPPING_CART_OPERATION = "ShoppingCartOperation.do";

	public static final String SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION = "SpecimenCollectionGroup.do";
	public static final String SPECIMEN_COLLECTION_GROUP_ADD_ACTION = "SpecimenCollectionGroupAdd.do";
	public static final String SPECIMEN_COLLECTION_GROUP_EDIT_ACTION = "SpecimenCollectionGroupEdit.do";

	//Constants required in FrozenEventParameters.jsp Page
	public static final String FROZEN_EVENT_PARAMETERS_SEARCH_ACTION = "FrozenEventParametersSearch.do";
	public static final String FROZEN_EVENT_PARAMETERS_ADD_ACTION = "FrozenEventParametersAdd.do";
	public static final String FROZEN_EVENT_PARAMETERS_EDIT_ACTION = "FrozenEventParametersEdit.do";

	//Constants required in CheckInCheckOutEventParameters.jsp Page
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_SEARCH_ACTION = "CheckInCheckOutEventParametersSearch.do";
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_ADD_ACTION = "CheckInCheckOutEventParametersAdd.do";
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_EDIT_ACTION = "CheckInCheckOutEventParametersEdit.do";

	//Constants required in ReceivedEventParameters.jsp Page
	public static final String RECEIVED_EVENT_PARAMETERS_SEARCH_ACTION = "receivedEventParametersSearch.do";
	public static final String RECEIVED_EVENT_PARAMETERS_ADD_ACTION = "ReceivedEventParametersAdd.do";
	public static final String RECEIVED_EVENT_PARAMETERS_EDIT_ACTION = "receivedEventParametersEdit.do";

	//Constants required in FluidSpecimenReviewEventParameters.jsp Page
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION = "FluidSpecimenReviewEventParametersSearch.do";
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION = "FluidSpecimenReviewEventParametersAdd.do";
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION = "FluidSpecimenReviewEventParametersEdit.do";

	//Constants required in CELLSPECIMENREVIEWParameters.jsp Page
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_SEARCH_ACTION = "CellSpecimenReviewParametersSearch.do";
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION = "CellSpecimenReviewParametersAdd.do";
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION = "CellSpecimenReviewParametersEdit.do";

	//Constants required in tissue SPECIMEN REVIEW event Parameters.jsp Page
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION = "TissueSpecimenReviewEventParametersSearch.do";
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION = "TissueSpecimenReviewEventParametersAdd.do";
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION = "TissueSpecimenReviewEventParametersEdit.do";

	//	Constants required in DisposalEventParameters.jsp Page	
	public static final String DISPOSAL_EVENT_PARAMETERS_SEARCH_ACTION = "DisposalEventParametersSearch.do";
	public static final String DISPOSAL_EVENT_PARAMETERS_ADD_ACTION = "DisposalEventParametersAdd.do";
	public static final String DISPOSAL_EVENT_PARAMETERS_EDIT_ACTION = "DisposalEventParametersEdit.do";
	
	//	Constants required in ThawEventParameters.jsp Page
	public static final String THAW_EVENT_PARAMETERS_SEARCH_ACTION = "ThawEventParametersSearch.do";
	public static final String THAW_EVENT_PARAMETERS_ADD_ACTION = "ThawEventParametersAdd.do";
	public static final String THAW_EVENT_PARAMETERS_EDIT_ACTION = "ThawEventParametersEdit.do";

	//	Constants required in MOLECULARSPECIMENREVIEWParameters.jsp Page
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_SEARCH_ACTION = "MolecularSpecimenReviewParametersSearch.do";
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION = "MolecularSpecimenReviewParametersAdd.do";
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION = "MolecularSpecimenReviewParametersEdit.do";

	//	Constants required in CollectionEventParameters.jsp Page
	public static final String COLLECTION_EVENT_PARAMETERS_SEARCH_ACTION = "CollectionEventParametersSearch.do";
	public static final String COLLECTION_EVENT_PARAMETERS_ADD_ACTION = "CollectionEventParametersAdd.do";
	public static final String COLLECTION_EVENT_PARAMETERS_EDIT_ACTION = "CollectionEventParametersEdit.do";
	
	//	Constants required in SpunEventParameters.jsp Page
	public static final String SPUN_EVENT_PARAMETERS_SEARCH_ACTION = "SpunEventParametersSearch.do";
	public static final String SPUN_EVENT_PARAMETERS_ADD_ACTION = "SpunEventParametersAdd.do";
	public static final String SPUN_EVENT_PARAMETERS_EDIT_ACTION = "SpunEventParametersEdit.do";
	
	//	Constants required in EmbeddedEventParameters.jsp Page
	public static final String EMBEDDED_EVENT_PARAMETERS_SEARCH_ACTION = "EmbeddedEventParametersSearch.do";
	public static final String EMBEDDED_EVENT_PARAMETERS_ADD_ACTION = "EmbeddedEventParametersAdd.do";
	public static final String EMBEDDED_EVENT_PARAMETERS_EDIT_ACTION = "EmbeddedEventParametersEdit.do";
	
	//	Constants required in TransferEventParameters.jsp Page
	public static final String TRANSFER_EVENT_PARAMETERS_SEARCH_ACTION = "TransferEventParametersSearch.do";
	public static final String TRANSFER_EVENT_PARAMETERS_ADD_ACTION = "TransferEventParametersAdd.do";
	public static final String TRANSFER_EVENT_PARAMETERS_EDIT_ACTION = "TransferEventParametersEdit.do";

//	Constants required in FixedEventParameters.jsp Page
	public static final String FIXED_EVENT_PARAMETERS_SEARCH_ACTION = "FixedEventParametersSearch.do";
	public static final String FIXED_EVENT_PARAMETERS_ADD_ACTION = "FixedEventParametersAdd.do";
	public static final String FIXED_EVENT_PARAMETERS_EDIT_ACTION = "FixedEventParametersEdit.do";

//	Constants required in ProcedureEventParameters.jsp Page
	public static final String PROCEDURE_EVENT_PARAMETERS_SEARCH_ACTION = "ProcedureEventParametersSearch.do";
	public static final String PROCEDURE_EVENT_PARAMETERS_ADD_ACTION = "ProcedureEventParametersAdd.do";
	public static final String PROCEDURE_EVENT_PARAMETERS_EDIT_ACTION = "ProcedureEventParametersEdit.do";
	
	//	Constants required in Distribution.jsp Page
	public static final String DISTRIBUTION_SEARCH_ACTION = "DistributionSearch.do";
	public static final String DISTRIBUTION_ADD_ACTION = "DistributionAdd.do";
	public static final String DISTRIBUTION_EDIT_ACTION = "DistributionEdit.do";
	
	public static final String SPECIMENARRAYTYPE_ADD_ACTION = "SpecimenArrayTypeAdd.do?operation=add";
	public static final String SPECIMENARRAYTYPE_EDIT_ACTION = "SpecimenArrayTypeEdit.do?operation=edit";
	public static final String ARRAY_DISTRIBUTION_ADD_ACTION = "ArrayDistributionAdd.do";
	
	public static final String SPECIMENARRAY_ADD_ACTION = "SpecimenArrayAdd.do";
	public static final String SPECIMENARRAY_EDIT_ACTION = "SpecimenArrayEdit.do";
    
	//Constants required in Main menu
	
	public static String HELP_FILE= "caTissue_Suite_User_Manual.pdf";
//	public static String USER_GUIDE = "userguide";
//	public static String TECHNICAL_GUIDE = "technicalguide";
//	public static String TRAINING_GUIDE = "trainingguide";
//	public static String UMLMODEL_GUIDE = "umlmodelguide";
//	public static String KNOWLEDGECENTER_GUIDE =  "knowledgecenterguide";
//	public static String GFORGE_GUIDE = "gforgeguide";
	public static String USER_GUIDE_LINK_PROPERTY = "userguide.link";
	public static String TECHNICAL_GUIDE_LINK_PROPERTY = "technicalguide.link";
	public static String TRAINING_GUIDE_LINK_PROPERTY = "trainingguide.link";
	public static String UML_MODEL_LINK_PROPERTY = "umlmodel.link";
	public static String KNOWLEDGE_CENTER_LINK_PROPERTY =  "knowledgecenter.link";
	public static String KNOWLEDGE_CENTER_FORUM_LINK_PROPERTY = "knowledgecenterforum.link";
	
	//Spreadsheet Export Action
	public static final String SPREADSHEET_EXPORT_ACTION = "SpreadsheetExport.do";
	
	//Aliquots Action
	public static final String ALIQUOT_ACTION = "Aliquots.do";
	public static final String CREATE_ALIQUOT_ACTION = "CreateAliquots.do";
	public static final String ALIQUOT_SUMMARY_ACTION = "AliquotSummary.do";
	
	//Constants related to Aliquots functionality
	public static final String PAGEOF_ALIQUOT = "pageOfAliquot";
	public static final String PAGEOF_CREATE_ALIQUOT = "pageOfCreateAliquot";
	public static final String PAGEOF_ALIQUOT_SUMMARY = "pageOfAliquotSummary";
	public static final String AVAILABLE_CONTAINER_MAP = "availableContainerMap";
	public static final String COMMON_ADD_EDIT = "commonAddEdit";
	

	//Constants related to SpecimenArrayAliquots functionality
	public static final String STORAGE_TYPE_ID="storageTypeId";
	public static final String ALIQUOT_SPECIMEN_ARRAY_TYPE="SpecimenArrayType";
	public static final String ALIQUOT_SPECIMEN_CLASS="SpecimenClass";
	public static final String ALIQUOT_SPECIMEN_TYPES="SpecimenTypes";
	public static final String ALIQUOT_ALIQUOT_COUNTS="AliquotCounts";
	public static final String QUERY_SESSION_DATA = "querySessionData";
	
	//Specimen Array Aliquots pages
	public static final String PAGEOF_SPECIMEN_ARRAY_ALIQUOT = "pageOfSpecimenArrayAliquot";
	public static final String PAGEOF_SPECIMEN_ARRAY_CREATE_ALIQUOT = "pageOfSpecimenArrayCreateAliquot";
	public static final String PAGEOF_SPECIMEN_ARRAY_ALIQUOT_SUMMARY = "pageOfSpecimenArrayAliquotSummary";	
		
	//Specimen Array Aliquots Action
	public static final String SPECIMEN_ARRAY_ALIQUOT_ACTION = "SpecimenArrayAliquots.do";
	public static final String SPECIMEN_ARRAY_CREATE_ALIQUOT_ACTION = "SpecimenArrayCreateAliquots.do";

	//Constants related to QuickEvents functionality
	public static final String QUICKEVENTS_ACTION = "QuickEventsSearch.do";	
	public static final String QUICKEVENTSPARAMETERS_ACTION = "ListSpecimenEventParameters.do";	
	
	//SimilarContainers Action
	public static final String SIMILAR_CONTAINERS_ACTION = "SimilarContainers.do";
	public static final String CREATE_SIMILAR_CONTAINERS_ACTION = "CreateSimilarContainers.do";
	public static final String SIMILAR_CONTAINERS_ADD_ACTION = "SimilarContainersAdd.do";
	
	//Constants related to Similar Containsers
	public static final String PAGEOF_SIMILAR_CONTAINERS = "pageOfSimilarContainers";
	public static final String PAGEOF_CREATE_SIMILAR_CONTAINERS = "pageOfCreateSimilarContainers";
	public static final String PAGEOF_STORAGE_CONTAINER = "pageOfStorageContainer";
	
	//Levels of nodes in query results tree.
	public static final int MAX_LEVEL = 5;
	public static final int MIN_LEVEL = 1;
	
	public static final String TABLE_NAME_COLUMN = "TABLE_NAME";
	
	//Spreadsheet view Constants in DataViewAction.
	public static final String PARTICIPANT = "Participant";
	public static final String ACCESSION = "Accession";
	public static final String QUERY_PARTICIPANT_SEARCH_ACTION = "QueryParticipantSearch.do?id=";
	public static final String QUERY_PARTICIPANT_EDIT_ACTION = "QueryParticipantEdit.do";
	public static final String QUERY_COLLECTION_PROTOCOL_SEARCH_ACTION = "QueryCollectionProtocolSearch.do?id=";
	public static final String QUERY_COLLECTION_PROTOCOL_EDIT_ACTION = "QueryCollectionProtocolEdit.do";
	public static final String QUERY_SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION = "QuerySpecimenCollectionGroupSearch.do?id=";
	public static final String QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION = "QuerySpecimenCollectionGroupEdit.do";
	public static final String QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION = "QuerySpecimenCollectionGroupAdd.do";
	public static final String QUERY_SPECIMEN_SEARCH_ACTION = "QuerySpecimenSearch.do?id=";
	public static final String QUERY_SPECIMEN_EDIT_ACTION = "QuerySpecimenEdit.do";
	//public static final String QUERY_ACCESSION_SEARCH_ACTION = "QueryAccessionSearch.do?id=";
	
	public static final String SPECIMEN = "Specimen";
	public static final String SEGMENT = "Segment";
	public static final String SAMPLE = "Sample";
	public static final String COLLECTION_PROTOCOL_REGISTRATION = "CollectionProtocolRegistration";
	public static final String PARTICIPANT_ID_COLUMN = "PARTICIPANT_ID";
	public static final String ACCESSION_ID_COLUMN = "ACCESSION_ID";
	public static final String SPECIMEN_ID_COLUMN = "SPECIMEN_ID";
	public static final String SEGMENT_ID_COLUMN = "SEGMENT_ID";
	public static final String SAMPLE_ID_COLUMN = "SAMPLE_ID";
	
	//For getting the tables for Simple Query and Fcon Query.
	public static final int ADVANCE_QUERY_TABLES = 2;
	
	//Identifiers for various Form beans
	public static final int DEFAULT_BIZ_LOGIC = 0;
	public static final int USER_FORM_ID = 1;
	public static final int ACCESSION_FORM_ID = 3;
	public static final int REPORTED_PROBLEM_FORM_ID = 4;
	public static final int INSTITUTION_FORM_ID = 5;
	public static final int APPROVE_USER_FORM_ID = 6;
	public static final int ACTIVITY_STATUS_FORM_ID = 7;
	public static final int DEPARTMENT_FORM_ID = 8;
	public static final int COLLECTION_PROTOCOL_FORM_ID = 9;
	public static final int DISTRIBUTIONPROTOCOL_FORM_ID = 10;
	public static final int STORAGE_CONTAINER_FORM_ID = 11;
	public static final int STORAGE_TYPE_FORM_ID = 12;
	public static final int SITE_FORM_ID = 13;
	public static final int CANCER_RESEARCH_GROUP_FORM_ID = 14;
	public static final int BIOHAZARD_FORM_ID = 15;
	public static final int FROZEN_EVENT_PARAMETERS_FORM_ID = 16;
	public static final int CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID = 17;
	public static final int RECEIVED_EVENT_PARAMETERS_FORM_ID = 18;
	public static final int FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID = 21;
	public static final int CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID =23;
	public static final int TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID = 24;
	public static final int DISPOSAL_EVENT_PARAMETERS_FORM_ID = 25;
	public static final int THAW_EVENT_PARAMETERS_FORM_ID = 26;
	public static final int MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID = 27;
	public static final int COLLECTION_EVENT_PARAMETERS_FORM_ID = 28;
	public static final int TRANSFER_EVENT_PARAMETERS_FORM_ID = 29;
	public static final int SPUN_EVENT_PARAMETERS_FORM_ID = 30;
	public static final int EMBEDDED_EVENT_PARAMETERS_FORM_ID = 31;
	public static final int FIXED_EVENT_PARAMETERS_FORM_ID = 32;	
	public static final int PROCEDURE_EVENT_PARAMETERS_FORM_ID = 33;
	public static final int CREATE_SPECIMEN_FORM_ID = 34;
	public static final int FORGOT_PASSWORD_FORM_ID = 35;
	public static final int SIGNUP_FORM_ID = 36;
	public static final int DISTRIBUTION_FORM_ID = 37;
	public static final int SPECIMEN_EVENT_PARAMETERS_FORM_ID = 38;
	public static final int SHOPPING_CART_FORM_ID = 39;
	public static final int CONFIGURE_RESULT_VIEW_ID = 41;
	public static final int ADVANCE_QUERY_INTERFACE_ID = 42;
	public static final int COLLECTION_PROTOCOL_REGISTRATION_FORM_ID = 19;
	public static final int PARTICIPANT_FORM_ID = 2;
	public static final int SPECIMEN_COLLECTION_GROUP_FORM_ID = 20;
	public static final int NEW_SPECIMEN_FORM_ID = 22;
	public static final int ALIQUOT_FORM_ID = 44;
	public static final int QUICKEVENTS_FORM_ID = 45;
	public static final int LIST_SPECIMEN_EVENT_PARAMETERS_FORM_ID = 46;
	public static final int SIMILAR_CONTAINERS_FORM_ID = 47;                  // chetan (13-07-2006)
	public static final int SPECIMEN_ARRAY_TYPE_FORM_ID = 48;
	public static final int ARRAY_DISTRIBUTION_FORM_ID = 49;
	public static final int SPECIMEN_ARRAY_FORM_ID = 50;
	public static final int SPECIMEN_ARRAY_ALIQUOT_FORM_ID = 51;
	public static final int ASSIGN_PRIVILEGE_FORM_ID = 52;
	public static final int CDE_FORM_ID = 53;
	public static final int MULTIPLE_SPECIMEN_STOGAGE_LOCATION_FORM_ID = 54;
	public static final int REQUEST_LIST_FILTERATION_FORM_ID = 55;
	public static final int ORDER_FORM_ID = 56;
	public static final int ORDER_ARRAY_FORM_ID = 57;
	public static final int REQUEST_DETAILS_FORM_ID = 64;
	public static final int ORDER_PATHOLOGY_FORM_ID = 58;
	public static final int NEW_PATHOLOGY_FORM_ID=59;
	public static final int DEIDENTIFIED_SURGICAL_PATHOLOGY_REPORT_FORM_ID=60;
	public static final int PATHOLOGY_REPORT_REVIEW_FORM_ID=61;
	public static final int QUARANTINE_EVENT_PARAMETER_FORM_ID=62;
	public static final int CONSENT_FORM_ID=63;
	public static final int BULK_OPERATIONS_FORM_ID = 68;
	//Misc
	public static final String SEPARATOR = " : ";
	
	//Identifiers for JDBC DAO.
	public static final int QUERY_RESULT_TREE_JDBC_DAO = 1;
	
	//Activity Status values
	public static final String ACTIVITY_STATUS_APPROVE = "Approve";
	public static final String ACTIVITY_STATUS_REJECT = "Reject";
	public static final String ACTIVITY_STATUS_NEW = "New";
	public static final String ACTIVITY_STATUS_PENDING = "Pending";
	
	//Approve User status values.
	public static final String APPROVE_USER_APPROVE_STATUS = "Approve";
	public static final String APPROVE_USER_REJECT_STATUS = "Reject";
	public static final String APPROVE_USER_PENDING_STATUS = "Pending";
	
	//Approve User Constants		
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int START_PAGE = 1;
	public static final int NUMBER_RESULTS_PER_PAGE = 20;
	public static final int NUMBER_RESULTS_PER_PAGE_SEARCH = 15;
	public static final String PAGE_NUMBER = "pageNum";
	public static final String RESULTS_PER_PAGE = "numResultsPerPage"; 
	public static final String TOTAL_RESULTS = "totalResults";
	public static final String PREVIOUS_PAGE = "prevpage";
	public static final String NEXT_PAGE = "nextPage";
	public static final String ORIGINAL_DOMAIN_OBJECT_LIST = "originalDomainObjectList";
	public static final String SHOW_DOMAIN_OBJECT_LIST = "showDomainObjectList";
	public static final String USER_DETAILS = "details";
	public static final String CURRENT_RECORD = "currentRecord";
	public static final String APPROVE_USER_EMAIL_SUBJECT = "Your membership status in caTISSUE Core.";
	
	//Query Interface Results View Constants
	public static final String PAGEOF_APPROVE_USER = "pageOfApproveUser";
	public static final String PAGEOF_SIGNUP = "pageOfSignUp";
	public static final String PAGEOF_USERADD = "pageOfUserAdd";
	public static final String PAGEOF_USER_ADMIN = "pageOfUserAdmin";
	public static final String PAGEOF_USER_PROFILE = "pageOfUserProfile";
	public static final String PAGEOF_CHANGE_PASSWORD = "pageOfChangePassword";
	
	//For Simple Query Interface and Edit.
	public static final String PAGEOF_EDIT_OBJECT = "pageOfEditObject";
	
	//Query results view temporary table columns.
	public static final String QUERY_RESULTS_PARTICIPANT_ID = "PARTICIPANT_ID";
	public static final String QUERY_RESULTS_COLLECTION_PROTOCOL_ID = "COLLECTION_PROTOCOL_ID";
	public static final String QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID = "COLLECTION_PROTOCOL_EVENT_ID";
	public static final String QUERY_RESULTS_SPECIMEN_COLLECTION_GROUP_ID = "SPECIMEN_COLLECTION_GROUP_ID";
	public static final String QUERY_RESULTS_SPECIMEN_ID = "SPECIMEN_ID";
	public static final String QUERY_RESULTS_SPECIMEN_TYPE = "SPECIMEN_TYPE";
	
	// Assign Privilege Constants.
	public static final boolean PRIVILEGE_DEASSIGN = false;
	public static final String OPERATION_DISALLOW = "Disallow";
	public static final String SUPERADMINISTRATOR = "Super Administrator";
	public static final String ALL_CURRENT_AND_FUTURE = "All Current and Future";
	public static final String NA = "N/A";
	public static final String IS_ALL_CP_CHECKED = "isAllCPChecked";
	public static final String IS_CUSTOM_CHECKBOX_CHECKED = "isCustChecked";
	
	public static final String CUSTOM_ROLE = "Custom";
	public static final String ROW_ID_OBJECT_BEAN_MAP = "rowIdObjectBeanMap";
	public static final String OPERATION_GET_CPS_FORTHIS_SITES = "getCPsForThisSites";
	public static final String OPERATION_GET_USERS_FORTHIS_SITES = "getUsersForThisSites";
	public static final String OPERATION_GET_ACTIONS_FORTHIS_SITES = "getActionsForThisSites";
	public static final String OPERATION_GET_ACTIONS_FORTHIS_ROLE = "getActionsForThisRole";
	public static final String OPERATION_GET_ACTIONS_FORTHIS_CPS = "getActionsForThisCPs";
	public static final String OPERATION_EDIT_ROW_FOR_USERPAGE = "editRowForUserPage";
	public static final String OPERATION_ADD_PRIVILEGE = "addPrivilege";
	public static final String OPERATION_ADD_PRIVILEGE_ON_USERPAGE = "addPrivOnUserPage";
	public static final String OPERATION_DELETE_ROW = "deleteRow";
	public static final String OPERATION_EDIT_ROW = "editRow";
	public static final String SELECTED_ACTION_IDS = "selectedActionIds";
	public static final String SELECTED_USER_IDS= "selectedUserIds";
	public static final String SELECTED_SITE_IDS = "selectedSiteIds";
	public static final String SELECTED_ROLE_IDS = "selectedRoleIds";
	public static final String SELECTED_CP_IDS = "selectedCPIds";
	public static final String ERROR_MESSAGE_FOR_ROLE = "errorMessageForRole";
	public static final String ERROR_MESSAGE_FOR_SITE = "errorMessageForSite";
	public static final String ERROR_MESSAGE_FOR_USER = "errorMessageForUser";
	public static final String ERROR_MESSAGE_FOR_CP = "errorMessageForCP";
	public static final String ERROR_MESSAGE_FOR_ACTION = "errorMessageForPrivilege";
	public static final String DEFAULT_ROLE = "Default role";
	public static final String PAGEOF_ASSIGN_PRIVILEGE = "pageOfAssignPrivilegePage";
	public static final String ALL_DEFAULT_PRIVILEGES= "All Default Privileges";
	public static final String ALL_DEFAULT_USERS = "All Default Users";
	
	
	//Constants for default column names to be shown for query result.
	public static final String[] DEFAULT_SPREADSHEET_COLUMNS = {
//	        	QUERY_RESULTS_PARTICIPANT_ID,QUERY_RESULTS_COLLECTION_PROTOCOL_ID,
//	        	QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID,QUERY_RESULTS_SPECIMEN_COLLECTION_GROUP_ID,
//	        	QUERY_RESULTS_SPECIMEN_ID,QUERY_RESULTS_SPECIMEN_TYPE
	        "IDENTIFIER","TYPE","ONE_DIMENSION_LABEL"
	};
	
	//Query results edit constants - MakeEditableAction.
	public static final String EDITABLE = "editable";
	
	//URL paths for Applet in TreeView.jsp
	public static final String QUERY_TREE_APPLET = "edu/wustl/common/treeApplet/TreeApplet.class";
	public static final String APPLET_CODEBASE = "Applet";
	
	//Shopping Cart
	public static final String SHOPPING_CART = "shoppingCart";
	public static final String QUERY_SHOPPING_CART = "queryShoppingCart";
	public static final String DELIMETER = ",";
	
	public static final int SELECT_OPTION_VALUE = -1;
	
	public static final String [] TIME_HOUR_AMPM_ARRAY = {"AM","PM"}; 
	
//	Constants required in CollectionProtocol.jsp Page
	public static final String COLLECTIONPROTOCOL_SEARCH_ACTION = "CollectionProtocolSearch.do";
	public static final String COLLECTIONPROTOCOL_ADD_ACTION = "CollectionProtocolAdd.do";
	public static final String COLLECTIONPROTOCOL_EDIT_ACTION = "CollectionProtocolEdit.do";

//	Constants required in DistributionProtocol.jsp Page
	public static final String DISTRIBUTIONPROTOCOL_SEARCH_ACTION = "DistributionProtocolSearch.do";
	public static final String DISTRIBUTIONPROTOCOL_ADD_ACTION = "DistributionProtocolAdd.do";
	public static final String DISTRIBUTIONPROTOCOL_EDIT_ACTION = "DistributionProtocolEdit.do";
	
	//Collection Status values
	public static final String COLLECTION_STATUS_PENDING = "Pending";
	public static final String COLLECTION_STATUS_COLLECTED = "Collected";
	
	public static final String [] ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Active",
	        "Closed",
			"Disabled"
	};

	public static final String [] SCG_COLLECTION_STATUS_VALUES = {
        SELECT_OPTION,
        "Pending",
        "Pending-Partially Complete",
        "Overdue",
		"Overdue-Partially Complete",
		"Complete",
		"Complete-Late",
		"Incomplete",
		"Not Collected"
	};
	
	public static final String [] SPECIMEN_COLLECTION_STATUS_VALUES = {
        SELECT_OPTION,
        "Pending",
        "Overdue",
		"Collected",
		"Not Collected"
	};
	
	public static final String [] SITE_ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Active",
	        "Closed"
	};

	public static final String [] USER_ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Active",
	        "Closed"
	};
	
	public static final String [] APPROVE_USER_STATUS_VALUES = {
	        SELECT_OPTION,
	        APPROVE_USER_APPROVE_STATUS,
	        APPROVE_USER_REJECT_STATUS,
	        APPROVE_USER_PENDING_STATUS,
	};

	public static final String [] REPORTED_PROBLEM_ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Closed",
	        "Pending"
	};

	public static final String [] DISPOSAL_EVENT_ACTIVITY_STATUS_VALUES = {
		"Closed",
		"Disabled"
	};
	
	public static final String TISSUE = "Tissue";
	public static final String FLUID = "Fluid";
	public static final String CELL = "Cell";
	public static final String MOLECULAR = "Molecular";
	
	public static final String [] SPECIMEN_TYPE_VALUES = {
	        SELECT_OPTION,
	        TISSUE,
	        FLUID,
	        CELL,
			MOLECULAR
	};
	
	public static final String [] HOUR_ARRAY = {
			"00",
			"1",
	        "2",
	        "3",
	        "4",
	        "5",
	        "6",
	        "7",
	        "8",
	        "9",
	        "10",
	        "11",
	        "12",
	        "13",
	        "14",
	        "15",
	        "16",
	        "17",
	        "18",
	        "19",
	        "20",
	        "21",
	        "22",
	        "23"
	        
	};

	
	public static final String [] MINUTES_ARRAY = {
	 		"00",
			"1",
			"2",
			"3",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9",
			"10",
			"11",
			"12",
			"13",
			"14",
			"15",
			"16",
			"17",
			"18",
			"19",
			"20",
			"21",
			"22",
			"23",
			"24",
			"25",
			"26",
			"27",
			"28",
			"29",
			"30",
			"31",
			"32",
			"33",
			"34",
			"35",
			"36",
			"37",
			"38",
			"39",
			"40",
			"41",
			"42",
			"43",
			"44",
			"45",
			"46",
			"47",
			"48",
			"49",
			"50",
			"51",
			"52",
			"53",
			"54",
			"55",
			"56",
			"57",
			"58",
			"59"
	};
	
	public static final String UNIT_GM = "gm";
	public static final String UNIT_ML = "ml";
	public static final String UNIT_CC = "cell count";
	public static final String UNIT_MG = "µg";
	public static final String UNIT_CN = "count";
	public static final String UNIT_CL = "cells";
	
	public static final String CDE_NAME_CLINICAL_STATUS = "Clinical Status";
	public static final String CDE_NAME_GENDER = "Gender";
	public static final String CDE_NAME_GENOTYPE = "Genotype";
	public static final String CDE_NAME_SPECIMEN_CLASS = "Specimen";
	public static final String CDE_NAME_SPECIMEN_TYPE = "Specimen Type";
	public static final String CDE_NAME_TISSUE_SIDE = "Tissue Side";
	public static final String CDE_NAME_PATHOLOGICAL_STATUS = "Pathological Status";
	public static final String CDE_NAME_RECEIVED_QUALITY = "Received Quality";
	public static final String CDE_NAME_FIXATION_TYPE = "Fixation Type";
	public static final String CDE_NAME_COLLECTION_PROCEDURE = "Collection Procedure";
	public static final String CDE_NAME_CONTAINER = "Container";
	public static final String CDE_NAME_METHOD = "Method";
	public static final String CDE_NAME_EMBEDDING_MEDIUM = "Embedding Medium";
	public static final String CDE_NAME_BIOHAZARD = "Biohazard";
	public static final String CDE_NAME_ETHNICITY = "Ethnicity";
	public static final String CDE_NAME_RACE = "Race";
	public static final String CDE_VITAL_STATUS = "Vital Status";
	public static final String CDE_NAME_CLINICAL_DIAGNOSIS = "Clinical Diagnosis";
	public static final String CDE_NAME_SITE_TYPE = "Site Type";
	public static final String CDE_NAME_COUNTRY_LIST = "Countries";
	public static final String CDE_NAME_STATE_LIST = "States";
	public static final String CDE_NAME_HISTOLOGICAL_QUALITY = "Histological Quality";
	//Constants for Advanced Search
	public static final String STRING_OPERATORS = "StringOperators";
	public static final String DATE_NUMERIC_OPERATORS = "DateNumericOperators";
	public static final String ENUMERATED_OPERATORS = "EnumeratedOperators";
	public static final String MULTI_ENUMERATED_OPERATORS = "MultiEnumeratedOperators";
	public static final int CHARACTERS_IN_ONE_LINE = 110;
	public static final String SINGLE_QUOTE_ESCAPE_SEQUENCE = "&#096;";
	public static final String COLUMN_NAMES = "columnNames";
	public static final String INDEX = "index";
	
	public static final String [] STORAGE_STATUS_ARRAY = {
	        SELECT_OPTION,
			"CHECK IN",
			"CHECK OUT"
	};

	
	// constants for Data required in query
	public static final String ALIAS_NAME_TABLE_NAME_MAP="objectTableNames";
	public static final String SYSTEM_IDENTIFIER_COLUMN_NAME = "IDENTIFIER";
	public static final String NAME = "name";
	
	
	public static final String EVENT_PARAMETERS[] = {	Constants.SELECT_OPTION,
							"Cell Specimen Review",	"Check In Check Out", "Collection",
							"Disposal", "Embedded", "Fixed", "Fluid Specimen Review",
							"Frozen", "Molecular Specimen Review", "Procedure", "Received",
							"Spun", "Thaw", "Tissue Specimen Review", "Transfer" };
	
	public static final String EVENT_PARAMETERS_COLUMNS[] = { "Identifier",
											"Event Parameter", "User", "Date / Time", "PageOf"};
	
	public static final String DERIVED_SPECIMEN_COLUMNS[] = { "Label",
			"Class", "Type", "Quantity", "rowSelected"};
	
	public static final String [] SHOPPING_CART_COLUMNS = {"","Identifier", 
													"Type", "Subtype", "Tissue Site", "Tissue Side", "Pathological Status"}; 
	
	//Constants required in AssignPrivileges.jsp
	public static final String ASSIGN = "assignOperation";
	public static final String PRIVILEGES = "privileges";
	public static final String OBJECT_TYPES = "objectTypes";
	public static final String OBJECT_TYPE_VALUES = "objectTypeValues";
	public static final String RECORD_IDS = "recordIds";
	public static final String ATTRIBUTES = "attributes";
	public static final String GROUPS = "groups";
	public static final String USERS_FOR_USE_PRIVILEGE = "usersForUsePrivilege";
	public static final String USERS_FOR_READ_PRIVILEGE = "usersForReadPrivilege";
	public static final String ASSIGN_PRIVILEGES_ACTION = "AssignPrivileges.do";
	public static final int CONTAINER_IN_ANOTHER_CONTAINER = 2;
	    /**
     * @param id
     * @return
     */
    public static String getUserPGName(Long identifier)
    {
        if(identifier == null)
	    {
	        return "USER_";
	    }
	    return "USER_"+identifier;
    }

    /**
     * @param id
     * @return
     */
    public static String getUserGroupName(Long identifier)
    {
        if(identifier == null)
	    {
	        return "USER_";
	    }
	    return "USER_"+identifier;
    }
	
	//Mandar 25-Apr-06 : bug 1414 : Tissue units as per type
	// tissue types with unit= count
	public static final String FROZEN_TISSUE_BLOCK = "Frozen Tissue Block";	// PREVIOUS FROZEN BLOCK 
	public static final String FROZEN_TISSUE_SLIDE = "Frozen Tissue Slide";	// SLIDE	 
	public static final String FIXED_TISSUE_BLOCK = "Fixed Tissue Block";	// PARAFFIN BLOCK	 
	public static final String NOT_SPECIFIED = "Not Specified";
	
	public static final String WITHDRAWN = "Withdrawn";
	// tissue types with unit= g
	public static final String FRESH_TISSUE = "Fresh Tissue";			 
	public static final String FROZEN_TISSUE = "Frozen Tissue";			 
	public static final String FIXED_TISSUE = "Fixed Tissue";			 
	public static final String FIXED_TISSUE_SLIDE = "Fixed Tissue Slide";
	//tissue types with unit= cc
	public static final String MICRODISSECTED = "Microdissected"; 
	
//	 constants required for Distribution Report
	public static final String CONFIGURATION_TABLES = "configurationTables";
	public static final String DISTRIBUTION_TABLE_AlIAS[] = {"CollectionProtReg","Participant","Specimen",
															 "SpecimenCollectionGroup","DistributedItem"};
	public static final String TABLE_COLUMN_DATA_MAP = "tableColumnDataMap";
	public static final String CONFIGURE_RESULT_VIEW_ACTION = "ConfigureResultView.do";
	public static final String TABLE_NAMES_LIST = "tableNamesList";
	public static final String COLUMN_NAMES_LIST = "columnNamesList";
	public static final String SPECIMEN_COLUMN_NAMES_LIST = "specimenColumnNamesList";	
	public static final String DISTRIBUTION_ID = "distributionId";
	public static final String CONFIGURE_DISTRIBUTION_ACTION = "ConfigureDistribution.do";
	public static final String DISTRIBUTION_REPORT_ACTION = "DistributionReport.do";
	public static final String ARRAY_DISTRIBUTION_REPORT_ACTION = "ArrayDistributionReport.do";
	
	public static final String DISTRIBUTION_REPORT_SAVE_ACTION="DistributionReportSave.do";
	public static final String ARRAY_DISTRIBUTION_REPORT_SAVE_ACTION="ArrayDistributionReportSave.do";
	
	//bug#4981 :kalpana
	public static final String SELECTED_COLUMNS[] = {"Specimen.LABEL.Label : Specimen",
												    "AbstractSpecimen.SPECIMEN_TYPE.Type : Specimen",
													"SpecimenCharacteristics.TISSUE_SITE.Tissue Site : Specimen",
													"SpecimenCharacteristics.TISSUE_SIDE.Tissue Side : Specimen",
													"AbstractSpecimen.PATHOLOGICAL_STATUS.Pathological Status : Specimen",
													"DistributedItem.QUANTITY.Quantity : Distribution"};
													//"SpecimenCharacteristics.PATHOLOGICAL_STATUS.Pathological Status : Specimen",
													
	public static final String SPECIMEN_IN_ARRAY_SELECTED_COLUMNS[] = {
		"Specimen.LABEL.Label : Specimen",
		"Specimen.BARCODE.barcode : Specimen",
		"SpecimenArrayContent.PositionDimensionOne.PositionDimensionOne : Specimen",
		"SpecimenArrayContent.PositionDimensionTwo.PositionDimensionTwo : Specimen",
		"Specimen.CLASS.CLASS : Specimen",
		"Specimen.TYPE.Type : Specimen",
		"SpecimenCharacteristics.TISSUE_SIDE.Tissue Side : Specimen",
		"SpecimenCharacteristics.TISSUE_SITE.Tissue Site : Specimen",
	};

	public static final String ARRAY_SELECTED_COLUMNS[] = {
		"SpecimenArray.Name.Name : SpecimenArray",
		"Container.barcode.Barcode : SpecimenArray",
		"ContainerType.name.ArrayType : ContainerType",
		"Container.PositionDimensionOne.Position One: Container",
		"Container.PositionDimensionTwo.Position Two: Container",
		"Container.CapacityOne.Dimension One: Container",
		"Container.CapacityTwo.Dimension Two: Container",
		"ContainerType.SpecimenClass.specimen class : ContainerType",
		"ContainerType.SpecimenTypes.specimen Types : ContainerType",
		"Container.Comment.comment: Container",
    };

												
	public static final String SPECIMEN_ID_LIST = "specimenIdList";
	public static final String DISTRIBUTION_ACTION = "Distribution.do?pageOf=pageOfDistribution";
	public static final String DISTRIBUTION_REPORT_NAME = "Distribution Report.csv";
	public static final String DISTRIBUTION_REPORT_FORM="distributionReportForm";
	public static final String DISTRIBUTED_ITEMS_DATA = "distributedItemsData";
	public static final String DISTRIBUTED_ITEM = "DistributedItem";
	//constants for Simple Query Interface Configuration
	public static final String CONFIGURE_SIMPLE_QUERY_ACTION = "ConfigureSimpleQuery.do";
	public static final String CONFIGURE_SIMPLE_QUERY_VALIDATE_ACTION = "ConfigureSimpleQueryValidate.do";
	public static final String CONFIGURE_SIMPLE_SEARCH_ACTION = "ConfigureSimpleSearch.do";
	public static final String SIMPLE_SEARCH_ACTION = "SimpleSearch.do";
	public static final String SIMPLE_SEARCH_AFTER_CONFIGURE_ACTION = "SimpleSearchAfterConfigure.do";
	public static final String PAGEOF_DISTRIBUTION = "pageOfDistribution";
	public static final String RESULT_VIEW_VECTOR = "resultViewVector";
	public static final String SPECIMENT_VIEW_ATTRIBUTE = "defaultViewAttribute";
	//public static final String SIMPLE_QUERY_COUNTER = "simpleQueryCount";
	
	public static final String UNDEFINED = "Undefined";
	public static final String UNKNOWN = "Unknown";
	public static final String UNSPECIFIED = "Unspecified";
	public static final String NOTSPECIFIED = "Not Specified";
	
	public static final String SEARCH_RESULT = "SearchResult.csv";
	
//	Mandar : LightYellow and Green colors for CollectionProtocol SpecimenRequirements. Bug id: 587 
//	public static final String ODD_COLOR = "#FEFB85";
//	public static final String EVEN_COLOR = "#A7FEAB";
//	Light and dark shades of GREY.
	public static final String ODD_COLOR = "#E5E5E5";
	public static final String EVEN_COLOR = "#F7F7F7"; 
		
	
	
	// TO FORWARD THE REQUEST ON SUBMIT IF STATUS IS DISABLED
	public static final String BIO_SPECIMEN = "/ManageBioSpecimen.do";
	public static final String ADMINISTRATIVE = "/ManageAdministrativeData.do";
	public static final String PARENT_SPECIMEN_ID = "parentSpecimenId";
	public static final String COLLECTION_REGISTRATION_ID = "collectionProtocolId";
	
	public static final String FORWARDLIST = "forwardToList";
	public static final String [][] SPECIMEN_FORWARD_TO_LIST = {
			{"Submit",				"success"},
			{"Derive",			"createNew"},
			{"Add Events",				"eventParameters"},
			{"More",	"sameCollectionGroup"},
			{"Distribute", "distribution" },
			{"Derive Multiple", "deriveMultiple"}
	};
	public static final String [] SPECIMEN_BUTTON_TIPS = {
		"Submit only",
		"Submit and derive",
		"Submit and add events",
		"Submit and add more to same group",
		"Submit and distribute",
		"Submit and derive multiple"
	};

	public static final String [][] SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST = {
			{"Submit",				"success"},
			{"Add Specimen",			"createNewSpecimen"},
			{"Add Multiple Specimens", "createMultipleSpecimen"}
	};
	
	public static final String [][] PROTOCOL_REGISTRATION_FORWARD_TO_LIST = {
			{"Submit",							"success"},
			{"Specimen Collection Group",			"createSpecimenCollectionGroup"}
	};

	public static final String [][] PARTICIPANT_FORWARD_TO_LIST = {
			{"Register Participant","success"},
			{"Submit",	"createParticipantRegistration"},
			{"Specimen Collection Group", "specimenCollectionGroup"},
			{"Submit", "pageOfParticipantCPQuery"}
			
	};
	
	public static final String [][] STORAGE_TYPE_FORWARD_TO_LIST = {
			{"Submit",					"success"},
			{"Add Container",	"storageContainer"}
	};
	
	//Constants Required for Advance Search
	//Tree related
	//public static final String PARTICIPANT ='Participant';
	public static final String[] ADVANCE_QUERY_TREE_HEIRARCHY={ //Represents the Advance Query tree Heirarchy.
			Constants.PARTICIPANT,
			Constants.COLLECTION_PROTOCOL,
			Constants.SPECIMEN_COLLECTION_GROUP,
			Constants.SPECIMEN
			};
	public static final String MENU_COLLECTION_PROTOCOL ="Collection Protocol";
	public static final String MENU_SPECIMEN_COLLECTION_GROUP ="Specimen Collection Group";
	public static final String MENU_DISTRIBUTION_PROTOCOL = "Distribution Protocol";
	
	public static final String SPECIMEN_COLLECTION_GROUP ="SpecimenCollectionGroup";
	public static final String DISTRIBUTION = "Distribution";
	public static final String DISTRIBUTION_PROTOCOL = "DistributionProtocol";
	public static final String CP = "CP";
	public static final String SCG = "SCG";
	public static final String D = "D";
	public static final String DP = "DP";
	public static final String C = "C";
	public static final String S = "S";
	public static final String P = "P";
	public static final String ADVANCED_CONDITIONS_QUERY_VIEW = "advancedCondtionsQueryView";
	public static final String ADVANCED_SEARCH_ACTION = "AdvanceSearch.do";
	public static final String ADVANCED_SEARCH_RESULTS_ACTION = "AdvanceSearchResults.do";
	public static final String CONFIGURE_ADVANCED_SEARCH_RESULTS_ACTION = "ConfigureAdvanceSearchResults.do";
	public static final String ADVANCED_QUERY_ADD = "Add";
	public static final String ADVANCED_QUERY_EDIT = "Edit";
	public static final String ADVANCED_QUERY_DELETE = "Delete";
	public static final String ADVANCED_QUERY_OPERATOR = "Operator";
	public static final String ADVANCED_QUERY_OR = "OR";
	public static final String ADVANCED_QUERY_AND = "pAND";
	public static final String EVENT_CONDITIONS = "eventConditions";
	
	public static final String IDENTIFIER_COLUMN_ID_MAP = "identifierColumnIdsMap";
	public static final String PAGEOF_PARTICIPANT_QUERY_EDIT= "pageOfParticipantQueryEdit";
	public static final String PAGEOF_COLLECTION_PROTOCOL_QUERY_EDIT= "pageOfCollectionProtocolQueryEdit";
	public static final String PAGEOF_SPECIMEN_COLLECTION_GROUP_QUERY_EDIT= "pageOfSpecimenCollectionGroupQueryEdit";
	public static final String PAGEOF_SPECIMEN_QUERY_EDIT= "pageOfSpecimenQueryEdit";
	public static final String PARTICIPANT_COLUMNS = "particpantColumns";
	public static final String COLLECTION_PROTOCOL_COLUMNS = "collectionProtocolColumns";
	public static final String SPECIMEN_COLLECTION_GROUP_COLUMNS = "SpecimenCollectionGroupColumns";
	public static final String SPECIMEN_COLUMNS = "SpecimenColumns";
	public static final String USER_ID_COLUMN = "USER_ID";
	
	public static final String GENERIC_SECURITYMANAGER_ERROR = "The Security Violation error occured during a database operation. Please report this problem to the adminstrator";
	
	public static final String BOOLEAN_YES = "Yes";
	public static final String BOOLEAN_NO = "No";
	
	public static final String PACKAGE_DOMAIN = "edu.wustl.catissuecore.domain";
	
	//Constants for isAuditable and isSecureUpdate required for Dao methods in Bozlogic
	public static final boolean IS_AUDITABLE_TRUE = true;
	public static final boolean IS_SECURE_UPDATE_TRUE = true;
	public static final boolean HAS_OBJECT_LEVEL_PRIVILEGE_FALSE = false;
	
	//Constants for HTTP-API
	public static final String CONTENT_TYPE = "CONTENT-TYPE";
	
	// For StorageContainer isFull status
	public static final String IS_CONTAINER_FULL_LIST = "isContainerFullList";
	public static final String [] IS_CONTAINER_FULL_VALUES = {
	        SELECT_OPTION,
	        "True",
	        "False"
	};
	
	public static final String STORAGE_CONTAINER_DIM_ONE_LABEL = "oneDimLabel";
	public static final String STORAGE_CONTAINER_DIM_TWO_LABEL = "twoDimLabel";
	
	//public static final String SPECIMEN_TYPE_TISSUE = "Tissue";
	//public static final String SPECIMEN_TYPE_FLUID = "Fluid";
	//public static final String SPECIMEN_TYPE_CELL = "Cell";
	//public static final String SPECIMEN_TYPE_MOL = "Molecular";
    public static final String SPECIMEN_TYPE_COUNT = "Count";
    public static final String SPECIMEN_TYPE_QUANTITY = "Quantity";
    public static final String SPECIMEN_TYPE_DETAILS = "Details";
    public static final String SPECIMEN_COUNT = "totalSpecimenCount";
    public static final String TOTAL = "Total";
    public static final String SPECIMENS = "Specimens";

	//User Roles
	public static final String TECHNICIAN = "Technician";
	public static final String SUPERVISOR = "Supervisor";
	public static final String SCIENTIST = "Scientist";
	
	public static final String CHILD_CONTAINER_TYPE = "childContainerType";
	public static final String UNUSED = "Unused";
	public static final String TYPE = "Type";
	
	//Mandar: 28-Apr-06 Bug 1129
	public static final String DUPLICATE_SPECIMEN="duplicateSpecimen";
	
	
	//Constants required in ParticipantLookupAction
	public static final String PARTICIPANT_MRN_COL_NAME="partMRNColName";
	public static final String PARTICIPANT_NAME_SEPARATOR="~";
	public static final String PARTICIPANT_NAME_HEADERLABEL="Name";
	public static final String PARTICIPANT_LOOKUP_ALGO_FOR_SPR="ParticipantLookupAlgoForSPR";
	public static final String PARTICIPANT_LOOKUP_PARAMETER="ParticipantLookupParameter";
	public static final String PARTICIPANT_LOOKUP_CUTOFF="lookup.cutoff";
	public static final String PARTICIPANT_LOOKUP_ALGO="ParticipantLookupAlgo";
	public static final String PARTICIPANT_LOOKUP_SUCCESS="success";
	public static final String PARTICIPANT_ADD_FORWARD="participantAdd";
	public static final String PARTICIPANT_SYSTEM_IDENTIFIER="IDENTIFIER";
	public static final String PARTICIPANT_LAST_NAME="LAST_NAME";
	public static final String PARTICIPANT_FIRST_NAME="FIRST_NAME";
	public static final String PARTICIPANT_MIDDLE_NAME="MIDDLE_NAME";
	public static final String PARTICIPANT_BIRTH_DATE="BIRTH_DATE";
	public static final String PARTICIPANT_DEATH_DATE="DEATH_DATE";
	public static final String PARTICIPANT_VITAL_STATUS="VITAL_STATUS";
	public static final String PARTICIPANT_GENDER="GENDER";
	public static final String PARTICIPANT_SEX_GENOTYPE="SEX_GENOTYPE";
	public static final String PARTICIPANT_RACE="RACE";
	public static final String PARTICIPANT_ETHINICITY="ETHINICITY";
	public static final String PARTICIPANT_SOCIAL_SECURITY_NUMBER="SOCIAL_SECURITY_NUMBER";
	public static final String PARTICIPANT_PROBABLITY_MATCH="Probability";
	public static final String PARTICIPANT_MEDICAL_RECORD_NO="MEDICAL_RECORD_NUMBER";
	public static final String PARTICIPANT_SSN_EXACT="SSNExact";
	public static final String PARTICIPANT_SSN_PARTIAL="SSNPartial";
	public static final String PARTICIPANT_PMI_EXACT="PMIExact";
	public static final String PARTICIPANT_PMI_PARTIAL="PMIPartial";
	public static final String PARTICIPANT_DOB_EXACT="DOBExact";
	public static final String PARTICIPANT_DOB_PARTIAL="DOBPartial";
	public static final String PARTICIPANT_LAST_NAME_EXACT="LastNameExact";
	public static final String PARTICIPANT_LAST_NAME_PARTIAL="LastNamePartial";
	public static final String PARTICIPANT_FIRST_NAME_EXACT="NameExact";
	public static final String PARTICIPANT_FIRST_NAME_PARTIAL="NamePartial";
	public static final String PARTICIPANT_MIDDLE_NAME_EXACT="MiddleNameExact";
	public static final String PARTICIPANT_MIDDLE_NAME_PARTIAL="MiddleNamePartial";
	public static final String PARTICIPANT_GENDER_EXACT="GenderExact";
	public static final String PARTICIPANT_RACE_EXACT="RaceExact";
	public static final String PARTICIPANT_RACE_PARTIAL="RacePartial";
	public static final String PARTICIPANT_BONUS="Bonus";
	public static final String PARTICIPANT_TOTAL_POINTS="TotalPoints";
	public static final String PARTICIPANT_MATCH_CHARACTERS_FOR_LAST_NAME="MatchCharactersForLastName";
	
	// Constants for SPR
	public static final String SPR_LOOKUP_PARAMETER="SPRLookupParameter";
	public static final String SPR_SSN_EXACT="SPRSSNExact";
	public static final String SPR_SSN_PARTIAL="SPRSSNPartial";
	public static final String SPR_PMI_EXACT="SPRPMIExact";
	public static final String SPR_PMI_PARTIAL="SPRPMIPartial";
	public static final String SPR_DOB_EXACT="SPRDOBExact";
	public static final String SPR_DOB_PARTIAL="SPRDOBPartial";
	public static final String SPR_LAST_NAME_EXACT="SPRLastNameExact";
	public static final String SPR_LAST_NAME_PARTIAL="SPRLastNamePartial";
	public static final String SPR_FIRST_NAME_EXACT="SPRNameExact";
	public static final String SPR_FIRST_NAME_PARTIAL="SPRNamePartial";
	public static final String SPR_CUT_OFF="SPRCutoff";
	
	public static final String SPR_THRESHOLD1="SPRThreshold1";
	public static final String SPR_THRESHOLD2="SPRThreshold2";
	
	// end here
	
	//Constants for integration of caTies and CAE with caTissue Core
	public static final String LINKED_DATA = "linkedData";
	public static final String APPLICATION_ID = "applicationId";
	public static final String CATIES = "caTies";
	public static final String CAE = "cae";
	public static final String EDIT_TAB_LINK = "editTabLink";
	public static final String CATIES_PUBLIC_SERVER_NAME = "CaTiesPublicServerName";
	public static final String CATIES_PRIVATE_SERVER_NAME = "CaTiesPrivateServerName";
	
	//Constants for StorageContainerMap Applet
	public static final String CONTAINER_STYLEID = "containerStyleId";
	public static final String CONTAINER_STYLE = "containerStyle";
	public static final String XDIM_STYLEID = "xDimStyleId";
	public static final String YDIM_STYLEID = "yDimStyleId";
	
	public static final String SELECTED_CONTAINER_NAME="selectedContainerName";
	public static final String CONTAINERID="containerId";
	public static final String POS1="pos1";
	public static final String POS2="pos2";
	
	
	//Constants for QuickEvents
	public static final String EVENT_SELECTED = "eventSelected";
	
	//Constant for SpecimenEvents page.
	public static final String EVENTS_TITLE_MESSAGE = "Existing events for the specimen with label {0}";
	public static final String SURGICAL_PATHOLOGY_REPORT = "Surgical Pathology Report";
	public static final String CLINICAL_ANNOTATIONS = "Clinical Annotations";
	
	//Constants for Specimen Collection Group name- new field 
	public static final String RESET_NAME ="resetName";
	
	// Labels for Storage Containers
	public static final String[] STORAGE_CONTAINER_LABEL = {" Name"," Pos1"," Pos2"};
	//Constans for Any field
	public static final String HOLDS_ANY = "--All--";
	
	//Constants : Specimen -> lineage
	public static final String NEW_SPECIMEN = "New";
	public static final String DERIVED_SPECIMEN = "Derived";
	public static final String ALIQUOT = "Aliquot";
	
	//Constant for length of messageBody in Reported problem page  
	public static final int MESSAGE_LENGTH = 500;
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
	public static final String ALL = "All";
	public static final String NONE = "None";
	
	//constant for pagination data list
	public static final String PAGINATION_DATA_LIST = "paginationDataList";
	
	public static final int SPECIMEN_DISTRIBUTION_TYPE = 1;
	public static final int SPECIMEN_ARRAY_DISTRIBUTION_TYPE = 2;

	public static final int BARCODE_BASED_DISTRIBUTION = 1;
	public static final int LABEL_BASED_DISTRIBUTION = 2;
	public static final String DISTRIBUTION_TYPE_LIST = "DISTRIBUTION_TYPE_LIST";
	public static final String DISTRIBUTION_BASED_ON = "DISTRIBUTION_BASED_ON";
	public static final String SYSTEM_LABEL = "label";
	public static final String SYSTEM_BARCODE = "barcode";
	public static final String SYSTEM_NAME = "name";
	
	//Mandar : 05Sep06 Array for multiple specimen field names
    public static final String DERIVED_OPERATION = "derivedOperation";
	public static final String [] MULTIPLE_SPECIMEN_FIELD_NAMES = {
	        "Collection Group",
			"Parent ID",
			"Name",
			"Barcode",
			"Class",
			"Type",
			"Tissue Site",
			"Tissue Side",
			"Pathological Status",
			"Concentration",
			"Quantity",
			"Storage Location",
			"Comments",
			"Events",
			"External Identifier",
			"Biohazards"
//			"Derived",
//			"Aliquots"
	};
	
	public static final String PAGEOF_MULTIPLE_SPECIMEN = "pageOfMultipleSpecimen";
	public static final String PAGEOF_MULTIPLE_SPECIMEN_MAIN = "pageOfMultipleSpecimenMain";
	public static final String MULTIPLE_SPECIMEN_ACTION = "MultipleSpecimen.do";
	public static final String INIT_MULTIPLE_SPECIMEN_ACTION = "InitMultipleSpecimen.do";
	public static final String MULTIPLE_SPECIMEN_APPLET_ACTION = "MultipleSpecimenAppletAction.do";
	public static final String NEW_MULTIPLE_SPECIMEN_ACTION = "NewMultipleSpecimenAction.do";
	public static final String MULTIPLE_SPECIMEN_RESULT = "multipleSpecimenResult";
	public static final String SAVED_SPECIMEN_COLLECTION = "savedSpecimenCollection";
	
	

	
	public static final String [] MULTIPLE_SPECIMEN_FORM_FIELD_NAMES = {
	        "CollectionGroup",
			"ParentID",
			"Name",
			"Barcode",
			"Class",
			"Type",
			"TissueSite",
			"TissueSide",
			"PathologicalStatus",
			"Concentration",
			"Quantity",
			"StorageLocation",
			"Comments",
			"Events",
			"ExternalIdentifier",
			"Biohazards"
//			"Derived",
//			"Aliquots"
	};

	public static final String MULTIPLE_SPECIMEN_MAP_KEY = "MULTIPLE_SPECIMEN_MAP_KEY";
	public static final String MULTIPLE_SPECIMEN_EVENT_MAP_KEY = "MULTIPLE_SPECIMEN_EVENT_MAP_KEY";
	public static final String MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY = "MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY";
	public static final String MULTIPLE_SPECIMEN_BUTTONS_MAP_KEY = "MULTIPLE_SPECIMEN_BUTTONS_MAP_KEY";
	public static final String MULTIPLE_SPECIMEN_LABEL_MAP_KEY = "MULTIPLE_SPECIMEN_LABEL_MAP_KEY";
	
	public static final String DERIVED_FORM = "DerivedForm";
	
	
	public static final String SPECIMEN_ATTRIBUTE_KEY =   "specimenAttributeKey";
	
	public static final String SPECIMEN_CLASS =   "specimenClass";
	public static final String SPECIMEN_CALL_BACK_FUNCTION =   "specimenCallBackFunction";
	
	public static final String APPEND_COUNT = "_count";
	public static final String EXTERNALIDENTIFIER_TYPE = "ExternalIdentifier";
	public static final String BIOHAZARD_TYPE = "BioHazard";
	public static final String COMMENTS_TYPE = "comments";
	public static final String EVENTS_TYPE = "Events";
	
	public static final String MULTIPLE_SPECIMEN_APPLET_NAME = "MultipleSpecimenApplet";
	public static final String INPUT_APPLET_DATA = "inputAppletData";
	
	// Start Specimen Array Applet related constants
	
	public static final String SPECIMEN_ARRAY_APPLET = "edu/wustl/catissuecore/applet/ui/SpecimenArrayApplet.class";
	public static final String SPECIMEN_ARRAY_APPLET_NAME = "specimenArrayApplet";
	
	public static final String SPECIMEN_ARRAY_CONTENT_KEY = "SpecimenArrayContentKey";
	
	public static final String SPECIMEN_LABEL_COLUMN_NAME = "label";
	public static final String SPECIMEN_BARCODE_COLUMN_NAME = "barcode";
	public static final String ARRAY_SPECIMEN_DOES_NOT_EXIST_EXCEPTION_MESSAGE = "Please enter valid specimen for specimen array!!specimen does not exist  ";
	public static final String ARRAY_SPECIMEN_NOT_ACTIVE_EXCEPTION_MESSAGE = "Please enter valid specimen for specimen array!! Specimen is closed/disabled  ";
	public static final String ARRAY_NO_SPECIMEN__EXCEPTION_MESSAGE = "Specimen Array should contain at least one specimen";
	public static final String ARRAY_SPEC_NOT_COMPATIBLE_EXCEPTION_MESSAGE = "Please add compatible specimens to specimen array (belong to same specimen class & specimen types of Array)";
	public static final String ARRAY_MOLECULAR_QUAN_EXCEPTION_MESSAGE = "Please enter quantity for Molecular Specimen ---->";
	/**
	 * Specify the SPECIMEN_ARRAY_LIST as key for specimen array type list 
	 */
	public static final String SPECIMEN_ARRAY_TYPE_LIST = "specimenArrayList";
	public static final String SPECIMEN_ARRAY_CLASSNAME = "edu.wustl.catissuecore.domain.SpecimenArray";
	public static final String SPECIMEN_CLASSNAME = "edu.wustl.catissuecore.domain.Specimen";
	public static final String SPECIMEN_ARRAY_TYPE_CLASSNAME = "edu.wustl.catissuecore.domain.SpecimenArrayType";
	public static final String TISSUE_SPECIMEN = "edu.wustl.catissuecore.domain.TissueSpecimen";
	public static final String MOLECULAR_SPECIMEN  = "edu.wustl.catissuecore.domain.MolecularSpecimen";
	public static final String FLUID_SPECIMEN  = "edu.wustl.catissuecore.domain.FluidSpecimen";
	public static final String CELL_SPECIMEN = "edu.wustl.catissuecore.domain.CellSpecimen";
	
	
	// End
	
	// Common Applet Constants
	public static final String APPLET_SERVER_HTTP_START_STR = "http://";
	public static final String APPLET_SERVER_URL_PARAM_NAME = "serverURL";

	//operators for simple query and advanced query
	public static final String IS_NOT_NULL = "is not null";
	public static final String IS_NULL = "is null";
	public static final String In = "In";
	public static final String Not_In = "Not In";
	public static final String Equals = "Equals";
	public static final String Not_Equals = "Not Equals";
	public static final String Between = "Between";
	public static final String Less_Than = "Less than";
	public static final String Less_Than_Or_Equals = "Less than or Equal to";
	public static final String Greater_Than = "Greater than";
	public static final String Greater_Than_Or_Equals = "Greater than or Equal to";
	public static final String Contains = "Contains";
	public static final String STRATS_WITH = "Starts With";
	public static final String ENDS_WITH = "Ends With";
	public static final String NOT_BETWEEN  = "Not Between";
	
	// Used in array action 
	public static final String ARRAY_TYPE_ANY_VALUE = "2";
	public static final String ARRAY_TYPE_ANY_NAME = "Any";
	// end
	
	// Array Type All Id in table
	public static final short ARRAY_TYPE_ALL_ID = 2; 
	
	// constants required for caching mechanism of ParticipantBizLogic
	
	public static final String MAP_OF_PARTICIPANTS = "listOfParticipants";
	public static final String LIST_OF_REGISTRATION_INFO = "listOfParticipantRegistrations";
	public static final String EHCACHE_FOR_CATISSUE_CORE = "cacheForCaTissueCore";
	public static final String MAP_OF_DISABLED_CONTAINERS = "listOfDisabledContainers";
	public static final String MAP_OF_CONTAINER_FOR_DISABLED_SPECIEN = "listOfContainerForDisabledContainerSpecimen";
	public static final String MAP_OF_CONTAINER_FOR_DISABLED_SPECIENARRAY = "listOfContainerForDisabledContainerSpecimenArray";
	public static final String ADD = "add";
	public static final String EDIT = "edit";
	public static final String ID = "id";
	public static final String FILE = "file";
	public static final String MANAGE_BIO_SPECIMEN_ACTION = "/ManageBioSpecimen.do";
	public static final String CREATE_PARTICIPANT_REGISTRATION = "createParticipantRegistration";
	public static final String CREATE_PARTICIPANT_REGISTRATION_ADD = "createParticipantRegistrationAdd";
	public static final String CREATE_PARTICIPANT_REGISTRATION_EDIT= "createParticipantRegistrationEdit";
	public static final String VIEW_ANNOTATION = "viewAnnotations";
	
	public static final String CAN_HOLD_CONTAINER_TYPE = "holdContainerType";
	public static final String CAN_HOLD_SPECIMEN_CLASS = "holdSpecimenClass";
	public static final String CAN_HOLD_COLLECTION_PROTOCOL = "holdCollectionProtocol";
	public static final String CAN_HOLD_SPECIMEN_ARRAY_TYPE = "holdSpecimenArrayType";
	public static final String COLLECTION_PROTOCOL_ID = "collectionProtocolId";
	public static final String SPECIMEN_CLASS_NAME = "specimeClassName";
	public static final String ENABLE_STORAGE_CONTAINER_GRID_PAGE = "enablePage";
	public static final int ALL_STORAGE_TYPE_ID = 1; //Constant for the "All" storage type, which can hold all container type
	public static final int ALL_SPECIMEN_ARRAY_TYPE_ID = 2;//Constant for the "All" storage type, which can hold all specimen array type
	
	public static final String SPECIMEN_LABEL_CONTAINER_MAP = "Specimen : ";
	public static final String CONTAINER_LABEL_CONTAINER_MAP = "Container : ";
	public static final String SPECIMEN_ARRAY_LABEL_CONTAINER_MAP = "Array : ";
	
	public static final String SPECIMEN_PROTOCOL ="SpecimenProtocol";
	public static final String SPECIMEN_PROTOCOL_SHORT_TITLE ="SHORT_TITLE";
	public static final String SPECIMEN_COLLECTION_GROUP_NAME ="NAME";
	public static final String SPECIMEN_LABEL = "LABEL";
	// Patch ID: Bug#3184_14
	public static final String NUMBER_OF_SPECIMEN = "numberOfSpecimen";
	
	//Constants required for max limit on no. of containers in the drop down
	public static final String CONTAINERS_MAX_LIMIT = "containers_max_limit";
	public static final String EXCEEDS_MAX_LIMIT = "exceedsMaxLimit";
	
	//MultipleSpecimen Constants
	public static final String MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE="multipleSpecimen.ColumnsPerPage";
	public static final String MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION="MultipleSpecimenStorageLocationAdd.do";
	public static final String MULTIPLE_SPECIMEN_STORAGE_LOCATION_AVAILABLE_MAP="locationMap";
	public static final String MULTIPLE_SPECIMEN_STORAGE_LOCATION_SPECIMEN_MAP= "specimenMap";
	public static final String MULTIPLE_SPECIMEN_STORAGE_LOCATION_KEY_SEPARATOR = "$";
	public static final String PAGEOF_MULTIPLE_SPECIMEN_STORAGE_LOCATION = "formSubmitted";
	public static final String MULTIPLE_SPECIMEN_SUBMIT_SUCCESSFUL = "submitSuccessful";
	public static final String MULTIPLE_SPECIMEN_SPECIMEN_ORDER_LIST= "specimenOrderList";
	public static final String MULTIPLE_SPECIMEN_DELETELAST_SPECIMEN_ID = "SpecimenId";
	public static final String MULTIPLE_SPECIMEN_PARENT_COLLECTION_GROUP = "ParentSpecimenCollectionGroup";

	public static final String NO_OF_RECORDS_PER_PAGE="resultView.noOfRecordsPerPage";

	/**
	 * Name: Prafull
	 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
	 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
	 * Changes resultper page options, removed 5000 from the array & added 500 as another option. 
	 */
	public static final int[] RESULT_PERPAGE_OPTIONS = {10,50,100,500,1000};
	
	/**
	 * Specify the SPECIMEN_MAP_KEY field ) used in multiple specimen applet action.  
	 */
	public static final String SPECIMEN_MAP_KEY = "Specimen_derived_map_key";
	
	/**
	 * Specify the SPECIMEN_MAP_KEY field ) used in multiple specimen applet action.  
	 */
	public static final String CONTAINER_MAP_KEY = "container_map_key";
	
	/**
	 * Used to saperate storage container, xPos, yPos
	 */
	public static final String STORAGE_LOCATION_SAPERATOR = "@";
	
	public static final String METHOD_NAME="method";
	public static final String GRID_FOR_EVENTS="eventParameter";
	public static final String GRID_FOR_EDIT_SEARCH="editSearch";
	public static final String GRID_FOR_DERIVED_SPECIMEN="derivedSpecimen";
	/*
	 * used to find whether iframe needs to be reloaded or not
	 */
	public static final String RELOAD ="false";
	public static final String CHILD_CONTAINER_NAME="childContainerName";
	
	//CpBasedSearch Constants
	public static final String CP_QUERY = "CPQuery";
	public static final String CP_QUERY_PARTICIPANT_EDIT_ACTION = "CPQueryParticipantEdit.do";
	public static final String CP_QUERY_PARTICIPANT_ADD_ACTION = "CPQueryParticipantAdd.do";
	public static final String CP_QUERY_SPECIMEN_COLLECTION_GROUP_ADD_ACTION = "CPQuerySpecimenCollectionGroupAdd.do";
	public static final String CP_QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION = "CPQuerySpecimenCollectionGroupEdit.do";
	public static final String CP_AND_PARTICIPANT_VIEW="cpAndParticipantView";
	public static final String DATA_DETAILS_VIEW="dataDetailsView";
	public static final String SHOW_CP_AND_PARTICIPANTS_ACTION="showCpAndParticipants.do";
	public static final String PAGE_OF_CP_QUERY_RESULTS = "pageOfCpQueryResults";
	public static final String CP_LIST = "cpList";
	public static final String CP_ID_TITLE_MAP = "cpIDTitleMap";
	public static final String REGISTERED_PARTICIPANT_LIST = "participantList";
	public static final String PAGE_OF_PARTICIPANT_CP_QUERY = "pageOfParticipantCPQuery";
	public static final String PAGE_OF_SCG_CP_QUERY = "pageOfSpecimenCollectionGroupCPQuery";
	public static final String CP_SEARCH_PARTICIPANT_ID="cpSearchParticipantId";
	public static final String CP_SEARCH_CP_ID="cpSearchCpId";
	public static final String CP_TREE_VIEW = "cpTreeView";
	public static final String CP_TREE_VIEW_ACTION = "showTree.do";
	public static final String PAGE_OF_SPECIMEN_CP_QUERY = "pageOfNewSpecimenCPQuery";
	public static final String CP_QUERY_SPECIMEN_ADD_ACTION = "CPQueryNewSpecimenAdd.do";
	public static final String CP_QUERY_CREATE_SPECIMEN_ACTION = "CPQueryCreateSpecimen.do";
	public static final String CP_QUERY_SPECIMEN_EDIT_ACTION = "CPQueryNewSpecimenEdit.do";
	public static final String PAGE_OF_CREATE_SPECIMEN_CP_QUERY = "pageOfCreateSpecimenCPQuery";
	public static final String PAGE_OF_ALIQUOT_CP_QUERY = "pageOfAliquotCPQuery";
	public static final String PAGE_OF_CREATE_ALIQUOT_CP_QUERY = "pageOfCreateAliquotCPQuery"; 
	public static final String PAGE_OF_ALIQUOT_SUMMARY_CP_QUERY = "pageOfAliquotSummaryCPQuery";
	public static final String CP_QUERY_CREATE_ALIQUOT_ACTION = "CPQueryCreateAliquots.do";
	public static final String CP_QUERY_ALIQUOT_SUMMARY_ACTION = "CPQueryAliquotSummary.do";
	public static final String CP_QUERY_CREATE_SPECIMEN_ADD_ACTION = "CPQueryAddSpecimen.do";
	public static final String PAGE_OF_DISTRIBUTION_CP_QUERY = "pageOfDistributionCPQuery";
	public static final String CP_QUERY_DISTRIBUTION_EDIT_ACTION = "CPQueryDistributionEdit.do";
	public static final String CP_QUERY_DISTRIBUTION_ADD_ACTION = "CPQueryDistributionAdd.do";
	public static final String CP_QUERY_DISTRIBUTION_REPORT_SAVE_ACTION="CPQueryDistributionReportSave.do";
	public static final String CP_QUERY_ARRAY_DISTRIBUTION_REPORT_SAVE_ACTION="CPQueryArrayDistributionReportSave.do";
	public static final String CP_QUERY_CONFIGURE_DISTRIBUTION_ACTION = "CPQueryConfigureDistribution.do";
	public static final String CP_QUERY_DISTRIBUTION_REPORT_ACTION = "CPQueryDistributionReport.do";
	public static final String PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY = "pageOfListSpecimenEventParametersCPQuery";
	public static final String PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS = "pageOfListSpecimenEventParameters";
	public static final String PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY = "pageOfCollectionProtocolRegistrationCPQuery";
	public static final String PAGE_OF_MULTIPLE_SPECIMEN_CP_QUERY = "pageOfMultipleSpecimenCPQuery";
	public static final String CP_QUERY_NEW_MULTIPLE_SPECIMEN_ACTION = "CPQueryNewMultipleSpecimenAction.do";
	public static final String CP_QUERY_MULTIPLE_SPECIMEN_STORAGE_LOCATION_ACTION="CPQueryMultipleSpecimenStorageLocationAdd.do";
	public static final String CP_QUERY_PAGEOF_MULTIPLE_SPECIMEN_STORAGE_LOCATION = "CPQueryformSubmitted";
	public static final String CP_QUERY_COLLECTION_PROTOCOL_REGISTRATION_ADD_ACTION = "CPQueryCollectionProtocolRegistrationAdd.do";
	public static final String CP_QUERY_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "CPQueryCollectionProtocolRegistrationEdit.do";
	public static final String CP_QUERY_PARTICIPANT_LOOKUP_ACTION= "CPQueryParticipantLookup.do";
	public static final String CP_QUERY_BIO_SPECIMEN = "/QueryManageBioSpecimen.do";
	
//Mandar : 15-Jan-07
	public static final String WITHDRAW_RESPONSE_NOACTION= "No Action";
	public static final String WITHDRAW_RESPONSE_DISCARD= "Discard";
	public static final String WITHDRAW_RESPONSE_RETURN= "Return";
	public static final String WITHDRAW_RESPONSE_RESET= "Reset";
	public static final String WITHDRAW_RESPONSE_REASON= "Specimen consents withdrawn";

	public static final String SEARCH_CATEGORY_LIST_SELECT_TAG_NAME="selectCategoryList";
	public static final String SEARCH_CATEGORY_LIST_FUNCTION_NAME="getSelectedEntities";
    
    
    public static final String EDIT_CONDN = "Edit";
	public static final String SPLITTER_STATUS_REQ_PARAM = "SPLITTER_STATUS";
     
    //mulltiple specimen Applet constants
    public static final int VALIDATE_TEXT =1;
    public static final int VALIDATE_COMBO =2;
    public static final int VALIDATE_DATE =3;   
    
    /**
	 * Constants required for maintaining toolTipText for event button on multiple specimen page
	 */
	public static final String TOOLTIP_TEXT="TOOLTIPTEXT";
	public static final String MULTIPLE_SPECIMEN_TOOLTIP_MAP_KEY="multipleSpecimenTooltipMapKey";
	public static final String DEFAULT_TOOLTIP_TEXT="DEFAULTTOOLTIPTEXT";
	
	/**
	 * Patch ID: Bug#3184_15 (for Multiple Specimen)
	 * Description: The following constants are used as key in the Map
	 */
	public static final String KEY_SPECIMEN_CLASS = "SpecimenClass";
	public static final String KEY_SPECIMEN_TYPE = "SpecimenType";
	public static final String KEY_TISSUE_SITE = "TissueSite";
	public static final String KEY_PATHOLOGICAL_STATUS = "PathologicalStatus";
	public static final String KEY_SPECIMEN_REQUIREMENT_PREFIX = "SpecimenRequirement_";
	public static final String KEY_RESTRICTED_VALUES = "RestictedValues";
	public static final String KEY_QUANTITY = "Quantity";
	
	// The following constants are used for multiple specimens applet classes
	public static final String NUMBER_OF_SPECIMEN_REQUIREMENTS = "numberOfSpecimenRequirements";
	public static final String RESTRICT_SCG_CHECKBOX = "restrictSCGCheckbox";
	public static final String ON_COLL_OR_CLASSCHANGE = "onCollOrClassChange";
	public static final String NUMBER_OF_SPECIMENS = "numberOfSpecimens";
	public static final String CHANGE_ON = "changeOn";
	
	// Patch ID: Bug#4180_3
	public static final String EVENT_NAME = "eventName";
	public static final String USER_NAME = "userName";
	public static final String EVENT_DATE = "eventDate";
	public static final String PAGE_OF = "pageOf";
	/**
	 * Patch ID: for Future SCG_15
	 * Description: The following constants are used as id for future scg in tree
	 */
	public static final String FUTURE_SCG = "future";

	
	// Patch ID: SimpleSearchEdit_5 
	// AliasName constants, used in Edit in Simple Search feature.
	public static final String SPREADSHEET_COLUMN_LIST = "spreadsheetColumnList";
	public static final String ALIAS_COLLECTION_PROTOCOL = "CollectionProtocol";
	public static final String ALIAS_BIOHAZARD = "Biohazard";
	public static final String ALIAS_CANCER_RESEARCH_GROUP = "CancerResearchGroup";
	public static final String ALIAS_COLLECTION_PROTOCOL_REG = "CollectionProtReg";
	public static final String ALIAS_DEPARTMENT = "Department";
	public static final String ALIAS_DISTRIBUTION = "Distribution";
	public static final String ALIAS_DISTRIBUTION_PROTOCOL = "DistributionProtocol";
	public static final String ALIAS_DISTRIBUTION_ARRAY = "Distribution_array";
	public static final String ALIAS_INSTITUTE = "Institution";
	public static final String ALIAS_PARTICIPANT = "Participant";
	public static final String ALIAS_SITE = "Site";
	public static final String ALIAS_SPECIMEN = "Specimen";
	public static final String ALIAS_SPECIMEN_ARRAY = "SpecimenArray";
	public static final String ALIAS_SPECIMEN_ARRAY_TYPE = "SpecimenArrayType";
	public static final String ALIAS_SPECIMEN_COLLECTION_GROUP = "SpecimenCollectionGroup";
	public static final String ALIAS_STORAGE_CONTAINER = "StorageContainer";
	public static final String ALIAS_STORAGE_TYPE= "StorageType";
	public static final String ALIAS_USER= "User";
	public static final String PAGE_OF_BIOHAZARD = "pageOfBioHazard";
	public static final String PAGE_OF_CANCER_RESEARCH_GROUP = "pageOfCancerResearchGroup";
	public static final String PAGE_OF_COLLECTION_PROTOCOL = "pageOfCollectionProtocol";
	public static final String PAGE_OF_COLLECTION_PROTOCOL_REG = "pageOfCollectionProtocolRegistration";
	public static final String PAGE_OF_DEPARTMENT = "pageOfDepartment";
	public static final String PAGE_OF_DISTRIBUTION = "pageOfDistribution";
	public static final String PAGE_OF_DISTRIBUTION_PROTOCOL = "pageOfDistributionProtocol";
	public static final String PAGE_OF_DISTRIBUTION_ARRAY = "pageOfArrayDistribution";
	public static final String PAGE_OF_INSTITUTE = "pageOfInstitution";
	public static final String PAGE_OF_PARTICIPANT = "pageOfParticipant";
	
	public static final String PAGE_OF_SITE = "pageOfSite";
	public static final String PAGE_OF_SPECIMEN = "pageOfSpecimen";
	public static final String PAGE_OF_SPECIMEN_ARRAY = "pageOfSpecimenArray";
	public static final String PAGE_OF_SPECIMEN_ARRAY_TYPE = "pageOfSpecimenArrayType";
	public static final String PAGE_OF_SPECIMEN_COLLECTION_GROUP = "pageOfSpecimenCollectionGroup";
	public static final String PAGE_OF_STORAGE_CONTAINER = "pageOfStorageContainer";
	public static final String PAGE_OF_STORAGE_TYPE = "pageOfStorageType";
	public static final String PAGE_OF_USER = "pageOfUserAdmin";
	
	//Patch ID: Bug#4227_3
	public static final String SUBMIT_AND_ADD_MULTIPLE = "submitAndAddMultiple";
	
	//constants for Storage container map radio button identification  Patch id: 4283_3
	public static final int RADIO_BUTTON_VIRTUALLY_LOCATED=1;
	public static final int RADIO_BUTTON_FOR_MAP=3;
//	constant for putting blank screen in the framed pages
	public static final String BLANK_SCREEN_ACTION="blankScreenAction.do";
	
	
	public static final String COLUMN_NAME_SPECIMEN_ID = "specimen.id";
	public static final String PARTICIPANT_MEDICAL_IDENTIFIER="ParticipantMedicalIdentifier:";
	public static final String PARTICIPANT_MEDICAL_IDENTIFIER_SITE_ID="_Site_id";
	public static final String PARTICIPANT_MEDICAL_IDENTIFIER_MEDICAL_NUMBER="_medicalRecordNumber";
	public static final String PARTICIPANT_MEDICAL_IDENTIFIER_ID="_id";
	public static final String COLUMN_NAME_SPECIEMEN_REQUIREMENT_COLLECTION="elements(specimenRequirementCollection)";
	public static final String COLUMN_NAME_PARTICIPANT="participant";
	public static final String ADDNEW_LINK="AddNew";
	public static final String COLUMN_NAME_STORAGE_CONTAINER = "storageContainer";
	public static final String COLUMN_NAME_SCG_CPR_CP_ID = "specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.id";
	public static final String COLUMN_NAME_CPR_CP_ID = "collectionProtocolRegistration.collectionProtocol.id";
	public static final String EQUALS = "=";
	public static final String COLUMN_NAME_SCG_NAME = "specimenCollectionGroup.name";
	public static final String COLUMN_NAME_SPECIMEN = "specimen";
	public static final String COLUMN_NAME_SCG = "specimenCollectionGroup";
	public static final String COLUMN_NAME_CHILDREN = "elements(children)";
	public static final String COLUMN_NAME_SCG_ID="specimenCollectionGroup.id";
	public static final String COLUMN_NAME_PART_MEDICAL_ID_COLL="elements(participantMedicalIdentifierCollection)";
	public static final String COLUMN_NAME_PART_RACE_COLL="elements(raceCollection)";
	public static final String COLUMN_NAME_CPR_COLL="elements(collectionProtocolRegistrationCollection)";
	public static final String COLUMN_NAME_SCG_COLL="elements(specimenCollectionGroupCollection)";
	public static final String COLUMN_NAME_COLL_PROT_EVENT_COLL="elements(collectionProtocolEventCollection)";
	public static final String COLUMN_NAME_CONCEPT_REF_COLL="elements(conceptReferentCollection)";
	public static final String COLUMN_NAME_DEID_REPORT="deIdentifiedSurgicalPathologyReport";
	public static final String COLUMN_NAME_REPORT_SOURCE="reportSource";
	public static final String COLUMN_NAME_TEXT_CONTENT="textContent";
	public static final String COLUMN_NAME_TITLE="title";
	public static final String COLUMN_NAME_PARTICIPANT_ID = "participant.id";
	public static final String COLUMN_NAME_REPORT_SECTION_COLL="elements(reportSectionCollection)";
	public static final String COLUMN_NAME_SCG_SITE="specimenCollectionSite";
	public static final String COLUMN_NAME_STATUS="status";
	public static final String COLUMN_NAME_CPR="collectionProtocolRegistration";
	public static final String COLUMN_NAME_COLL_PROT_EVENT="collectionProtocolEvent";
	public static final String COLUMN_NAME_SCG_CPR_PARTICIPANT_ID="specimenCollectionGroup.collectionProtocolRegistration.participant.id";
	
	//Bug 2833. Field for the length of CP Title
	public static final int COLLECTION_PROTOCOL_TITLE_LENGTH=30; 
	//Bug ID 4794: Field for advance time to warn a user about session expiry
	public static final String SESSION_EXPIRY_WARNING_ADVANCE_TIME = "session.expiry.warning.advanceTime";
  //	Constants required in RequestDetailsPage
	public static final String SUBMIT_REQUEST_DETAILS_ACTION="SubmitRequestDetails.do";
	public static final String REQUEST_HEADER_OBJECT = "requestHeaderObject";
	public static final String SITE_LIST_OBJECT = "siteList";
	public static final String REQUEST_DETAILS_PAGE = "RequestDetails.do";
	public static final String ARRAYREQUEST_DETAILS_PAGE = "ArrayRequests.do";
	public static final String ARRAY_REQUESTS_LIST = "arrayRequestsList";
	public static final String EXISISTINGARRAY_REQUESTS_LIST = "existingArrayRequestDetailsList";
	public static final String DEFINEDARRAY_REQUESTS_LIST = "DefinedRequestDetailsMapList";
	public static final String ITEM_STATUS_LIST="itemsStatusList";
	public static final String ITEM_STATUS_LIST_WO_DISTRIBUTE="itemsStatusListWithoutDistribute";
	public static final String ITEM_STATUS_LIST_FOR_ITEMS_IN_ARRAY="itemsStatusListForItemsInArray";
	public static final String REQUEST_FOR_LIST="requestForList";
	
//	Constants for Order Request Status.
	public static final String ORDER_REQUEST_STATUS_NEW = "New";
	public static final String ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW = "Pending - Protocol Review";
	public static final String ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION = "Pending - Specimen Preparation";
	public static final String ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION = "Pending - For Distribution";
	public static final String ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST = "Rejected - Inappropriate Request";	
	public static final String ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE = "Rejected - Specimen Unavailable";
	public static final String ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE = "Rejected - Unable to Create";
	public static final String ORDER_REQUEST_STATUS_DISTRIBUTED = "Distributed";
	public static final String ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION = "Ready For Array Preparation";
//	Used for tree display in RequestDetails page
	public static final String TREE_DATA_LIST = "treeDataList";
	
	//Constants for Order Status
	public static final String ORDER_STATUS_NEW = "New";
	public static final String ORDER_STATUS_PENDING = "Pending";
	public static final String ORDER_STATUS_REJECTED = "Rejected";
	public static final String ORDER_STATUS_COMPLETED = "Completed";
	
//	Ordering System Status
	public static final String CDE_NAME_REQUEST_STATUS="Request Status";
	public static final String CDE_NAME_REQUESTED_ITEMS_STATUS="Requested Items Status";

	public static final String REQUEST_LIST="requestStatusList";
	public static final String REQUESTED_ITEMS_STATUS_LIST="requestedItemsStatusList";
	public static final String ARRAY_STATUS_LIST="arrayStatusList";
	
	public static final String REQUEST_OBJECT="requestObjectList";
	public static final String REQUEST_DETAILS_LIST="requestDetailsList";
	public static final String ARRAY_REQUESTS_BEAN_LIST="arrayRequestsBeanList";
	
    public static final String SPECIMEN_ORDER_FORM_TYPE = "specimen";
    public static final String ARRAY_ORDER_FORM_TYPE = "specimenArray";
    public static final String PATHOLOGYCASE_ORDER_FORM_TYPE="pathologyCase";
    public static final String REQUESTED_BIOSPECIMENS="RequestedBioSpecimens";
	
    //Constants required in Ordering System.
    public static final String ACTION_ORDER_LIST = "OrderExistingSpecimen.do";  
    public static final String SPECIMEN_TREE_SPECIMEN_ID = "specimenId";
    public static final String SPECIMEN_TREE_SPECCOLLGRP_ID = "specimenCollGrpId";
    public static final String ACTION_REMOVE_ORDER_ITEM = "AddToOrderListSpecimen.do?remove=yes";
    public static final String ACTION_REMOVE_ORDER_ITEM_ARRAY = "AddToOrderListArray.do?remove=yes";
    public static final String ACTION_REMOVE_ORDER_ITEM_PATHOLOGYCASE = "AddToOrderListPathologyCase.do?remove=yes";
    public static final String DEFINEARRAY_REQUESTMAP_LIST = "definedArrayRequestMapList";
    public static final String CREATE_DEFINED_ARRAY = "CreateDefinedArray.do";
    
    public static final String ACTION_SAVE_ORDER_ITEM = "SaveOrderItems.do";

    public static final String ORDERTO_LIST_ARRAY = "orderToListArrayList";
    public static final String ACTION_SAVE_ORDER_ARRAY_ITEM = "SaveOrderArrayItems.do";
    public static final String ACTION_SAVE_ORDER_PATHOLOGY_ITEM="SaveOrderPathologyItems.do";
    
    public static final String ACTION_ADD_ORDER_SPECIMEN_ITEM="AddToOrderListSpecimen.do";
    public static final String ACTION_ADD_ORDER_ARRAY_ITEM="AddToOrderListArray.do";
    public static final String ACTION_ADD_ORDER_PATHOLOGY_ITEM="AddToOrderListPathologyCase.do";
    public static final String ACTION_DEFINE_ARRAY="DefineArraySubmit.do";
    public static final String ACTION_ORDER_SPECIMEN="OrderExistingSpecimen.do";
    public static final String ACTION_ORDER_BIOSPECIMENARRAY="OrderBiospecimenArray.do";
    public static final String ACTION_ORDER_PATHOLOGYCASE="OrderPathologyCase.do";
    
    //Specimen Label generation realted constants 
    public static final String PARENT_SPECIMEN_ID_KEY="parentSpecimenID";
    public static final String PARENT_SPECIMEN_LABEL_KEY="parentSpecimenLabel";
    public static final String SCG_NAME_KEY="SCGName";
    
    //ordering system
    public static final String TAB_INDEX_ID="tabIndexId";
    
    public static final String FORWARD_TO_HASHMAP="forwardToHashMap";
    public static final String SELECTED_COLLECTION_PROTOCOL_ID = "0";
    public static final String LIST_OF_SPECIMEN_COLLECTION_GROUP = "specimenCollectionGroupResponseList";
    public static final String PARTICIPANT_PROTOCOL_ID="participantProtocolId";
    
    // caTIES
	public static final String CONCEPT_REFERENT_CLASSIFICATION_LIST = "conceptRefernetClassificationList";
	public static final String CONCEPT_LIST = "conceptList";
	public static final String[] CATEGORY_HIGHLIGHTING_COLOURS = {"#ff6d5f","#db9fe5","#2dcc00","#69bbf7","#f8e754"};
	public static final String CONCEPT_BEAN_LIST = "conceptBeanList";
 
    //Admin View
	public static final String 	IDENTIFIER_NO="#";
	public static final String 	REQUEST_DATE="Request Date";
	public static final String 	USER_NAME_ADMIN_VIEW="User Name";
	public static final String 	SCG_NAME="Specimen Collection Group";
	public static final String 	ACCESSION_NO="Accession Number";
	public static final String  PAGEOF_REVIEW_SPR="pageOfReviewSPR";
	public static final String  PAGEOF_QUARANTINE_SPR="pageOfQuarantineSPR";
	public static final String  SITE="Site";
	public static final String  REQUEST_FOR="requestFor";
	public static final String  REPORT_ACTION="reportAction";
	public static final String  REPORT_STATUS_LIST="reportStatusList";
	public static final String  COLUMN_LIST="columnList";
	public static final String  NO_PENDING_REQUEST="noPendingRequest";

	//Surgical Pathology Report UI constants
	public static final String VIEW_SPR_ACTION="ViewSurgicalPathologyReport.do";
	public static final String SPR_EVENT_PARAM_ACTION="SurgicalPathologyReportEventParam.do";
	public static final String VIEW_SURGICAL_PATHOLOGY_REPORT="viewSPR";
	public static final String PAGEOF_SPECIMEN_COLLECTION_GROUP="pageOfSpecimenCollectionGroup";
	public static final String PAGEOF_PARTICIPANT="pageOfParticipant";
	public static final String PAGEOF_NEW_SPECIMEN="pageOfNewSpecimen";
	public static final String REVIEW="review";
	public static final String QUARANTINE="quarantine";
	public static final String COMMENT_STATUS_RENDING="PENDING";
	public static final String COMMENT_STATUS_REVIEWED="REVIEWED";
	public static final String COMMENT_STATUS_REPLIED="REPLIED";
	public static final String COMMENT_STATUS_NOT_REVIEWED="NOT_REVIEWED";
	public static final String COMMENT_STATUS_QUARANTINED="QUARANTINED";
	public static final String COMMENT_STATUS_NOT_QUARANTINED="DEQUARANTINED";
	public static final String ROLE_ADMINISTRATOR="Administrator";
	public static final String REPORT_LIST="reportIdList";
	public static final String QUARANTINE_REQUEST="QUARANTINEREQUEST";
	public static final String IDENTIFIED_REPORT_NOT_FOUND_MSG="Indentified Report Not Found!";
	public static final String DEID_REPORT_NOT_FOUND_MSG="De-Indentified Report Not Found!";

//  Local extensions constants
    public static final String LOCAL_EXT="localExt";
    public static final String LINK="link";
    public static final String LOAD_INTEGRATION_PAGE="loadIntegrationPage";
    public static final String DEFINE_ENTITY = "defineEntity";
    public static final String SELECTED_ENTITY_ID="selectedStaticEntityId";
    public static final String CONTAINER_NAME="containerName"; 
    public static final String STATIC_ENTITY_NAME = "staticEntityName";
    public static final String SPREADSHEET_DATA_GROUP="spreadsheetDataGroup"; 
    public static final String SPREADSHEET_DATA_ENTITY="spreadsheetDataEntity";
    public static final String SPREADSHEET_DATA_RECORD="spreadsheetDataRecord";
    public static final String TITLE = "title";
    
       
    //clinportal constants
    public static final int CLINICALSTUDY_FORM_ID = 65;
    public static final int CLINICAL_STUDY_REGISTRATION_FORM_ID=66;
    
    // Save query constant
    public static final int CATISSUECORE_QUERY_INTERFACE_ID = 67;
    
    //Ordering constants
    public static final String ARRAY_NAME = "array";
	
    
    //Query Shopping cart constants
    public static final String SHOPPING_CART_FILE_NAME = "MyList.csv";
    public static final String DUPLICATE_OBJECT = "duplicateObject";
    public static final String DIFFERENT_VIEW_IN_CART = "differentCartView";
    public static final String IS_SPECIMENID_PRESENT = "isSpecimenIdPresent";
    public static final String IS_SPECIMENARRAY_PRESENT = "isSpecimenArrayPresent";
    public static final String VALIDATION_MESSAGE_FOR_ORDERING = "validationMessageForOrdering";
    public static final String UPDATE_SESSION_DATA = "updateSessionData";
    public static final String IS_LIST_EMPTY = "isListEmpty";
    public static final String DOT_CSV = ".csv";
    public static final String APPLICATION_DOWNLOAD = "application/download";
    
    //For SNT
    public static final String IS_CONTAINER_PRESENT = "isContainerPresent";

    // Constants for CP Based Entry
    public static final String COLLECTION_PROTOCOL_EVENT_ID = "Event_Id";
	public  static final String SPECIMEN_LIST_SESSION_MAP = "SpecimenListBean";
	public static final String COLLECTION_PROTOCOL_EVENT_SESSION_MAP = "collectionProtocolEventMap";
	public static final String COLLECTION_PROTOCOL_SESSION_BEAN = "collectionProtocolBean";
	public static final String STORAGE_CONTAINER_SESSION_BEAN = "storageContainerBean";
	public static final String EDIT_SPECIMEN_REQUIREMENT_BEAN ="EditSpecimenRequirementBean";
	public static final String VIEW_SUMMARY ="ViewSummary";
	public static final String EVENT_KEY ="key";
	public static final String TREE_NODE_ID ="nodeId";
	public static final String PAGE_OF_DEFINE_EVENTS = "defineEvents";
	public static final String PAGE_OF_ADD_NEW_EVENT = "newEvent";
	public static final String PAGE_OF_SPECIMEN_REQUIREMENT = "specimenRequirement";
	public static final String ADD_NEW_EVENT = "-1";
	public static final String NEW_EVENT_KEY = "listKey";
	public static final String LINEAGE_NEW_SPECIMEN = "New Specimen";
	public static final String UNIQUE_IDENTIFIER_FOR_DERIVE = "_D";
	public static final String UNIQUE_IDENTIFIER_FOR_ALIQUOT = "_A";
	public static final String UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN = "_S";
	public static final String UNIQUE_IDENTIFIER_FOR_EVENTS = "E";
	public static final String CLICKED_NODE="clickedNode";
	/*
     * Conflict resolver constants
     * 
     */
         
    public static final String [] CONFLICT_LIST_HEADER = {
      		"Participant name",
      		"ID",
      		"Surgical pathological number",
      		"Report loaded on",
      		"Status",
      		"Site Name",
      		"Report Collection Date"
    };
      
    public static final String [] CONFLICT_FILTER_LIST = {
          "All",
          "Ambiguous Participants",
          "Ambiguous Specimen Collection Groups"
      };
    public static final String FILTER_LIST = "filterList";
    public static final String REPORT_ID = "reportQueueId";
    
    public static final String CONFLICT_STATUS = "conflictStatus";
    public static final String REPORT_INFORMATION = "ReportInformation";
    public static final String SELECTED_FILTER = "selectedFilter";
    public static final String CONFLICT_COMMON_VIEW="conflictCommonView";
    public static final String CONFLICT_TREE_VIEW="conflictTreeView";
    public static final String CONFLICT_DATA_VIEW="conflictDataView";
    public static final String CONFLICT_COMMON_VIEW_ACTION="ConflictCommon.do";
    public static final String CONFLICT_TREE_VIEW_ACTION="ConflictParticipantSCGTree.do";
    public static final String CONFLICT_DATA_VIEW_ACTION="ConflictParticipantDataDetails.do";
    public static final String SURGICAL_PATHOLOGY_NUMBER="surgicalPathologyNumber";
    public static final String REPORT_DATE="reportDate";
    public static final String SITE_NAME = "siteName";
    public static final String PARTICIPANT_NAME = "participantName";
    public static final String COLUMN_NAME_PART_COLL="elements(participantCollection)";
  	
    public static final String PARTICIPANT_ID_TO_ASSOCIATE = "participantIdToAssociate";
	public static final String SCG_ID_TO_ASSOCIATE = "scgIdToAssociate";
	public static final String REPORT_QUEUE_LIST = "ReportQueueList";
	public static final String USE_SELECTED_PARTICIPANT = "createNewSCG";
  	public static final String USE_SELECTED_SCG = "associateSCG";
  	public static final String CREATE_NEW_PARTICIPANT = "creatNewParticipant";
	public static final String OVERWRITE_REPORT = "overwriteReport";
	public static final String IGNORE_NEW_REPORT = "ignoreNewReport";
 	
  	public static final String CONFLICT_BUTTON = "conflictButton";
  	public static final String IDENTIFIED_SURGICAL_PATHOLOGY_REPORT = "identifiedSurgicalPathologyReport";
  	public static final String REPORT_COLLECTION_DATE = "reportCollectionDate";
  	
  	//Save Query Constants
  	public static final String HTML_CONTENTS = "HTMLContents";
  	public static final String SHOW_ALL= "showall";
	public static final String SHOW_ALL_ATTRIBUTE = "Show all attributes";
	public static final String SHOW_SELECTED_ATTRIBUTE = "Show selected attributes";
	public static final String ADD_EDIT_PAGE ="Add Edit Page";
	public static final String SAVE_QUERY_PAGE ="Save Query Page";
	public static final String EXECUTE_QUERY_PAGE ="Execute Query Page";
	public static final String FETCH_QUERY_ACTION ="FetchQuery.do"; 
	public static final String EXECUTE_QUERY_ACTION ="ExecuteQueryAction.do"; 
	public static final String EXECUTE_QUERY = "executeQuery";
	public static final String INVALID_CONDITION_VALUES ="InvalidValues";
	//	Save Query Constants ends
	
	public static final String NEW_SPECIMEN_TYPE = "New_Specimen";
	public static final String DERIVED_SPECIMEN_TYPE = "Derived_Specimen";
	
	public static final String SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME="specimenLabelGeneratorClass";
	public static final String SPECIMEN_BARCODE_GENERATOR_PROPERTY_NAME="specimenBarcodeGeneratorClass";
	public static final String STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME="storageContainerLabelGeneratorClass";
	public static final String STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME="storageContainerBarcodeGeneratorClass";
	public static final String SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME="speicmenCollectionGroupLabelGeneratorClass";
	public static final String SPECIMEN_COLL_GROUP_BARCODE_GENERATOR_PROPERTY_NAME="speicmenCollectionGroupBarcodeGeneratorClass";
	public static final String COLL_PROT_REG_BARCODE_GENERATOR_PROPERTY_NAME="collectionProtocolRegistrationBarcodeGeneratorClass";
	public static final String PROTOCOL_PARTICIPANT_IDENTIFIER_LABEL_GENERATOR_PROPERTY_NAME="protocolParticipantIdentifierLabelGeneratorClass";
	//Added By Geeta
	//public static final String ECMC_LABEL_GENERATOR_PROPERTY_NAME="LabelGeneratorForECMCClass";
	public static final String IS_STATE_REQUIRED="isStateRequired";
	public static final String IS_POST_CODE="isPostCode";
	public static final String CHANGE_DATE_FORMAT="changeDateFormat";
	
	public static final String IS_CP_TITLE_CHANGE="isCPTitleChange";

	
	public static final String IS_REMOVE_SSN="isRemoveSSN";
	public static final String IS_REMOVE_SEX_GENOTYPE="isRemoveSexGenotype";
	public static final String IS_REMOVE_RACE="isRemoveRace";
	public static final String IS_REMOVE_ETHNICITY="isRemoveEthnicity";
	public static final String IS_PHONENO_TO_BE_VALIDATED="isPhoneNumberToBeValidated";
	public static final String DATEFORMAT="dateFormat";
	
	//SPECIMEN COllection Status
	public static final String SPECIMEN_COLLECTED = "Collected";
	public static final String SPECIMEN_ANTICOPATED = "Anticipated";
		
	//Shopping cart constants.
	public static final String SPECIMEN_ARRAY_CLASS_NAME = SpecimenArray.class.getName();
	public static final String IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME = IdentifiedSurgicalPathologyReport.class.getName();
	public static final String DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME = DeidentifiedSurgicalPathologyReport.class.getName();
	public static final String SURGICAL_PATHALOGY_REPORT_CLASS_NAME = SurgicalPathologyReport.class.getName();
	public static final String SPECIMEN_NAME = Specimen.class.getName();
	
	//Shopping cart constants for Shipping And Tracking
	public static final String STORAGE_CONTAINER_CLASS_NAME = StorageContainer.class.getName();
	
	public static final String[] specimenNameArray = {
        SPECIMEN_NAME,
        FluidSpecimen.class.getName(),
        MolecularSpecimen.class.getName(),
        TissueSpecimen.class.getName(),
        CellSpecimen.class.getName()
    };
	
	public static final String[] entityNameArray = {
		              SPECIMEN_NAME,
			          FluidSpecimen.class.getName(),
			          MolecularSpecimen.class.getName(),
			          TissueSpecimen.class.getName(),
			          CellSpecimen.class.getName(),
			          SPECIMEN_ARRAY_CLASS_NAME,
			          IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
			          DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
			          SURGICAL_PATHALOGY_REPORT_CLASS_NAME
    };
	public static final String IDENTIFIED_REPORT_ID="identifiedReportId";
	
	public static final String BULK_TRANSFERS = "bulkTransfers";
	public static final String BULK_DISPOSALS = "bulkDisposals";
	
	public static final String EDIT_MULTIPLE_SPECIMEN = "editMultipleSp";
	public static final int CATISSUE_ENTITY_GROUP = 1;
	
	//Query-CSM related constants
	public static final String QUERY_REASUL_OBJECT_DATA_MAP = "queryReasultObjectDataMap";
	public static final String DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP = "defineViewQueryReasultObjectDataMap";
	public static final String HAS_CONDITION_ON_IDENTIFIED_FIELD = "hasConditionOnIdentifiedField";
	public static final String CONTAINTMENT_ASSOCIATION = "CONTAINTMENT";
	public static final String MAIN_ENTITY_MAP = "mainEntityMap";
	
	public static final String PAGEOF_SPECIMEN_COLLECTION_REQUIREMENT_GROUP = "pageOfSpecimenCollectionRequirementGroup";
	public static final String NO_MAIN_OBJECT_IN_QUERY = "noMainObjectInQuery";
	public static final String QUERY_ALREADY_DELETED = "queryAlreadyDeleted";
	public static final String[] INHERITED_ENTITY_NAMES = {
        FluidSpecimen.class.getName(),
        MolecularSpecimen.class.getName(),
        TissueSpecimen.class.getName(),
        CellSpecimen.class.getName(),
        //IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
        //DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
};
	
	//For enhanced CP
	public static final String PARENT_CP_TYPE = "Parent";
	public static final String ARM_CP_TYPE = "Arm" ;
	public static final String PHASE_CP_TYPE = "Phase" ;
	public static final String CYCLE_CP_TYPE = "Cycle" ;
	public  static final String SUB_COLLECTION_PROTOCOL = "SubProtocol";
	public  static final String CP_TYPE = "type";
	public static final int DAYS_TO_ADD_CP = 1;
	public static final String REG_DATE="regDate";
	public static final String CHILD_COLLECTION_PROTOCOL_COLLECTION = "childCollectionProtocolCollection";
	
//	Constants required in SubCollectionProtocolRegistration.jsp Page
	public static final String SUB_COLLECTION_PROTOCOL_REGISTRATION_SEARCH_ACTION = "SubCollectionProtocolRegistrationSearch.do";
	public static final String SUB_COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION = "SubCollectionProtocolRegistrationAdd.do";
	public static final String SUB_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "SubCollectionProtocolRegistrationEdit.do";
	public static final String CP_QUERY_SUB_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "CPQuerySubCollectionProtocolRegistrationEdit.do";
	public static final String CP_QUERY_SUB_COLLECTION_PROTOCOL_REGISTRATION_ADD_ACTION = "CPQuerySubCollectionProtocolRegistrationAdd.do";
	public static final String ID_COLUMN_ID = "ID_COLUMN_ID";
	public static final String SQL = "SQL";
	/**
	 * 
	 */
	public static final String SCGFORM = "SCGFORM";
	public static final String SPECIMENFORM = "SpecimenForm";
	public static final String FILE_TYPE = "file";	
	public static final String ORDER_REQUEST_STATUS_DISTRIBUTED_AND_DISPOSE="orderRequestStatusAndDistributedAndDispose";
	public static final String EXPORT_FILE_NAME_START = "Report_Content_";
	public static final String EXPORT_ZIP_NAME = "SearchResult.zip";	
	public static final String ZIP_FILE_EXTENTION = ".zip";
	public static final String CSV_FILE_EXTENTION = ".csv";
	public static final String EXPORT_DATA_LIST = "exportDataList"; 
	public static final String ENTITY_IDS_MAP = "entityIdsMap";
	public static final String DUMMY_NODE_NAME = "Loading...";
	public static final String NODE_NAME = "nodeName";
	public static final String CONTAINER_IDENTIFIER = "containerId";
	public static final String PARENT_IDENTIFIER = "parentId";
	public static final String COLLECTION_PROTOCOL = "edu.wustl.catissuecore.domain.CollectionProtocol";
	public static final String ENTITY_GROUP = "EntityGroup";
	public static final String ENTITY = "Entity";
	public static final String FORM = "Form";
	public static final String CATEGORY = "Category";

	/**
	 * 
	 * Constant required for Specimen Array Delete Action
	 */
	public static final String DELETE_SPECIMEN_ARRAY="DeleteSpecimenArray.do";
	public static final String LIST_SPECIMEN="specimenList";
	public static final String CP_QUERY_PRINT_ALIQUOT="CPQueryPrintAliquot";
	public static final String CP_QUERY_ALIQUOT_ADD="CPQueryAliquotAdd";
	public static final String FROM_PRINT_ACTION="fromPrintAction";
	public static final String PRINT_ALIQUOT="printAliquot";
		public static final String PRINTER_TYPE="printerType";
	public static final String PRINTER_LOCATION="printerLocation";
	
	
	//constant required for setting the participant id on pathReport
	public static final String PARTICIPANTIDFORREPORT="participantIdForReport";
	public static final int FIRST_COUNT_1 = 1;
	
	public static final String INSTITUTION_NAME = "instituteName";
	public static final String DEPARTMENT_NAME = "departmentName";
	public static final String CRG_NAME ="crgName"; 
	
	public static final String RESPONSE_SEPARATOR = "#@#";
	
//	Constants required to differentiate between patological cases.
	public static final int IDENTIFIED_SURGPATH_REPORT = 1;
	public static final int DEIDENTIFIED_SURGPATH_REPORT = 2;
	public static final int SURGPATH_REPORT = 3;
	
	public static final String CHILD_STATUS = "Child_Status";
	public static final String CHILD_DATE   = "Child_Date";
	public static final String CHILD_OFFSET = "Child_Offset";
// COnstants required for Collection Protocol Tree 
	public static final String ROOTNODE_FOR_CPTREE="rootnode";
	public static final String OBJECTNAME_FOR_CP="cpName";
	
//	Constants required for temporal query
	public static final String DATE_TYPE = "Date";
	public static final String INTEGER_TYPE = "Integer";
	public static final String FLOAT_TYPE = "Float";
	public static final String DOUBLE_TYPE = "Double";
	public static final String LONG_TYPE = "Long";
	public static final String SHORT_TYPE = "Short";
	
	public static final String FIRST_NODE_ATTRIBUTES = "firstDropDown";
	public static final String ARITHMETIC_OPERATORS = "secondDropDown";
	public static final String SECOND_NODE_ATTRIBUTES = "thirdDropDown";
	public static final String RELATIONAL_OPERATORS = "fourthDropDown";
	public static final String TIME_INTERVALS_LIST = "timeIntervals";
	public static final String ENTITY_LABEL_LIST = "entityList";
	
	public static final String LEFT_OPERAND = "leftTerm" ;
	public static final String RIGHT_OPERAND = "rightTerm";
	public static final String ARITHMETIC_OPERATOR  ="arithmaticOperator";
	public static final String RELATIONAL_OPERATOR = "relationalOperator";
	public static final String TIME_VALUE = "timeValue";
	public static final String TIME_INTERVAL_VALUE = "timeIntervalValue"; 
	public static final String LEFT_OPERAND_TYPE = "leftOperandType";
	public static final String RIGHT_OPERAND_TYPE = "rightOperandType";
	
	public static final String PLUS = "plus";
	public static final String MINUS = "Minus";
	public static final String MULTIPLYBY  = "MultipliedBy";
	public static final String DIVIDEBY = "DividedBy";
	public static final String CART_COLUMN_LIST = "cartColumnList";
	public static final String CART_ATTRIBUTE_LIST = "cartAttributeList";
	public static final String ADD_SPECIMEN_TO_CART="addSpecimenToCart";
public static final String ADD_MULTIPLE_SPECIMEN_TO_CART="addMltipleSpecimenToCart";
	
	public static final String ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE = "Distributed And Close"; 				
	
	public static final String shortTitle="shortTitle";
	
	// MSR purpose
	// Used for Authentication, Authorization through CSM
	public static final String ADMIN_USER="1";
	public static final String NON_ADMIN_USER="7";
    public static final String SUPER_ADMIN_USER="13";
	public static final String ADD_GLOBAL_PARTICIPANT="ADD_GLOBAL_PARTICIPANT";
	public static final String cannotCreateSuperAdmin = "cannotCreateSuperAdmin";
	public static final String siteIsRequired = "siteIsRequired";
	public static final String roleIsRequired = "roleIsRequired";
	
	public static final String getCurrentAndFuturePGAndPEName(long siteId)
	{
		String roleName = "SITE_" + siteId + "_All_CP";
		return roleName;
	}
	
	public static final String getCurrentAndFutureRoleName(long siteId, long userId, String defaultRole)
	{
		String roleName = defaultRole+"_"+"All CP's for SITE_" + siteId + "_For User"+userId;
		return roleName;
	}
	
	public static final String getCPRoleName(long collectionProtocolId, long userId, String defaultRole)
	{
		String roleName = defaultRole+"_"+"CP_" + collectionProtocolId + "_USER_" + userId;
		return roleName;
	}
	
	public static final String getSiteRoleName(long siteId, long userId, String defaultRole)
	{
		String roleName = defaultRole+"_"+"SITE_" + siteId + "_USER_" + userId;
		return roleName;
	}
	
	public static final String getSiteUserGroupName(long siteId, long csmUserId)
	{
		return "SITE_"+siteId+"_USER_"+csmUserId;
	}
	
	public static final String getCPUserGroupName(long cpId, long csmUserId)
	{
		return "CP_"+cpId+"_USER_"+csmUserId;
	}
	
	// Constants used from file 'PermissionMapDetails.xml'
	public static final String ADMIN_PROTECTION_ELEMENT = "ADMIN_PROTECTION_ELEMENT";
	public static final String ADD_EDIT_USER = "ADD_EDIT_USER";
	public static final String ADD_EDIT_INSTITUTION = "ADD_EDIT_INSTITUTION";
	public static final String ADD_EDIT_DEPARTMENT = "ADD_EDIT_DEPARTMENT";
	public static final String ADD_EDIT_CRG = "ADD_EDIT_CRG";
	public static final String ADD_EDIT_SITE = "ADD_EDIT_SITE";
	public static final String ADD_EDIT_STORAGE_TYPE = "ADD_EDIT_STORAGE_TYPE";
	public static final String ADD_EDIT_STORAGE_CONTAINER = "ADD_EDIT_STORAGE_CONTAINER";
	public static final String ADD_EDIT_SPECIMEN_ARRAY_TYPE = "ADD_EDIT_SPECIMEN_ARRAY_TYPE";
	public static final String ADD_EDIT_BIOHAZARD = "ADD_EDIT_BIOHAZARD";
	public static final String ADD_EDIT_CP = "ADD_EDIT_CP";
	public static final String ADD_EDIT_DP = "ADD_EDIT_DP";
	public static final String ADD_EDIT_PARTICIPANT = "ADD_EDIT_PARTICIPANT";
	public static final String ADD_EDIT_SPECIMEN = "ADD_EDIT_SPECIMEN";
	public static final String ALIQUOT_SPECIMEN = "ALIQUOT_SPECIMEN";
	public static final String DERIVE_SPECIMEN = "DERIVE_SPECIMEN";
	public static final String ADD_EDIT_SCG = "ADD_EDIT_SCG";
	public static final String ADD_EDIT_SPECIMEN_ARRAY = "ADD_EDIT_SPECIMEN_ARRAY";
	public static final String ADD_EDIT_SPECIMEN_EVENTS = "ADD_EDIT_SPECIMEN_EVENTS";
	public static final String DISTRIBUTE_SPECIMENS = "DISTRIBUTE_SPECIMENS";
	public static final String OUTPUT_TERMS_COLUMNS = "outputTermsColumns";
	public static final String CP_BASED_VIEW_FILTRATION = "CP_BASED_VIEW_FILTRATION";
	public static final String EDIT_PROFILE_PRIVILEGE= "EDIT_PROFILE_PRIVILEGE";
	public static final String CP_CHILD_SUBMIT = "cpChildSubmit";
	public static final String SITE_CLASS_NAME = "edu.wustl.catissuecore.domain.Site";//CollectionProtocol.class.getName();
	public static final String USER_ROW_ID_BEAN_MAP = "rowIdBeanMapForUserPage";
	public static final String ON = "on";
	public static final String OFF = "off";
	public static final String TEXT_RADIOBUTTON = "text_radioButton";
	public static final String TREENO_ZERO = "zero";
	public static final String PAGE_OF_QUERY_MODULE = "pageOfQueryModule";
	public static final String ORDER_DETAILS = "orderDetails";
	public static final String Association = "ASSOCIATION";
	
	//For Shipping And Tracking My List
	public static final String SPECIMEN_LABELS_LIST = "specimenLabelName";
	public static final String CONTAINER_NAMES_LIST = "containerNames";
	
	public static final String CONTAINER_DELETE_MAPPING="/StorageContainerDelete.do";
	public static final String CONTAINER_ERROR_MSG="The specimen {0} cannot be placed in container {1} since the position {2},{3} is not free";
	
	// COnstant for API
	public static final String API_FILTERED_OBJECT_LIMIT= "apiFilteredObjectLimit";
	public static final String CP_DEFAULT ="Use CP Defaults";
	public static final String REGISTRATION_FOR_REPORT_LOADER = "REGISTRATION_FOR_REPORT_LOADER";
	public static final String REPORT_LOADER_SCG = "SurgPReport_";
	public static final String DISPOSAL_REASON = "Specimen was Distributed";
	public static final String SPECIMEN_DISPOSAL_REASON = "Specimen was Aliquoted";
	public static final String DERIVED_SPECIMEN_DISPOSAL_REASON = "Specimen was Derived";
	public static final String SPECIMEN_DISCARD_DISPOSAL_REASON = "Specimen was Discarded";
	
	// Constants for ACORN
	public static final String MSSQLSERVER_DATABASE = "MSSQLSERVER";
	public static final String FORMAT_FILE_EXTENTION = "_FormatFile.txt";
	public static final String MSSQLSERVER_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION="CAST(label as numeric(19))";
	public static final String MSSQLSERVER_MAX_BARCODE_COL="CAST(barcode as numeric(19))";
}
