
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.common.action.BaseAction;

/**
 * @author renuka_bajpai
 *
 */
public class RequestToOrderSubmitAction extends BaseAction
{

	/**
	 * @param mapping ActionMapping object
	 * @param form ActionForm object
	 * @param request HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception object
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		final OrderForm requestOrder = (OrderForm) form;
		final HttpSession session = request.getSession();

		session.setAttribute("OrderForm", requestOrder);
		return mapping.findForward("orderExistingAction");
	}
}
