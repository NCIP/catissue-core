
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.SpunEventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author renuka_bajpai
 */
public class SpunEventParametersAction extends SpecimenEventParametersAction
{
	/**
	* @param request object of HttpServletRequest
	* @param eventParametersForm : eventParametersForm
	* @throws Exception : Exception
	*/
	@Override
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{
		// TODO Auto-generated method stub
		SpunEventParametersForm spunEventParametersForm = (SpunEventParametersForm) eventParametersForm;

		// String operation = (String)
		// request.getAttribute(Constants.OPERATION);
		String formName, specimenId = null;

		boolean readOnlyValue;
		if (spunEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.SPUN_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.SPUN_EVENT_PARAMETERS_ADD_ACTION;
			specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			readOnlyValue = false;
		}
		String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);

	}
}
