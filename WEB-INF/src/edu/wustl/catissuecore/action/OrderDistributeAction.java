
package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * @author renuka_bajpai
 */
public class OrderDistributeAction extends BaseAction
{

	/**
	 * @param mapping
	 *            ActionMapping object
	 * @param form
	 *            ActionForm object
	 * @param request
	 *            HttpServletRequest object
	 * @param response
	 *            HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception
	 *             object
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		final OrderForm requestOrder = (OrderForm) form;
		requestOrder.setOrderRequestName(null);

		final HttpSession session = request.getSession();

		session.setAttribute("OrderForm", requestOrder);

		final List<String> specimenIds = (List) session.getAttribute(Constants.SPECIMEN_ID);
		final List<String> specimenArrayIds = (List) session
				.getAttribute(Constants.SPECIMEN_ARRAY_ID);
		final List<String> pathalogicalCaseIds = (List) session
				.getAttribute(Constants.PATHALOGICAL_CASE_ID);
		final List<String> deidentifiedPathalogicalCaseIds = (List) session
				.getAttribute(Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID);
		final List<String> surgicalPathalogicalCaseIds = (List) session
				.getAttribute(Constants.SURGICAL_PATHALOGY_CASE_ID);

		String target = null;

		if (specimenIds != null && !specimenIds.isEmpty())
		{
			target = Constants.SPECIMEN_ORDER_FORM_TYPE;
		}
		else if (specimenArrayIds != null && !specimenArrayIds.isEmpty())
		{
			target = Constants.ARRAY_ORDER_FORM_TYPE;
		}
		else if (pathalogicalCaseIds != null && !pathalogicalCaseIds.isEmpty()
				|| deidentifiedPathalogicalCaseIds != null
				&& !deidentifiedPathalogicalCaseIds.isEmpty()
				|| surgicalPathalogicalCaseIds != null && !surgicalPathalogicalCaseIds.isEmpty())
		{
			target = Constants.PATHOLOGYCASE_ORDER_FORM_TYPE;
		}
		else
		{
			final ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.ordering.note2"));
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.ordering.a"));
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.ordering.b"));
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.ordering.c"));
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.ordering.d"));

			this.saveErrors(request, errors);
			target = "failure";
		}

		return mapping.findForward(target);
	}
}
