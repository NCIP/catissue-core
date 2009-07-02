/**
 * <p>Title: FrozenEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the FrozenEventParameters Add/Edit webpage.</p>
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
import edu.wustl.catissuecore.actionForm.FrozenEventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;

/**
 * This class initializes the fields in the FrozenEventParameters Add/Edit webpage.
 * @author mandar deshmukh
 */
public class FrozenEventParametersAction extends SpecimenEventParametersAction
{

	/**
	 * @param request object of HttpServletRequest
	 * @param eventParametersForm : eventParametersForm
	 * @throws Exception : Exception
	 */
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{

		//String operation = (String) request.getAttribute(Constants.OPERATION);
		String formName, specimenId = null;

		boolean readOnlyValue;
		FrozenEventParametersForm frozenEventParametersForm =
			(FrozenEventParametersForm) eventParametersForm;
		if (frozenEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.FROZEN_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.FROZEN_EVENT_PARAMETERS_ADD_ACTION;
			readOnlyValue = false;
		}
		specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);

		String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);
		request.setAttribute("specimenId", specimenId);
		//set array of methods
		List methodList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_METHOD, null);
		request.setAttribute("methodList", methodList);
		//request.setAttribute("frozenEventParametersAddAction",
		//Constants.FROZEN_EVENT_PARAMETERS_ADD_ACTION);
	}

}