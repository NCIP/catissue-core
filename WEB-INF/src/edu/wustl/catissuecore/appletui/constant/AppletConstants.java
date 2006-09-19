/*
 * <p>Title: AppletConstants.java</p>
 * <p>Description:	This class initializes the fields of AppletConstants.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 18, 2006
 */
package edu.wustl.catissuecore.appletui.constant;

import java.awt.Color;

/**
 * <p>
 * AppletConstants interface is used to contain constants required for applet/components like
 * Image path,font for components etc.
 * </p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public interface AppletConstants {
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
		String ARRAY_CONTENT_SPECIMEN_PREFIX = "Specimen:";
		
		/**
		 * array attributes name
		 */
		String[] ARRAY_CONTENT_ATTRIBUTE_NAMES = {ARRAY_CONTENT_SPECIMEN_PREFIX + "label",ARRAY_CONTENT_SPECIMEN_PREFIX + "barcode","quantity","concentration","positionDimensionOne","positionDimenstionTwo","id",ARRAY_CONTENT_SPECIMEN_PREFIX + "id"};
		
}
