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
	Color CELL_SELECTION_COLOR = Color.BLUE;

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
	 * array attributes name
	 */
	String[] ARRAY_CONTENT_ATTRIBUTE_NAMES = {SPECIMEN_PREFIX + "label",
			SPECIMEN_PREFIX + "barcode", "quantity", "concentration", "positionDimensionOne",
			"positionDimenstionTwo", "id", SPECIMEN_PREFIX + "id"};

	/**
	 * Specimen Attributes Row Nos
	 * */
	short SPECIMEN_COLLECTION_GROUP_ROW_NO = 0;
	short SPECIMEN_PARENT_ROW_NO = 1;
	short SPECIMEN_LABEL_ROW_NO = 2;
	short SPECIMEN_BARCODE_ROW_NO = 3;
	short SPECIMEN_CLASS_ROW_NO = 4;
	short SPECIMEN_TYPE_ROW_NO = 5;
	short SPECIMEN_TISSUE_SITE_ROW_NO = 6;
	short SPECIMEN_PATHOLOGICAL_STATUS_ROW_NO = 7;
	short SPECIMEN_CONCENTRATION_ROW_NO = 8;
	short SPECIMEN_QUANTITY_ROW_NO = 9;
	short SPECIMEN_COMMENTS_ROW_NO = 10;
	short SPECIMEN_EVENTS_ROW_NO = 11;
	short SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO = 12;
	short SPECIMEN_BIOHAZARDS_ROW_NO = 13;
}
