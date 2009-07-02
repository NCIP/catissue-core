
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.query.util.global.AQConstants;

/**
 * @author santhoshkumar_c
 */
public class ViewCartAction extends QueryShoppingCartAction
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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		HttpSession session = request.getSession();
		String target = null;

		QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);

		request.setAttribute(Constants.EVENT_PARAMETERS_LIST, Constants.EVENT_PARAMETERS);
		setCartView(request, cart);
		target = new String(Constants.VIEW);
		session.removeAttribute(AQConstants.HYPERLINK_COLUMN_MAP);
		String eventArray[] = Constants.EVENT_PARAMETERS;
		String newEvenetArray[] = new String[2];
		int count = 0;
		for (String eventName : eventArray)
		{
			if (("Transfer").equals(eventName) || ("Disposal").equals(eventName))
			{
				newEvenetArray[count] = eventName;
				count++;
			}
		}
		request.setAttribute("eventArray", newEvenetArray);
		return mapping.findForward(target);
	}
}
