package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

@SuppressWarnings("deprecation")
public class XSSViolationAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = "generic";
		if (Constants.PAGE_OF_PARTICIPANT_CP_QUERY.equals(request.getParameter(Constants.PAGE_OF))
				|| Constants.PAGE_OF_SPECIMEN_CP_QUERY.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.PAGE_OF_SCG_CP_QUERY.equals(request.getParameter(Constants.PAGE_OF)))
		{
			target="cpbasedview";
		}
		return mapping.findForward(target);
		

	}
}