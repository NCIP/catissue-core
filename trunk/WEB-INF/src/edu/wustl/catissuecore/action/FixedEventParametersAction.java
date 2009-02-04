/**
 * <p>Title: FixedEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the FixedEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28, 2005
 */
package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;


/**
 * @author mandar_deshmukh
 * This class initializes the fields in the FixedEventParameters Add/Edit webpage.
 */
public class FixedEventParametersAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		// SETS THE FIXATION LIST
		List fixationList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_FIXATION_TYPE,null);
    	request.setAttribute(Constants.FIXATION_LIST, fixationList);
	}
}
