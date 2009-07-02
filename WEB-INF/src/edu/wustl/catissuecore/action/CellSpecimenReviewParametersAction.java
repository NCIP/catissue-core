
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.CellSpecimenReviewParametersForm;
import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author renuka_bajpai
 */
public class CellSpecimenReviewParametersAction extends SpecimenEventParametersAction
{
/**
 * @param request : request
 * @param eventParametersForm : eventParametersForm
 * @throws Exception : Exception
 */
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{
		String formName = null;
		boolean readOnlyValue;
		CellSpecimenReviewParametersForm cellSpecimenReviewParametersForm =
			(CellSpecimenReviewParametersForm) eventParametersForm;
		if (cellSpecimenReviewParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION;
			readOnlyValue = false;
		}
		String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);
		request.setAttribute("CellSpecimenReviewParametersAdd",
				Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION);
		request.setAttribute("containerList", Constants.CONTAINER_LIST);
	}
}
