/*
 * <p>Title: AppletConstants.java</p>
 * <p>Description:	This class initializes the fields of AppletConstants.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */

package edu.wustl.catissuecore.applet;

import java.awt.Color;

/**
 * <p>
 * AppletConstants interface is used to contain constants required for applet/components like
 * Image path,font for components etc.
 * </p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public interface AppletConstants
{

	/**
	 * Array grid component key used in map.
	 */
	String ARRAY_GRID_COMPONENT_KEY = "arrayGridComponentKey";

	/**
	 * selected cell color 
	 */
	Color CELL_SELECTION_COLOR = Color.blue;

	/**
	 * delimiter 
	 */
	String delimiter = "_";

	/**
	 * key prefix 
	 */
	String ARRAY_CONTENT_KEY_PREFIX = "SpecimenArrayContent:";
	/**
	 * Arrau specimen prefix
	 */
	String SPECIMEN_PREFIX = "Specimen:";

	/**
	 * Array specimen prefix
	 */
	String ARRAY_CONTENT_SPECIMEN_PREFIX = "Specimen_";

	/**
	 * Array specimen prefix
	 */
	String ARRAY_CONTENT_QUANTITY_PREFIX = "initialQuantity";

	
	String VIRTUALLY_LOCATED_CHECKBOX = "virtuallyLocatedCheckBox";
	
	/**
	 * array attributes name
	 */
	String[] ARRAY_CONTENT_ATTRIBUTE_NAMES = {ARRAY_CONTENT_SPECIMEN_PREFIX + "label",ARRAY_CONTENT_SPECIMEN_PREFIX + "barcode",ARRAY_CONTENT_QUANTITY_PREFIX,"concentrationInMicrogramPerMicroliter","positionDimensionOne","positionDimensionTwo","id"};
	// ,ARRAY_CONTENT_SPECIMEN_PREFIX + "id"
	
	/**
	 * Specify the ARRAY_CONTENT_ATTR_LABEL_INDEX field 
	 */
	int ARRAY_CONTENT_ATTR_LABEL_INDEX = 0;
	
	/**
	 * Specify the ARRAY_CONTENT_ATTR_BARCODE_INDEX field 
	 */
	int ARRAY_CONTENT_ATTR_BARCODE_INDEX = 1;
	
	/**
	 * Specify the ARRAY_CONTENT_ATTR_QUANTITY_INDEX field 
	 */
	int ARRAY_CONTENT_ATTR_QUANTITY_INDEX = 2;

	/**
	 * Specify the ARRAY_CONTENT_ATTR_CONC_INDEX field 
	 */
	int ARRAY_CONTENT_ATTR_CONC_INDEX = 3;

	/**
	 * Specify the ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX field 
	 */
	int ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX = 4;

	/**
	 * Specify the ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX field 
	 */
	int ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX = 5;
	
	/**
	 * Specify the ARRAY_CONTENT_ATTR_ID_INDEX field 
	 */
	int ARRAY_CONTENT_ATTR_ID_INDEX = 6;
	
	/**
	 * Specify the ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX field 
	 */
	int ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX = 7;
	
	/**
	 * Specify the SPECIMEN_ARRAY_APPLET_ACTION field 
	 */
	String SPECIMEN_ARRAY_APPLET_ACTION = "/SpecimenArrayAppletAction.do";

	/**
	 * Specify the ADD_TO_LIMIT_ACTION field 
	 */
	String ADD_TO_LIMIT_ACTION = "/addToLimitSet.do";
	/**
	 * Specify the GET_SEARCH_RESULTS field 
	 */
	String GET_SEARCH_RESULTS = "/ViewSearchResultsAction.do";
	/**
	 * Specify the PATH_FINDER field 
	 */
	String PATH_FINDER = "/PathFinderAction.do";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field 
	 */
	String GET_DAG_VIEW_DATA = "/getDagViewDataAction.do";
	/**
	 * 
	 */
	public static final String RESOURCE_BUNDLE_PATH = "dagViewApplet.jar/ApplicationResources.properties"; 
	public static final String DefineSearchResultsViewAction = "/DefineSearchResultsView.do";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field 
	 */
	String DAG_VIEW_DATA = "dagViewData";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field 
	 */
	String ENTITY_MAP = "entity_map";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field 
	 */
	String ENTITY_STR = "entity_str";

	/**
	 * Specimen Attributes Row Nos
	 * */
	short SPECIMEN_CHECKBOX_ROW_NO = 0;		//	FOR CHECKBOXES
	short SPECIMEN_COLLECTION_GROUP_ROW_NO = 1;
	short SPECIMEN_PARENT_ROW_NO = 2;
	short SPECIMEN_LABEL_ROW_NO = 3;
	short SPECIMEN_BARCODE_ROW_NO = 4;
	short SPECIMEN_CLASS_ROW_NO = 5;
	short SPECIMEN_TYPE_ROW_NO = 6;
	short SPECIMEN_TISSUE_SITE_ROW_NO = 7;
	short SPECIMEN_TISSUE_SIDE_ROW_NO = 8;
    
    /**
     * Patch ID: 3835_1_14
     * See also: 1_1 to 1_5
     * Description : Added created date row and changed below row no accordingly. 
     */
    
	short SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO = 9;
    short SPECIMEN_CREATED_DATE_ROW_NO = 10;
	short SPECIMEN_QUANTITY_ROW_NO = 11;
	short SPECIMEN_CONCENTRATION_ROW_NO = 12;
	short SPECIMEN_COMMENTS_ROW_NO = 13;
	short SPECIMEN_EVENTS_ROW_NO = 14;
	short SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO = 15;
	short SPECIMEN_BIOHAZARDS_ROW_NO = 16;
	short SPECIMEN_DERIVE_ROW_NO = 17;

//	Mandar: 06Nov06: location removed since auto allocation will take place.
//	short SPECIMEN_STORAGE_LOCATION_ROW_NO = 11;
	
	String  NO_OF_SPECIMENS = "NO_OF_SPECIMENS";
	// this is key to put specimen map in session.
	
	String APPLET_ACTION_PARAM_NAME = "method";
	
	//Constants for buttons
	public static final String MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS = "Add";
	public static final String MULTIPLE_SPECIMEN_BIOHAZARDS = "Add";
	public static final String MULTIPLE_SPECIMEN_EVENTS = "Add";
	public static final String MULTIPLE_SPECIMEN_DERIVE = "Add";
	public static final String MULTIPLE_SPECIMEN_MAP = "Map";
	public static final String MULTIPLE_SPECIMEN_COMMENTS = "Add";
	public static final String ADD = "Add";
	public static final String EDIT = "Edit";
	
	public static final String MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS_STRING = "external";
	public static final String MULTIPLE_SPECIMEN_BIOHAZARDS_STRING = "biohazard";
	public static final String MULTIPLE_SPECIMEN_EVENTS_STRING = "event";
	public static final String MULTIPLE_SPECIMEN_DERIVE_STRING = "derive";
	public static final String MULTIPLE_SPECIMEN_COMMENTS_STRING = "comment";
	
	public static final String MULTIPLE_SPECIMEN_ADD_SPECIMEN = "More";
	public static final String MULTIPLE_SPECIMEN_COPY = "Copy";
	public static final String MULTIPLE_SPECIMEN_PASTE = "Paste";
	public static final String MULTIPLE_SPECIMEN_MANDATORY = "*";
	public static final String MULTIPLE_SPECIMEN_SUBMIT="Submit";
	public static final String MULTIPLE_SPECIMEN_DELETE_LAST="Delete Last";
	
	public static final String MULTIPLE_SPECIMEN_LOCATION_LABEL = "Containerlabel_temp";
	public static final String MULTIPLE_SPECIMEN_ROW_COLUMN_SEPARATOR="@";
	public static final String MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR="@";
	//for parent specimen enable
	public static final String MULTIPLE_SPECIMEN_COLLECTION_GROUP_RADIOMAP="collectionGroupRadioMap";
	
	public static final String COPY_OPERATION = "copy";
	public static final String PASTE_OPERATION = "paste";
	public static final String VALIDATOR_MODEL = "validatorModel";
	
	public static final String ARRAY_COPY_OPTION_LABELBAR = "Label/Barcode";
	public static final String ARRAY_COPY_OPTION_QUANTITY = "Quantity";
	public static final String ARRAY_COPY_OPTION_CONCENTRATION = "Concentration";
	public static final String ARRAY_COPY_OPTION_ALL = "All";
	public static final int LINK_BUTTON_WIDTH = 70;
	
	public static final Color BG_COLOR = new Color(0xf4f4f5);
	
	public static final char MULTIPLE_SPECIMEN_COPY_ACCESSKEY = 'C';
	public static final char MULTIPLE_SPECIMEN_PASTE_ACCESSKEY = 'P';
	public static final char MULTIPLE_SPECIMEN_DELETE_LAST_ACCESSKEY='D';

	public static final String MULTIPLE_SPECIMEN_CHECKBOX_LABEL="Specimen ";
	
	// Dagviewapplet constants
	public static final String QUERY_OBJECT = "queryObject";
	public static final String PARAMETERIZED_QUERY = "parameterizedQuery";
	public static final String SESSION_ID = "session_id";
	public static final String STR_TO_CREATE_QUERY_OBJ = "strToCreateQueryObject";
	public static final String ENTITY_NAME = "entityName";
	public static final String INIT_DATA = "initData";
	public static final String ATTRIBUTES = "Attributes";
	public static final String ATTRIBUTE_OPERATORS = "AttributeOperators";
	public static final String FIRST_ATTR_VALUES = "FirstAttributeValues";
	public static final String SECOND_ATTR_VALUES = "SecondAttributeValues";
	public static final String SHOW_ENTITY_INFO = "showEntityInformation";
	public static final String SRC_ENTITY = "srcEntity";
	public static final String PATHS = "paths";
	public static final String DEST_ENTITY = "destEntity";
	public static final String ERROR_MESSAGE = "errorMessage";
	public static final String SHOW_VALIDATION_MESSAGES = "showValidationMessages";
	public static final String SHOW_RESULTS_PAGE = "showViewSearchResultsJsp";
	public static final String ATTR_VALUES = "AttributeValues";
	public static final String SHOW_ERROR_PAGE = "showErrorPage";
	public static final String GET_DATA = "getData";
	public static final String SET_DATA = "setData";
	public static final String EMPTY_LIMIT_ERROR_MESSAGE = "<li><font color='red'>Please enter at least one condition to add a limit to limit set.</font></li>";
	public static final String EMPTY_DAG_ERROR_MESSAGE = "<li><font color='red'>Limit set should contain at least one limit.</font></li>";
	public static final String MULTIPLE_ROOTS_EXCEPTION = "<li><font color='red'>Expression graph should be a connected graph.</font></li>";
	public static final String EDIT_LIMITS = "<li><font color='blue'>Limit succesfully edited.</font></li>";
	public static final String DELETE_LIMITS = "<li><font color='blue'>Limit succesfully deleted.</font></li>";
	
	//public static final String ATTRIBUTES = "Attributes";
	
}
