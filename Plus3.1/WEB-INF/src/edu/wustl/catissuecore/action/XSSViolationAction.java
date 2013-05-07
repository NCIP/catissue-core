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
				||Constants.PAGE_OF_SCG_CP_QUERY.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.PAGE_OF_COLLECTION_PROTOCOL_QUERY.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.DEFINE_EVENTS_PAGE.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.PAGE_OF_DEFINE_EVENTS.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.PAGE_OF_SPECIMEN_REQUIREMENT.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.ERROR.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.PAGE_OF_CREATE_ALIQUOT.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.PAGE_OF_CREATE_DERIVATIVE.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.PAGE_OF_MULTIPLE_SPECIMEN_WITHOUT_MENU.equals(request.getParameter(Constants.PAGE_OF))
				||Constants.PAGE_OF_SPECIMEN_SUMMARY.equals(request.getParameter(Constants.PAGE_OF)))
		{
			target="cpbasedview";
		}
		return mapping.findForward(target);
		

	}
}