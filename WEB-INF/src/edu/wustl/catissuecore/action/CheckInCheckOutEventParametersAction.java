/**
 * <p>Title: CheckInCheckOutEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the CheckInCheckOutEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 31, 2005
 */
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;


/**
 * @author mandar_deshmukh
 *
 * This class initializes the fields in the CheckInCheckOutEventParameters Add/Edit webpage.
 */
public class CheckInCheckOutEventParametersAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		//set array of CheckInCheckOutEventParameters
		request.setAttribute(Constants.STORAGE_STATUS_LIST, Constants.STORAGE_STATUS_ARRAY);
	}
}
