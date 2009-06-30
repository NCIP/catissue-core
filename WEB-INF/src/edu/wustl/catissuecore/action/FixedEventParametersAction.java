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

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.FixedEventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;

/**
 * @author mandar_deshmukh
 * This class initializes the fields in the FixedEventParameters Add/Edit webpage.
 */
public class FixedEventParametersAction extends SpecimenEventParametersAction
{

	/**
	 * @param request object of HttpServletRequest
	 * @throws Exception generic exception
	 */
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{
		String formName = null;
		boolean readOnlyValue;
		FixedEventParametersForm fixedEventParametersForm = (FixedEventParametersForm) eventParametersForm;

		if (fixedEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.FIXED_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.FIXED_EVENT_PARAMETERS_ADD_ACTION;
			readOnlyValue = false;
		}
		//String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("fixedEventParametersAddAction",
				Constants.FIXED_EVENT_PARAMETERS_ADD_ACTION);
		// SETS THE FIXATION LIST
		List fixationList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_FIXATION_TYPE, null);
		request.setAttribute("fixationList", fixationList);
	}

}
