/**
 * <p>Title: Distribution Class>
 * <p>Description:	This class initializes the fields in the  Distribution Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on Aug 10, 2005
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class initializes the fields in the  Distribution Add/Edit webpage.
 
 */
public class  DistributionAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request)
	{
		super.setRequestParameters(request);
		
		
		request.setAttribute(Constants.FROMSITELIST, Constants.FROMSITEARRAY);
		request.setAttribute(Constants.TOSITELIST, Constants.TOSITEARRAY);
	}
}