/**
 * <p>Title: CheckInCheckOutEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the
 *  CheckInCheckOutEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 31, 2005
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.CheckInCheckOutEventParametersForm;
import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author mandar_deshmukh
 *
 * This class initializes the fields in the CheckInCheckOutEventParameters Add/Edit webpage.
 */
public class CheckInCheckOutEventParametersAction extends SpecimenEventParametersAction
{

	/**
	 * @param  request : request
	 * @param eventParametersForm : eventParametersForm
	 * @throws Exception : Exception
	 */
	@Override
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{

		String formName = null;
		boolean readOnlyValue;
		final CheckInCheckOutEventParametersForm checkInCheckOutEventParametersForm = (CheckInCheckOutEventParametersForm) eventParametersForm;
		if (checkInCheckOutEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_ADD_ACTION;
			readOnlyValue = false;
		}
		final String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);

		request.setAttribute("checkInCheckOutEventParametersAction",
				Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_ADD_ACTION);
		//request.setAttribute("checkInCheckOutEventParametersForm",checkInCheckOutEventParametersForm);
		//		 if(checkInCheckOutEventParametersForm.getStorageStatus()==null)
		//		 {}

		//set array of CheckInCheckOutEventParameters
		request.setAttribute("storageStatusList", Constants.STORAGE_STATUS_ARRAY);

	}

}
