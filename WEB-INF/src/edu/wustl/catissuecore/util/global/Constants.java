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

import java.util.HashMap;

/**
 * This class stores the constants used in the operations in the application.
 * @author gautam_shetty
 */

public class Constants
{	
 
    //Constants used for authentication module.
    public static final String LOGIN = "login";
    public static final String SESSION_DATA = "sessionData";
    
    public static final String AND_JOIN_CONDITION = "AND";
	public static final String OR_JOIN_CONDITION = "OR";
	
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm aa";
	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_PATTERN_MM_DD_YYYY = "MM-dd-yyyy";
	
	
	//Database constants.
	public static final String MYSQL_DATE_PATTERN = "%m-%d-%Y";
	//DAO Constants.
	public static final int HIBERNATE_DAO = 1;
	public static final int JDBC_DAO = 2;
	
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String ADD = "add";
	public static final String EDIT = "edit";
	public static final String VIEW = "view";
	public static final String SEARCH = "search";
	public static final String DELETE = "delete";
	public static final String EXPORT = "export";
	public static final String SHOPPING_CART_ADD = "shoppingCartAdd";
	public static final String SHOPPING_CART_DELETE = "shoppingCartDelete";
	public static final String SHOPPING_CART_EXPORT = "shoppingCartExport";
	public static final String NEWUSERFORM = "newUserForm";
	public static final String ACCESS_DENIED = "access_denied";

	//Constants required for Forgot Password
	public static final String FORGOT_PASSWORD = "forgotpassword";
	
	public static final String IDENTIFIER = "systemIdentifier";
	public static final String LOGINNAME = "loginName";
	public static final String LASTNAME = "lastName";
	public static final String FIRSTNAME = "firstName";
    public static final String ERROR_DETAIL = "Error Detail";
	public static final String INSTITUTION = "institution";
	public static final String EMAIL = "email";
	public static final String DEPARTMENT = "department";
	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String COUNTRY = "country";
	public static final String OPERATION = "operation";
	public static final String ACTIVITY_STATUS = "activityStatus";
	public static final String NEXT_CONTAINER_NO = "startNumber";
	
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
	public static final String USERLIST = "userList";
	public static final String SITETYPELIST = "siteTypeList";
	public static final String STORAGETYPELIST="storageTypeList";
	public static final String STORAGECONTAINERLIST="storageContainerList";
	public static final String SITELIST="siteList";
//	public static final String SITEIDLIST="siteIdList";
	public static final String USERIDLIST = "userIdList";
	public static final String STORAGETYPEIDLIST="storageTypeIdList";
	public static final String SPECIMENCOLLECTIONLIST="specimentCollectionList";
	public static final String APPROVE_USER_STATUS_LIST = "statusList";
	public static final String EVENT_PARAMETERS_LIST = "eventParametersList";
		
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
	
	//SpecimenCollecionGroup lists.
	public static final String PROTOCOL_TITLE_LIST = "protocolTitleList";
	public static final String PARTICIPANT_NAME_LIST = "participantNameList";
	public static final String PROTOCOL_PARTICIPANT_NUMBER_LIST = "protocolParticipantNumberList";
	//public static final String PROTOCOL_PARTICIPANT_NUMBER_ID_LIST = "protocolParticipantNumberIdList";
	public static final String STUDY_CALENDAR_EVENT_POINT_LIST = "studyCalendarEventPointList";
	//public static final String STUDY_CALENDAR_EVENT_POINT_ID_LIST="studyCalendarEventPointIdList";
	public static final String PARTICIPANT_MEDICAL_IDNETIFIER_LIST = "participantMedicalIdentifierArray";
	//public static final String PARTICIPANT_MEDICAL_IDNETIFIER_ID_LIST = "participantMedicalIdentifierIdArray";
	
	public static final String CLINICAL_STATUS_LIST = "cinicalStatusList";
	public static final String SPECIMEN_CLASS_LIST = "specimenClassList";
	public static final String SPECIMEN_CLASS_ID_LIST = "specimenClassIdList";
	public static final String SPECIMEN_TYPE_MAP = "specimenTypeMap";
	//Simple Query Interface Lists
	public static final String OBJECT_NAME_LIST = "objectNameList";
	public static final String OBJECT_COMPLETE_NAME_LIST = "objectCompleteNameList";
	public static final String ATTRIBUTE_NAME_LIST = "attributeNameList";
	public static final String ATTRIBUTE_CONDITION_LIST = "attributeConditionList";
	
	//Constants for Storage Container.
	public static final String STORAGE_CONTAINER_TYPE = "storageType";
	public static final String STORAGE_CONTAINER_GRID_OBJECT = "storageContainerGridObject";
	public static final String STORAGE_CONTAINER_CHILDREN_STATUS = "storageContainerChildrenStatus";
	public static final String START_NUMBER = "startNumber";
	public static final String CHILD_CONTAINER_SYSTEM_IDENTIFIERS = "childContainerSystemIdentifiers";
	
	//event parameters lists
	public static final String METHODLIST = "methodList";
	public static final String HOURLIST = "hourList";
	public static final String MINUTESLIST = "minutesList";
	public static final String EMBEDDINGMEDIUMLIST = "embeddingMediumList";
	public static final String PROCEDURELIST = "procedureList";
	public static final String PROCEDUREIDLIST = "procedureIdList";
	public static final String CONTAINERLIST = "containerList";
	public static final String CONTAINERIDLIST = "containerIdList";
	public static final String FROMCONTAINERLIST="fromContainerList";
	public static final String TOCONTAINERLIST="toContainerList";
	public static final String FIXATIONLIST = "fixationList";	
	public static final String FROMSITELIST="fromsiteList";
	public static final String TOSITELIST="tositeList";	
	public static final String ITEMLIST="itemList";
	public static final String DISTRIBUTIONPROTOCOLLIST="distributionProtocolList";
	public static final String TISSUE_SPECIMEN_ID_LIST="tissueSpecimenIdList";
	public static final String MOLECULAR_SPECIMEN_ID_LIST="molecularSpecimenIdList";
	public static final String CELL_SPECIMEN_ID_LIST="cellSpecimenIdList";
	public static final String FLUID_SPECIMEN_ID_LIST="fluidSpecimenIdList";
	public static final String STORAGESTATUSLIST="storageStatusList";
	public static final String CLINICAL_DIAGNOSIS_LIST = "clinicalDiagnosisList";
	public static final String HISTOLOGICALQUALITYLIST="histologicalQualityList";
	
	//Constants required in User.jsp Page
	public static final String USER_SEARCH_ACTION = "UserSearch.do";
	public static final String USER_ADD_ACTION = "UserAdd.do";
	public static final String USER_EDIT_ACTION = "UserEdit.do";
	public static final String APPROVE_USER_ADD_ACTION = "ApproveUserAdd.do";
	public static final String APPROVE_USER_EDIT_ACTION = "ApproveUserEdit.do";
	public static final String SIGNUP_USER_ADD_ACTION = "SignUpUserAdd.do";

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
	
	public static final String SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION = "/catissuecore/ShowStorageGridView.do";
	
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
	public static final String DATA_VIEW_FRAME_ACTION = "SpreadsheetView.do";
	
	//New Specimen Data Actions.
	public static final String SPECIMEN_ADD_ACTION = "NewSpecimenAdd.do";
	public static final String SPECIMEN_EDIT_ACTION = "NewSpecimenEdit.do";
	public static final String SPECIMEN_SEARCH_ACTION = "NewSpecimenSearch.do";
	
	//Create Specimen Data Actions.
	public static final String CREATE_SPECIMEN_ADD_ACTION = "CreateSpecimenAdd.do";
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
	
	//Levels of nodes in query results tree.
	public static final int MAX_LEVEL = 5;
	public static final int MIN_LEVEL = 1;
	
	public static final String[] DEFAULT_TREE_SELECT_COLUMNS = {
	        
	};
	
	//Frame names in Query Results page.
	public static final String DATA_VIEW_FRAME = "myframe1";
	public static final String APPLET_VIEW_FRAME = "appletViewFrame";
	
	//NodeSelectionlistener - Query Results Tree node selection (For spreadsheet or individual view).
	public static final String DATA_VIEW_ACTION = "/catissuecore/DataView.do?nodeName=";
	public static final String VIEW_TYPE = "viewType";
	
	//TissueSite Tree View Constants.
	public static final String PROPERTY_NAME = "propertyName";
	
	//Constants for type of query results view.
	public static final String SPREADSHEET_VIEW = "Spreadsheet View";
	public static final String OBJECT_VIEW = "Object View";
	
	//Spreadsheet view Constants in DataViewAction.
	public static final String PARTICIPANT = "Participant";
	public static final String ACCESSION = "Accession";
	public static final String QUERY_PARTICIPANT_SEARCH_ACTION = "QueryParticipantSearch.do?systemIdentifier=";
	public static final String QUERY_ACCESSION_SEARCH_ACTION = "QueryAccessionSearch.do?systemIdentifier=";
	
	//Individual view Constants in DataViewAction.
	public static final String SPREADSHEET_COLUMN_LIST = "spreadsheetColumnList";
	public static final String SPREADSHEET_DATA_LIST = "spreadsheetDataList";
	public static final String SELECT_COLUMN_LIST = "selectColumnList";
	
	//Tree Data Action
	public static final String TREE_DATA_ACTION = "/catissuecore/Data.do";
	
	public static final String SPECIMEN = "Specimen";
	public static final String SEGMENT = "Segment";
	public static final String SAMPLE = "Sample";
	
	public static final String PARTICIPANT_ID_COLUMN = "PARTICIPANT_ID";
	public static final String ACCESSION_ID_COLUMN = "ACCESSION_ID";
	public static final String SPECIMEN_ID_COLUMN = "SPECIMEN_ID";
	public static final String SEGMENT_ID_COLUMN = "SEGMENT_ID";
	public static final String SAMPLE_ID_COLUMN = "SAMPLE_ID";
	
	//Identifiers for various Form beans
	public static final int USER_FORM_ID = 1;
	public static final int PARTICIPANT_FORM_ID = 2;
	public static final int ACCESSION_FORM_ID = 3;
	public static final int REPORTEDPROBLEM_FORM_ID = 4;
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
	public static final int COLLECTION_PROTOCOL_REGISTRATION_FORM_ID = 19;
	public static final int SPECIMEN_COLLECTION_GROUP_FORM_ID = 20;
	public static final int FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID = 21;
	public static final int NEW_SPECIMEN_FORM_ID = 22;
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
	public static final int SIMPLE_QUERY_INTERFACE_ID = 40;
	
	//Misc
	public static final String SEPARATOR = " : ";
		
	//Status message key Constants
	public static final String STATUS_MESSAGE_KEY = "statusMessageKey";
	
	//Identifiers for JDBC DAO.
	public static final int QUERY_RESULT_TREE_JDBC_DAO = 1;
	
	//Activity Status values
	public static final String ACTIVITY_STATUS_ACTIVE = "Active";
	public static final String ACTIVITY_STATUS_APPROVE = "Approve";
	public static final String ACTIVITY_STATUS_REJECT = "Reject";
	public static final String ACTIVITY_STATUS_NEW = "New";
	public static final String ACTIVITY_STATUS_PENDING = "Pending";
	public static final String ACTIVITY_STATUS_CLOSED = "Closed";
	
	//Approve User status values.
	public static final String APPROVE_USER_APPROVE_STATUS = "Approve";
	public static final String APPROVE_USER_REJECT_STATUS = "Reject";
	public static final String APPROVE_USER_PENDING_STATUS = "Pending";
	
	//Approve User Constants
	public static final int ZERO = 0;
	public static final int START_PAGE = 1;
	public static final int NUMBER_RESULTS_PER_PAGE = 5;
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
	
	//Tree View Constants.
	public static final String ROOT = "Root";
	public static final String CATISSUE_CORE = "caTISSUE Core";
	public static final String TISSUE_SITE = "Tissue Site";
	public static final int TISSUE_SITE_TREE_ID = 1;
	public static final int STORAGE_CONTAINER_TREE_ID = 2;
	public static final int QUERY_RESULTS_TREE_ID = 3;
	
	//Edit Object Constants.
	public static final String TABLE_ALIAS_NAME = "aliasName"; 
	public static final String FIELD_TYPE_VARCHAR = "varchar";
	public static final String FIELD_TYPE_DATE = "date";
	
	//Query Interface Results View Constants
	public static final String PAGEOF = "pageOf";
	public static final String QUERY = "query";
	public static final String PAGEOF_APPROVE_USER = "pageOfApproveUser";
	public static final String PAGEOF_SIGNUP = "pageOfSignUp";
	public static final String PAGEOF_USERADD = "pageOfUserAdd";
	public static final String PAGEOF_USER_ADMIN = "pageOfUserAdmin";
	
	//For Tree Applet
	public static final String PAGEOF_QUERY_RESULTS = "pageOfQueryResults";
	public static final String PAGEOF_STORAGE_LOCATION = "pageOfStorageLocation";
	public static final String PAGEOF_SPECIMEN = "pageOfSpecimen";
	public static final String PAGEOF_TISSUE_SITE = "pageOfTissueSite";

	//For Simple Query Interface and Edit.
	public static final String PAGEOF_SIMPLE_QUERY_INTERFACE = "pageOfSimpleQueryInterface";
	public static final String PAGEOF_EDIT_OBJECT = "pageOfEditObject";

	//Query results view temporary table name.
	public static final String QUERY_RESULTS_TABLE = "CATISSUE_QUERY_RESULTS";
	
	//Query results view temporary table columns.
	public static final String QUERY_RESULTS_PARTICIPANT_ID = "PARTICIPANT_ID";
	public static final String QUERY_RESULTS_COLLECTION_PROTOCOL_ID = "COLLECTION_PROTOCOL_ID";
	public static final String QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID = "COLLECTION_PROTOCOL_EVENT_ID";
	public static final String QUERY_RESULTS_SPECIMEN_COLLECTION_GROUP_ID = "SPECIMEN_COLLECTION_GROUP_ID";
	public static final String QUERY_RESULTS_SPECIMEN_ID = "SPECIMEN_ID";
	public static final String QUERY_RESULTS_SPECIMEN_TYPE = "SPECIMEN_TYPE";
	
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
	public static final String QUERY_TREE_APPLET = "QueryTree.class";
	public static final String APPLET_CODEBASE = "Applet";
	public static final String TREE_APPLET_NAME = "treeApplet";
	
	//Shopping Cart
	public static final String SHOPPING_CART = "shoppingCart";
	
	public static final String SELECT_OPTION = "-- Select --";
	
//	public static final String[] TISSUE_SITE_ARRAY = {
//	        SELECT_OPTION,"Sex","male","female",
//	        "Tissue","kidney","Left kidney","Right kidney"
//	};
	
	public static final String[] ATTRIBUTE_NAME_ARRAY = {
	        SELECT_OPTION
	};
	
	public static final String[] ATTRIBUTE_CONDITION_ARRAY = {
	        "=","<",">"
	};
	
	public static final String [] RECEIVEDMODEARRAY = {
	        SELECT_OPTION,
	        "by hand", "courier", "FedEX", "UPS"
	};
	public static final String [] GENDER_ARRAY = {
	        SELECT_OPTION,
	        "Male",
	        "Female"
	};
	
	public static final String [] GENOTYPE_ARRAY = {
	        SELECT_OPTION,
	        "XX",
	        "XY"
	};
	
	public static final String [] RACEARRAY = {
	        SELECT_OPTION,
	        "Asian",
	        "American"
	};
	
	public static final String [] PROTOCOLARRAY = {
	        SELECT_OPTION,
	        "aaaa",
	        "bbbb",
	        "cccc"
	};
	
	public static final String [] RECEIVEDBYARRAY = {
	        SELECT_OPTION,
	        "xxxx",
	        "yyyy",
	        "zzzz"
	};
	
	public static final String [] COLLECTEDBYARRAY = {
	        SELECT_OPTION,
	        "xxxx",
	        "yyyy",
	        "zzzz"
	};
	
	public static final String [] TIME_HOUR_ARRAY = {"1","2","3","4","5"};
	
	public static final String [] TIME_HOUR_AMPM_ARRAY = {"AM","PM"}; 
	
	public static final String [] STATEARRAY = 
	{
	        SELECT_OPTION,
	        "Alabama",//Alabama 
            "Alaska",//Alaska 
            "Arizona",//Arizona 
            "Arkansas",//Arkansas 
            "California",//California 
            "Colorado",//Colorado 
            "Connecticut",//Connecticut 
            "Delaware",//Delaware 
            "D.C.",//D.C. 
            "Florida",//Florida 
            "Georgia",//Georgia 
            "Hawaii",//Hawaii 
            "Idaho",//Idaho 
            "Illinois",//Illinois 
            "Indiana",//Indiana 
            "Iowa",//Iowa 
            "Kansas",//Kansas 
            "Kentucky",//Kentucky 
            "Louisiana",//Louisiana 
            "Maine",//Maine 
            "Maryland",//Maryland 
            "Massachusetts",//Massachusetts 
            "Michigan",//Michigan 
            "Minnesota",//Minnesota 
            "Mississippi",//Mississippi 
            "Missouri",//Missouri 
            "Montana",//Montana 
            "Nebraska",//Nebraska 
            "Nevada",//Nevada 
            "New Hampshire",//New Hampshire 
            "New Jersey",//New Jersey 
            "New Mexico",//New Mexico 
            "New York",//New York 
            "North Carolina",//North Carolina 
            "North Dakota",//North Dakota 
            "Ohio",//Ohio 
            "Oklahoma",//Oklahoma 
            "Oregon",//Oregon 
            "Pennsylvania",//Pennsylvania 
            "Rhode Island",//Rhode Island 
            "South Carolina",//South Carolina 
            "South Dakota",//South Dakota 
            "Tennessee",//Tennessee 
            "Texas",//Texas 
            "Utah",//Utah 
            "Vermont",//Vermont 
            "Virginia",//Virginia 
            "Washington",//Washington 
            "West Virginia",//West Virginia 
            "Wisconsin",//Wisconsin 
            "Wyoming",//Wyoming 
            "Other",//Other 
	};
	
	public static final String [] COUNTRYARRAY = 
	{  
	        SELECT_OPTION,
	        "United States", 
            "Canada", 
            "Afghanistan", 
            "Albania", 
            "Algeria", 
            "American Samoa", 
            "Andorra", 
            "Angola", 
            "Anguilla", 
            "Antarctica", 
            "Antigua and Barbuda", 
            "Argentina",
            "Armenia",
            "Aruba", 
            "Australia", 
            "Austria",
            "Azerbaijan", 
            "Bahamas", 
            "Bahrain",
            "Bangladesh", 
            "Barbados", 
            "Belarus", 
            "Belgium", 
            "Belize",
            "Benin",
            "Bermuda",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegovina",
            "Botswana", 
            "Bouvet Island",
            "Brazil",
            "Brunei Darussalam",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cambodia",
            "Cameroon",//Cameroon 
            "Cape Verde",//Cape Verde 
            "Cayman Islands",//Cayman Islands 
            "Central African Republic",//Central African Republic 
            "Chad",//Chad 
            "Chile",//Chile 
            "China",//China 
            "Christmas Island",//Christmas Island 
            "Cocos Islands",//Cocos Islands 
            "Colombia",//Colombia 
            "Comoros",//Comoros 
            "Congo",//Congo 
            "Cook Islands",//Cook Islands 
            "Costa Rica",//Costa Rica 
            "Cote D ivoire",//Cote D ivoire 
            "Croatia",//Croatia 
            "Cyprus",//Cyprus 
            "Czech Republic",//Czech Republic 
            "Denmark",//Denmark 
            "Djibouti",//Djibouti 
            "Dominica",//Dominica 
            "Dominican Republic",//Dominican Republic 
            "East Timor",//East Timor 
            "Ecuador",//Ecuador 
            "Egypt",//Egypt 
            "El Salvador",//El Salvador 
            "Equatorial Guinea",//Equatorial Guinea 
            "Eritrea",//Eritrea 
            "Estonia",//Estonia 
            "Ethiopia",//Ethiopia 
            "Falkland Islands",//Falkland Islands 
            "Faroe Islands",//Faroe Islands 
            "Fiji",//Fiji 
            "Finland",//Finland 
            "France",//France 
            "French Guiana",//French Guiana 
            "French Polynesia",//French Polynesia 
            "French S. Territories",//French S. Territories 
            "Gabon",//Gabon 
            "Gambia",//Gambia 
            "Georgia",//Georgia 
            "Germany",//Germany 
            "Ghana",//Ghana 
            "Gibraltar",//Gibraltar 
            "Greece",//Greece 
            "Greenland",//Greenland 
            "Grenada",//Grenada 
            "Guadeloupe",//Guadeloupe 
            "Guam",//Guam 
            "Guatemala",//Guatemala 
            "Guinea",//Guinea 
            "Guinea-Bissau",//Guinea-Bissau 
            "Guyana",//Guyana 
            "Haiti",//Haiti 
            "Honduras",//Honduras 
            "Hong Kong",//Hong Kong 
            "Hungary",//Hungary 
            "Iceland",//Iceland 
            "India",//India 
            "Indonesia",//Indonesia 
            "Iran",//Iran 
            "Iraq",//Iraq 
            "Ireland",//Ireland 
            "Israel",//Israel 
            "Italy",//Italy 
            "Jamaica",//Jamaica 
            "Japan",//Japan 
            "Jordan",//Jordan 
            "Kazakhstan",//Kazakhstan 
            "Kenya",//Kenya 
            "Kiribati",//Kiribati 
            "Korea",//Korea 
            "Kuwait",//Kuwait 
            "Kyrgyzstan",//Kyrgyzstan 
            "Laos",//Laos 
            "Latvia",//Latvia 
            "Lebanon",//Lebanon 
            "Lesotho",//Lesotho 
            "Liberia",//Liberia 
            "Liechtenstein",//Liechtenstein 
            "Lithuania",//Lithuania 
            "Luxembourg",//Luxembourg 
            "Macau",//Macau 
            "Macedonia",//Macedonia 
            "Madagascar",//Madagascar 
            "Malawi",//Malawi 
            "Malaysia",//Malaysia 
            "Maldives",//Maldives 
            "Mali",//Mali 
            "Malta",//Malta 
            "Marshall Islands",//Marshall Islands 
            "Martinique",//Martinique 
            "Mauritania",//Mauritania 
            "Mauritius",//Mauritius 
            "Mayotte",//Mayotte 
            "Mexico",//Mexico 
            "Micronesia",//Micronesia 
            "Moldova",//Moldova 
            "Monaco",//Monaco 
            "Mongolia",//Mongolia 
            "Montserrat",//Montserrat 
            "Morocco",//Morocco 
            "Mozambique",//Mozambique 
            "Myanmar",//Myanmar 
            "Namibia",//Namibia 
            "Nauru",//Nauru 
            "Nepal",//Nepal 
            "Netherlands",//Netherlands 
            "Netherlands Antilles",//Netherlands Antilles 
            "New Caledonia",//New Caledonia 
            "New Zealand",//New Zealand 
            "Nicaragua",//Nicaragua 
            "Niger",//Niger 
            "Nigeria",//Nigeria 
            "Niue",//Niue 
            "Norfolk Island",//Norfolk Island 
            "Norway",//Norway 
            "Oman",//Oman 
            "Pakistan",//Pakistan 
            "Palau",//Palau 
            "Panama",//Panama 
            "Papua New Guinea",//Papua New Guinea 
            "Paraguay",//Paraguay 
            "Peru",//Peru 
            "Philippines",//Philippines 
            "Pitcairn",//Pitcairn 
            "Poland",//Poland 
            "Portugal",//Portugal 
            "Puerto Rico",//Puerto Rico 
            "Qatar",//Qatar 
            "Reunion",//Reunion 
            "Romania",//Romania 
            "Russian Federation",//Russian Federation 
            "Rwanda",//Rwanda 
            "Saint Helena",//Saint Helena 
            "Saint Kitts and Nevis",//Saint Kitts and Nevis 
            "Saint Lucia",//Saint Lucia 
            "Saint Pierre",//Saint Pierre 
            "Saint Vincent",//Saint Vincent 
            "Samoa",//Samoa 
            "San Marino",//San Marino 
            "Sao Tome and Principe",//Sao Tome and Principe 
            "Saudi Arabia",//Saudi Arabia 
            "Senegal",//Senegal 
            "Seychelles",//Seychelles 
            "Sierra Leone",//Sierra Leone 
            "Singapore",//Singapore 
            "Slovakia",//Slovakia 
            "Slovenia",//Slovenia 
            "Solomon Islands",//Solomon Islands 
            "Somalia",//Somalia 
            "South Africa",//South Africa 
            "Spain",//Spain 
            "Sri Lanka",//Sri Lanka 
            "Sudan",//Sudan 
            "Suriname",//Suriname 
            "Swaziland",//Swaziland 
            "Sweden",//Sweden 
            "Switzerland",//Switzerland 
            "Syrian Arab Republic",//Syrian Arab Republic 
            "Taiwan",//Taiwan 
            "Tajikistan",//Tajikistan 
            "Tanzania",//Tanzania 
            "Thailand",//Thailand 
            "Togo",//Togo 
            "Tokelau",//Tokelau 
            "Tonga",//Tonga 
            "Trinidad and Tobago",//Trinidad and Tobago 
            "Tunisia",//Tunisia 
            "Turkey",//Turkey 
            "Turkmenistan",//Turkmenistan 
            "Turks and Caicos Islands",//Turks and Caicos Islands 
            "Tuvalu",//Tuvalu 
            "Uganda",//Uganda 
            "Ukraine",//Ukraine 
            "United Arab Emirates",//United Arab Emirates 
            "United Kingdom",//United Kingdom 
            "Uruguay",//Uruguay 
            "Uzbekistan",//Uzbekistan 
            "Vanuatu",//Vanuatu 
            "Vatican City State",//Vatican City State 
            "Venezuela",//Venezuela 
            "Vietnam",//Vietnam 
            "Virgin Islands",//Virgin Islands 
            "Wallis And Futuna Islands",//Wallis And Futuna Islands 
            "Western Sahara",//Western Sahara 
            "Yemen",//Yemen 
            "Yugoslavia",//Yugoslavia 
            "Zaire",//Zaire 
            "Zambia",//Zambia 
            "Zimbabwe",//Zimbabwe  
	};
	
//	Constants required in CollectionProtocol.jsp Page
	public static final String COLLECTIONPROTOCOL_SEARCH_ACTION = "CollectionProtocolSearch.do";
	public static final String COLLECTIONPROTOCOL_ADD_ACTION = "CollectionProtocolAdd.do";
	public static final String COLLECTIONPROTOCOL_EDIT_ACTION = "CollectionProtocolEdit.do";

//	Constants required in DistributionProtocol.jsp Page
	public static final String DISTRIBUTIONPROTOCOL_SEARCH_ACTION = "DistributionProtocolSearch.do";
	public static final String DISTRIBUTIONPROTOCOL_ADD_ACTION = "DistributionProtocolAdd.do";
	public static final String DISTRIBUTIONPROTOCOL_EDIT_ACTION = "DistributionProtocolEdit.do";
	
	public static final String [] ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Active",
	        "Disabled",
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

	
	public static final String [] SPECIMEN_TYPE_VALUES = {
	        SELECT_OPTION,
	        "Tissue",
	        "Fluid",
	        "Cell",
	        "Molecular"
	};
	
	public static final String [] SITE_TYPE_ARRAY = {
	        SELECT_OPTION,
			"Collection",
			"Laboratory",
			"Repository", 
			"Hospital"
	};
	
	public static final String [] HOURARRAY = {
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
	        "00"
	};

	
	public static final String [] MINUTESARRAY = {
	 		"00",
			"01",
			"02",
			"03",
			"04",
			"05",
			"06",
			"07",
			"08",
			"09",
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
	
	public static final String [] METHODARRAY = {
	        SELECT_OPTION,
			"LN2",
			"Dry Ice",
			"Iso pentane"
	};
	
	public static final String [] EMBEDDINGMEDIUMARRAY = {
				SELECT_OPTION,
				"Plastic",
				"Glass",
				
		};
	
	public static final String [] ITEMARRAY = {
			SELECT_OPTION,
			"Item1",
			"Item2",
	
	};
	
	public static final String UNIT_GM = "gm";
	public static final String UNIT_ML = "ml";
	public static final String UNIT_CC = "cell count";
	public static final String UNIT_MG = "mg";
	
	public static final String [] PROCEDUREARRAY = {
	        SELECT_OPTION,
			"Procedure 1",
			"Procedure 2",
			"Procedure 3"
	};
	
	public static final String [] CONTAINERARRAY = {
	        SELECT_OPTION,
			"Container 1",
			"Container 2",
			"Container 3"
	};


	public static final String [] FIXATIONARRAY = {
	        SELECT_OPTION,
			"FIXATION 1",
			"FIXATION 2",
			"FIXATION 3"
	};
	
	public static final  HashMap STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES = new HashMap();

	
	public static final String CDE_CONF_FILE = "CDEConfig.xml";
	public static final String CDE_NAME_TISSUE_SITE = "Tissue Site";
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
	public static final String CDE_NAME_CLINICAL_DIAGNOSIS = "Clinical Diagnosis";
	
	public static final String [] STORAGESTATUSARRAY = {
	        SELECT_OPTION,
			"CHECK IN",
			"CHECK OUT"
	};

	public static final String [] CLINICALDIAGNOSISARRAY = {
	        SELECT_OPTION,
			"CLINICALDIAGNOSIS 1",
			"CLINICALDIAGNOSIS 2",
			"CLINICALDIAGNOSIS 3"
	};
	
	public static final String [] HISTOLOGICALQUALITYARRAY = {
	        SELECT_OPTION,
			"GOOD",
			"OK",
			"DAMAGED"
	};
	
	// constants for Data required in query
	public static final String ALIAS_NAME_TABLE_NAME_MAP="objectTableNames";
	public static final String SYSTEM_IDENTIFIER = "systemIdentifier";
	
	
	public static final String EVENT_PARAMETERS[] = {	Constants.SELECT_OPTION,
							"Cell Specimen Review",	"Check In Check Out", "Collection",
							"Disposal", "Embedded", "Fixed", "Fluid Specimen Review",
							"Frozen", "Molecular Specimen Review", "Procedure", "Received",
							"Spun", "Thaw", "Tissue Specimen Review", "Transfer" };
	
	public static final String EVENT_PARAMETERS_COLUMNS[] = { "Identifier",
											"Event Parameter", "User", "Time", "PageOf"};
}