
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.ThawEventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author renuka_bajpai
 */
public class ThawEventParametersAction extends SpecimenEventParametersAction
{

	/**
	 * @param request
	 *            object of HttpServletRequest
	 * @param eventParametersForm
	 *            : eventParametersForm
	 * @throws Exception
	 *             : Exception
	 */
	@Override
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{
		// TODO Auto-generated method stub
		// String operation = (String)
		// request.getAttribute(Constants.OPERATION);
		String formName, specimenId = null;
		ThawEventParametersForm thawEventParametersForm = (ThawEventParametersForm) eventParametersForm;

		boolean readOnlyValue;
		if (thawEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.THAW_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.THAW_EVENT_PARAMETERS_ADD_ACTION;
			specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			readOnlyValue = false;
		}
		String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);

	}
}
