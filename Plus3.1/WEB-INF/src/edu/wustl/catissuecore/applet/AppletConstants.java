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
 * AppletConstants interface is used to contain constants required for applet/components like.
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
	 * selected cell color.
	 */
	Color CELL_SELECTION_COLOR = Color.blue;
	/**
	 * delimiter.
	 */
	String delimiter = "_";
	/**
	 * key prefix.
	 */
	String ARRAY_CONTENT_KEY_PREFIX = "SpecimenArrayContent:";
	/**
	 * Arrau specimen prefix.
	 */
	String SPECIMEN_PREFIX = "Specimen:";
	/**
	 * Array specimen prefix.
	 */
	String ARRAY_CONTENT_SPECIMEN_PREFIX = "Specimen_";
	/**
	 * Array specimen prefix.
	 */
	String ARRAY_CONTENT_QUANTITY_PREFIX = "initialQuantity";
	/**
	 * VIRTUALLY_LOCATED_CHECKBOX.
	 */
	String VIRTUALLY_LOCATED_CHECKBOX = "virtuallyLocatedCheckBox";
	/**
	 * array attributes name.
	 */
	String[] ARRAY_CONTENT_ATTRIBUTE_NAMES = {ARRAY_CONTENT_SPECIMEN_PREFIX + "label",
			ARRAY_CONTENT_SPECIMEN_PREFIX + "barcode", ARRAY_CONTENT_QUANTITY_PREFIX,
			"concentrationInMicrogramPerMicroliter", "positionDimensionOne",
			"positionDimensionTwo", "id"};
	// ,ARRAY_CONTENT_SPECIMEN_PREFIX + "id"
	/**
	 * Specify the ARRAY_CONTENT_ATTR_LABEL_INDEX field.
	 */
	int ARRAY_CONTENT_ATTR_LABEL_INDEX = 0;
	/**
	 * Specify the ARRAY_CONTENT_ATTR_BARCODE_INDEX field.
	 */
	int ARRAY_CONTENT_ATTR_BARCODE_INDEX = 1;
	/**
	 * Specify the ARRAY_CONTENT_ATTR_QUANTITY_INDEX field.
	 */
	int ARRAY_CONTENT_ATTR_QUANTITY_INDEX = 2;
	/**
	 * Specify the ARRAY_CONTENT_ATTR_CONC_INDEX field.
	 */
	int ARRAY_CONTENT_ATTR_CONC_INDEX = 3;
	/**
	 * Specify the ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX field.
	 */
	int ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX = 4;
	/**
	 * Specify the ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX field.
	 */
	int ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX = 5;
	/**
	 * Specify the ARRAY_CONTENT_ATTR_ID_INDEX field.
	 */
	int ARRAY_CONTENT_ATTR_ID_INDEX = 6;

	/**
	 * Specify the ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX field.
	 */
	int ARRAY_CONTENT_ATTR_QUANTITY_ID_INDEX = 7;
	/**
	 * Specify the SPECIMEN_ARRAY_APPLET_ACTION field.
	 */
	String SPECIMEN_ARRAY_APPLET_ACTION = "/SpecimenArrayAppletAction.do";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field.
	 */
	String ADD_TO_LIMIT_ACTION = "/addToLimitSet.do";
	/**
	 * Specify the GET_SEARCH_RESULTS field.
	 */
	String GET_SEARCH_RESULTS = "/ViewSearchResultsAction.do";
	/**
	 * Specify the PATH_FINDER field.
	 */
	String PATH_FINDER = "/PathFinderAction.do";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field.
	 */
	String GET_DAG_VIEW_DATA = "/getDagViewDataAction.do";
	/**
	 * RESOURCE_BUNDLE_PATH.
	 */
	String RESOURCE_BUNDLE_PATH = "dagViewApplet.jar/ApplicationResources.properties";
	/**
	 * DefineSearchResultsViewAction.
	 */
	String DefineSearchResultsViewAction = "/DefineSearchResultsView.do";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field.
	 */
	String DAG_VIEW_DATA = "dagViewData";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field.
	 */
	String ENTITY_MAP = "entity_map";
	/**
	 * Specify the ADD_TO_LIMIT_ACTION field.
	 */
	String ENTITY_STR = "entity_str";
	/**
	 * Specimen Attributes Row Nos.
	 * */
	int SPECIMEN_CHECKBOX_ROW_NO = 0; //	FOR CHECKBOXES
	/**
	 * SPECIMEN_COLLECTION_GROUP_ROW_NO.
	 */
	int SPECIMEN_COLLECTION_GROUP_ROW_NO = 1;
	/**
	 * SPECIMEN_PARENT_ROW_NO.
	 */
	int SPECIMEN_PARENT_ROW_NO = 2;
	/**
	 * SPECIMEN_LABEL_ROW_NO.
	 */
	int SPECIMEN_LABEL_ROW_NO = 3;
	/**
	 * SPECIMEN_BARCODE_ROW_NO.
	 */
	int SPECIMEN_BARCODE_ROW_NO = 4;
	/**
	 * SPECIMEN_CLASS_ROW_NO.
	 */
	int SPECIMEN_CLASS_ROW_NO = 5;
	/**
	 * SPECIMEN_TYPE_ROW_NO.
	 */
	int SPECIMEN_TYPE_ROW_NO = 6;
	/**
	 * SPECIMEN_TISSUE_SITE_ROW_NO.
	 */
	int SPECIMEN_TISSUE_SITE_ROW_NO = 7;
	/**
	 * SPECIMEN_TISSUE_SIDE_ROW_NO.
	 */
	int SPECIMEN_TISSUE_SIDE_ROW_NO = 8;
	/**
	 * Patch ID: 3835_1_14
	 * See also: 1_1 to 1_5
	 * Description : Added created date row and changed below row no accordingly.
	 */
	int SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO = 9;
	/**
	 * SPECIMEN_CREATED_DATE_ROW_NO.
	 */
	int SPECIMEN_CREATED_DATE_ROW_NO = 10;
	/**
	 * SPECIMEN_QUANTITY_ROW_NO.
	 */
	int SPECIMEN_QUANTITY_ROW_NO = 11;
	/**
	 * SPECIMEN_CONCENTRATION_ROW_NO.
	 */
	int SPECIMEN_CONCENTRATION_ROW_NO = 12;
	/**
	 * SPECIMEN_COMMENTS_ROW_NO.
	 */
	int SPECIMEN_COMMENTS_ROW_NO = 13;
	/**
	 * SPECIMEN_EVENTS_ROW_NO.
	 */
	int SPECIMEN_EVENTS_ROW_NO = 14;
	/**
	 * SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO.
	 */
	int SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO = 15;
	/**
	 * SPECIMEN_BIOHAZARDS_ROW_NO.
	 */
	int SPECIMEN_BIOHAZARDS_ROW_NO = 16;
	/**
	 * SPECIMEN_DERIVE_ROW_NO.
	 */
	int SPECIMEN_DERIVE_ROW_NO = 17;
	//	Mandar: 06Nov06: location removed since auto allocation will take place.
	//	short SPECIMEN_STORAGE_LOCATION_ROW_NO = 11;
	/**
	 * NO_OF_SPECIMENS.
	 */
	String NO_OF_SPECIMENS = "NO_OF_SPECIMENS";
	// this is key to put specimen map in session.
	/**
	 * APPLET_ACTION_PARAM_NAME.
	 */
	String APPLET_ACTION_PARAM_NAME = "method";
	//Constants for buttons
	/**
	 * MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS.
	 */
	String MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS = "Add";
	/**
	 * MULTIPLE_SPECIMEN_BIOHAZARDS.
	 */
	String MULTIPLE_SPECIMEN_BIOHAZARDS = "Add";
	/**
	 * MULTIPLE_SPECIMEN_EVENTS.
	 */
	String MULTIPLE_SPECIMEN_EVENTS = "Add";
	/**
	 * MULTIPLE_SPECIMEN_DERIVE.
	 */
	String MULTIPLE_SPECIMEN_DERIVE = "Add";
	/**
	 * MULTIPLE_SPECIMEN_MAP.
	 */
	String MULTIPLE_SPECIMEN_MAP = "Map";
	/**
	 * MULTIPLE_SPECIMEN_COMMENTS.
	 */
	String MULTIPLE_SPECIMEN_COMMENTS = "Add";
	/**
	 * ADD.
	 */
	String ADD = "Add";
	/**
	 * EDIT.
	 */
	String EDIT = "Edit";
	/**
	 * MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS_STRING.
	 */
	String MULTIPLE_SPECIMEN_EXTERNAL_IDENTIFIERS_STRING = "external";
	/**
	 * MULTIPLE_SPECIMEN_BIOHAZARDS_STRING.
	 */
	String MULTIPLE_SPECIMEN_BIOHAZARDS_STRING = "biohazard";
	/**
	 * MULTIPLE_SPECIMEN_EVENTS_STRING.
	 */
	String MULTIPLE_SPECIMEN_EVENTS_STRING = "event";
	/**
	 * MULTIPLE_SPECIMEN_DERIVE_STRING.
	 */
	String MULTIPLE_SPECIMEN_DERIVE_STRING = "derive";
	/**
	 * MULTIPLE_SPECIMEN_COMMENTS_STRING.
	 */
	String MULTIPLE_SPECIMEN_COMMENTS_STRING = "comment";
	/**
	 * MULTIPLE_SPECIMEN_ADD_SPECIMEN.
	 */
	String MULTIPLE_SPECIMEN_ADD_SPECIMEN = "More";
	/**
	 * MULTIPLE_SPECIMEN_COPY.
	 */
	String MULTIPLE_SPECIMEN_COPY = "Copy";
	/**
	 * MULTIPLE_SPECIMEN_PASTE.
	 */
	String MULTIPLE_SPECIMEN_PASTE = "Paste";
	/**
	 * MULTIPLE_SPECIMEN_MANDATORY.
	 */
	String MULTIPLE_SPECIMEN_MANDATORY = "*";
	/**
	 * MULTIPLE_SPECIMEN_SUBMIT.
	 */
	String MULTIPLE_SPECIMEN_SUBMIT = "Submit";
	/**
	 * MULTIPLE_SPECIMEN_DELETE_LAST.
	 */
	String MULTIPLE_SPECIMEN_DELETE_LAST = "Delete Last";
	/**
	 * MULTIPLE_SPECIMEN_LOCATION_LABEL.
	 */
	String MULTIPLE_SPECIMEN_LOCATION_LABEL = "Containerlabel_temp";
	/**
	 * MULTIPLE_SPECIMEN_ROW_COLUMN_SEPARATOR.
	 */
	String MULTIPLE_SPECIMEN_ROW_COLUMN_SEPARATOR = "@";
	/**
	 * MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR.
	 */
	String MULTIPLE_SPECIMEN_BUTTON_MAP_KEY_SEPARATOR = "@";
	//for parent specimen enable
	/**
	 * MULTIPLE_SPECIMEN_COLLECTION_GROUP_RADIOMAP.
	 */
	String MULTIPLE_SPECIMEN_COLLECTION_GROUP_RADIOMAP = "collectionGroupRadioMap";
	/**
	 * COPY_OPERATION.
	 */
	String COPY_OPERATION = "copy";
	/**
	 * PASTE_OPERATION.
	 */
	String PASTE_OPERATION = "paste";
	/**
	 * VALIDATOR_MODEL.
	 */
	String VALIDATOR_MODEL = "validatorModel";
	/**
	 * ARRAY_COPY_OPTION_LABELBAR.
	 */
	String ARRAY_COPY_OPTION_LABELBAR = "Label/Barcode";
	/**
	 * ARRAY_COPY_OPTION_QUANTITY.
	 */
	String ARRAY_COPY_OPTION_QUANTITY = "Quantity";
	/**
	 * ARRAY_COPY_OPTION_CONCENTRATION.
	 */
	String ARRAY_COPY_OPTION_CONCENTRATION = "Concentration";
	/**
	 * ARRAY_COPY_OPTION_ALL.
	 */
	String ARRAY_COPY_OPTION_ALL = "All";
	/**
	 * LINK_BUTTON_WIDTH.
	 */
	int LINK_BUTTON_WIDTH = 70;
	/**
	 * BG_COLOR.
	 */
	Color BG_COLOR = new Color(0xf4f4f5);
	/**
	 * MULTIPLE_SPECIMEN_COPY_ACCESSKEY.
	 */
	char MULTIPLE_SPECIMEN_COPY_ACCESSKEY = 'C';
	/**
	 * MULTIPLE_SPECIMEN_PASTE_ACCESSKEY.
	 */
	char MULTIPLE_SPECIMEN_PASTE_ACCESSKEY = 'P';
	/**
	 * MULTIPLE_SPECIMEN_DELETE_LAST_ACCESSKEY.
	 */
	char MULTIPLE_SPECIMEN_DELETE_LAST_ACCESSKEY = 'D';
	/**
	 * MULTIPLE_SPECIMEN_CHECKBOX_LABEL.
	 */
	String MULTIPLE_SPECIMEN_CHECKBOX_LABEL = "Specimen ";
	// Dagviewapplet constants
	/**
	 * QUERY_OBJECT.
	 */
	String QUERY_OBJECT = "queryObject";
	/**
	 * PARAMETERIZED_QUERY.
	 */
	String PARAMETERIZED_QUERY = "parameterizedQuery";
	/**
	 * SESSION_ID.
	 */
	String SESSION_ID = "session_id";
	/**
	 * STR_TO_CREATE_QUERY_OBJ.
	 */
	String STR_TO_CREATE_QUERY_OBJ = "strToCreateQueryObject";
	/**
	 * ENTITY_NAME.
	 */
	String ENTITY_NAME = "entityName";
	/**
	 * INIT_DATA.
	 */
	String INIT_DATA = "initData";
	/**
	 * ATTRIBUTES.
	 */
	String ATTRIBUTES = "Attributes";
	/**
	 * ATTRIBUTE_OPERATORS.
	 */
	String ATTRIBUTE_OPERATORS = "AttributeOperators";
	/**
	 * FIRST_ATTR_VALUES.
	 */
	String FIRST_ATTR_VALUES = "FirstAttributeValues";
	/**
	 * SECOND_ATTR_VALUES.
	 */
	String SECOND_ATTR_VALUES = "SecondAttributeValues";
	/**
	 * SHOW_ENTITY_INFO.
	 */
	String SHOW_ENTITY_INFO = "showEntityInformation";
	/**
	 * SRC_ENTITY.
	 */
	String SRC_ENTITY = "srcEntity";
	/**
	 * PATHS.
	 */
	String PATHS = "paths";
	/**
	 * DEST_ENTITY.
	 */
	String DEST_ENTITY = "destEntity";
	/**
	 * ERROR_MESSAGE.
	 */
	String ERROR_MESSAGE = "errorMessage";
	/***
	 * SHOW_VALIDATION_MESSAGES.
	 */
	String SHOW_VALIDATION_MESSAGES = "showValidationMessages";
	/**
	 * SHOW_RESULTS_PAGE.
	 */
	String SHOW_RESULTS_PAGE = "showViewSearchResultsJsp";
	/**
	 * ATTR_VALUES.
	 */
	String ATTR_VALUES = "AttributeValues";
	/**
	 * SHOW_ERROR_PAGE.
	 */
	String SHOW_ERROR_PAGE = "showErrorPage";
	/**
	 * GET_DATA.
	 */
	String GET_DATA = "getData";
	/**
	 * SET_DATA.
	 */
	String SET_DATA = "setData";
	/**
	 * EMPTY_LIMIT_ERROR_MESSAGE.
	 */
	String EMPTY_LIMIT_ERROR_MESSAGE = "<li><font color='red'>" +
			"Please enter at least one condition to add a limit to limit set.</font></li>";
	/**
	 * EMPTY_DAG_ERROR_MESSAGE.
	 */
	String EMPTY_DAG_ERROR_MESSAGE = "<li><font color='red'>" +
			"Limit set should contain at least one limit.</font></li>";
	/**
	 * MULTIPLE_ROOTS_EXCEPTION.
	 */
	String MULTIPLE_ROOTS_EXCEPTION = "<li><font color='red'>" +
			"Expression graph should be a connected graph.</font></li>";
	/**
	 * EDIT_LIMITS.
	 */
	String EDIT_LIMITS = "<li><font color='blue'>Limit succesfully edited.</font></li>";
	/**
	 * DELETE_LIMITS : DELETE_LIMITS.
	 */
	String DELETE_LIMITS = "<li><font color='blue'>Limit succesfully deleted.</font></li>";

	//public static String ATTRIBUTES = "Attributes";
}
