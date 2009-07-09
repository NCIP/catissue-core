/*
 * <p>Title: SpecimenArrayAppletUtil.java</p>
 * <p>Description:	This class initializes the fields of SpecimenArrayAppletUtil.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 19, 2006
 */
/**
 */

package edu.wustl.catissuecore.applet.util;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * <p>This class initializes the fields of SpecimenArrayAppletUtil.java</p>.
 * @author Ashwin Gupta
 * @version 1.1
 */

public final class SpecimenArrayAppletUtil
{

	/**
	 * Get Array map key.
	 * @param row row number
	 * @param col column number
	 * @param columnCount column count
	 * @param attributeIndex attribute index
	 * @return string -- map key
	 */
	public static String getArrayMapKey(int row, int col, int columnCount, int attributeIndex)
	{
		String mapKey = null;
		int position = (row * columnCount) + col;
		mapKey = new String(AppletConstants.ARRAY_CONTENT_KEY_PREFIX + position
				+ AppletConstants.delimiter
				+ AppletConstants.ARRAY_CONTENT_ATTRIBUTE_NAMES[attributeIndex]);
		return mapKey;
	}

	/**
	 * check whether a specimen class is type of molecular.
	 * @param specimenClass specimen class
	 * @return true if molecular specimen
	 */
	public static boolean isMolecularSpecimen(String specimenClass)
	{
		boolean molecularSpecimen = false;
		if ((specimenClass != null) && (specimenClass.equalsIgnoreCase(Constants.MOLECULAR)))
		{
			molecularSpecimen = true;
		}
		return molecularSpecimen;
	}
}
