/**
 * <p>Title: DisposalEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the DisposalEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 05, 2005
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author vaishali_khandelwal
 *  * This class initializes the fields in the DisposalEventParameters Add/Edit webpage.
 */
public class DisposalEventParametersAction extends SpecimenEventParametersAction
{

	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		/*DisposalEventParametersForm form = (DisposalEventParametersForm) request
				.getAttribute("disposalEventParametersForm");*/
		
		request.setAttribute(Constants.ACTIVITYSTATUSLIST,
				Constants.DISPOSAL_EVENT_ACTIVITY_STATUS_VALUES);

	}
}
