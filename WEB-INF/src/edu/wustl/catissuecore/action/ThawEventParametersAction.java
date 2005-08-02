/**
 * <p>Title: ThawEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the FrozenEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti singh
 * @version 1.00
 * Created on Aug 1, 2005
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class initializes the fields in the ThawEventParameters Add/Edit webpage.
 */
public class ThawEventParametersAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request)
	{
		super.setRequestParameters(request);
		
		//set array of methods
		//Sets the hourList attribute to be used in the Add/Edit ThawEventParameters Page.
		//request.setAttribute("methodList", Constants.METHODARRAY);
	}
}